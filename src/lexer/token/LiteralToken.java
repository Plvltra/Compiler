package lexer.token;

import dataType.FinalSymbol;

// ����ֵ����
public abstract class LiteralToken<Type> extends FinalToken {
	
	// Functions
	public LiteralToken(FinalSymbol finalSymbol, Position position, String content) {
		super(finalSymbol, position, content);
	}

	/** �����������ݶ�Ӧ��ʵ��ֵ  */
	public abstract Type getContent();
	
}

class DoubleToken extends LiteralToken<Double> {

	public DoubleToken(FinalSymbol finalSymbol, Position position, String content) {
		super(finalSymbol, position, content);
	}

	@Override
	public Double getContent() {
		Double ans = Double.parseDouble(content);
		return ans;
	}
	
}

class StringToken extends LiteralToken<String>{

	public StringToken(FinalSymbol finalSymbol, Position position, String content) {
		super(finalSymbol, position, content);
	}
	
	@Override
	public String getContent() {
		return content;
	}  
	
}

class IntToken extends LiteralToken<Integer> {

	public IntToken(FinalSymbol finalSymbol, Position position, String content) {
		super(finalSymbol, position, content);
	}

	@Override
	public Integer getContent() {
		Integer ans = Integer.parseInt(content); 
		return ans;
	}
	
}
