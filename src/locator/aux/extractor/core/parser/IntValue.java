package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class IntValue extends ConstValue {

	private Integer _value = 0;
	
	public IntValue(String file, int line, int column, Integer value, Type type) {
		super(file, line, column, type);
		_value = value;
	}

	@Override
	public Integer getValue() {
		return _value;
	}

}
