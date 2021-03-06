package parser.nodeDefine;


import java.beans.Expression;

import dataType.FinalSymbol;
import lexer.token.AbstractToken;
import lexer.token.FinalToken;
import parser.nodeInterface.ConstValueNode;
import parser.nodeInterface.Node;

public class NodeFactory {
	
	public static Node createFinalNode(FinalToken token) {
		FinalSymbol symbol = (FinalSymbol)token.getSymbol();
		if (symbol == FinalSymbol.PLUS) {
			return new PlusOpNode(token);
		} else if(symbol == FinalSymbol.MINUS) {
			return new MinusOpNode(token);
		} else if(symbol == FinalSymbol.MULTI) {
			return new MultiOpNode(token);
		} else if(symbol == FinalSymbol.DIVIDE) {
			return new DivideOpNode(token);
		} else if(symbol == FinalSymbol.CONST_INT) {
			return new ConstIntNode(token);
		}
		return null;
	}

	public static Node createNode(Node[] nodes, int ruleID) {
		switch (ruleID) {
		case 0: {
			ProgramNode program = (ProgramNode) nodes[0];
			DOLLARNode DOLLAR = (DOLLARNode) nodes[1];
			SpNode ans = new SpNode(program, DOLLAR);
			return ans;
		}
		case 1: {
			FuncDefNode funcDef = (FuncDefNode) nodes[0];
			ProgramNode program = (ProgramNode) nodes[1];
			ProgramNode ans = new ProgramNode(funcDef, program);
			return ans;
		}
		case 2: {
			MainFuncNode mainFunc = (MainFuncNode) nodes[0];
			ProgramNode ans = new ProgramNode(mainFunc);
			return ans;
		}
		case 3: {
			INTNode INT = (INTNode) nodes[0];
			TypeNode ans = new TypeNode(INT);
			return ans;
		}
		case 4: {
			DOUBLENode DOUBLE = (DOUBLENode) nodes[0];
			TypeNode ans = new TypeNode(DOUBLE);
			return ans;
		}
		case 5: {
			STRINGNode STRING = (STRINGNode) nodes[0];
			TypeNode ans = new TypeNode(STRING);
			return ans;
		}
		case 6: {
			BOOLNode BOOL = (BOOLNode) nodes[0];
			TypeNode ans = new TypeNode(BOOL);
			return ans;
		}
		case 7: {
			PLUSNode PLUS = (PLUSNode) nodes[0];
			OperatorNode ans = new OperatorNode(PLUS);
			return ans;
		}
		case 8: {
			MINUSNode MINUS = (MINUSNode) nodes[0];
			OperatorNode ans = new OperatorNode(MINUS);
			return ans;
		}
		case 9: {
			MULTINode MULTI = (MULTINode) nodes[0];
			OperatorNode ans = new OperatorNode(MULTI);
			return ans;
		}
		case 10: {
			DIVIDENode DIVIDE = (DIVIDENode) nodes[0];
			OperatorNode ans = new OperatorNode(DIVIDE);
			return ans;
		}
		case 11: {
			CONST_REALNode CONST_REAL = (CONST_REALNode) nodes[0];
			ConstValueNode ans = new ConstValueNode(CONST_REAL);
			return ans;
		}
		case 12: {
			CONST_INTNode CONST_INT = (CONST_INTNode) nodes[0];
			ConstValueNode ans = new ConstValueNode(CONST_INT);
			return ans;
		}
		case 13: {
			CONST_STRINGNode CONST_STRING = (CONST_STRINGNode) nodes[0];
			ConstValueNode ans = new ConstValueNode(CONST_STRING);
			return ans;
		}
		case 14: {
			CONST_TRUENode CONST_TRUE = (CONST_TRUENode) nodes[0];
			ConstValueNode ans = new ConstValueNode(CONST_TRUE);
			return ans;
		}
		case 15: {
			CONST_FALSENode CONST_FALSE = (CONST_FALSENode) nodes[0];
			ConstValueNode ans = new ConstValueNode(CONST_FALSE);
			return ans;
		}
		case 16: {
			ExpressionNode expression = (ExpressionNode) nodes[0];
			OperatorNode operator = (OperatorNode) nodes[1];
			ExpressionNode expression = (ExpressionNode) nodes[2];
			ArithmExprNode ans = new ArithmExprNode(expression, operator, expression);
			return ans;
		}
		case 17: {
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[0];
			ASSIGNNode ASSIGN = (ASSIGNNode) nodes[1];
			ExpressionNode expression = (ExpressionNode) nodes[2];
			AssignExprNode ans = new AssignExprNode(IDENTIFIER, ASSIGN, expression);
			return ans;
		}
		case 18: {
			ArithmExprNode arithmExpr = (ArithmExprNode) nodes[0];
			ExpressionNode ans = new ExpressionNode(arithmExpr);
			return ans;
		}
		case 19: {
			AssignExprNode assignExpr = (AssignExprNode) nodes[0];
			ExpressionNode ans = new ExpressionNode(assignExpr);
			return ans;
		}
		case 20: {
			FuncCallNode funcCall = (FuncCallNode) nodes[0];
			ExpressionNode ans = new ExpressionNode(funcCall);
			return ans;
		}
		case 21: {
			ConstValueNode constValue = (ConstValueNode) nodes[0];
			ExpressionNode ans = new ExpressionNode(constValue);
			return ans;
		}
		case 22: {
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[0];
			ExpressionNode ans = new ExpressionNode(IDENTIFIER);
			return ans;
		}
		case 23: {
			LBRACENode LBRACE = (LBRACENode) nodes[0];
			StatmListNode statmList = (StatmListNode) nodes[1];
			RBRACENode RBRACE = (RBRACENode) nodes[2];
			BodyNode ans = new BodyNode(LBRACE, statmList, RBRACE);
			return ans;
		}
		case 24: {
			LPARENNode LPAREN = (LPARENNode) nodes[0];
			ExpressionNode expression = (ExpressionNode) nodes[1];
			RPARENNode RPAREN = (RPARENNode) nodes[2];
			ConditionNode ans = new ConditionNode(LPAREN, expression, RPAREN);
			return ans;
		}
		case 25: {
			ExpressionNode expression = (ExpressionNode) nodes[0];
			SEMICOLONNode SEMICOLON = (SEMICOLONNode) nodes[1];
			ExprStatmNode ans = new ExprStatmNode(expression, SEMICOLON);
			return ans;
		}
		case 26: {
			TypeNode type = (TypeNode) nodes[0];
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[1];
			SEMICOLONNode SEMICOLON = (SEMICOLONNode) nodes[2];
			DefineStatmNode ans = new DefineStatmNode(type, IDENTIFIER, SEMICOLON);
			return ans;
		}
		case 27: {
			AssignExprNode assignExpr = (AssignExprNode) nodes[0];
			SEMICOLONNode SEMICOLON = (SEMICOLONNode) nodes[1];
			AssighStatmNode ans = new AssighStatmNode(assignExpr, SEMICOLON);
			return ans;
		}
		case 28: {
			IfStatmNode ifStatm = (IfStatmNode) nodes[0];
			IfElseStatmNode ans = new IfElseStatmNode(ifStatm);
			return ans;
		}
		case 29: {
			IfStatmNode ifStatm = (IfStatmNode) nodes[0];
			ElseStatmNode elseStatm = (ElseStatmNode) nodes[1];
			IfElseStatmNode ans = new IfElseStatmNode(ifStatm, elseStatm);
			return ans;
		}
		case 30: {
			IFNode IF = (IFNode) nodes[0];
			ConditionNode condition = (ConditionNode) nodes[1];
			BodyNode body = (BodyNode) nodes[2];
			IfStatmNode ans = new IfStatmNode(IF, condition, body);
			return ans;
		}
		case 31: {
			ELSENode ELSE = (ELSENode) nodes[0];
			IfElseStatmNode ifElseStatm = (IfElseStatmNode) nodes[1];
			ElseStatmNode ans = new ElseStatmNode(ELSE, ifElseStatm);
			return ans;
		}
		case 32: {
			ELSENode ELSE = (ELSENode) nodes[0];
			BodyNode body = (BodyNode) nodes[1];
			ElseStatmNode ans = new ElseStatmNode(ELSE, body);
			return ans;
		}
		case 33: {
			FORNode FOR = (FORNode) nodes[0];
			LPARENNode LPAREN = (LPARENNode) nodes[1];
			StatementNode statement = (StatementNode) nodes[2];
			StatementNode statement = (StatementNode) nodes[3];
			ExpressionNode expression = (ExpressionNode) nodes[4];
			RPARENNode RPAREN = (RPARENNode) nodes[5];
			BodyNode body = (BodyNode) nodes[6];
			ForStatmNode ans = new ForStatmNode(FOR, LPAREN, statement, statement, expression, RPAREN, body);
			return ans;
		}
		case 34: {
			WHILENode WHILE = (WHILENode) nodes[0];
			ConditionNode condition = (ConditionNode) nodes[1];
			BodyNode body = (BodyNode) nodes[2];
			WhileStatmNode ans = new WhileStatmNode(WHILE, condition, body);
			return ans;
		}
		case 35: {
			RETURNNode RETURN = (RETURNNode) nodes[0];
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[1];
			SEMICOLONNode SEMICOLON = (SEMICOLONNode) nodes[2];
			ReturnStatmNode ans = new ReturnStatmNode(RETURN, IDENTIFIER, SEMICOLON);
			return ans;
		}
		case 36: {
			RETURNNode RETURN = (RETURNNode) nodes[0];
			SEMICOLONNode SEMICOLON = (SEMICOLONNode) nodes[1];
			ReturnStatmNode ans = new ReturnStatmNode(RETURN, SEMICOLON);
			return ans;
		}
		case 37: {
			ExprStatmNode exprStatm = (ExprStatmNode) nodes[0];
			StatementNode ans = new StatementNode(exprStatm);
			return ans;
		}
		case 38: {
			DefineStatmNode defineStatm = (DefineStatmNode) nodes[0];
			StatementNode ans = new StatementNode(defineStatm);
			return ans;
		}
		case 39: {
			AssighStatmNode assighStatm = (AssighStatmNode) nodes[0];
			StatementNode ans = new StatementNode(assighStatm);
			return ans;
		}
		case 40: {
			IfElseStatmNode ifElseStatm = (IfElseStatmNode) nodes[0];
			StatementNode ans = new StatementNode(ifElseStatm);
			return ans;
		}
		case 41: {
			ForStatmNode forStatm = (ForStatmNode) nodes[0];
			StatementNode ans = new StatementNode(forStatm);
			return ans;
		}
		case 42: {
			WhileStatmNode whileStatm = (WhileStatmNode) nodes[0];
			StatementNode ans = new StatementNode(whileStatm);
			return ans;
		}
		case 43: {
			ReturnStatmNode returnStatm = (ReturnStatmNode) nodes[0];
			StatementNode ans = new StatementNode(returnStatm);
			return ans;
		}
		case 44: {
			StatmListNode statmList = (StatmListNode) nodes[0];
			StatementNode statement = (StatementNode) nodes[1];
			StatmListNode ans = new StatmListNode(statmList, statement);
			return ans;
		}
		case 45: {
			StatementNode statement = (StatementNode) nodes[0];
			StatmListNode ans = new StatmListNode(statement);
			return ans;
		}
		case 46: {
			StatmListNode ans = new StatmListNode();
			return ans;
		}
		case 47: {
			TypeNode type = (TypeNode) nodes[0];
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[1];
			FormalParamNode ans = new FormalParamNode(type, IDENTIFIER);
			return ans;
		}
		case 48: {
			EmptyFormalParamListNode ans = new EmptyFormalParamListNode();
			return ans;
		}
		case 49: {
			FormalParamNode formalParam = (FormalParamNode) nodes[0];
			COMMANode COMMA = (COMMANode) nodes[1];
			FormalParamListNode formalParamList = (FormalParamListNode) nodes[2];
			FormalParamListNode ans = new FormalParamListNode(formalParam, COMMA, formalParamList);
			return ans;
		}
		case 50: {
			FormalParamNode formalParam = (FormalParamNode) nodes[0];
			FormalParamListNode ans = new FormalParamListNode(formalParam);
			return ans;
		}
		case 51: {
			INTNode INT = (INTNode) nodes[0];
			MAINNode MAIN = (MAINNode) nodes[1];
			LPARENNode LPAREN = (LPARENNode) nodes[2];
			EmptyFormalParamListNode emptyFormalParamList = (EmptyFormalParamListNode) nodes[3];
			RPARENNode RPAREN = (RPARENNode) nodes[4];
			BodyNode body = (BodyNode) nodes[5];
			MainFuncNode ans = new MainFuncNode(INT, MAIN, LPAREN, emptyFormalParamList, RPAREN, body);
			return ans;
		}
		case 52: {
			TypeNode type = (TypeNode) nodes[0];
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[1];
			LPARENNode LPAREN = (LPARENNode) nodes[2];
			FormalParamListNode formalParamList = (FormalParamListNode) nodes[3];
			RPARENNode RPAREN = (RPARENNode) nodes[4];
			BodyNode body = (BodyNode) nodes[5];
			FuncDefNode ans = new FuncDefNode(type, IDENTIFIER, LPAREN, formalParamList, RPAREN, body);
			return ans;
		}
		case 53: {
			TypeNode type = (TypeNode) nodes[0];
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[1];
			LPARENNode LPAREN = (LPARENNode) nodes[2];
			EmptyFormalParamListNode emptyFormalParamList = (EmptyFormalParamListNode) nodes[3];
			RPARENNode RPAREN = (RPARENNode) nodes[4];
			BodyNode body = (BodyNode) nodes[5];
			FuncDefNode ans = new FuncDefNode(type, IDENTIFIER, LPAREN, emptyFormalParamList, RPAREN, body);
			return ans;
		}
		case 54: {
			MainFuncNode mainFunc = (MainFuncNode) nodes[0];
			FuncDefNode ans = new FuncDefNode(mainFunc);
			return ans;
		}
		case 55: {
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[0];
			ActualParamNode ans = new ActualParamNode(IDENTIFIER);
			return ans;
		}
		case 56: {
			EmptyActualParamListNode ans = new EmptyActualParamListNode();
			return ans;
		}
		case 57: {
			ActualParamNode actualParam = (ActualParamNode) nodes[0];
			COMMANode COMMA = (COMMANode) nodes[1];
			ActualParamNode actualParam = (ActualParamNode) nodes[2];
			ActualParamListNode ans = new ActualParamListNode(actualParam, COMMA, actualParam);
			return ans;
		}
		case 58: {
			ActualParamNode actualParam = (ActualParamNode) nodes[0];
			ActualParamListNode ans = new ActualParamListNode(actualParam);
			return ans;
		}
		case 59: {
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[0];
			LPARENNode LPAREN = (LPARENNode) nodes[1];
			ActualParamListNode actualParamList = (ActualParamListNode) nodes[2];
			RPARENNode RPAREN = (RPARENNode) nodes[3];
			FuncCallNode ans = new FuncCallNode(IDENTIFIER, LPAREN, actualParamList, RPAREN);
			return ans;
		}
		case 60: {
			IDENTIFIERNode IDENTIFIER = (IDENTIFIERNode) nodes[0];
			LPARENNode LPAREN = (LPARENNode) nodes[1];
			EmptyActualParamListNode emptyActualParamList = (EmptyActualParamListNode) nodes[2];
			RPARENNode RPAREN = (RPARENNode) nodes[3];
			FuncCallNode ans = new FuncCallNode(IDENTIFIER, LPAREN, emptyActualParamList, RPAREN);
			return ans;
		}
		}
	}
}


