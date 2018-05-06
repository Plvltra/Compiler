package parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import dataType.CombineSymbol;
import dataType.Symbol;
import dataType.SymbolList;
import util.Constant;
import util.FileUtil;
import util.StringUtil;

public class RuleGetter {
	
	/** 依照顺序从文件中解析全部的规则 
	 * 若出错捕获异常 
	 */
	public static ArrayList<Term> resolveRule() {
		ArrayList<String> lines = FileUtil.readLine(Constant.ruleFile);
		ArrayList<Term> rules = new ArrayList<Term>();
		int size = lines.size();
		for(int i = 0; i < size; i++) {
			try { 
				String line = lines.get(i);
				if (!StringUtil.isBlank(line)) {
					Term rule = resolve(line);
					rules.add(rule);
				}
			} catch (UnresolvedNameException e) {
				System.out.println("解析规则出错: " + e.toString()); 
				System.exit(0);
			}
		}
		return rules;
	}
	
	/**
	 * @return 解析出的一行规则
	 * @throws UnresolvedNameException 文件中找到符号列表中没有的符号
	 */
	private static Term resolve(String line) throws UnresolvedNameException {
		String[] names = line.split("\\s+");
		String intString = names[0].substring(0, names[0].length()-1); // 删除'.'号
		int ruleID = Integer.parseInt(intString);
		try {
			CombineSymbol left = (CombineSymbol) find(names[1]);
			ArrayList<Symbol> right = new ArrayList<Symbol>();
			for(int i = 3; i < names.length; i++) { // names[2] = "->", 从第三个起为展开式
				Symbol symbol = find(names[i]);
				right.add(symbol);
			}
			return new Term(ruleID, left, right);
		} catch (UnresolvedNameException e) {
			e.setRuleID(ruleID);
			throw e;
		}
	}
	
	/**
	 * @param symbolName
	 * @return
	 * @throws UnresolvedNameException 文件中找到符号列表中没有的符号
	 */
	private static Symbol find(String symbolName) throws UnresolvedNameException {
		for(Symbol symbol : SymbolList.values()) {
			if (symbol.name().equals(symbolName)) {
			//if (symbol.name() == symbolName) {
				return symbol;
			}
		}
		throw new UnresolvedNameException(symbolName);
	}
	
	public static void main(String[] args) {
		RuleAnalyzer.codeGen();
	}
	
	@SuppressWarnings("serial")
	private static class UnresolvedNameException extends Exception{
		private int ruleID;
		private String name;
		
		public UnresolvedNameException(String name) {
			this.name = name;
		}
		
		public void setRuleID(int ruleID) {
			this.ruleID = ruleID;
		}
		
		@Override
		public String toString() {
			return "无法找到的符号名 : " + name + "  规则号: " + ruleID;
		}
	}
	
}

class RuleAnalyzer{

	private final static String arrName = "nodes";
	
	public static void codeGen() {
		ArrayList<Term> rules = RuleGetter.resolveRule();
		String code = "";
		for(Term rule : rules) {
			code += toCode(rule);
		}
		System.out.println(code);
		FileUtil.writeFile(Constant.factoryCodeFile, code);
	}
	
	private static String toCode(Term rule) {
		int ruleID = rule.getRuleID();
		CombineSymbol left = rule.getLeft();
		Queue<Symbol> right = rule.getRight();
		return toCaseCode(ruleID, left, right);
	}
	
	private static String toCaseCode(
			int ruleID, CombineSymbol left, Queue<Symbol> _right){
		
		Queue<Symbol> right = new LinkedList<Symbol>(_right);
		String code = "case " + ruleID + ":{\n";
		code += toRightCode(right);
		code += toReturnCode(left, right);
		code += "}\n";
		return code;
	}

	// 创建返回值的所有构成元素
	private static String toRightCode(Queue<Symbol> _right){
		Queue<Symbol> right = new LinkedList<Symbol>(_right);
		String code = "";
		int index = 0;
		while (!right.isEmpty()) {
			Symbol symbol = right.poll();
			String var = toVarName(symbol);
			String type = toTypeName(symbol);
			String line = type + " " + var + " = "
					+ "(" + type + ")" + arrName +"[" + index++ +"];\n";
			code += line;
		}
		return code;
	}
	
	// 创建返回对象，并返回
	private static String toReturnCode(CombineSymbol left, Queue<Symbol> _right){
		Queue<Symbol> right = new LinkedList<Symbol>(_right);
		ArrayList<String> params = new ArrayList<String>();
		while (!right.isEmpty()) {
			Symbol front = right.poll();
			params.add(front.name());
		}
		String paramList = StringUtil.getDelimString(params, ',');
		
		String type = toTypeName(left);
		String code = type + " " + "ans" + " = "
				+ "new " + type + "(" + paramList + ");\n";
		code += "return ans;\n";
		return code;
	}
	
	private static String toTypeName(Symbol symbol) {
		String name = symbol.name();
		// 首字母大写
		char first = name.charAt(0);
		char upperFirst = Character.toUpperCase(first);
		name = StringUtil.replace(name, 0, upperFirst);
		// 后缀Node
		name += "Node";
		return name;
	}
	
	private static String toVarName(Symbol symbol) {
		return symbol.name();
	}
	
}