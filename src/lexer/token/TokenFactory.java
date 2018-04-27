package lexer.token;

import dataType.CombineSymbol;
import dataType.FinalSymbol;

public class TokenFactory {
	
	public static FinalToken makeFinalToken(FinalSymbol finalSymbol, Position position, String content) {
		if (finalSymbol == FinalSymbol.DOUBLE) {
			return new DoubleToken(finalSymbol, position, content);
		} else if (finalSymbol == FinalSymbol.INT) {
			return new IntToken(finalSymbol, position, content);
		} else if (finalSymbol == FinalSymbol.STRING) {
			return new StringToken(finalSymbol, position, content);
		} else {
			return new FinalToken(finalSymbol, position, content);
		}           
		// FIXME: complete case
	}
	
	public static CombineToken makeCombineToken(CombineSymbol combineSymbol, Position position) {
		return new CombineToken(combineSymbol, position);
	}
	
}
