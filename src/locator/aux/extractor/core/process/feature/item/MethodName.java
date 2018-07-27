/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.process.feature.item;

import locator.aux.extractor.core.ast.node.BasicBlock;
import locator.aux.extractor.core.ast.node.Use;
import locator.aux.extractor.core.ast.node.Variable;
import locator.aux.extractor.core.ast.node.BasicBlock.BLOCKTYPE;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class MethodName extends Feature {

	private static MethodName instance = null;
	private Use _use;
	private String _name;
	
	public static MethodName getInstance() {
		if(instance == null) {
			instance = new MethodName();
		}
		return instance;
	}
	
	protected MethodName() {
		super("MethodName");
	}
	
	private MethodName(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		MethodName methodName = new MethodName(use);
		BasicBlock basicBlock = use.getParentBlock();
		while(basicBlock != null && basicBlock.getBlockType() != BLOCKTYPE.METHOD) {
			basicBlock = basicBlock.getParent();
		}
		if(basicBlock == null) {
			methodName._name = "DUMMY";
		} else {
			methodName._name = basicBlock.getBlockName();
		}
		return methodName;
	}

	@Override
	public String getStringFormat() {
		return _name;
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		BasicBlock basicBlock = variable.getParentBlock();
		if(basicBlock == null) {
			return "DUMMY";
		}
		basicBlock = basicBlock.getMinimalBasicBlock(line);
		while(basicBlock != null && basicBlock.getBlockType() != BLOCKTYPE.METHOD) {
			basicBlock = basicBlock.getParent();
		}
		if(basicBlock != null) {
			return basicBlock.getBlockName();
		}
		return "DUMMY";
	}
	
}
