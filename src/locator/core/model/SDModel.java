package locator.core.model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;
import locator.inst.visitor.NoSideEffectPredicateInstrumentVisitor;

public class SDModel extends Model {

	public final static String NAME = "dnn";
	
	
	public SDModel() {
		super(NAME, "predicates_backup_sd.txt");
		__name__ = "@SDModel ";
	}

	@Override
	public boolean modelExist(Subject subject) {
		return true;
	}

	@Override
	public boolean prepare(Subject subject) {
		return true;
	}
	
	@Override
	public boolean trainModel(Subject subject) {
		return true;
	}

	@Override
	public boolean evaluate(Subject subject) {
		return true;
	}
	
	@Override
	public boolean instrumentPredicates(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates,
			boolean useSober) {
		return true;
	}
	
	@Override
	public Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredicates(Subject subject, Set<String> allStatements, boolean useSober) {
		String srcPath = subject.getHome() + subject.getSsrc();
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
		Map<String, List<Integer>> file2LocationList = mapLocations2File(subject, allStatements);
		NoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NoSideEffectPredicateInstrumentVisitor(useSober);
		for (Entry<String, List<Integer>> entry : file2LocationList.entrySet()) {
			String relJavaPath = entry.getKey();
			String fileName = srcPath + "/" + relJavaPath;
			Set<Integer> locations = new HashSet<>(entry.getValue());
			CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSourceWithType(
					JavaFile.readFileToString(fileName), ASTParser.K_COMPILATION_UNIT, fileName, subject);
			instrumentVisitor.initOneRun(locations, srcPath, relJavaPath);
			unit.accept(instrumentVisitor);
			JavaFile.writeStringToFile(fileName, unit.toString());
			file2Line2Predicates.put(fileName, instrumentVisitor.getPredicates());
		}
		return file2Line2Predicates;
	}
	
	private Map<String, List<Integer>> mapLocations2File(Subject subject, Set<String> allStatements) {
		String srcPath = subject.getHome() + subject.getSsrc();
		Map<String, List<Integer>> file2LocationList = new HashMap<>();
		int allStmtCount = allStatements.size();
		int currentStmtCount = 1;
		for (String stmt : allStatements) {
			LevelLogger.debug("======================== [" + currentStmtCount + "/" + allStmtCount
					+ "] statements (statistical debugging) =================.");
			currentStmtCount++;
			String[] stmtInfo = stmt.split("#");
			if (stmtInfo.length != 2) {
				LevelLogger.error(__name__ + "#mapLocations2File statement parse error : " + stmt);
				System.exit(0);
			}
			Integer methodID = Integer.valueOf(stmtInfo[0]);
			int line = Integer.parseInt(stmtInfo[1]);
			if (line == 2317) {
				LevelLogger.debug(__name__ + "#mapLocations2File : exist");
			}
			String methodString = Identifier.getMessage(methodID);
			LevelLogger.debug("Current statement  : **" + methodString + "#" + line + "**");
			String[] methodInfo = methodString.split("#");
			if (methodInfo.length < 4) {
				LevelLogger.error(__name__ + "#mapLocations2File method info parse error : " + methodString);
				System.exit(0);
			}
			String clazz = methodInfo[0].replace(".", Constant.PATH_SEPARATOR);
			int index = clazz.indexOf("$");
			if (index > 0) {
				clazz = clazz.substring(0, index);
			}
			String relJavaPath = clazz + ".java";

			String fileName = srcPath + Constant.PATH_SEPARATOR + relJavaPath;

			List<Integer> list = file2LocationList.get(relJavaPath);
			if (list == null) {
				File file = new File(fileName);
				if (!file.exists()) {
					LevelLogger.error("Cannot find file : " + fileName);
					continue;
				}
				list = new LinkedList<>();
			}
			list.add(line);
			file2LocationList.put(relJavaPath, list);
		}
		return file2LocationList;
	}
	
}
