package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class EnumValue extends ConstValue {

	private Object _value;
	
	public EnumValue(String file, int line, int column, Object object, Type type) {
		super(file, line, column, type);
		_value = object;
	}

	@Override
	public Object getValue() {
		return _value;
	}

}
