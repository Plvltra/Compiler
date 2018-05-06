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
	// ��ӡ
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
		Stack<Object> stack = new Stack<Object>(); // ���ܰ���Integer��AbstractToken
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
	 * ջ����һ�����ַ���Ϊ��ǰ״̬��ѹ��һ�����Ÿ��ݹ����ջ�н��д���
	 * ���ջ��Ϊ��һ����ʼ״̬
	 * @param stack
	 * @param nodeStack
	 * @param received
	 * @return
	 */
	private boolean pushToken(
			Stack<Object> stack, Stack<Node> nodeStack, AbstractToken token, Node node) {//token��node��Ӧ
		
		int currID = (int) stack.peek(); // ��ǰ״̬��
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
			return success1 && success2; // �²�ɹ�ȡ���������ɹ�
		} else {						 // accept
			return true;
		}
	}
	
	/**
	 * ջ����һ�����ַ���Ϊ��ǰ״̬���������Լջ��(ֱ�����������Ҳ��һ������ǰ��״̬)��
	 * ��ѹ���Լ���÷���
	 * @param stack
	 * @param ruleID
	 * @return
	 */
	private boolean reduce(
			Stack<Object> stack, Stack<Node> nodeStack, int ruleID) {
		
		Term rule = Define.getRule(ruleID);
		int symbolCount = rule.right.size(); // �����Ҳ������
		int tokenCount = 2 * symbolCount;	 // pop token�����Ƿ�����+״̬��(������=״̬��)
		Node[] nodes = new Node[symbolCount];
		while (tokenCount-- > 0) {
			if (tokenCount % 2 != 0) {  	// tokenCount����Ϊ����
				stack.pop();
				Node node = nodeStack.pop();
				nodes[tokenCount/2] = node;
			} else {
				stack.pop();
			}
		}
		
		Node gotNode = NodeFactory.createNode(nodes,ruleID);
		CombineSymbol symbol = rule.left;
		CombineToken token = new CombineToken(symbol); // FIXME:ȷ�����token��λ��
		debuger.println("reduce->" + token.toString());
		return pushToken(stack, nodeStack, token, gotNode);
//		return pushToken(stack, nodeStack, token, null);
	}
	
	private void printTokens() {
		debuger.println("token��:");
		for(FinalToken token : tokens) {
			debuger.println(token.toString());
		}
	}
	
	private void printError(int currID, AbstractToken token) {
		debuger.println("ƥ��ʧ��");
		debuger.println("���token��:" + token.toString());
		debuger.print("�ڴ�token��:");
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
