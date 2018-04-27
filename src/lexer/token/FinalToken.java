package lexer.token;

import dataType.FinalSymbol;
import dataType.Symbol;

public class FinalToken implements AbstractToken {
	
	// Attributes
	protected FinalSymbol symbol;
	protected Position position;
	public String content; // TODO: change modifier
	
	// Constructors
	public FinalToken(FinalSymbol symbol, Position position, String content) {
		if (symbol == null || content == null) {
			throw new IllegalArgumentException();
		}
		this.symbol = symbol;
		this.position = position.clone();
		this.content = content;
	}
	
	public FinalToken(FinalSymbol symbol) {
		this.symbol = symbol;
		this.position = Position.unknown();
		content = "";
	}

	public int length() {
		return content.length();
	}
	
	// Functions
	@Override
	public FinalSymbol getSymbol(){
		return this.symbol;
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
		return " symbol:" + symbol.name() + " position:" + position.toString()
				+ " content:" + content; 
	}
	
}