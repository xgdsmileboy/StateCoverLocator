package locator.inst.visitor.feature;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import locator.common.java.JavaFile;

public class SideEffectAnalysis {
	public static boolean hasSideEffect(String expression) {
		ASTNode node = JavaFile.genASTFromSource(expression, ASTParser.K_EXPRESSION);

		SideEffectAnalysisVisitor visitor = new SideEffectAnalysisVisitor();
		node.accept(visitor);
		return visitor.hasSideEffect();
	}
	
	static class SideEffectAnalysisVisitor extends ASTVisitor {
		private boolean _hasSideEffect;
		
		public boolean hasSideEffect() {
			return _hasSideEffect;
		}
		
		public boolean visit(PrefixExpression node) {
			if (node.getOperator().equals(PrefixExpression.Operator.DECREMENT)
					|| node.getOperator().equals(PrefixExpression.Operator.INCREMENT)) {
				_hasSideEffect = true;
				return false;
			}
			return true;
		}

		public boolean visit(PostfixExpression node) {
			_hasSideEffect = true;
			return false;
		}

		public boolean visit(Assignment node) {
			_hasSideEffect = true;
			return false;
		}
	}
}
