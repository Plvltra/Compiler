package parser;

import java.util.ArrayList;
import java.util.Stack;

import dataType.CombineSymbol;
import dataType.FinalSymbol;
import dataType.Symbol;
import dataType.SymbolList;
import lexer.Lexer;
import lexer.token.AbstractToken;
import lexer.token.CombineToken;
import lexer.token.FinalToken;
import parser.nodeDefine.NodeFactory;
import parser.nodeInterface.Node;
import util.Constant;
import util.Debuger;


//prog = expr'\0'
//expr = term '+' term | term '-' term
//term = fact '*' fact | fact '/' fact
//fact = INT | '('expr')'


public class Parser {
	//------------------ Static part ----------------------// 
	static Table table;
	// 打印
	static Debuger debuger = new Debuger(true);
	static {
		table = new Table();
	}
	
	// Attributes
	private ArrayList<FinalToken> tokens;
	
	// Constructors
	public Parser(ArrayList<FinalToken> programTokens) {
		this.tokens = programTokens;
		FinalToken dollar = new FinalToken(FinalSymbol.DOLLAR); // TODO: more elegant
		this.tokens.add(dollar);
		printTokens();
	}
	
	// Functions
	public boolean check(){
		Stack<Object> stack = new Stack<Object>(); // 仅能包含Integer和AbstractToken
		Stack<Node> nodeStack = new Stack<Node>();
		stack.push(0);
		for(int i = 0; i < tokens.size(); i++){
			FinalToken token = tokens.get(i);
			boolean success = 
					pushToken(stack, nodeStack, token, NodeFactory.createFinalNode(token));
			if (!success) {
				return false;
			}
		}
		//Node node = nodeStack.peek();
		//System.out.println(node.getValue());
		return true;
	}

	/**
	 * 栈顶第一个数字符号为当前状态，压入一个符号根据规则对栈中进行处理，
	 * 最后栈顶为下一步起始状态
	 * @param stack
	 * @param nodeStack
	 * @param received
	 * @return
	 */
	private boolean pushToken(
			Stack<Object> stack, Stack<Node> nodeStack, AbstractToken token, Node node) {//token与node对应
		
		int currID = (int) stack.peek(); // 当前状态号
		Status nextStatus = table.jump(currID, token.getSymbol());
		if (nextStatus == null) {
			printError(currID, token);
			return false;
		} else if (nextStatus.action == Action.shift 
				|| nextStatus.action == Action.goTo) {
			stack.push(token);
			nodeStack.push(node);
			stack.push(nextStatus.target);
			debuger.println("push token->" + token.toString());
			return true;
		} else if (nextStatus.action == Action.reduce) {
			int ruleID = nextStatus.target; 
			boolean success1 = reduce(stack, nodeStack, ruleID);
			boolean success2 = pushToken(stack, nodeStack, token, node);
			return success1 && success2; // 猜测成功取决于两步成功
		} else {						 // accept
			return true;
		}
	}
	
	/**
	 * 栈顶第一个数字符号为当前状态，按规则归约栈顶(直到遇到规则右侧第一个符号前的状态)，
	 * 再压入归约所得符号
	 * @param stack
	 * @param ruleID
	 * @return
	 */
	private boolean reduce(
			Stack<Object> stack, Stack<Node> nodeStack, int ruleID) {
		
		Term rule = Define.getRule(ruleID);
		int symbolCount = rule.right.size(); // 规则右侧符号数
		int tokenCount = 2 * symbolCount;	 // pop token数量是符号数+状态数(符号数=状态数)
		Node[] nodes = new Node[symbolCount];
		while (tokenCount-- > 0) {
			if (tokenCount % 2 != 0) {  	// tokenCount奇数为符号
				stack.pop();
				Node node = nodeStack.pop();
				nodes[tokenCount/2] = node;
			} else {
				stack.pop();
			}
		}
		
		Node gotNode = NodeFactory.createNode(nodes,ruleID);
		CombineSymbol symbol = rule.left;
		CombineToken token = new CombineToken(symbol); // FIXME:确定添加token的位置
		debuger.println("reduce->" + token.toString());
		return pushToken(stack, nodeStack, token, gotNode);
//		return pushToken(stack, nodeStack, token, null);
	}
	
	private void printTokens() {
		debuger.println("token串:");
		for(FinalToken token : tokens) {
			debuger.println(token.toString());
		}
	}
	
	private void printError(int currID, AbstractToken token) {
		debuger.println("匹配失败");
		debuger.println("获得token是:" + token.toString());
		debuger.print("期待token是:");
		Row row = table.getRow(currID);
		for(Symbol symbol : SymbolList.values()) {
			if(row.get(symbol) != null && symbol != token.getSymbol()) {
				debuger.print(symbol.name() + ",");
			}
		}
		debuger.println();
	}
	
	public static void main(String args[]) {
		//test2
//		ArrayList<FinalToken> t = new ArrayList<FinalToken>();
//		t.add(new FinalToken(FinalSymbol.CONST_INT));
//		t.add(new FinalToken(FinalSymbol.DIVIDE));
//		t.add(new FinalToken(FinalSymbol.CONST_INT));
//		t.add(new FinalToken(FinalSymbol.MULTI));
//		t.add(new FinalToken(FinalSymbol.CONST_INT));
//		t.add(new FinalToken(FinalSymbol.MULTI));
//		t.add(new FinalToken(FinalSymbol.CONST_INT));
//		Parser parser = new Parser(t);
//		System.out.println(parser.check());
		
		Lexer lexer = new Lexer(Constant.codeFile);
		ArrayList<FinalToken> t = lexer.tokenize();
		Parser parser = new Parser(t);
		System.out.println(parser.check());
	}
	
}
