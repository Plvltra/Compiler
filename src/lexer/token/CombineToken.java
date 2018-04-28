package lexer.token;

import dataType.CombineSymbol;

public class CombineToken implements AbstractToken{

	protected CombineSymbol symbol;
	protected Position position;
	
	public CombineToken(CombineSymbol symbol, Position position) {
		this.symbol = symbol;
		this.position = position.clone();
	}
	
	public CombineToken(CombineSymbol symbol) {
		this.symbol = symbol;
		position = Position.unknown();
	}

	@Override
	public CombineSymbol getSymbol() {
		return symbol;
	}

	@Override
	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}
	
	@Override
	public String toString(){
		return " symbol:" + symbol.name() + " position:" + position.toString(); 
	}

}
