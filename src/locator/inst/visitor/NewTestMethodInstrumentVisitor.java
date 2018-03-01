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
import org.eclipse.jdt.core.dom.TryStatement;

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
	
	private boolean _useSober = false;

	public NewTestMethodInstrumentVisitor(Set<Integer> failedMethods, boolean useSober) {
		_failedTest = failedMethods;
		_methodFlag = Constant.INSTRUMENT_K_TEST;
		_useSober = useSober;
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
		
		// bug fix: does not instrument test cases with parameters 2018-3-1
		if (!name.startsWith("test") && !hasAnnotation || node.parameters().size() > 0) {
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
			AST ast = AST.newAST(Constant.AST_LEVEL);

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
			

			body.statements().clear();
			Statement insert = null;
			if (_useSober) {
				insert = GenStatement.genInstrumentStatementForTestWithResetTrueOrFalse(succTest);
			} else {
				insert = GenStatement.genInstrumentStatementForTestWithReset(succTest);
			}
			Block tryBody = ast.newBlock();
			if (thisOrSuperStatement != null) {
				tryBody.statements().add(ASTNode.copySubtree(tryBody.getAST(), thisOrSuperStatement));
			}
			tryBody.statements().add(ASTNode.copySubtree(tryBody.getAST(), insert));
			for (ASTNode statement : backupStatement) {
				tryBody.statements().add(ASTNode.copySubtree(tryBody.getAST(), statement));
			}
			TryStatement tryStatement = ast.newTryStatement();
			tryStatement.setBody(tryBody);
			Statement dumpStatement = null;
			if (_useSober) {
				dumpStatement = GenStatement.genInstrumentStatementForResultDumpTrueOrFalse();
			} else {
				dumpStatement = GenStatement.genInstrumentStatementForResultDump();
			}
			Block dumpBlock = ast.newBlock();
			dumpBlock.statements().add(ASTNode.copySubtree(dumpBlock.getAST(), dumpStatement));
			tryStatement.setFinally(dumpBlock);
			body.statements().add(ASTNode.copySubtree(body.getAST(), tryStatement));
		}

		return true;
	}

}
