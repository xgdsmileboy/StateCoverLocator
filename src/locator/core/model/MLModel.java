package locator.core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import jdk7.wrapper.JCompiler;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;
import locator.core.LineInfo;
import locator.core.run.Runner;
import locator.inst.visitor.MultiLinePredicateInstrumentVisitor;
import locator.inst.visitor.PredicateInstrumentVisitor;
import locator.inst.visitor.feature.ExprFilter;
import locator.inst.visitor.feature.FeatureExtraction;

public abstract class MLModel extends Model {

	protected MLModel(String modelName) {
		super(modelName, "predicates_backup.txt");
	}
	
	public boolean trainModel(Subject subject) {
		// train model
		try {
			LevelLogger.info(">>>>>> Begin Trainning ...");
			ExecuteCommand.executeTrain(subject, this);
			LevelLogger.info(">>>>>> End Trainning !");
		} catch (Exception e) {
			LevelLogger.error("Failed to train model", e);
			return false;
		}
		return true;
	}
	
	public boolean evaluate(Subject subject) {
		// evaluate model
		try {
			LevelLogger.info(">>>>>> Begin Evaluating ...");
			ExecuteCommand.executeEvaluate(subject, this);
			LevelLogger.info(">>>>>> End Evaluating !");
		} catch (Exception e) {
			LevelLogger.error("Failed to evaluate model", e);
			return false;
		}
		return true;
	}

	public boolean instrumentPredicates(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates, boolean useSober) {
		MultiLinePredicateInstrumentVisitor instrumentVisitor = new MultiLinePredicateInstrumentVisitor(useSober);
		for (Entry<String, Map<Integer, List<Pair<String, String>>>> entry : file2Line2Predicates.entrySet()) {
			String fileName = entry.getKey();
			Map<Integer, List<Pair<String, String>>> allPreds = entry.getValue();
			CompilationUnit unit = JavaFile.genAST(fileName);
			instrumentVisitor.setCondition(allPreds);
			unit.accept(instrumentVisitor);
			JavaFile.writeStringToFile(fileName, unit.toString());
		}
		return true;
	}
	
