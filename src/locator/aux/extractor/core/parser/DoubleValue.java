package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class DoubleValue extends ConstValue {

	private Double _value;
	
	public DoubleValue(String file, int line, int column, Double value, Type type) {
		super(file, line, column, type);
		_value = value;
	}

	@Override
	public Double getValue() {
		return _value;
	}
	
}
