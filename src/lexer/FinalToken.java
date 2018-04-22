package lexer;

import dataType.FinalSymbol;
import dataType.Symbol;

public class FinalToken implements AbstractToken {
	
	// Attributes
	protected FinalSymbol symbol;
	protected int rowNum;
	protected String content;
	
	// Constructors
	public FinalToken(FinalSymbol symbol, int rowNum, String content) {
		if (symbol == null || content == null) {
			throw new IllegalArgumentException();
		}
		this.symbol = symbol;
		this.rowNum = rowNum;
		this.content = content;
	}
	
	public FinalToken(FinalSymbol symbol) {
		this.symbol = symbol;
		rowNum = 0;
		content = "";
	}

	public int length() {
		return content.length();
	}
	
	// Functions
	@Override
	public Symbol getSymbol(){
		return this.symbol;
	}
	
	@Override
	public void setRow(int rowNum) {
		this.rowNum = rowNum;
	}
	
	@Override
	public int getRow() {
		return this.rowNum;
	}
	
	@Override
	public String toString(){
		return " symbol:" + symbol.name() + " rowNum:" + rowNum
				+ " content:" + content; 
	}
	
}