package parser;

import java.util.ArrayList;
import java.util.Stack;

import dataType.CombineSymbol;
import dataType.FinalSymbol;
import debuger.Debuger;
import lexer.AbstractToken;
import lexer.CombineToken;
import lexer.FinalToken;
import lexer.Lexer;
import lexer.TokenFactory;


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
		stack.push(0);
		for(int i = 0; i < tokens.size(); i++){
			FinalToken token = tokens.get(i);
			boolean success = pushToken(stack, token);
			if (!success) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 假设栈顶存在一个数字符号为当前状态，压入一个符号根据规则对栈中进行处理，
	 * 最后栈顶为下一步起始状态
	 * @param stack
	 * @param received
	 * @return
	 */
	private boolean pushToken(Stack<Object> stack, AbstractToken token) {
		int currID = (int) stack.peek(); // 当前状态号
		Status nextStatus = table.jump(currID, token.getSymbol());
		if (nextStatus == null) {
			printError(token);
			return false;
		} else if (nextStatus.action == Action.shift 
				|| nextStatus.action == Action.goTo) {
			stack.push(token);
			stack.push(nextStatus.target);
			debuger.println("push token->" + token.toString());
			return true;
		} else if (nextStatus.action == Action.reduce) {
			int ruleID = nextStatus.target; 
			boolean success1 = reduce(stack, ruleID);
			boolean success2 = pushToken(stack, token);
			return success1 && success2; // 猜测成功取决于两步成功
		} else {						 // accept
			return true;
		}
	}
	
	/**
	 * 假设栈顶存在一个数字符号为当前状态，按规则归约栈顶(直到遇到规则右侧第一个符号前的状态)，
	 * 再压入归约所得符号
	 * @param stack
	 * @param ruleID
	 * @return
	 */
	private boolean reduce(Stack<Object> stack, int ruleID) {
		Term rule = Define.getRule(ruleID);
		int num = rule.right.size(); // 规则右侧符号数
		num *= 2;					 // pop数量是符号数+状态数(符号数=状态数)
		while (num-- > 0) {
			stack.pop();
		}
		CombineSymbol symbol = rule.left;
		int rowNum = 0; // FIXME: symbol->token  确定rowNum
		CombineToken token = new CombineToken(symbol, rowNum);
		debuger.println("reduce->" + token.toString());
		return pushToken(stack, token);
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
		
		Lexer lexer = new Lexer("C:/Users/asus/Desktop/code.txt");
		ArrayList<FinalToken> t = lexer.tokenize();
		Parser parser = new Parser(t);
		System.out.println(parser.check());
	}
	
	private void printTokens() {
		debuger.println("token串:");
		for(FinalToken token : tokens) {
			debuger.println(token.toString());
		}
	}
	
	private void printError(AbstractToken token) {
		debuger.println("匹配失败");
		debuger.println("token是:" + token.toString());
	}
	
}
