package parser.node;


import dataType.FinalSymbol;
import lexer.token.AbstractToken;
import lexer.token.FinalToken;

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
		Node ans;
		switch (ruleID) {
		case 1:{
			ExprNode expr = (ExprNode)nodes[0];
			PlusOpNode plus = (PlusOpNode)nodes[1];
			TermNode term = (TermNode)nodes[2];
			ans = new ExprNode(expr, plus, term);
			break;
		}
		case 2:{
			ExprNode expr = (ExprNode)nodes[0];
			MinusOpNode minus = (MinusOpNode)nodes[1];
			TermNode term = (TermNode)nodes[2];
			ans = new ExprNode(expr, minus, term);
			break;
		}
		case 3:{
			TermNode term = (TermNode)nodes[0];
			ans = new ExprNode(term);
			break;
		}
		case 4:{
			TermNode term = (TermNode)nodes[0];
			MultiOpNode multi = (MultiOpNode)nodes[1];
			ConstIntNode constInt = (ConstIntNode)nodes[2];
			ans = new TermNode(term, multi, constInt);
			break;			
		}
		case 5:{
			TermNode term = (TermNode)nodes[0];
			DivideOpNode divide = (DivideOpNode)nodes[1];
			ConstIntNode constInt = (ConstIntNode)nodes[2];
			ans = new TermNode(term, divide, constInt);
			break;			
		}
		case 6:{
			ConstIntNode constInt = (ConstIntNode)nodes[0];
			ans = new TermNode(constInt);
			break;
		}
		default:{
			ans = null;
			break;
		}
			
		}
		return ans;
	}
}

class ExprNode implements Node{

	ExprNode left;
	TermNode right;
	BiopNode op;
	
	public ExprNode(ExprNode expr, BiopNode biop, TermNode term) {
		left = expr;
		op = biop;
		right = term;
	}

	public ExprNode(TermNode term) {
		right = term;
		op = null;
		left = null;
	}

	@Override
	public int getValue() {
		if (op != null) {
			System.out.println("Term -> lvalue op rvalue: " + op.calc(left.getValue(), right.getValue()));
			return op.calc(left.getValue(), right.getValue());
		} else {
			System.out.println("right -> lvalue: " + right.getValue());
			return right.getValue();
		}
	}

}

class TermNode implements Node{
	
	TermNode left;
	ConstIntNode right;
	BiopNode op;
	
	public TermNode(TermNode term, BiopNode biop, ConstIntNode constInt) {
		left = term;
		op = biop;
		right = constInt;
	}

	public TermNode(ConstIntNode constInt) {
		right = constInt;
		op = null;
		left = null;
	}

	@Override
	public int getValue() {
		if (op != null) {
			System.out.println("Term -> lvalue op rvalue: " + op.calc(left.getValue(), right.getValue()));
			return op.calc(left.getValue(), right.getValue());
		} else {
			System.out.println("constInt -> lvalue: " + right.getValue());
			return right.getValue();
		}
	}
}

abstract class BiopNode implements Node{
	abstract int calc(int left, int right);
}

class PlusOpNode extends BiopNode{
	AbstractToken token;
	
	public PlusOpNode(AbstractToken token) {
		this.token = token;
	}

	@Override
	int calc(int left, int right) {
		return left + right;
	}
}

class MinusOpNode extends BiopNode{
	AbstractToken token;
	
	public MinusOpNode(AbstractToken token) {
		this.token = token;
	}
	
	@Override
	int calc(int left, int right) {
		return left - right;
	}
}

class MultiOpNode extends BiopNode{
	AbstractToken token;
	
	public MultiOpNode(AbstractToken token) {
		this.token = token;
	}
	
	@Override
	int calc(int left, int right) {
		return left * right;
	}
}

class DivideOpNode extends BiopNode{
	AbstractToken token;
	
	public DivideOpNode(AbstractToken token) {
		this.token = token;
	}
	
	@Override
	int calc(int left, int right) {
		return left / right;
	}
}

class ConstIntNode implements Node{
	FinalToken token;
	
	public ConstIntNode(FinalToken token) {
		this.token = token;
	}
	
	@Override
	public int getValue() {
		String content = token.content;
		System.out.println("Integer:" + Integer.parseInt(content));
		return Integer.parseInt(content);
	}
}
