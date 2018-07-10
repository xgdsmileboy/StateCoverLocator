package locator.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.inst.visitor.NewNoSideEffectPredicateInstrumentVisitor;

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
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredicates(Subject subject, Set<String> allStatements, boolean useSober) {
		String srcPath = subject.getHome() + subject.getSsrc();
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
		Map<String, List<Integer>> file2LocationList = mapLocations2File(subject, allStatements);
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(
				useSober);
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
	
	@Override
	public boolean instrumentPredicates(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates,
			boolean useSober) {
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

}
