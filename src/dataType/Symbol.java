package dataType;

/**
 * hypothesis1: 第一个规则归约是终结状态,第一个规则形如sp = X(任意表达式)$
 * hypothesis2: 首字母大写是终结符
 */
public interface Symbol {
	
	boolean isFinal();
	
	String name();
	
	int index(); // 取代ordinal，表示在SymbolList的位置
	
}
