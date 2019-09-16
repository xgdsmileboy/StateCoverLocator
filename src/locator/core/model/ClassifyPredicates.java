package locator.core.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.aux.extractor.FeatureGenerator;
import locator.aux.extractor.core.feature.ClassifierFeature;
import locator.aux.extractor.core.feature.item.FileName;
import locator.aux.extractor.core.feature.item.VarName;
import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;
import locator.common.util.Utils;
import locator.core.LineInfo;
import locator.core.alg.Ochiai;
import locator.inst.visitor.SDStmtPredicateInstrumentVisitor;

public class ClassifyPredicates extends MLModel {

	public final static String NAME = "classifier";
	
	public ClassifyPredicates() {
		super(NAME);
		__name__ = "@ClassifyPredicates ";
	}

	@Override
	public boolean modelExist(Subject subject) {
		File model = new File(_modelPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/classify");
		if (model.exists()) {
			LevelLogger.info("Models are already exist and will be used directly !");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean prepare(Subject subject) {
		// get train features
		String outPath = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId();

		// create necessary directories
		Utils.pathGuarantee(outPath + "/predicate", outPath + "/cluster", outPath + "/pred",
				getPredictResultPath(subject));

		String targetPath = outPath + "/predicate/" + subject.getNameAndId() + ".csv";
		
		String curName = subject.getName();
		int id = subject.getId();
		int bound = Constant.BUG_NUMBER.get(curName);
		bound = Math.min(bound, id + 10);
		String srcPath = null;
		String algName = new Ochiai().getName();
		boolean addHeader = true;
		for(id ++;id <= bound; id ++) {
			Subject subj = Configure.getSubject(curName, id);
			srcPath = subj.getHome() + subj.getSsrc();
			FeatureGenerator.generateTrainClassifierFeatures(srcPath, targetPath, curName, id, algName, addHeader);
			addHeader = false;
		}
		
		return true;
	}
	
	
	@Override
	public boolean instrumentPredicates(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates,
			boolean useSober) {
		return true;
	}
	
	@Override
	public Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredicates(Subject subject,
			Set<String> allStatements, boolean useSober) {
		
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = null;
		if (Constant.BOOL_RECOVER_PREDICATE_FROM_FILE) {
			file2Line2Predicates = Utils.recoverPredicates(subject, _predicates_backup_file);
		}
		if (file2Line2Predicates != null) {
			return file2Line2Predicates;
		}
		
		String srcPath = subject.getHome() + subject.getSsrc();
		
		Map<String, List<Integer>> file2LocationList = mapLocations2File(subject, allStatements);
		Map<String, Map<Integer, Set<String>>> allPossiblePredicates = new HashMap<>();
		
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(useSober,
				Constant.BOOL_BRANCH_COVERAGE, Constant.BOOL_ASSIGNMENT_COVERAGE, Constant.BOOL_RETURN_COVERAGE);
		for (Entry<String, List<Integer>> entry : file2LocationList.entrySet()) {
			String relJavaPath = entry.getKey();
			String fileName = srcPath + "/" + relJavaPath;
			Set<Integer> locations = new HashSet<>(entry.getValue());
			CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSourceWithType(
					JavaFile.readFileToString(fileName), ASTParser.K_COMPILATION_UNIT, fileName, subject);
			instrumentVisitor.initOneRun(locations, srcPath, relJavaPath);
			unit.accept(instrumentVisitor);
			JavaFile.writeStringToFile(fileName, unit.toString());
			
			Map<Integer, Set<String>> line2PredSet = new HashMap<>();
			for(Entry<Integer, List<Pair<String, String>>> inner : instrumentVisitor.getPredicates().entrySet()) {
				Set<String> predicates = new HashSet<>();
				for(Pair<String, String> pair : inner.getValue()) {
					String pred = pair.getFirst();
					int index = pred.lastIndexOf('#');
					if(index > 0) {
						pred = pred.substring(0, index);
					}
					predicates.add(pred);
				}
				line2PredSet.put(inner.getKey(), predicates);
			}
			allPossiblePredicates.put(relJavaPath, line2PredSet);
		}
		
		int fileNameIndex = ClassifierFeature.getFeatureIndex(FileName.class);
		int varNameIndex = ClassifierFeature.getFeatureIndex(VarName.class);
		Map<String, LineInfo> lineInfoMap = new HashMap<>();
		
		StringBuffer buffer = new StringBuffer(ClassifierFeature.getFeatureHeader());
		String[] data = null;
		String ori_srcPath = subject.getHome() + subject.getSsrc() + "_ori";
		for(Entry<String, Map<Integer, Set<String>>> entry : allPossiblePredicates.entrySet()) {
			String relJavaFile = entry.getKey();
			for(Entry<Integer, Set<String>> inner : entry.getValue().entrySet()) {
				int line = inner.getKey();
				List<String> features = FeatureGenerator.generateClassifierFeatureForLine(ori_srcPath, relJavaFile, line, inner.getValue());
				for(String string : features) {
					buffer.append("\n" + string);
					data = string.split("\t");
					String key = data[fileNameIndex] + "::" + line + "::" + data[varNameIndex];
					lineInfoMap.put(key, new LineInfo(line, relJavaFile, ""));
				}
			}
		}
		
		JavaFile.writeStringToFile(getClassifyFeatureOutputFile(subject), buffer.toString());
		
		try {
			ExecuteCommand.executePredict(subject, this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		file2Line2Predicates = new HashMap<>();
		List<String> content = JavaFile.readFileToStringList(getPredictResultFile(subject));
		if(content != null) {
			LineInfo info = null;
			Map<Integer, List<Pair<String, String>>> line2Predicates;
			for(String string : content) {
				data = string.split("\t");
				if(data.length != 3) {
					continue;
				}
				info = lineInfoMap.get(data[0]);
				if(info == null) {
					continue;
				}
				String javaFile = srcPath + Constant.PATH_SEPARATOR + info.getRelJavaPath();
				int line = info.getLine();
				line2Predicates = file2Line2Predicates.get(javaFile);
				if(line2Predicates == null) {
					line2Predicates = new HashMap<>();
					file2Line2Predicates.put(javaFile, line2Predicates);
				}
				List<Pair<String, String>> predicates = line2Predicates.get(line);
				if(predicates == null) {
					predicates = new LinkedList<>();
					line2Predicates.put(line, predicates);
				}
				predicates.add(new Pair<String, String>(data[1], data[2]));
			}
		}
		
		Utils.printPredicateInfo(file2Line2Predicates, subject, _predicates_backup_file);
		file2Line2Predicates = Utils.recoverPredicates(subject, _predicates_backup_file);
		return file2Line2Predicates;
	}

}
