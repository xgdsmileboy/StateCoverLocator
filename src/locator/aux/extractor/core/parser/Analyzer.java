/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor.core.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import locator.aux.extractor.core.parser.BasicBlock.BLOCKTYPE;
import locator.aux.extractor.core.parser.Use.USETYPE;
import locator.common.java.JavaFile;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date Jul 7, 2018
 */
public class Analyzer {

	private static Map<String, BasicBlock> _cache = new HashMap<>();
	
	public static void clearCache() {
		_cache.clear();
	}
	
	public static BasicBlock analyze(String relJavaFile, String srcBase) {
		String file = new File(srcBase + "/" + relJavaFile).getAbsolutePath();
		BasicBlock basicBlock = _cache.get(file);
		if(basicBlock != null) {
			return basicBlock;
		}
		CompilationUnit unit = JavaFile.genASTFromFileWithType(file, srcBase);
		String clazz = relJavaFile.substring(relJavaFile.lastIndexOf('/') + 1, relJavaFile.length() - ".java".length());
		DefUseAnalyzer analyzer = null;
		try {
			analyzer = new DefUseAnalyzer(file, unit, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		basicBlock = analyzer.getRoot();
		_cache.put(file, basicBlock);
		return basicBlock;
	}

	private static class DefUseAnalyzer {

		private String _file;
		private CompilationUnit _unit;
		private BasicBlock _basicBlock;
		private Stack<BasicBlock> _visitedBlock = new Stack<>();
		private Stack<StmtType> _visitedStatement = new Stack<>();
		private String _package;
		private String _publicClazz;

		public DefUseAnalyzer(String file, CompilationUnit unit, String publicClazz) throws Exception {
			_file = file;
			_unit = unit;
			_publicClazz = publicClazz;
			_package = unit.getPackage().getName().getFullyQualifiedName();
			_basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, 0, 0, null);
			String fileName = file.substring(file.lastIndexOf("/") + 1);
			_basicBlock.setBlockType(BLOCKTYPE.FILE, fileName);
			_visitedBlock.add(_basicBlock);
			for (Object object : unit.types()) {
				process((ASTNode) object, USETYPE.UNKNOWN, null);
			}
			_visitedBlock.pop();
		}

		public BasicBlock getRoot() {
			return _basicBlock;
		}

		private boolean visit(TypeDeclaration node, USETYPE useType, Expression booleanExpr) {

			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int position = node.getStartPosition() + node.getLength();
			// Bug fix : should not reach the last character
			int maxLineNumber = _unit.getLineNumber(position - 1);

			BasicBlock basicBlock = null;
			if (_publicClazz.equals(node.getName().getFullyQualifiedName())) {
				_basicBlock.setCodeRange(minLineNumber, maxLineNumber);
				basicBlock = _basicBlock;
			} else {
				basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
						_visitedBlock.peek());
				basicBlock.setBlockType(BLOCKTYPE.TYPE, node.getName().getFullyQualifiedName());
			}

			// process fields in class
			if (!Modifier.isPrivate(node.getModifiers())) {
				for (FieldDeclaration fd : node.getFields()) {
					int modifier = fd.getModifiers();
					Type type = null;
					// global variables
					if (Modifier.isStatic(modifier)) {
						if (Modifier.isPublic(modifier) /*&& !Modifier.isFinal(modifier)*/) {
							for (Object fragment : fd.fragments()) {
								VariableDeclarationFragment vdf = (VariableDeclarationFragment) fragment;
								type = parseArrayType(fd.getType(), vdf.getExtraDimensions());
								Name name = vdf.getName();
								int line = _unit.getLineNumber(name.getStartPosition());
								int column = _unit.getColumnNumber(name.getStartPosition());
								Variable variable = new Variable(_file, line, column, modifier, vdf.getInitializer(),
										name.getFullyQualifiedName(), type);
								variable.isField();
								Use use = new Use(line, column, vdf, null, USETYPE.DEFINE, StmtType.FIELDDECL, basicBlock);
								variable.addUse(use);
								BasicBlock.addGlobalVariables(variable);
							}
						}
						// local variables
					} else /*if (!Modifier.isFinal(modifier)) */{
						for (Object fragment : fd.fragments()) {
							VariableDeclarationFragment vdf = (VariableDeclarationFragment) fragment;
							type = parseArrayType(fd.getType(), vdf.getExtraDimensions());
							Name name = vdf.getName();
							int line = _unit.getLineNumber(name.getStartPosition());
							int column = _unit.getColumnNumber(name.getStartPosition());
							Variable variable = new Variable(_file, line, column, modifier, vdf.getInitializer(),
									name.getFullyQualifiedName(), type);
							variable.setField();
							Use use = new Use(line, column, vdf, null, USETYPE.DEFINE, StmtType.FIELDDECL, basicBlock);
							variable.addUse(use);
							basicBlock.addVariables(variable);
						}
					}
				}
			}

			// process method body
			_visitedBlock.push(basicBlock);
			for (MethodDeclaration methodDeclaration : node.getMethods()) {
				process(methodDeclaration, USETYPE.UNKNOWN, null);
			}
			_visitedBlock.pop();
			return true;
		}

		private boolean visit(EnumDeclaration node, USETYPE useType, Expression booleanExpr) {

			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());

			BasicBlock basicBlock = null;
			if (_publicClazz.equals(node.getName().getFullyQualifiedName())) {
				_basicBlock.setCodeRange(minLineNumber, maxLineNumber);
				basicBlock = _basicBlock;
			} else {
				basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
						_visitedBlock.peek());
				basicBlock.setBlockType(BLOCKTYPE.TYPE, node.getName().getFullyQualifiedName());
			}

