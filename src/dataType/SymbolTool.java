package dataType;

import static dataType.FinalSymbol.*;

import java.util.HashMap;
import java.util.Vector;

public class SymbolTool {
	
	// Attributes
	protected static FinalSymbol[] finalSymbols;
	protected static final int length;
	protected static HashMap<FinalSymbol, String> finalSymbolMap;
	protected static Vector<FinalSymbol> others;
	protected static Vector<FinalSymbol> keywords;
	protected static Vector<FinalSymbol> delimiters;
	protected static Vector<FinalSymbol> operators;
	
	// Initialize Block
	static {
		finalSymbols = FinalSymbol.values();
		length = finalSymbols.length;
		
		// TODO: change FinalSymbol's range
		others =  new Vector<FinalSymbol>();
		for(int i = IDENTIFIER.ordinal(); i <= SPACE.ordinal(); i++)
			others.add(get(i));
		keywords =  new Vector<FinalSymbol>();
		for(int i = IF.ordinal(); i <= FOR.ordinal(); i++)
			keywords.add(get(i));
		delimiters =  new Vector<FinalSymbol>();
		for(int i = COMMA.ordinal(); i <= RBRACE.ordinal(); i++)
			delimiters.add(get(i));
		operators =  new Vector<FinalSymbol>();
		for(int i = ASSIGN.ordinal(); i <= LEQU.ordinal(); i++)
			operators.add(get(i));
		
		finalSymbolMap = new HashMap<FinalSymbol, String>();
		// TODO: change finalSymbol's content
		// Others
		finalSymbolMap.put(IDENTIFIER, "[a-zA-Z][a-zA-Z0-9_]*");
		finalSymbolMap.put(CONST_REAL, "\\d+(?:\\.\\d+)");
		finalSymbolMap.put(CONST_INT, "\\d+");
		finalSymbolMap.put(CONST_STRING, "\"[^\"]+\""); 
		finalSymbolMap.put(NEW_LINE, "\\n");
		finalSymbolMap.put(SPACE, "[\\s]+"); // 包括空格,制表符,换页符等等
		// Keywords
		finalSymbolMap.put(IF, "if");
		finalSymbolMap.put(ELSE, "else");
		finalSymbolMap.put(BOOL, "bool");
		finalSymbolMap.put(CONST_TRUE, "true");
		finalSymbolMap.put(CONST_FALSE, "false");
		finalSymbolMap.put(INT, "int");	
		finalSymbolMap.put(DOUBLE, "double");
		finalSymbolMap.put(STRING, "string");
		finalSymbolMap.put(FOR, "for");
		// Delimiters
		finalSymbolMap.put(COMMA, ",");
		finalSymbolMap.put(SEMICOLON, ";");
		finalSymbolMap.put(LPAREN, "\\(");
		finalSymbolMap.put(RPAREN, "\\)");
		finalSymbolMap.put(LBRACKET, "\\[");
		finalSymbolMap.put(RBRACKET, "\\]");
		finalSymbolMap.put(LBRACE, "\\{");
		finalSymbolMap.put(RBRACE, "\\}");
		// Operators
		finalSymbolMap.put(ASSIGN, "=");
		finalSymbolMap.put(PLUS, "\\+");
		finalSymbolMap.put(MINUS, "-");
		finalSymbolMap.put(MULTI, "\\*");
		finalSymbolMap.put(DIVIDE, "/");
		finalSymbolMap.put(EQUAL, "==");
		finalSymbolMap.put(GTHEN, ">");
		finalSymbolMap.put(LTHEN, "<");
		finalSymbolMap.put(GEQU, ">=");
		finalSymbolMap.put(LEQU, "<=");
	}
	
	// Functions
	public static FinalSymbol get(int index){
		if (index >= length) {
			throw new IllegalArgumentException();
		}
		return finalSymbols[index];
	}
	
	/** @return: 第index类字符串表示 */
	public static String getSymbol(int index){
		if (index >= length) {
			throw new IllegalArgumentException();
		}
		FinalSymbol finalSymbol = get(index);
		return finalSymbolMap.get(finalSymbol);
	}
	
	/**
	 * 根据content在关键词中找对应symbol
	 * @return null:未找到对应类    otherwise: keyword which name equals to content 
	 * */
	public static FinalSymbol toKeyword(String content) {
		FinalSymbol ans = null;
		for(FinalSymbol key : keywords){
			String value = finalSymbolMap.get(key);
			if (value.equals(content)) {
				ans = key;
			}
		}
		return ans;
	}
	
	public static int getLength() {
		return length;
	}
	
}