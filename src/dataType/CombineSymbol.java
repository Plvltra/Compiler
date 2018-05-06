package dataType;

public enum CombineSymbol implements Symbol{
	// TODO: change combineSymbol
	sp,
	program,
	
	type,
	operator,
	constValue,
	
	arithmExpr,
	assignExpr,
	expression,
	
	body,
	condition,
	
	exprStatm,
	defineStatm,
	assighStatm,
	ifElseStatm,
	ifStatm,
	elseStatm,
	forStatm,
	whileStatm,
	returnStatm,
	
	statement,
	statmList,
	
	formalParam,
	emptyFormalParamList,
	formalParamList,
	mainFunc,
	funcDef,
	
	actualParam,
	emptyActualParamList,
	actualParamList,
	funcCall,
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