	public Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredicates(Subject subject, Set<String> allStatements, boolean useSober) {
		
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
		
		if(Constant.RECOVER_PREDICATE_FROM_FILE) {
			file2Line2Predicates = recoverPredicates(subject);
		}
		if(file2Line2Predicates != null) {
			return file2Line2Predicates;
		}
		// parse all object type
        ExprFilter.init(subject);

        String srcPath = subject.getHome() + subject.getSsrc();

        int allStmtCount = allStatements.size();
        int currentStmtCount = 1;

        Map<String, LineInfo> lineInfoMapping = new HashMap<String, LineInfo>();
        List<String> varFeatures = new ArrayList<String>();
        List<String> exprFeatures = new ArrayList<String>();
        for (String stmt : allStatements) {
            LevelLogger.info("======================== [" + currentStmtCount + "/" + allStmtCount
                    + "] statements =================.");
            currentStmtCount++;

            String[] stmtInfo = stmt.split("#");
            if (stmtInfo.length != 2) {
                LevelLogger.error(__name__ + "#getAllPredicates statement parse error : " + stmt);
                System.exit(0);
            }
            Integer methodID = Integer.valueOf(stmtInfo[0]);
            int line = Integer.parseInt(stmtInfo[1]);
            String methodString = Identifier.getMessage(methodID);
            LevelLogger.info("Current statement  : **" + methodString + "#" + line + "**");
            String[] methodInfo = methodString.split("#");
            if (methodInfo.length < 4) {
                LevelLogger.error(__name__ + "#getAllPredicates method info parse error : " + methodString);
                System.exit(0);
            }
            String clazz = methodInfo[0].replace(".", Constant.PATH_SEPARATOR);
            int index = clazz.indexOf("$");
            if (index > 0) {
                clazz = clazz.substring(0, index);
            }
            String relJavaPath = clazz + ".java";

            String fileName = srcPath + "/" + relJavaPath;
            File file = new File(fileName);
            if (!file.exists()) {
                LevelLogger.error("Cannot find file : " + fileName);
                continue;
            }

            // <varName, type>
            LineInfo info = new LineInfo(line, relJavaPath, clazz);
            Set<String> newAddedKeys = FeatureExtraction.generateFeatures(srcPath, relJavaPath, line, info,
                    varFeatures, exprFeatures);
            for (String key : newAddedKeys) {
                lineInfoMapping.put(key, info);
            }
        }
        Map<String, Map<String, List<Pair<String, String>>>> conditionsForRightVars = this.predict(subject,
                varFeatures, exprFeatures, lineInfoMapping);
        // TODO : currently, only instrument predicates for right variables
        // if predicted conditions are not empty for right variables,
        // instrument each condition one by one and compute coverage
        // information for each predicate
        JCompiler compiler = JCompiler.getInstance();
        for (Map.Entry<String, Map<String, List<Pair<String, String>>>> entry : conditionsForRightVars.entrySet()) {
            final LineInfo info = lineInfoMapping.get(entry.getKey());
            int line = info.getLine();
            if (entry.getValue() != null && entry.getValue().size() > 0) {
                String relJavaPath = info.getRelJavaPath();
                String javaFile = srcPath + Constant.PATH_SEPARATOR + relJavaPath;
                // the source file will instrumented iteratively, before which
                // the original source file should be saved
                // ExecuteCommand.copyFile(javaFile, javaFile + ".bak");
                PredicateInstrumentVisitor newPredicateInstrumentVisitor = new PredicateInstrumentVisitor(null,
                        line);
                List<Pair<String, String>> legalConditions = new ArrayList<>();
                // read original file once
                String source = JavaFile.readFileToString(javaFile);

                for (Entry<String, List<Pair<String, String>>> innerEntry : entry.getValue().entrySet()) {
                    int count = 0;
                    int allConditionCount = innerEntry.getValue().size();
                    int currentConditionCount = 1;
                    for (Pair<String, String> condition : innerEntry.getValue()) {
                        LevelLogger.info("Validate conditions by compiling : [" + currentConditionCount + "/"
                                + allConditionCount + "].");
                        currentConditionCount++;

                        // instrument one condition statement into source file
                        CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(source,
                                ASTParser.K_COMPILATION_UNIT);

                        List<Pair<String, String>> onePredicate = new ArrayList<>();
                        onePredicate.add(condition);
                        newPredicateInstrumentVisitor.setCondition(onePredicate);

                        compilationUnit.accept(newPredicateInstrumentVisitor);

                        if (compiler.compile(subject, relJavaPath, compilationUnit.toString())) {
                            legalConditions.add(condition);
                            if (!useSober) {
                                // add opposite conditions as well
                                Pair<String, String> otherSide = new Pair<>();
                                otherSide.setFirst("!(" + condition.getFirst() + ")");
                                otherSide.setSecond(condition.getSecond());
                                legalConditions.add(otherSide);
                            }
                            LevelLogger.info("Passed build : " + condition.toString() + "\t ADD \t");
                            count++;
                            // only keep partial predicates "top K"
                            if (count >= Constant.TOP_K_PREDICATES_FOR_EACH_VAR) {
                                break;
                            }
                        } else {
                            LevelLogger.info("Build failed : " + condition.toString());
                        }
                    }
                }

                if (legalConditions.size() > 0) {
                    Map<Integer, List<Pair<String, String>>> line2Predicate = file2Line2Predicates.get(javaFile);
                    if (line2Predicate == null) {
                        line2Predicate = new HashMap<>();
                    }
                    List<Pair<String, String>> predicates = line2Predicate.get(line);
                    if (predicates == null) {
                        predicates = new ArrayList<>();
                    }
                    predicates.addAll(legalConditions);
                    line2Predicate.put(line, predicates);
                    file2Line2Predicates.put(javaFile, line2Predicate);
                }
                if (!compiler.compile(subject, relJavaPath, source)) {
                    if (!Runner.compileSubject(subject)) {
                        LevelLogger.error(
                                __name__ + "#getAllPredicates ERROR : compile original source failed : "
                                        + javaFile);
                    }
                }
                // delete corresponding class file to enable re-compile for next
                // loop.
                // ExecuteCommand.deleteGivenFile(binFile);
                // restore original source file
                // ExecuteCommand.moveFile(javaFile + ".bak", javaFile);
            } // end of "conditionsForRightVars != null"
        } // end of "for(String stmt : allStatements)"

        LevelLogger.debug("-------------------FOR DEBUG----------------------");
		printPredicateInfo(file2Line2Predicates, subject);
        file2Line2Predicates = recoverPredicates(subject);
        return file2Line2Predicates;
	}
}
