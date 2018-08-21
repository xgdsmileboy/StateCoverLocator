package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class StringValue extends ConstValue {

	private String _value = "";
	
	public StringValue(String file, int line, int column, String value, Type type) {
		super(file, line, column, type);
		_value = value;
	}

	@Override
	public String getValue() {
		return _value;
	}
	
	
}
