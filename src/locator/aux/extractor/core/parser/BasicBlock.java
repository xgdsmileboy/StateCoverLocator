/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor.core.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;

import locator.aux.extractor.core.feature.VarFeature;
import locator.aux.extractor.core.parser.Use.USETYPE;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;

/**
 * 
 * @author Jiajun
 *
 *         Jul 11, 2018
 */
public class BasicBlock {

	public enum BLOCKTYPE{
		FILE,
		TYPE,
		METHOD,
		IF,
		SWITCH,
		ENFOR,
		FOR,
		WHILE,
		DO,
		BLOCK,
		TRY,
		SYNC,
		CATCH,
		FINALLY,
		CASE,
		UNKNOWN
	}
	
	private final static boolean _record_global_variable = false;
	private static Set<Variable> _globalVariables = new HashSet<>();
	// private static Map<String, Use> _unresolvedUse = new HashMap<>();

	public static void addGlobalVariables(Variable variable) {
//		_globalVariables.add(variable);
	}

	public static Set<Variable> getGlobalVariables() {
		return _globalVariables;
	}

	private int _level;
	private String _file;
	private String _package;
	private String _publicClazz;
	private CompilationUnit _unit;
	private Set<Variable> _variables;
	private Set<ConstValue> _constants;
	private Set<BasicBlock> _children;
	private BasicBlock _parent;
	private BLOCKTYPE _blockType = BLOCKTYPE.UNKNOWN;
	private String _blockName = "Unknown";
	private Pair<Integer, Integer> _codeRange = new Pair<Integer, Integer>(0, 0);

	public BasicBlock(String file, String pkage, String clazz, CompilationUnit unit, int minLineNumber, int maxLineNumber, BasicBlock parent)
			throws IllegalArgumentException {
		setCodeRange(minLineNumber, maxLineNumber);
		_file = file;
		_package = pkage;
		_publicClazz = clazz;
		_unit = unit;
		_variables = new HashSet<>(7);
		_constants = new HashSet<>(7);
		_children = new HashSet<>(3);
		_parent = parent;
		_level = 0;
		if (parent != null) {
			_level = parent.getBlockLevel() + 1;
			parent.addChild(this);
		}
	}

	public void setCodeRange(int minLineNumber, int maxLineNumber) throws IllegalArgumentException {
		if (minLineNumber > maxLineNumber) {
			throw new IllegalArgumentException(
					"minLineRange should not be bigger than maxLineRange : " + minLineNumber + " " + maxLineNumber);
		}
		_codeRange.setFirst(minLineNumber);
		_codeRange.setSecond(maxLineNumber);
	}
	
	public void setBlockType(BLOCKTYPE type, String blockName) {
		_blockType = type;
		_blockName = blockName;
	}

	public void addVariables(Variable variable) {
		_variables.add(variable);
		variable.setParentBlock(this);
		variable.setClazz(_publicClazz);
		variable.setPackage(_package);
	}
	
	public void addConstant(ConstValue constValue) {
		_constants.add(constValue);
		constValue.setParentBlock(this);
	}

	public void addVariableUse(String varName, Use use) {
		Variable variable = null;
		if (use.isFieldUse()) {
			variable = topDownSearchVariable(varName);
		} else {
			variable = bottomUpSearchVariable(varName);
		}

		if (variable == null) {
			if(_record_global_variable) {
				LevelLogger.warn("@BasicBlock Cannot find defined variable with name : " + varName);
			}
		} else {
			variable.addUse(use);
		}
	}

	private Variable topDownSearchVariable(String varName) {
		Variable temp = null;
		BasicBlock basicBlock = this;
		while (basicBlock != null) {
			for (Variable variable : basicBlock.getDefinedVariables()) {
				if (varName.equals(variable.getName())) {
					temp = variable;
					break;
				}
			}
			basicBlock = basicBlock.getParent();
		}
		return temp;
	}

	private Variable bottomUpSearchVariable(String varName) {
		for (Variable variable : _variables) {
			if (varName.equals(variable.getName())) {
				return variable;
			}
		}
		if (getParent() != null) {
			return getParent().bottomUpSearchVariable(varName);
		} else {
			for (Variable variable : getGlobalVariables()) {
				if (varName.equals(variable.getName())) {
					return variable;
				}
			}
		}
		return null;
	}

	public void addChild(BasicBlock child) {
		_children.add(child);
	}

	public String getSourceFile() {
		return _file;
	}

	public BasicBlock getParent() {
		return _parent;
	}
	
	public CompilationUnit getCompilationUnit() {
		return _unit;
	}

	public int getBlockLevel() {
		return _level;
	}
	
	public BLOCKTYPE getBlockType() {
		return _blockType;
	}
	
	public String getBlockName() {
		return _blockName;
	}

	public Set<Variable> getDefinedVariables() {
		return _variables;
	}

	public Set<BasicBlock> getChildrenBlock() {
		return _children;
	}
	
	public Set<ConstValue> getConstants() {
		return _constants;
	}

	public Pair<Integer, Integer> getCodeRange() {
		return _codeRange;
	}

