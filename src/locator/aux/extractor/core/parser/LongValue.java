package locator.aux.extractor.core.parser;

import org.eclipse.jdt.core.dom.Type;

public class LongValue extends ConstValue {

	private Long _value;
	
	public LongValue(String file, int line, int column, Long value, Type type) {
		super(file, line, column, type);
		_value = value;
	}

	@Override
	public Long getValue() {
		return _value;
	}

}
