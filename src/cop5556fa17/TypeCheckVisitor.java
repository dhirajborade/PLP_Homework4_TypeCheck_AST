package cop5556fa17;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils.Type;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression_Binary;
import cop5556fa17.AST.Expression_BooleanLit;
import cop5556fa17.AST.Expression_Conditional;
import cop5556fa17.AST.Expression_FunctionAppWithExprArg;
import cop5556fa17.AST.Expression_FunctionAppWithIndexArg;
import cop5556fa17.AST.Expression_Ident;
import cop5556fa17.AST.Expression_IntLit;
import cop5556fa17.AST.Expression_PixelSelector;
import cop5556fa17.AST.Expression_PredefinedName;
import cop5556fa17.AST.Expression_Unary;
import cop5556fa17.AST.Index;
import cop5556fa17.AST.LHS;
import cop5556fa17.AST.Program;
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_Assign;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class SemanticException extends Exception {
		Token t;

		public SemanticException(Token t, String message) {
			super("line " + t.line + " pos " + t.pos_in_line + ": " + message);
			this.t = t;
		}

	}

	SymbolTable symTab = new SymbolTable();

	/**
	 * The program name is only used for naming the class. It does not rule out
	 * variables with the same name. It is returned for convenience.
	 * 
	 * @throws Exception
	 */
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for (ASTNode node : program.decsAndStatements) {
			node.visit(this, arg);
		}
		return program.name;
	}

	@Override
	public Object visitDeclaration_Variable(Declaration_Variable declaration_Variable, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary, Object arg) throws Exception {
		// TODO Auto-generated method stub
		expression_Binary.e0.visit(this, arg);
		expression_Binary.e1.visit(this, arg);
		if (expression_Binary.e0.nodeType == expression_Binary.e1.nodeType && expression_Binary.nodeType != null) {
			if (expression_Binary.op == Kind.OP_EQ || expression_Binary.op == Kind.OP_NEQ) {
				expression_Binary.nodeType = Type.BOOLEAN;
			} else if ((expression_Binary.op == Kind.OP_GE || expression_Binary.op == Kind.OP_GT || expression_Binary.op == Kind.OP_LE || expression_Binary.op == Kind.OP_LT) && expression_Binary.e0.nodeType == Type.INTEGER) {
				expression_Binary.nodeType = Type.BOOLEAN;
			} else if ((expression_Binary.op == Kind.OP_AND || expression_Binary.op == Kind.OP_OR) && (expression_Binary.e0.nodeType == Type.INTEGER || expression_Binary.e0.nodeType == Type.BOOLEAN)) {
				expression_Binary.nodeType = expression_Binary.e0.nodeType;
			} else if ((expression_Binary.op == Kind.OP_DIV || expression_Binary.op == Kind.OP_MINUS || expression_Binary.op == Kind.OP_MOD || expression_Binary.op == Kind.OP_PLUS || expression_Binary.op == Kind.OP_POWER || expression_Binary.op == Kind.OP_TIMES) && expression_Binary.e0.nodeType == Type.INTEGER) {
				expression_Binary.nodeType = Type.INTEGER;
			} else {
				expression_Binary.nodeType = null;
			}
		} else {
			String message = "Visit Binary Expression";
			throw new SemanticException(expression_Binary.firstToken, message);
		}
		return expression_Binary;
	}

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSource_CommandLineParam(Source_CommandLineParam source_CommandLineParam, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		source_CommandLineParam.paramNum.visit(this, null);
		source_CommandLineParam.nodeType = source_CommandLineParam.paramNum.nodeType;
		if (source_CommandLineParam.nodeType != Type.INTEGER) {
			String message = "Visit Source Command Line Parameter";
			throw new SemanticException(source_CommandLineParam.firstToken, message);
		}
		return source_CommandLineParam;
	}

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if (!symTab.lookupNode(source_Ident.name)) {
			String message = "Visit Source Identifier";
			throw new SemanticException(source_Ident.firstToken, message);
		}
		source_Ident.nodeType = symTab.getNode(source_Ident.name).nodeType;
		if (source_Ident.nodeType != Type.FILE ||source_Ident.nodeType != Type.URL) {
			String message = "Source Ident Type in Visit Source Identifier is not a File or URL";
			throw new SemanticException(source_Ident.firstToken, message);
		}
		return source_Ident;
	}

	@Override
	public Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
