package lexer;

import dataType.FinalSymbol;

// 字面值类型
public abstract class LiteralToken<Type> extends FinalToken {
	
	// Functions
	public LiteralToken(FinalSymbol finalSymbol, int rowNum, String content) {
		super(finalSymbol, rowNum, content);
	}

	/** 返回类型内容对应的实际值  */
	public abstract Type getContent();
	
}

class DoubleToken extends LiteralToken<Double> {

	public DoubleToken(FinalSymbol finalSymbol, int rowNum, String content) {
		super(finalSymbol, rowNum, content);
	}

	@Override
	public Double getContent() {
		Double ans = Double.parseDouble(content);
		return ans;
	}
	
}

class StringToken extends LiteralToken<String>{

	public StringToken(FinalSymbol finalSymbol, int rowNum, String content) {
		super(finalSymbol, rowNum, content);
	}
	
	@Override
	public String getContent() {
		return content;
	}  
	
}

class IntToken extends LiteralToken<Integer> {

	public IntToken(FinalSymbol finalSymbol, int rowNum, String content) {
		super(finalSymbol, rowNum, content);
	}

	@Override
	public Integer getContent() {
		Integer ans = Integer.parseInt(content); 
		return ans;
	}
	
}
