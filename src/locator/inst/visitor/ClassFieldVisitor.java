package locator.inst.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import locator.common.java.Pair;

public class ClassFieldVisitor extends TraversalVisitor {
	
	private List<Pair<String, String>> classFields = new ArrayList<Pair<String, String>>();
	
	public List<Pair<String, String>> getFields() {
		return classFields;
	}
	
	public boolean visit(FieldDeclaration node) {
		String type = node.getType().toString();
		boolean isStatic = Modifier.isStatic(node.getModifiers());
		List<VariableDeclarationFragment> fields = node.fragments();
		for(VariableDeclarationFragment field : fields) {
			if (isStatic) {				
				classFields.add(new Pair<String, String>(_clazzName + "." + field.getName().toString(), type));
			} else {
				classFields.add(new Pair<String, String>("this." + field.getName().toString(), type));
			}
		}
		return true;
	}
}
