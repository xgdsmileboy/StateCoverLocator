package locator.inst.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import locator.common.util.Pair;

public abstract class NoSideEffectPreidcateInstrumentVisitor extends TraversalVisitor{

	protected Map<Integer, List<Pair<String, String>>> _predicates = new HashMap<>();
	
	public abstract void initOneRun(Set<Integer> lines, String srcPath, String relJavaPath);
	
	public Map<Integer, List<Pair<String, String>>> getPredicates() {
		return _predicates;
	}
	
}
