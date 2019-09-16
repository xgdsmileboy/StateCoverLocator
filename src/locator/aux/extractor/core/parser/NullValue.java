package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.AST;

public class NullValue extends ConstValue {

	public NullValue(String file, int line, int column) {
		super(file, line, column, AST.newAST(AST.JLS8).newWildcardType());
	}

	@Override
	public Object getValue() {
		return null;
	}

}