			// process enum constant
			if (!Modifier.isPrivate(node.getModifiers())) {
				for (Object object : node.enumConstants()) {
					EnumConstantDeclaration ecd = (EnumConstantDeclaration) object;
					Name name = ecd.getName();
					int line = _unit.getLineNumber(name.getStartPosition());
					int column = _unit.getColumnNumber(name.getStartPosition());
					Variable variable = new Variable(_file, line, column, ecd.getModifiers(), null,
							name.getFullyQualifiedName(), genPrimitiveType(ecd.getAST(), PrimitiveType.INT));
					variable.setField();
					Use use = new Use(line, column, ecd, null, USETYPE.DEFINE, StmtType.FIELDDECL, basicBlock);
					variable.addUse(use);
					BasicBlock.addGlobalVariables(variable);
				}
			}

			// process method body
			_visitedBlock.push(basicBlock);
			for (Object object : node.bodyDeclarations()) {
				if (object instanceof MethodDeclaration) {
					process((MethodDeclaration) object, USETYPE.UNKNOWN, null);
				}
			}
			_visitedBlock.pop();
			return true;
		}
		
		/**
		 * declaration and a constructor declaration.
		 *   MethodDeclaration:
		 *      [ Javadoc ] { ExtendedModifier } [ < TypeParameter { , TypeParameter } > ] ( Type | void )
		 *         Identifier (
		 *            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
		 *         ) { Dimension }
		 *         [ throws Type { , Type } ]
		 *         ( Block | ; )
		 *   ConstructorDeclaration:
		 *      [ Javadoc ] { ExtendedModifier } [ < TypeParameter { , TypeParameter } > ]
		 *         Identifier (
		 *            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
		 *         ) { Dimension }
		 *         [ throws Type { , Type } ]
		 *         ( Block | ; )
		 */
		public boolean visit(MethodDeclaration node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber, _visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.METHOD, node.getName().getFullyQualifiedName());
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.METHODECL);
			for (Object arg : node.parameters()) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) arg;
				Type type = parseArrayType(svd.getType(), node.getExtraDimensions());
				SimpleName name = svd.getName();
				int line = _unit.getLineNumber(name.getStartPosition());
				int column = _unit.getColumnNumber(name.getStartPosition());
				Variable variable = new Variable(_file, line, column, node.getModifiers(), svd.getInitializer(), name.getFullyQualifiedName(), type);
				variable.setArgument();
				Use use = new Use(line, column, node, booleanExpr, USETYPE.DEFINE, StmtType.METHODECL, basicBlock);
				variable.addUse(use);
				basicBlock.addVariables(variable);
				process(svd.getInitializer(), USETYPE.READ, booleanExpr);
			}
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}

		/*****************************************************************************/
		/***************************** Statement Parser *************************/
		/*****************************************************************************/
		/*
		 * AssertStatement:
		 *   assert Expression [ : Expression ] ;
		 */
		private boolean visit(AssertStatement node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.ASSERT);
			process(node.getExpression(), USETYPE.READ, node.getExpression());
			_visitedStatement.pop();
			return true;
		}
		
		/*
		 * Block statement AST node type.
		 *   Block:
		 *     { { Statement } }
		 */
		private boolean visit(Block node, USETYPE useType, Expression booleanExpr) {
			int minLine = _unit.getLineNumber(node.getStartPosition());
			int maxLine = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLine, maxLine,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.BLOCK, "Block");
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.BLOCK);
			for (Object astnode : node.statements()) {
				process((ASTNode) astnode, USETYPE.UNKNOWN, null);
			}
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}
		
		/*
		 * BreakStatement:
		 * 	break [ Identifier ] ;
		 */
		private boolean visit(BreakStatement node, USETYPE useType, Expression booleanExpr) {
			if (node.getLabel() != null) {
				Name name = node.getLabel();
				int line = _unit.getLineNumber(name.getStartPosition());
				int column = _unit.getColumnNumber(name.getStartPosition());
				String varName = name.getFullyQualifiedName();
				Use use = new Use(line, column, name, null, USETYPE.READ, StmtType.BREAK, _visitedBlock.peek());
				_visitedBlock.peek().addVariableUse(varName, use);
			}
			return true;
		}

		/*
		 * ConstructorInvocation:
		 *     [ < Type { , Type } > ]
		 *     this ( [ Expression { , Expression } ] ) ;
		 */
		private boolean visit(ConstructorInvocation node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.CONSTRUCTINV);
			for (Object args : node.arguments()) {
				process((ASTNode) args, USETYPE.READ, booleanExpr);
			}
			_visitedStatement.pop();
			return true;
		}

		/*
		 * ContinueStatement:
		 *   continue [ Identifier ] ;
		 */
		private boolean visit(ContinueStatement node, USETYPE useType, Expression booleanExpr) {
			if (node.getLabel() != null) {
				Name name = node.getLabel();
				int line = _unit.getLineNumber(name.getStartPosition());
				int column = _unit.getColumnNumber(name.getStartPosition());
				String varName = name.getFullyQualifiedName();
				Use use = new Use(line, column, name, null, USETYPE.READ, StmtType.CONTINUE, _visitedBlock.peek());
				_visitedBlock.peek().addVariableUse(varName, use);
			}
			return true;
		}

		/*
		 * DoStatement:
		 *   do Statement while ( Expression ) ;
		 */
		private boolean visit(DoStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.DO, "DoStatement");
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.DO);
			process(node.getBody(), USETYPE.UNKNOWN, null);
			process(node.getExpression(), USETYPE.READ, node.getExpression());
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}

		/*
		 * EmptyStatement:
		 *   ;
		 */
		private boolean visit(EmptyStatement node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * EnhancedForStatement:
		 *   for ( FormalParameter : Expression )
         *               Statement
		 */
		private boolean visit(EnhancedForStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.ENFOR, "EnhancedForStatement");

			SingleVariableDeclaration svd = node.getParameter();
			Name name = svd.getName();
			Type type = parseArrayType(svd.getType(), svd.getExtraDimensions());
			int line = _unit.getLineNumber(name.getStartPosition());
			int column = _unit.getColumnNumber(name.getStartPosition());
			Variable variable = new Variable(_file, line, column, svd.getModifiers(), svd.getInitializer(),
					name.getFullyQualifiedName(), type);
			variable.setLocalVar();
			Use use = new Use(line, column, svd, null, USETYPE.DEFINE, StmtType.ENHANCEDFOR, basicBlock);
			variable.addUse(use);
			basicBlock.addVariables(variable);

			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.ENHANCEDFOR);
			process(svd.getInitializer(), USETYPE.READ, null);
			process(node.getExpression(), USETYPE.READ, null);
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}

		/*
		 * ExpressionStatement:
		 *   StatementExpression ;
		 */
		private boolean visit(ExpressionStatement node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.EXRESSION);
			process(node.getExpression(), USETYPE.READ, null);
			_visitedStatement.pop();
			return true;
		}

		/*
		 * ForStatement:
		 *   for (
         *           [ ForInit ];
         *           [ Expression ] ;
         *           [ ForUpdate ] )
         *           Statement
         *   ForInit:
         *      Expression { , Expression }
         *   ForUpdate:
         *      Expression { , Expression }
		 */
		private boolean visit(ForStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.FOR, "ForStatement");

			_visitedStatement.push(StmtType.FOR_INIT);
			_visitedBlock.push(basicBlock);
			if (node.initializers() != null) {
				for (Object init : node.initializers()) {
					process((ASTNode) init, USETYPE.READ, null);
				}
			}
			_visitedStatement.pop();

			_visitedStatement.push(StmtType.FOR_COND);
			if (node.getExpression() != null) {
				process(node.getExpression(), USETYPE.READ, node.getExpression());
			}
			_visitedStatement.pop();

			_visitedStatement.push(StmtType.FOR_UPDT);
			if (node.updaters() != null) {
				for (Object update : node.updaters()) {
					process((ASTNode) update, USETYPE.READ, null);
				}
			}
			_visitedStatement.pop();

			_visitedStatement.push(StmtType.FOR_BODY);
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedBlock.pop();
			_visitedStatement.pop();
			return true;
		}

		/*
		 * IfStatement:
		 *   if ( Expression ) Statement [ else Statement]
		 */
		private boolean visit(IfStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.IF, "IfStatement");

			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.IF);
			process(node.getExpression(), USETYPE.READ, node.getExpression());
			process(node.getThenStatement(), USETYPE.UNKNOWN, null);
			process(node.getElseStatement(), USETYPE.UNKNOWN, null);
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}

		/*
		 * LabeledStatement:
		 *   Identifier : Statement
		 */
		private boolean visit(LabeledStatement node, USETYPE useType, Expression booleanExpr) {
			Name name = node.getLabel();
			int line = _unit.getLineNumber(name.getStartPosition());
			int column = _unit.getColumnNumber(name.getStartPosition());
			Variable variable = new Variable(_file, line, column, Modifier.PUBLIC, null,
					name.getFullyQualifiedName(), node.getAST().newWildcardType());
			variable.setLocalVar();
			Use use = new Use(line, column, node, null, USETYPE.DEFINE, StmtType.LABELED, _visitedBlock.peek());
			variable.addUse(use);
			_visitedBlock.peek().addVariables(variable);

			_visitedStatement.push(StmtType.LABELED);
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedStatement.pop();

			return true;
		}

		/*
		 * ReturnStatement:
		 *   return [ Expression ] ;
		 */
		private boolean visit(ReturnStatement node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.RETURN);
			Expression expression = null;
			ASTNode parent = node.getParent();
			while(parent != null) {
				if(parent instanceof MethodDeclaration) {
					MethodDeclaration md = (MethodDeclaration) parent;
					if(md.getReturnType2() != null && md.getReturnType2().toString().equals("boolean")) {
						expression = node.getExpression();
					}
					break;
				}
				parent = parent.getParent();
			}
			process(node.getExpression(), USETYPE.READ, expression);
			_visitedStatement.pop();
			return true;
		}

		/*
		 * SuperConstructorInvocation:
		 *   [ Expression . ]
		 *     [ < Type { , Type } > ]
		 *     super ( [ Expression { , Expression } ] ) ;
		 */
		private boolean visit(SuperConstructorInvocation node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.SUPERCONSTRUCTORINV);
			process(node.getExpression(), USETYPE.READ, null);
			for(Object arg : node.arguments()) {
				process((ASTNode) arg, USETYPE.READ, null);
			}
			_visitedStatement.pop();
			return true;
		}

		/*
		 * SwitchCase:
         *      case Expression  :
         *      default :
		 */
		private boolean visit(SwitchCase node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.CASE);
			process(node.getExpression(), USETYPE.READ, null);
			_visitedStatement.pop();
			return true;
		}

		/*
		 * SwitchStatement:
         *       switch ( Expression )
         *              { { SwitchCase | Statement } } }
		 */
		private boolean visit(SwitchStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber, _visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.SWITCH, "SwitchStatement");
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.SWITCH);
			process(node.getExpression(), USETYPE.READ, null);
			boolean notfirst = false;
			int minLine = Integer.MAX_VALUE;
			int maxLine = 0;
			for(Object stmt : node.statements()) {
				if(stmt instanceof SwitchCase) {
					if(notfirst) {
						_visitedBlock.pop().setCodeRange(minLine, maxLine);
						minLine = Integer.MAX_VALUE;
						maxLine = 0;
					}
					notfirst = true;
					BasicBlock caseBlock = new BasicBlock(_file, _package, _publicClazz, _unit, 0, 0, _visitedBlock.peek());
					caseBlock.setBlockType(BLOCKTYPE.CASE, "SwitchCase");
					_visitedBlock.push(caseBlock);
				}
				ASTNode subNode = (ASTNode) stmt;
				int min = _unit.getLineNumber(subNode.getStartPosition());
				int max = _unit.getLineNumber(subNode.getStartPosition() + subNode.getLength());
				minLine = minLine < min ? minLine : min;
				maxLine = maxLine > max ? maxLine : max;
				process(subNode, USETYPE.UNKNOWN, null);
			}
			_visitedBlock.pop();
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}

		/*
		 * SynchronizedStatement:
		 *   synchronized ( Expression ) Block
		 */
		private boolean visit(SynchronizedStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber, _visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.SYNC, "SynchronizedStatement");
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.SYNCHRONIZED);
			process(node.getExpression(), USETYPE.READ, null);
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedBlock.pop();
			_visitedStatement.pop();
			return true;
		}

		/*
		 * ThrowStatement:
		 *   throw Expression ;
		 */
		private boolean visit(ThrowStatement node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.THROW);
			process(node.getExpression(), USETYPE.READ, null);
			_visitedStatement.pop();
			return true;
		}

		/*
		 * TryStatement:
		 *   try [ ( Resources ) ]
		 *     Block
		 *     [ { CatchClause } ]
		 *     [ finally Block ]
		 */
		private boolean visit(TryStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.TRY, "TryStatement");
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.TRY);
			if (node.resources() != null) {
				for (Object object : node.resources()) {
					process((ASTNode) object, USETYPE.READ, null);
				}
			}
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedBlock.pop();
			_visitedStatement.pop();

			if (node.catchClauses() != null) {
				for (Object object : node.catchClauses()) {
					process((ASTNode) object, USETYPE.UNKNOWN, null);
				}
			}

			if (node.getFinally() != null) {
				Block finallyBlock = node.getFinally();
				minLineNumber = _unit.getLineNumber(finallyBlock.getStartPosition());
				maxLineNumber = _unit.getLineNumber(finallyBlock.getStartPosition() + finallyBlock.getLength());
				BasicBlock basicBlock2 = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber, basicBlock);
				basicBlock2.setBlockType(BLOCKTYPE.FINALLY, "Finally");
				_visitedBlock.add(basicBlock2);
				process(node.getFinally(), USETYPE.UNKNOWN, null);
				_visitedBlock.pop();
			}

			return true;
		}

		/*
		 * TypeDeclarationStatement:
		 *   TypeDeclaration
		 *   EnumDeclaration
		 */
		private boolean visit(TypeDeclarationStatement node, USETYPE useType, Expression booleanExpr) {
			process(node.getDeclaration(), USETYPE.UNKNOWN, null);
			return true;
		}

		/*
		 * VariableDeclarationStatement:
		 *   { ExtendedModifier } Type VariableDeclarationFragment
		 *     { , VariableDeclarationFragment } ;
		 */
		private boolean visit(VariableDeclarationStatement node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.VARDECL);
			int modifier = node.getModifiers();
			for (Object fragment : node.fragments()) {
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) fragment;
				Name name = vdf.getName();
				Type type = parseArrayType(node.getType(), vdf.getExtraDimensions());
				int line = _unit.getLineNumber(name.getStartPosition());
				int column = _unit.getColumnNumber(name.getStartPosition());
				Variable variable = new Variable(_file, line, column, modifier, vdf.getInitializer(),
						name.getFullyQualifiedName(), type);
				Use use = new Use(line, column, vdf, null, USETYPE.DEFINE, StmtType.VARDECL, _visitedBlock.peek());
				variable.addUse(use);
				_visitedBlock.peek().addVariables(variable);
				process(vdf.getInitializer(), USETYPE.READ, null);
			}
			_visitedStatement.pop();
			return true;
		}

		/*
		 * WhileStatement:
		 *   while ( Expression ) Statement
		 */
		private boolean visit(WhileStatement node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.WHILE, "WhileStatement");
			_visitedBlock.push(basicBlock);
			_visitedStatement.push(StmtType.WHILE);
			process(node.getExpression(), useType, node.getExpression());
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedStatement.pop();
			_visitedBlock.pop();

			return true;
		}
		
		/*
		 * CatchClause:
		 *   catch ( FormalParameter ) Block
		 */
		private boolean visit(CatchClause node, USETYPE useType, Expression booleanExpr) {
			int minLineNumber = _unit.getLineNumber(node.getStartPosition());
			int maxLineNumber = _unit.getLineNumber(node.getStartPosition() + node.getLength());
			BasicBlock basicBlock = new BasicBlock(_file, _package, _publicClazz, _unit, minLineNumber, maxLineNumber,
					_visitedBlock.peek());
			basicBlock.setBlockType(BLOCKTYPE.CATCH, "CatchClause");
			SingleVariableDeclaration svd = node.getException();
			Name name = svd.getName();
			Type type = parseArrayType(svd.getType(), svd.getExtraDimensions());
			int line = _unit.getLineNumber(name.getStartPosition());
			int column = _unit.getColumnNumber(name.getStartPosition());
			Variable variable = new Variable(_file, line, column, svd.getModifiers(), svd.getInitializer(),
					name.getFullyQualifiedName(), type);
			Use use = new Use(line, column, svd, null, USETYPE.DEFINE, StmtType.CATCH, basicBlock);
			variable.addUse(use);
			basicBlock.addVariables(variable);

			_visitedBlock.add(basicBlock);
			_visitedStatement.add(StmtType.CATCH);
			process(node.getBody(), USETYPE.UNKNOWN, null);
			_visitedStatement.pop();
			_visitedBlock.pop();
			return true;
		}

		/*****************************************************************************/
		/***************************** Expression Parser *************************/
		/*****************************************************************************/
		/*
		 * Annotation:
         *      NormalAnnotation
         *      MarkerAnnotation
         *      SingleMemberAnnotation
		 */
		private boolean visit(Annotation node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * ArrayAccess:
		 *   Expression [ Expression ]
		 */
		private boolean visit(ArrayAccess node, USETYPE useType, Expression booleanExpr) {
			process(node.getArray(), useType, booleanExpr);
			process(node.getIndex(), USETYPE.READ, booleanExpr);
			return true;
		}

		/*
		 * ArrayCreation:
		 *   new PrimitiveType [ Expression ] { [ Expression ] } { [ ] }
		 *   new TypeName [ < Type { , Type } > ]
		 *      [ Expression ] { [ Expression ] } { [ ] }
		 *   new PrimitiveType [ ] { [ ] } ArrayInitializer
		 *   new TypeName [ < Type { , Type } > ]
		 *      [ ] { [ ] } ArrayInitializer
		 */
		private boolean visit(ArrayCreation node, USETYPE useType, Expression booleanExpr) {
			for(Object dim : node.dimensions()) {
				process((ASTNode) dim, USETYPE.READ, null);
			}
			process(node.getInitializer(), USETYPE.READ, null);
			return true;
		}

		/*
		 * ArrayInitializer:
         *      { [ Expression { , Expression} [ , ]] }
		 */
		private boolean visit(ArrayInitializer node, USETYPE useType, Expression booleanExpr) {
			for(Object expr : node.expressions()) {
				process((ASTNode) expr, USETYPE.READ, null);
			}
			return true;
		}

		/*
		 * Assignment:
		 *   Expression AssignmentOperator Expression
		 */
		private boolean visit(Assignment node, USETYPE useType, Expression booleanExpr) {
			_visitedStatement.push(StmtType.ASSIGNMENT);
			process(node.getLeftHandSide(), USETYPE.WRITE, booleanExpr);
			process(node.getRightHandSide(), USETYPE.READ, booleanExpr);
			_visitedStatement.pop();
			return true;
		}

		/*
		 * BooleanLiteral:
         *      true
         *      false
		 */
		private boolean visit(BooleanLiteral node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * CastExpression:
		 *   ( Type ) Expression
		 */
		private boolean visit(CastExpression node, USETYPE useType, Expression booleanExpr) {
			process(node.getExpression(), useType, booleanExpr);
			return true;
		}

		private boolean visit(CharacterLiteral node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * ClassInstanceCreation:
		 *   [ Expression . ]
		 *     new [ < Type { , Type } > ]
		 *     Type ( [ Expression { , Expression } ] )
		 *     [ AnonymousClassDeclaration ]
		 */
		private boolean visit(ClassInstanceCreation node, USETYPE useType, Expression booleanExpr) {
			process(node.getExpression(), USETYPE.READ, null);
			for(Object arg : node.arguments()) {
				process((ASTNode) arg, USETYPE.READ, null);
			}
			return true;
		}

		/*
		 * ConditionalExpression:
		 *   Expression ? Expression : Expression
		 */
		private boolean visit(ConditionalExpression node, USETYPE useType, Expression booleanExpr) {
			process(node.getExpression(), USETYPE.READ, node.getExpression());
			process(node.getThenExpression(), USETYPE.READ, booleanExpr);
			process(node.getElseExpression(), USETYPE.READ, booleanExpr);
			return true;
		}

		/*
		 * CreationReference:
		 *   Type :: 
		 *     [ < Type { , Type } > ]
		 *     new
		 */
		private boolean visit(CreationReference node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * ExpressionMethodReference:
		 *   Expression :: 
		 *     [ < Type { , Type } > ]
		 *     Identifier
		 */
		private boolean visit(ExpressionMethodReference node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * FieldAccess:
         *     Expression . Identifier
		 */
		private boolean visit(FieldAccess node, USETYPE useType, Expression booleanExpr) {
			process(node.getExpression(), useType, booleanExpr);
			Name name = node.getName();
			int line = _unit.getLineNumber(name.getStartPosition());
			int column = _unit.getColumnNumber(name.getStartPosition());
			Use use = new Use(line, column, node, booleanExpr, useType, _visitedStatement.peek(), _visitedBlock.peek());
			String expr = node.getExpression().toString();
			if(expr.length() > 0) {
				use.setIsField(true, node.getExpression().toString());
			} else {
				use.setIsField(true, "this");
			}
			_visitedBlock.peek().addVariableUse(node.getName().getFullyQualifiedName(), use);
			return true;
		}

		/*
		 * InfixExpression:
		 *   Expression InfixOperator Expression { InfixOperator Expression }
		 */
		private boolean visit(InfixExpression node, USETYPE useType, Expression booleanExpr) {
			Expression left = booleanExpr;
			Expression right = booleanExpr;
//			*    TIMES
//		    /  DIVIDE
//		    %  REMAINDER
//		    +  PLUS
//		    -  MINUS
//		    <<  LEFT_SHIFT
//		    >>  RIGHT_SHIFT_SIGNED
//		    >>>  RIGHT_SHIFT_UNSIGNED
//		    <  LESS
//		    >  GREATER
//		    <=  LESS_EQUALS
//		    >=  GREATER_EQUALS
//		    ==  EQUALS
//		    !=  NOT_EQUALS
//		    ^  XOR
//		    &  AND
//		    |  OR
//		    &&  CONDITIONAL_AND
//		    ||  CONDITIONAL_OR
			switch(node.getOperator().toString()) {
			case "&&":
			case "||":
				left = node.getLeftOperand();
				right = node.getRightOperand();
				break;
			default:
			}
			process(node.getLeftOperand(), USETYPE.READ, left);
			process(node.getRightOperand(), USETYPE.READ, right);
			return true;
		}

		/*
		 * InstanceofExpression:
		 *   Expression instanceof Type
		 */
		private boolean visit(InstanceofExpression node, USETYPE useType, Expression booleanExpr) {
			process(node.getLeftOperand(), USETYPE.READ, node);
			return true;
		}

		/*
		 * LambdaExpression:
		 *   Identifier -> Body
		 *   ( [ Identifier { , Identifier } ] ) -> Body
		 *   ( [ FormalParameter { , FormalParameter } ] ) -> Body
		 */
		private boolean visit(LambdaExpression node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * MethodInvocation:
		 *   [ Expression . ]
		 *     [ < Type { , Type } > ]
		 *     Identifier ( [ Expression { , Expression } ] )
		 */
		private boolean visit(MethodInvocation node, USETYPE useType, Expression booleanExpr) {
			process(node.getExpression(), USETYPE.READ, booleanExpr);
			for(Object arg : node.arguments()) {
				process((ASTNode) arg, USETYPE.READ, booleanExpr);
			}
			return true;
		}

		/*
		 * MethodReference:
		 *   CreationReference
		 *   ExpressionMethodReference
		 *   SuperMethodReference
		 *   TypeMethodReference
		 */
		private boolean visit(MethodReference node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * Name:
		 *   SimpleName
		 *   QualifiedName
		 */
		Pattern clazz = Pattern.compile("^[A-Z][A-Za-z_]*");
		Pattern constant=Pattern.compile("^[A-Z][A-Z_]*");
		private boolean visit(Name node, USETYPE useType, Expression booleanExpr) {
			if(node instanceof SimpleName) {
				String name = node.getFullyQualifiedName();
				if (constant.matcher(name).matches() || clazz.matcher(name).matches()) {
					return true;
				}
				int line = _unit.getLineNumber(node.getStartPosition());
				int column = _unit.getColumnNumber(node.getStartPosition());
				Use use = new Use(line, column, node.getParent(), booleanExpr, useType, _visitedStatement.peek(), _visitedBlock.peek());
				_visitedBlock.peek().addVariableUse(node.getFullyQualifiedName(), use);
			} else {
				QualifiedName qName = (QualifiedName) node;
				// skip some constant and static access
				if (constant.matcher(qName.getName().getFullyQualifiedName()).find()
						|| clazz.matcher(qName.getQualifier().getFullyQualifiedName()).find()) {
					return true;
				}
				Name name = qName.getName();
				int line = _unit.getLineNumber(name.getStartPosition());
				int column = _unit.getColumnNumber(name.getStartPosition());
				Use use = new Use(line, column, qName, booleanExpr, useType, _visitedStatement.peek(), _visitedBlock.peek());
				_visitedBlock.peek().addVariableUse(name.getFullyQualifiedName(), use);
				process(qName.getQualifier(), USETYPE.READ, booleanExpr);
			}
			return true;
		}

		private boolean visit(NullLiteral node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		private boolean visit(NumberLiteral node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * ParenthesizedExpression:
		 *   ( Expression )
		 */
		private boolean visit(ParenthesizedExpression node, USETYPE useType, Expression booleanExpr) {
			process(node.getExpression(), USETYPE.READ, booleanExpr);
			return true;
		}

		/*
		 * PostfixExpression:
		 *   Expression PostfixOperator
		 */
		private boolean visit(PostfixExpression node, USETYPE useType, Expression booleanExpr) {
			process(node.getOperand(), USETYPE.READ, booleanExpr);
			return true;
		}

		/*
		 * PrefixExpression:
		 *   PrefixOperator Expression
		 */
		private boolean visit(PrefixExpression node, USETYPE useType, Expression booleanExpr) {
			process(node.getOperand(), USETYPE.READ, booleanExpr);
			return true;
		}

		private boolean visit(StringLiteral node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * SuperFieldAccess:
		 *   [ ClassName . ] super . Identifier
		 */
		private boolean visit(SuperFieldAccess node, USETYPE useType, Expression booleanExpr) {
			// TODO : may be deleted
			SimpleName name = node.getName();
			int line = _unit.getLineNumber(name.getStartPosition());
			int column = _unit.getColumnNumber(name.getStartPosition());
			Use use = new Use(line, column, node, booleanExpr, useType, _visitedStatement.peek(), _visitedBlock.peek());
			Expression expr = node.getQualifier();
			if(expr != null) {
				use.setIsField(true, expr.toString() + ".super");
			} else {
				use.setIsField(true, "super");
			}
			_visitedBlock.peek().addVariableUse(name.getFullyQualifiedName(), use);
			process(node.getQualifier(), USETYPE.READ, booleanExpr);
			return true;
		}

		/*
		 * SuperMethodInvocation:
		 *   [ ClassName . ] super .
		 *     [ < Type { , Type } > ]
		 *     Identifier ( [ Expression { , Expression } ] )
		 */
		private boolean visit(SuperMethodInvocation node, USETYPE useType, Expression booleanExpr) {
			process(node.getQualifier(), USETYPE.READ, booleanExpr);
			for(Object arg : node.arguments()) {
				process((ASTNode) arg, USETYPE.READ, booleanExpr);
			}
			return true;
		}

		/*
		 * SuperMethodReference:
		 *   [ ClassName . ] super ::
		 *     [ < Type { , Type } > ]
		 *     Identifier
		 */
		private boolean visit(SuperMethodReference node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * ThisExpression:
		 *   [ ClassName . ] this
		 */
		private boolean visit(ThisExpression node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * TypeLiteral:
		 *   ( Type | void ) . class
		 */
		private boolean visit(TypeLiteral node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * TypeMethodReference:
		 *   Type :: 
		 *     [ < Type { , Type } > ]
		 *     Identifier
		 */
		private boolean visit(TypeMethodReference node, USETYPE useType, Expression booleanExpr) {
			return true;
		}

		/*
		 * VariableDeclarationExpression:
		 *   { ExtendedModifier } Type VariableDeclarationFragment
		 *     { , VariableDeclarationFragment }
		 */
		private boolean visit(VariableDeclarationExpression node, USETYPE useType, Expression booleanExpr) {
			Type type = null;
			int modifier = node.getModifiers();
			BasicBlock basicBlock = _visitedBlock.peek();
			for(Object object : node.fragments()) {
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) object;
				SimpleName name = vdf.getName();
				int line = _unit.getLineNumber(name.getStartPosition());
				int column = _unit.getColumnNumber(name.getStartPosition());
				type = parseArrayType(node.getType(), vdf.getExtraDimensions());
				Variable variable = new Variable(_file, line, column, modifier, vdf.getInitializer(), name.getFullyQualifiedName(), type);
				Use use = new Use(line, column, vdf, null, USETYPE.DEFINE, _visitedStatement.peek(), basicBlock);
				variable.addUse(use);
				basicBlock.addVariables(variable);
				process(vdf.getInitializer(), USETYPE.READ, null);
			}
			return true;
		}
		
		/*
		 * SingleVariableDeclaration:
		 *   { ExtendedModifier } Type {Annotation} [ ... ] Identifier { Dimension } [ = Expression ]
		 */
		private boolean visit(SingleVariableDeclaration node, USETYPE useType, Expression booleanExpr) {
			Type type = parseArrayType(node.getType(), node.getExtraDimensions());
			Name name = node.getName();
			int line = _unit.getLineNumber(name.getStartPosition());
			int column = _unit.getColumnNumber(name.getStartPosition());
			Variable variable = new Variable(_file, line, column, node.getModifiers(), node.getInitializer(), name.getFullyQualifiedName(), type);
			Use use = new Use(line, column, node, booleanExpr, USETYPE.DEFINE, _visitedStatement.peek(), _visitedBlock.peek());
			variable.addUse(use);
			_visitedBlock.peek().addVariables(variable);
			process(node.getInitializer(), USETYPE.READ, booleanExpr);
			return true;
		}
		
		/*
		 * dispatch method
		 */
		private boolean process(ASTNode node, USETYPE varUsetype, Expression booleanExpr) {
			if(node == null) return true;
			if (node instanceof TypeDeclaration) {
				return visit((TypeDeclaration) node, varUsetype, booleanExpr);
			} else if (node instanceof EnumDeclaration) {
				return visit((EnumDeclaration) node, varUsetype, booleanExpr);
			} else if (node instanceof MethodDeclaration) {
				return visit((MethodDeclaration) node, varUsetype, booleanExpr);
			} else if (node instanceof AssertStatement) {
				return visit((AssertStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof Block) {
				return visit((Block) node, varUsetype, booleanExpr);
			} else if (node instanceof BreakStatement) {
				return visit((BreakStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof CatchClause) {
				return visit((CatchClause) node, varUsetype, booleanExpr);
			} else if (node instanceof ConstructorInvocation) {
				return visit((ConstructorInvocation) node, varUsetype, booleanExpr);
			} else if (node instanceof ContinueStatement) {
				return visit((ContinueStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof DoStatement) {
				return visit((DoStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof EmptyStatement) {
				return visit((EmptyStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof EnhancedForStatement) {
				return visit((EnhancedForStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof ExpressionStatement) {
				return visit((ExpressionStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof ForStatement) {
				return visit((ForStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof IfStatement) {
				return visit((IfStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof LabeledStatement) {
				return visit((LabeledStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof ReturnStatement) {
				return visit((ReturnStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof SuperConstructorInvocation) {
				return visit((SuperConstructorInvocation) node, varUsetype, booleanExpr);
			} else if (node instanceof SwitchCase) {
				return visit((SwitchCase) node, varUsetype, booleanExpr);
			} else if (node instanceof SwitchStatement) {
				return visit((SwitchStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof SynchronizedStatement) {
				return visit((SynchronizedStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof ThrowStatement) {
				return visit((ThrowStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof TryStatement) {
				return visit((TryStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof TypeDeclarationStatement) {
				return visit((TypeDeclarationStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof VariableDeclarationStatement) {
				return visit((VariableDeclarationStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof WhileStatement) {
				return visit((WhileStatement) node, varUsetype, booleanExpr);
			} else if (node instanceof Annotation) {
				return visit((Annotation) node, varUsetype, booleanExpr);
			} else if (node instanceof ArrayAccess) {
				return visit((ArrayAccess) node, varUsetype, booleanExpr);
			} else if (node instanceof ArrayCreation) {
				return visit((ArrayCreation) node, varUsetype, booleanExpr);
			} else if (node instanceof ArrayInitializer) {
				return visit((ArrayInitializer) node, varUsetype, booleanExpr);
			} else if (node instanceof Assignment) {
				return visit((Assignment) node, varUsetype, booleanExpr);
			} else if (node instanceof BooleanLiteral) {
				return visit((BooleanLiteral) node, varUsetype, booleanExpr);
			} else if (node instanceof CastExpression) {
				return visit((CastExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof CharacterLiteral) {
				return visit((CharacterLiteral) node, varUsetype, booleanExpr);
			} else if (node instanceof ClassInstanceCreation) {
				return visit((ClassInstanceCreation) node, varUsetype, booleanExpr);
			} else if (node instanceof ConditionalExpression) {
				return visit((ConditionalExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof CreationReference) {
				return visit((CreationReference) node, varUsetype, booleanExpr);
			} else if (node instanceof ExpressionMethodReference) {
				return visit((ExpressionMethodReference) node, varUsetype, booleanExpr);
			} else if (node instanceof FieldAccess) {
				return visit((FieldAccess) node, varUsetype, booleanExpr);
			} else if (node instanceof InfixExpression) {
				return visit((InfixExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof InstanceofExpression) {
				return visit((InstanceofExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof LambdaExpression) {
				return visit((LambdaExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof MethodInvocation) {
				return visit((MethodInvocation) node, varUsetype, booleanExpr);
			} else if (node instanceof MethodReference) {
				return visit((MethodReference) node, varUsetype, booleanExpr);
			} else if (node instanceof Name) {
				return visit((Name) node, varUsetype, booleanExpr);
			} else if (node instanceof NullLiteral) {
				return visit((NullLiteral) node, varUsetype, booleanExpr);
			} else if (node instanceof NumberLiteral) {
				return visit((NumberLiteral) node, varUsetype, booleanExpr);
			} else if (node instanceof ParenthesizedExpression) {
				return visit((ParenthesizedExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof PostfixExpression) {
				return visit((PostfixExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof PrefixExpression) {
				return visit((PrefixExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof StringLiteral) {
				return visit((StringLiteral) node, varUsetype, booleanExpr);
			} else if (node instanceof SuperFieldAccess) {
				return visit((SuperFieldAccess) node, varUsetype, booleanExpr);
			} else if (node instanceof SuperMethodInvocation) {
				return visit((SuperMethodInvocation) node, varUsetype, booleanExpr);
			} else if (node instanceof SuperMethodReference) {
				return visit((SuperMethodReference) node, varUsetype, booleanExpr);
			} else if (node instanceof ThisExpression) {
				return visit((ThisExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof TypeLiteral) {
				return visit((TypeLiteral) node, varUsetype, booleanExpr);
			} else if (node instanceof TypeMethodReference) {
				return visit((TypeMethodReference) node, varUsetype, booleanExpr);
			} else if (node instanceof VariableDeclarationExpression) {
				return visit((VariableDeclarationExpression) node, varUsetype, booleanExpr);
			} else if (node instanceof SingleVariableDeclaration) {
				return visit((SingleVariableDeclaration) node, varUsetype, booleanExpr);
			} else {
				LevelLogger.error("Cannot parse node : " + node.toString());
			}
			return true;
		}
		
		private Type genPrimitiveType(AST ast, PrimitiveType.Code code) {
			return ast.newPrimitiveType(code);
		}
		
		private Type parseArrayType(Type type, int extra) {
			if(extra > 0) {
				AST ast = type.getAST();
				if(type.isArrayType()) {
					ArrayType arrayType = (ArrayType) type;
					return ast.newArrayType((Type) ASTNode.copySubtree(ast, arrayType.getElementType()), arrayType.getDimensions() + extra); 
				} else {
					return ast.newArrayType((Type) ASTNode.copySubtree(ast, type), extra);
				}
			} else {
				return type;
			}
		}
	}
	
}