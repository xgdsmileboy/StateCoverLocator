package locator.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;
import locator.common.util.Utils;
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
		NoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NoSideEffectPredicateInstrumentVisitor(useSober,
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
			file2Line2Predicates.put(fileName, instrumentVisitor.getPredicates());
		}
		LevelLogger.debug("-------------------FOR DEBUG----------------------");
		Utils.printPredicateInfo(file2Line2Predicates, subject, _predicates_backup_file);
		return file2Line2Predicates;
	}
	
}
