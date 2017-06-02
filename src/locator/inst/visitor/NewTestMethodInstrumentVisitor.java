/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.inst.gen.GenStatement;

/**
 * @author Jiajun
 * @date Jun 2, 2017
 */
public class NewTestMethodInstrumentVisitor extends TraversalVisitor {

	private final static String __name__ = "@NewTestMethodInstrumentVisitor ";
	
	private Set<Integer> _failedTest = new HashSet<>();

	public NewTestMethodInstrumentVisitor(Set<Integer> failedMethods) {
		_failedTest = failedMethods;
		_methodFlag = Constant.INSTRUMENT_K_TEST;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		String name = node.getName().getFullyQualifiedName();
		
		if ((name.equals("setUp") || name.equals("countTestCases") || name.equals("createResult") || name.equals("run")
				|| name.equals("runBare") || name.equals("runTest") || name.equals("tearDown")
				|| name.equals("toString") || name.equals("getName") || name.equals("setName"))) {
			return true;
		}
		
		// filter those functional methods in test class path, test method name
		// starting with "test" in Junit 3 while with annotation as "@Test" in
		// Junit 4,
		// TODO should be optimized since the "contain" method is time consuming
		boolean hasAnnotation = false;
		for(Object object : node.modifiers()){
			if(object instanceof MarkerAnnotation){
				if(object.toString().equals("@Test")){
					hasAnnotation = true;
					break;
				}
			}
		}
		
		if (!name.startsWith("test") && !hasAnnotation) {
			return true;
		}

		String message = buildMethodInfoString(node);
		if (message == null) {
			return true;
		}

		int keyValue = Identifier.getIdentifier(message);

		boolean succTest = true;

		if (_failedTest.contains(keyValue)) {
			succTest = false;
		}

		if (node.getBody() != null) {
			Block body = node.getBody();
			List<ASTNode> backupStatement = new ArrayList<>();
			AST ast = AST.newAST(AST.JLS8);

			ASTNode thisOrSuperStatement = null;
			if (body.statements().size() > 0) {
				ASTNode astNode = (ASTNode) body.statements().get(0);
				int startIndex = 0;
				if (astNode instanceof SuperConstructorInvocation
						|| (astNode instanceof ConstructorInvocation && astNode.toString().startsWith("this"))) {
					thisOrSuperStatement = ASTNode.copySubtree(ast, astNode);
					startIndex = 1;
				}
				for (; startIndex < body.statements().size(); startIndex++) {
					ASTNode statement = (ASTNode) body.statements().get(startIndex);
					backupStatement.add(ASTNode.copySubtree(ast, statement));
				}
			}

			// // optimize instrument
			// message = Constant.INSTRUMENT_FLAG + _methodFlag + "#" +
			// String.valueOf(keyValue);

			// int lineNumber =
			// _cu.getLineNumber(node.getBody().getStartPosition());

			// Statement insert = GenStatement.genASTNode(message, lineNumber);
			Statement insert = GenStatement.genInstrumentStatementForTestWithReset(succTest);

			body.statements().clear();
			if (thisOrSuperStatement != null) {
				body.statements().add(ASTNode.copySubtree(body.getAST(), thisOrSuperStatement));
			}
			body.statements().add(ASTNode.copySubtree(body.getAST(), insert));
			for (ASTNode statement : backupStatement) {
				body.statements().add(ASTNode.copySubtree(body.getAST(), statement));
			}
		}

		return true;
	}

}
