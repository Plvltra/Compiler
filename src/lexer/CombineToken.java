package lexer;

import dataType.CombineSymbol;
import dataType.Symbol;

public class CombineToken implements AbstractToken{

	protected CombineSymbol symbol;
	protected int rowNum;
	
	public CombineToken(CombineSymbol symbol, int rowNum) {
		this.symbol = symbol;
		this.rowNum = rowNum;
	}
	
	public CombineToken(CombineSymbol symbol) {
		this.symbol = symbol;
		rowNum = 0;
	}

	@Override
	public Symbol getSymbol() {
		return symbol;
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
		return " symbol:" + symbol.name() + " rowNum:" + rowNum; 
	}

}
