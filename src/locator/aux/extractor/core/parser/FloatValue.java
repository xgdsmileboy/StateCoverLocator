package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class FloatValue extends ConstValue {

	private Float _value = 0.0f;
	
	public FloatValue(String file, int line, int column, Float value, Type type) {
		super(file, line, column, type);
		_value = value;
	}

	@Override
	public Float getValue() {
		return _value;
	}

}
