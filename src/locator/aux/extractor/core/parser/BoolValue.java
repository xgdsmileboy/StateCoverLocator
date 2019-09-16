package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class BoolValue extends ConstValue {

	private Boolean _value = false;
	
	public BoolValue(String file, int line, int column, Boolean value, Type type) {
		super(file, line, column, type);
		_value = value;
	}

	@Override
	public Boolean getValue() {
		return _value;
	}

}
