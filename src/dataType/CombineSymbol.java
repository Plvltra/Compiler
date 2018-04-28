package dataType;

public enum CombineSymbol implements Symbol{
	// TODO: change combineSymbol
	sp,
	program,
	statement,
	defineStatm,
	assighStatm,
	ifStatm,
	elseStatm,
	body,
	;
	
	//test1
//	sp,
//	s,
//	l,
//	r,
//	;
	
	//test2
//	sp,
//	expr,
//	term,
//	;
	
	@Override
	public int index() {
		int bias = FinalSymbol.values().length;
		return this.ordinal() + bias;
	}
	
	@Override
	public boolean isFinal() {
		return false;
	}
	
}