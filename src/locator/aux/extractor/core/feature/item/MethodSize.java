/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor.core.feature.item;

import locator.aux.extractor.core.parser.BasicBlock;
import locator.aux.extractor.core.parser.BasicBlock.BLOCKTYPE;
import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;
import locator.common.util.Pair;

/**
 * @author Jiajun
 * @date Aug 5, 2018
 */
public class MethodSize extends Feature {

	
	private static MethodSize instance = null; 
	private Use _use;
	private int _size;
	
	
	protected MethodSize() {
		super("MethodSize");
	}
	
	private MethodSize(Use use) {
		this();
		_use = use;
	}
	
	public static MethodSize getInstance() {
		if(instance == null) {
			instance = new MethodSize();
		}
		return instance;
	}

	@Override
	public Feature extractFeature(Use use) {
		MethodSize methodSize = new MethodSize(use);
		BasicBlock basicBlock = use.getParentBlock();
		if(basicBlock == null) {
			methodSize._size = Integer.MAX_VALUE;
		} else {
			// find the biggest block
			while(basicBlock.getParent() != null) {
				if(basicBlock.getBlockType() == BLOCKTYPE.METHOD) {
					break;
				}
				basicBlock = basicBlock.getParent();
			}
			Pair<Integer, Integer> range = basicBlock.getCodeRange();
			methodSize._size = range.getSecond() - range.getFirst();
		}
		return methodSize;
	}

	@Override
	public String extractFeature(Variable variable, int line) {
		BasicBlock basicBlock = variable.getParentBlock();
		String value = String.valueOf(Integer.MAX_VALUE);
		if(basicBlock == null) {
			return value;
		}
		basicBlock = basicBlock.getMinimalBasicBlock(line);
		if(basicBlock == null) {
			return value;
		} else {
			while(basicBlock.getParent() != null) {
				if(basicBlock.getBlockType() == BLOCKTYPE.METHOD) {
					break;
				}
				basicBlock = basicBlock.getParent();
			}
			Pair<Integer, Integer> range = basicBlock.getCodeRange();
			value = String.valueOf(range.getSecond() - range.getFirst());
		}
		return value;
	}

	@Override
	public String getStringFormat() {
		return String.valueOf(_size);
	}

}