	public BasicBlock getMinimalBasicBlock(int line) {
		if (line < _codeRange.getFirst() || _codeRange.getSecond() < line) {
			return null;
		}
		for (BasicBlock basicBlock : _children) {
			BasicBlock block = basicBlock.getMinimalBasicBlock(line);
			if (block != null) {
				return block;
			}
		}
		return this;
	}

	public Set<Variable> getAllValidVariables(int line) {
		BasicBlock basicBlock = getMinimalBasicBlock(line);
		Set<Variable> variables = new HashSet<>();
		while (basicBlock != null) {
			for (Variable variable : basicBlock._variables) {
				if (variable.getLineNumber() < line) {
					variables.add(variable);
				}
			}
			basicBlock = basicBlock.getParent();
		}
		variables.addAll(getGlobalVariables());
		return Collections.unmodifiableSet(variables);
	}

	public Set<Variable> getAllValidVariables(int line, Type type) {
		if (type == null) {
			return getAllValidVariables(line);
		} else {
			return getAllValidVariables(line, type.toString());
		}
	}

	public Set<Variable> getAllValidVariables(int line, String type) {
		BasicBlock basicBlock = getMinimalBasicBlock(line);
		Set<Variable> variables = new HashSet<>();
		while (basicBlock != null) {
			for (Variable variable : basicBlock._variables) {
				if (variable.getLineNumber() < line && type.equals(variable.getType().toString())) {
					variables.add(variable);
				}
			}
			basicBlock = basicBlock.getParent();
		}
		variables.addAll(getGlobalVariables());
		return Collections.unmodifiableSet(variables);
	}
	
	public Set<Variable> recursivelyGetVariables() {
		Set<Variable> variables = new HashSet<>();
		variables.addAll(_variables);
		for(BasicBlock basicBlock : getChildrenBlock()) {
			variables.addAll(basicBlock.recursivelyGetVariables());
		}
		return Collections.unmodifiableSet(variables);
	}
	
	public Set<ConstValue> getAllConstValueUsedUp2Now() {
		Set<ConstValue> constValues = new HashSet<>();
		constValues.addAll(_constants);
		BasicBlock parent = this.getParent();
		while(parent != null) {
			if(parent.getBlockType() == BLOCKTYPE.METHOD) {
				break;
			}
			constValues.addAll(parent.getConstants());
			parent = parent.getParent();
		}
		return constValues;
	}
	
	public Set<Use> recursivelyGetUses() {
		Set<Use> uses = new HashSet<>();
		for(Variable variable : _variables) {
			uses.addAll(variable.getUseSet());
		}
		for(BasicBlock basicBlock : getChildrenBlock()) {
			uses.addAll(basicBlock.recursivelyGetUses());
		}
		return Collections.unmodifiableSet(uses);
	}
	
	public List<VarFeature> getUsedVarFeatures(int line) {
		List<VarFeature> list = new LinkedList<>();
		Set<Use> uses = getVariableUses(line);
		for(Use use : uses) {
			VarFeature varFeature = new VarFeature(use);
			list.add(varFeature);
		}
		return list;
	}

	public Set<Use> getVariableUses(int line) {
		BasicBlock basicBlock = getMinimalBasicBlock(line);
		Set<Use> uses = new HashSet<>();
		Set<Use> filter = new HashSet<>();
		while (basicBlock != null) {
			for (Variable variable : basicBlock._variables) {
				for (Use use : variable.getUseSet()) {
					if (use.getLineNumber() == line) {
						if (use.getUseType() == USETYPE.DEFINE) {
							filter.add(use);
						} else {
							uses.add(use);
						}
					}
				}
			}
			basicBlock = basicBlock.getParent();
		}
		Set<Use> filtered = new HashSet<>();
		for (Use use : uses) {
			boolean canUse = true;
			for (Use f : filter) {
				if (use.getVariableDefine() == f.getVariableDefine() && use.getColumnNumber() < f.getColumnNumber()) {
					canUse = false;
					break;
				}
			}
			if (canUse) {
				filtered.add(use);
			}
		}
		return Collections.unmodifiableSet(filtered);
	}

	public void dump() {
		System.out.println("FILE : " + getSourceFile());
		System.out.println("PACKAGE : " + _package);
		System.out.println("CLASS : " + _publicClazz);
		System.out.println("Code range : " + getCodeRange().toString());
		System.out.println("Variables : ");
		List<Variable> variables = new ArrayList<>(getDefinedVariables());
		Collections.sort(variables, new Comparator<Variable>() {
			@Override
			public int compare(Variable o1, Variable o2) {
				int line = o1.getLineNumber() - o2.getLineNumber();
				if (line == 0) {
					return o1.getColumn() - o2.getColumn();
				}
				return line;
			}
		});
		for (Variable variable : variables) {
			variable.dump();
		}

		List<BasicBlock> children = new ArrayList<>(getChildrenBlock());
		Collections.sort(children, new Comparator<BasicBlock>() {

			@Override
			public int compare(BasicBlock o1, BasicBlock o2) {
				int line = o1.getCodeRange().getFirst() - o2.getCodeRange().getFirst();
				if (line == 0) {
					return o1.getCodeRange().getSecond() - o2.getCodeRange().getSecond();
				}
				return line;
			}

		});

		for (BasicBlock child : getChildrenBlock()) {
			child.dump();
		}

	}

}
