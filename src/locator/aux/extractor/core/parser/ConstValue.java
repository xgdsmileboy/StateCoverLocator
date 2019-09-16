package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public abstract class ConstValue {

	protected String _file;
	protected int _line;
	protected int _column;
	protected Type _type;
	protected BasicBlock _basicBlock;
	

	public ConstValue(String file, int line, int column, Type type) {
		_file = file;
		_line = line;
		_column = column;
		_type = type;
	}

	public abstract Object getValue();
	
	public void setParentBlock(BasicBlock parent) {
		_basicBlock = parent;
	}
	
	public int getLineNumber() {
		return _line;
	}
	
	public Type getType() {
		return _type;
	}
	
	public BasicBlock getParentBlock() {
		return _basicBlock;
	}
	
	public boolean isPrimitive() {
		return false;
	}
	
	public boolean isNumeric() {
		return false;
	}
	
}
