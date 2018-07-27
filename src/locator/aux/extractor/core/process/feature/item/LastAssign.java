/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.process.feature.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import locator.aux.extractor.core.ast.node.BasicBlock;
import locator.aux.extractor.core.ast.node.Use;
import locator.aux.extractor.core.ast.node.Variable;
import locator.aux.extractor.core.ast.node.Use.USETYPE;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 *
 * Jul 19, 2018
 */
public class LastAssign extends Feature {

	
	private static LastAssign instance = null;
	
	private Use _assignUse = null; 
	private Use _use = null;
	private int _distance = Integer.MAX_VALUE; 
	
	public static LastAssign getInstance() {
		if(instance == null) {
			instance = new LastAssign(); 
		}
		return instance;
	}
	
	protected LastAssign() {
		super("LastAssign");
	}
	
	private LastAssign(Use use, Use assignUse) {
		super("LastAssign");
		_use = use;
		_assignUse = assignUse;
	}
	
	@Override
	public Feature extractFeature(Use use) {
		Variable variable = use.getVariableDefine();
		Use assignUse = findAssignUse(variable, use.getLineNumber(), use.getColumnNumber(),
				use.getParentBlock().getBlockLevel());

		LastAssign lastAssign = new LastAssign(use, assignUse);
		if (assignUse == null) {
			LevelLogger.warn("@LastAssign Failed to find use of variable write.");
		} else {
			lastAssign._distance = use.getLineNumber() - assignUse.getLineNumber();
		}
		return lastAssign;
	}
	
	private Use findAssignUse(Variable variable, int line, int column, int blockLevel) {
		List<Use> uses = new ArrayList<>(variable.getUseSet());
		// sort by descending order of line number / column number
		Collections.sort(uses, new Comparator<Use>() {
			@Override
			public int compare(Use o1, Use o2) {
				int gap = o2.getLineNumber() - o1.getLineNumber();
				if(gap == 0) {
					return o2.getColumnNumber() - o1.getColumnNumber();
				} else {
					return gap;
				}
			}
		});

		Use assignUse = null;
		for (Use u : uses) {
			if (u.getUseType() == USETYPE.WRITE || u.getUseType() == USETYPE.DEFINE) {
				if (u.getLineNumber() > line || u.getParentBlock().getBlockLevel() > blockLevel) {
					continue;
				}
				if (u.getLineNumber() == line) {
					if(u.getColumnNumber() <= column) {
						assignUse = u;
						break;
					}
				} else {
					assignUse = u;
					break;
				}	
			}
		}
		return assignUse;
	}
	
	@Override
	public String getStringFormat() {
		return String.valueOf(_distance);
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		BasicBlock basicBlock = variable.getParentBlock();
		if(basicBlock == null) {
			return String.valueOf(Integer.MAX_VALUE);
		}
		int level = basicBlock.getMinimalBasicBlock(line).getBlockLevel();
		Use assignUse = findAssignUse(variable, line, 0, level);
		if(assignUse == null) {
			return String.valueOf(Integer.MAX_VALUE);
		}
		return String.valueOf(line - assignUse.getLineNumber());
	}

}
