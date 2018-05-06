package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;


import dataType.CombineSymbol;
import dataType.FinalSymbol;
import dataType.Symbol;
import dataType.SymbolList;
import util.Debuger;

enum Action{
	shift,
	goTo,
	reduce,
	accept;
}

/**
 * 1.建立规则
 * 2.确定table的起始项
 * 3.根据规则确定nullable集合, front集合
 */
public class Define{
	//	打印rules,nullable集合,前端符号集合
	protected static Debuger debuger = new Debuger(true); 
	protected static HashSet<CombineSymbol> nullableSet; 		// 为空的非终结符
	protected static HashMap<Symbol, HashSet<FinalSymbol>> frontMap; // 每个符号展开式中可能出想的最前端终结符的对应
	protected static ArrayList<Term> rules;
	protected static final int ROW_SIZE;
	public static final Term srcTerm;
	
	static {
		ROW_SIZE = SymbolList.values().size();
		// TODO: change srcTerm
		srcTerm = new Term();
		srcTerm.ruleID = 0;
		srcTerm.left = CombineSymbol.sp;
		srcTerm.add(CombineSymbol.program);
		srcTerm.insert(FinalSymbol.DOLLAR); // DOLLAR不放入expected会导致第一项没有expected集
											// 无法接受字符
		buildRule();
		printRule();
		buildNullable();
		printNullable();
		buildFront(); // 先建立nullable表才能创建前置表
		printFrontSet();
	}
	
	protected static void buildRule() {
		rules = RuleGetter.resolveRule();
	}
	
	protected static void buildNullable() {
		nullableSet = new HashSet<CombineSymbol>();
		boolean stillChange = true;
		while (stillChange) {
			stillChange = false;
			for (Term rule : rules) {
				CombineSymbol toAdd = rule.left;
				if (rule.right.isEmpty()) { // 产生式为left = 空
					boolean changed = insert(nullableSet, toAdd);
					if(changed) {
						stillChange = true;						
					}
				} else { 					// 产生式为left = r1,r2,r3...
					boolean isNullable = true;
					for (Symbol symbol : rule.right) {
						if(!isNullable(symbol)){
							isNullable = false;
						}
					}
					if(isNullable){
						boolean changed = insert(nullableSet, toAdd);
						if (changed) {
							stillChange = true;							
						}
					}
				}
			}
		}
	}
	
	/** 根据规则,建立每个符号对应的前置符号集合 */
	protected static void buildFront() {
		// initialize
		frontMap = new HashMap<Symbol, HashSet<FinalSymbol>>(); 
		for (Symbol symbol : SymbolList.values()) { 
			HashSet<FinalSymbol> set = new HashSet<FinalSymbol>(); 
			if (symbol.isFinal()) { // 终结符的前置集合就是自己  
				set.add((FinalSymbol)symbol);  
			}
			frontMap.put(symbol, set);
		}
		// build notFinal
		boolean stillChange = true;
		while (stillChange) {
			stillChange = false;
			for(Term rule : rules){
				Symbol toChange = rule.left;
				HashSet<FinalSymbol> toChangeSet = frontMap.get(toChange);
				for(Symbol toAdd : rule.right){
					if (toAdd.isFinal()) {
						// 1.如果是第一轮循环读到此终结符
						//   1)如果此终结符前的非终结符已经往集合里插入了此符号，
						//   则那个终结符一定改变stillChange为true
						//   2)如果没有上述非终结符，则此处success为true，并改变stillChange
						// 2.如果是第n轮循环读到此终结符
						//   必然无法插入成功，不改变stillChange
						boolean changed = insert(toChangeSet, (FinalSymbol)toAdd);
						if (changed) {
							stillChange = true;
						}
						break;
					} else {
						HashSet<FinalSymbol> toAddSet = frontOf(toAdd);
						boolean changed = union(toChangeSet, toAddSet);
						if (changed) {
							stillChange = true;	
						}
						if(!isNullable(toAdd)){
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * @param symbol 符号
	 * @return true:符号属于nullable集合  false:反之
	 */
	protected static boolean isNullable(Symbol symbol){
		return nullableSet.contains(symbol);			
	}
	
	/**
	 * @param symbol 必须是非终结符
	 * @return symbol的前置集合,为空返回空集合
	 */
	protected static HashSet<FinalSymbol> frontOf(Symbol symbol){
		return frontMap.get(symbol);			
	}
	
	public static Term getRule(int ruleID){
		return rules.get(ruleID);
	}
	
	/**
	 * @param rightPart 寻找first集合的头部分
	 * @param finalSet 寻找first集合的垫底部分(都是终结符)
	 * @return 右产生式可能的首个元素(都是终结符)的集合
	 */
	public static HashSet<FinalSymbol> frontSet(
			Queue<Symbol> rightPart, HashSet<FinalSymbol> finalSet){
		
		if(rightPart == null || finalSet == null)
			throw new NullPointerException();
		HashSet<FinalSymbol> ans = new HashSet<FinalSymbol>();
		for(Symbol symbol : rightPart){
			union(ans, frontOf(symbol)); 
			if(!isNullable(symbol)){
				return ans;
			}
		}
		union(ans, finalSet);
		return ans;
	}
	
	/** 合并addSet集合到changeSet集合，返回changeSet是否被改变 */
	public static <T> boolean union(HashSet<T> changeSet, HashSet<? extends T> addSet){
		if(changeSet == null || addSet == null)
			throw new NullPointerException();
		boolean changed = false;
		for(T symbol : addSet){
			if (!changeSet.contains(symbol)) {
				changeSet.add(symbol);
				changed = true;
			}
		}
		return changed;
	}
	
	/**
	 * @return true:set被改变 false:反之
	 */
	public static <T> boolean insert(HashSet<T> set, T symbol){
		if (set.contains(symbol)) {
			return false;
		} else {
			set.add(symbol);
			return true;
		}
	}
	
	private static void printRule() {
		debuger.println("rules:");
		for(Term rule : rules) {
			debuger.println(rule.toString());
		}
		debuger.println();
	}
	
	private static void printNullable() {
		debuger.println("nullableSet:");
		for(Symbol symbol : nullableSet){
			debuger.println(symbol.name());
		}
		debuger.println();
	}
	
	private static void printFrontSet() {
		debuger.println("frontSet:");
		for(Symbol symbol : SymbolList.values()){
			debuger.print(symbol.name() + ": ");
			HashSet<FinalSymbol> frontSet = frontMap.get(symbol);
			for(FinalSymbol symbol2 : frontSet){
				debuger.print(symbol2.name() + " ");
			}
			debuger.println();
		}
		debuger.println();
	}
	
}
