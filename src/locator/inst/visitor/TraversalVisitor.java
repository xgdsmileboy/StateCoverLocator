/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.java.Method;


/**
 * Traverse a {@code CompilationUnit}
 * 
 * @author Jiajun
 *
 */
public abstract class TraversalVisitor extends ASTVisitor {
	
	public final boolean traverse(CompilationUnit compilationUnit){
		compilationUnit.accept(this);
		return true;
	}
	
	public abstract void reset();
	
	public abstract void setFlag(String methodFlag);
	
	public abstract void setMethod(Method method); 
	
}
