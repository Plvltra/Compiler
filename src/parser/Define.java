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
 * 1.��������
 * 2.ȷ��table����ʼ��
 * 3.���ݹ���ȷ��nullable����, front����
 */
public class Define{
	//	��ӡrules,nullable����,ǰ�˷��ż���
	protected static Debuger debuger = new Debuger(true); 
	protected static HashSet<CombineSymbol> nullableSet; 		// Ϊ�յķ��ս��
	protected static HashMap<Symbol, HashSet<FinalSymbol>> frontMap; // ÿ������չ��ʽ�п��ܳ������ǰ���ս���Ķ�Ӧ
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
		srcTerm.insert(FinalSymbol.DOLLAR); // DOLLAR������expected�ᵼ�µ�һ��û��expected��
											// �޷������ַ�
		buildRule();
		printRule();
		buildNullable();
		printNullable();
		buildFront(); // �Ƚ���nullable����ܴ���ǰ�ñ�
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
				if (rule.right.isEmpty()) { // ����ʽΪleft = ��
					boolean changed = insert(nullableSet, toAdd);
					if(changed) {
						stillChange = true;						
					}
				} else { 					// ����ʽΪleft = r1,r2,r3...
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
	
	/** ���ݹ���,����ÿ�����Ŷ�Ӧ��ǰ�÷��ż��� */
	protected static void buildFront() {
		// initialize
		frontMap = new HashMap<Symbol, HashSet<FinalSymbol>>(); 
		for (Symbol symbol : SymbolList.values()) { 
			HashSet<FinalSymbol> set = new HashSet<FinalSymbol>(); 
			if (symbol.isFinal()) { // �ս����ǰ�ü��Ͼ����Լ�  
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
						// 1.����ǵ�һ��ѭ���������ս��
						//   1)������ս��ǰ�ķ��ս���Ѿ�������������˴˷��ţ�
						//   ���Ǹ��ս��һ���ı�stillChangeΪtrue
						//   2)���û���������ս������˴�successΪtrue�����ı�stillChange
						// 2.����ǵ�n��ѭ���������ս��
						//   ��Ȼ�޷�����ɹ������ı�stillChange
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
	 * @param symbol ����
	 * @return true:��������nullable����  false:��֮
	 */
	protected static boolean isNullable(Symbol symbol){
		return nullableSet.contains(symbol);			
	}
	
	/**
	 * @param symbol �����Ƿ��ս��
	 * @return symbol��ǰ�ü���,Ϊ�շ��ؿռ���
	 */
	protected static HashSet<FinalSymbol> frontOf(Symbol symbol){
		return frontMap.get(symbol);			
	}
	
	public static Term getRule(int ruleID){
		return rules.get(ruleID);
	}
	
	/**
	 * @param rightPart Ѱ��first���ϵ�ͷ����
	 * @param finalSet Ѱ��first���ϵĵ�ײ���(�����ս��)
	 * @return �Ҳ���ʽ���ܵ��׸�Ԫ��(�����ս��)�ļ���
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
	
	/** �ϲ�addSet���ϵ�changeSet���ϣ�����changeSet�Ƿ񱻸ı� */
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
	 * @return true:set���ı� false:��֮
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
