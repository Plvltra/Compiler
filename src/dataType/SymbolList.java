package dataType;

import java.util.ArrayList;

@SuppressWarnings("serial")
// SymbolList模拟实现enum类型静态类
public class SymbolList extends ArrayList<Symbol>{
	
	private static final SymbolList symbolList;
	private static final ArrayList<FinalSymbol> finalList;
	private static final ArrayList<CombineSymbol> combineList;
	
	static {
		symbolList = new SymbolList();
		finalList = new ArrayList<FinalSymbol>();
		combineList = new ArrayList<CombineSymbol>();
		
		for(FinalSymbol symbol : FinalSymbol.values()) {
			symbolList.add(symbol);
			finalList.add(symbol);
		}
		for(CombineSymbol symbol : CombineSymbol.values()) {
			symbolList.add(symbol);
			combineList.add(symbol);
		}
	}
	
	private SymbolList() {
		
	}

	public static ArrayList<Symbol> values() {
		return symbolList;
	}
	
	public static ArrayList<FinalSymbol> finalValues() {
		return finalList;
	}
	
	public static ArrayList<CombineSymbol> combineValues() {
		return combineList;
	}
	
//	public static void main(String[] args) {
//		for(Symbol symbol : SymbolList.values()) {
//			if(symbol.name() == " sp")
//				System.out.println(symbol.name());
//		}
//	}
	
}
