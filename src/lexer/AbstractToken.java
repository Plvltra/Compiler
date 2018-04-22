package lexer;

import dataType.Symbol;

public interface AbstractToken {
	
	// Functions
	Symbol getSymbol();

	void setRow(int rowNum);

	int getRow();

	String toString();

}