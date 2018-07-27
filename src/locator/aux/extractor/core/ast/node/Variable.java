/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor.core.ast.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;

/**
 * @author Jiajun
 * @date Jul 7, 2018
 */
public class Variable {

	private String _package;
	private String _clazz;
	private String _file;
	private int _line;
	private int _column;
	private int _modifier;
	private Expression _initializer;
	private String _name;
	private Type _type;
	private BasicBlock _basicBlock;
	
	private Set<Use> _uSet;

	public Variable(String file, int line, int column, int modifier, Expression initializer, String name, Type type) {
		_modifier = modifier;
		_initializer = initializer;
		_file = file;
		_line = line;
		_column = column;
		_name = name;
		_type = type;
		_uSet = new HashSet<>();
	}
	
	public void addUse(Use use) {
		_uSet.add(use);
		use.setVariable(this);
	}
	
	public void setPackage(String pkg) {
		_package = pkg;
	}
	
	public void setClazz(String clazz) {
		_clazz = clazz;
	}
	
	public void setParentBlock(BasicBlock basicBlock) {
		_basicBlock = basicBlock;
	}
	
	public BasicBlock getParentBlock() {
		return _basicBlock;
	}
	
	public int getModifiers() {
		return _modifier;
	}
	
	public Expression getInitializer() {
		return _initializer;
	}
	
	public String getPackage() {
		return _package;
	}
	
	public String getClazz() {
		return _clazz;
	}
	
	public String getFile() {
		return _file;
	}
	
	public int getLineNumber() {
		return _line;
	}
	
	public int getColumn() {
		return _column;
	}
	
	public String getName() {
		return _name;
	}
	
	public Type getType() {
		return _type;
	}
	
	public Set<Use> getUseSet() {
		return _uSet;
	}
	
	public void dump() {
		System.out.print("LINE : " + getLineNumber());
		System.out.print(", COLUMN : " + getColumn());
		System.out.println(", VAR : " + getName() + "(" + _type + ")");
		List<Use> uses = new ArrayList<>(getUseSet());
		Collections.sort(uses, new Comparator<Use>() {

			@Override
			public int compare(Use o1, Use o2) {
				int line = o1.getLineNumber() - o2.getLineNumber();
				if(line == 0) {
					return o1.getColumnNumber() - o2.getColumnNumber();
				}
				return line;
			}
		});
		for(Use use : uses) {
			use.dump();
		}
	}
	
	@Override
	public String toString() {
		return "[VAR ]" + _name + "(" + _type.toString() + ")(" + _line + ", " + _column + ")";
	}
}
