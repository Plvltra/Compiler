package dataType;

public enum FinalSymbol implements Symbol{
	/* 
	 * Conflicts:
	 * IDENTIFIER & Keywords
	 * CONST_REAL & Operators(+-)
	 */
	// TODO: change finalSymbol
	// �����Ⱥ�˳��Ӱ��ƥ���Ⱥ�˳��
	// Others: 6
	IDENTIFIER, 
	CONST_REAL,
	CONST_INT,
	CONST_STRING,
	NEW_LINE,
	SPACE,
	// Keywords: 6
	IF,
	ELSE,
	BOOL,
	CONST_TRUE,
	CONST_FALSE,
	INT,	
	DOUBLE,
	STRING,
	MAIN,
	WHILE,
	RETURN,
	FOR,
	// Delimiters: 8
	COMMA,
	SEMICOLON,
	LPAREN,
	RPAREN,
	LBRACKET,
	RBRACKET,
	LBRACE,
	RBRACE,
	// Operators: 10
	ASSIGN,
	PLUS,
	MINUS,
	MULTI,
	DIVIDE,
	EQUAL,
	GTHEN,
	LTHEN,
	GEQU,
	LEQU,
	// Special: 1
	DOLLAR,	// FIXME: û�п������û�ж�Ӧ������ʽ�ĺ����check bug
	;
	
	//test1: build table
//	ID,
//	STAR,
//	EQUAL,
//	DOLLAR,
//	;
	
	//test2 : parse
//	PLUS,
//	MINUS,
//	MULTI,
//	DIVIDE,
//	INT,
//	DOLLAR,
//	;
	
	@Override
	public boolean isFinal(){
		return true;
	}
	
	public int index() {
		return this.ordinal();
	}
	
}