//
//class ExprNode implements Node{
//
//	ExprNode left;
//	TermNode right;
//	BiopNode op;
//	
//	public ExprNode(ExprNode expr, BiopNode biop, TermNode term) {
//		left = expr;
//		op = biop;
//		right = term;
//	}
//
//	public ExprNode(TermNode term) {
//		right = term;
//		op = null;
//		left = null;
//	}
//
//	@Override
//	public int getValue() {
//		if (op != null) {
//			System.out.println("Term -> lvalue op rvalue: " + op.calc(left.getValue(), right.getValue()));
//			return op.calc(left.getValue(), right.getValue());
//		} else {
//			System.out.println("right -> lvalue: " + right.getValue());
//			return right.getValue();
//		}
//	}
//
//}
//
//class TermNode implements Node{
//	
//	TermNode left;
//	ConstIntNode right;
//	BiopNode op;
//	
//	public TermNode(TermNode term, BiopNode biop, ConstIntNode constInt) {
//		left = term;
//		op = biop;
//		right = constInt;
//	}
//
//	public TermNode(ConstIntNode constInt) {
//		right = constInt;
//		op = null;
//		left = null;
//	}
//
//	@Override
//	public int getValue() {
//		if (op != null) {
//			System.out.println("Term -> lvalue op rvalue: " + op.calc(left.getValue(), right.getValue()));
//			return op.calc(left.getValue(), right.getValue());
//		} else {
//			System.out.println("constInt -> lvalue: " + right.getValue());
//			return right.getValue();
//		}
//	}
//}
//
//abstract class BiopNode implements Node{
//	abstract int calc(int left, int right);
//}
//
//class PlusOpNode extends BiopNode{
//	AbstractToken token;
//	
//	public PlusOpNode(AbstractToken token) {
//		this.token = token;
//	}
//
//	@Override
//	int calc(int left, int right) {
//		return left + right;
//	}
//}
//
//class MinusOpNode extends BiopNode{
//	AbstractToken token;
//	
//	public MinusOpNode(AbstractToken token) {
//		this.token = token;
//	}
//	
//	@Override
//	int calc(int left, int right) {
//		return left - right;
//	}
//}
//
//class MultiOpNode extends BiopNode{
//	AbstractToken token;
//	
//	public MultiOpNode(AbstractToken token) {
//		this.token = token;
//	}
//	
//	@Override
//	int calc(int left, int right) {
//		return left * right;
//	}
//}
//
//class DivideOpNode extends BiopNode{
//	AbstractToken token;
//	
//	public DivideOpNode(AbstractToken token) {
//		this.token = token;
//	}
//	
//	@Override
//	int calc(int left, int right) {
//		return left / right;
//	}
//}
//
//class ConstIntNode implements Node{
//	FinalToken token;
//	
//	public ConstIntNode(FinalToken token) {
//		this.token = token;
//	}
//	
//	@Override
//	public int getValue() {
//		String content = token.content;
//		System.out.println("Integer:" + Integer.parseInt(content));
//		return Integer.parseInt(content);
//	}
//}
