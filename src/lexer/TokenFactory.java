package lexer;

import dataType.CombineSymbol;
import dataType.FinalSymbol;

public class TokenFactory {
	
	public static FinalToken makeFinalToken(FinalSymbol finalSymbol, int rowNum, String content) {
		if (finalSymbol == FinalSymbol.DOUBLE) {
			return new DoubleToken(finalSymbol, rowNum, content);
		} else if (finalSymbol == FinalSymbol.INT) {
			return new IntToken(finalSymbol, rowNum, content);
		} else if (finalSymbol == FinalSymbol.STRING) {
			return new StringToken(finalSymbol, rowNum, content);
		} else {
			return new FinalToken(finalSymbol, rowNum, content);
		}           
		// FIXME: complete case
	}
	
	public static CombineToken makeCombineToken(CombineSymbol combineSymbol, int rowNum) {
		return new CombineToken(combineSymbol, rowNum);
	}
	
}
