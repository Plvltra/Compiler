package lexer.token;

import dataType.Symbol;

public interface AbstractToken {
	
	// Functions
	Symbol getSymbol();

	void setPosition(Position position);

	Position getPosition();

	String toString();

}