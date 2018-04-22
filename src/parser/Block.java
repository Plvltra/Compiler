package parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;

import dataType.CombineSymbol;
import dataType.FinalSymbol;
import dataType.Symbol;


//------------------- Block part -------------------//
public class Block{
	// Attributes
	private ArrayList<Term> block;
	
	// Constructors
	public Block() {
		block = new ArrayList<Term>();
	}
	
	// Functions
	public ArrayList<Term> getTerms(){
		return block;
	}
	
	public String toString(){
		String s = "Block:\n";
		for(Term term : block){
			s += term.toString() + "\n";
		}
		return s;
	}
	
	/** 将块闭包化 */
	public Block closure(){
		HashSet<Term> termSet = new HashSet<Term>(); 
		for(Term term : block){
			termSet.add(term);
		}
		
		boolean stillChange = true;
		while (stillChange) {
			stillChange = false;
			ArrayList<Term> addInTerms = new ArrayList<Term>(); // 临时保存新添加的项
			for (Term srcTerm : block) {
				Symbol toExtendSym = srcTerm.peek(); // 待扩展符号 
				if (toExtendSym != null) {
					ArrayList<Term> extendList = findRule(toExtendSym); 
					for (Term newTerm : extendList) { 	 // 创建每个新的项
						Term srcCopy = srcTerm.clone(); 
						srcCopy.poll(); // 去除扩展的符号
						HashSet<FinalSymbol> expectedSet =
							Define.frontSet(srcCopy.right, srcCopy.expected);
						newTerm.insert(expectedSet);	// 补全新生成项的期待集合
						if(!termSet.contains(newTerm)){ //防止产生重复项
							addInTerms.add(newTerm);
							termSet.add(newTerm);
							stillChange = true;
						}
					}
				}
			}
			// 向块中添加新的项
			for (Term addInTerm : addInTerms) {
				Term similatTerm = null;
				for(Term srcTerm : block) {
					if (addInTerm.similar(srcTerm)) {
						similatTerm = srcTerm;
						break;
					}
				}
				if (similatTerm == null) {
					block.add(addInTerm);	
				} else {
					similatTerm.union(addInTerm);
				}
			}
		}
		return this;
	}
	
	/**
	 * get the block next to this block via symbol
	 * @param symbol
	 * @return null : 不存在经由symbol到达的块
	 */
	public Block nextBlock(Symbol eaten){
		Block nextBlock = new Block();
		for(Term term : block){
			if(term.peek() == eaten){ // 项最左侧符号符合条件
				Term addIn = term.clone();
				addIn.poll(); // 点号移进
				nextBlock.add(addIn);
			}
		}
		if(nextBlock.isEmpty()){
			return null;
		} else {
			return nextBlock.closure();
		}
	}
	
	// Tricky functions
	public void add(Term t){
		block.add(t);
	}
	
	public boolean isEmpty() {
		return block.isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		Block other = (Block) obj;
		return block.equals(other.block);
	}
	
	@Override
	public int hashCode() {
		// FIXME: optimize hashCode
		int ans = 0;
		for(Term term : block){
			ans += term.hashCode();
		}
		return ans;
	}
	
	/** 返回符号对应规则 ,终结符返回空集*/
	private ArrayList<Term> findRule(Symbol symbol){
		if (symbol.isFinal()) {
			return new ArrayList<Term>(); 
		} else {
			ArrayList<Term> ans = new ArrayList<Term>();
			for(Term term : Define.rules) {
				if(term.left == symbol){
					ans.add(term);
				}
			}
			return ans;
		}
	}
}

class Term implements Cloneable{
	// Attributes
	protected int ruleID;
	protected CombineSymbol left;
	protected Queue<Symbol> right;
	protected HashSet<FinalSymbol> expected;
	
	// Constructors
	public Term(){
		right = new NotNullLinkedList<Symbol>();
		expected = new HashSet<FinalSymbol>();
	}
	
	public Term(int ruleID) {
		this.left = Define.getRule(ruleID).left;
		this.ruleID = ruleID;
		right = new NotNullLinkedList<Symbol>();
		expected = new HashSet<FinalSymbol>();
	}
	
	// Functions
	@Override
	public Term clone(){
		Term term = null;
		try {
			term = (Term) super.clone();
			term.right = new NotNullLinkedList<Symbol>(right);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return term;
	}
	
	public String toString(){
		String s = ruleID + ": " + left.name() + " ->";
		for(Symbol symbol : right){
			s += " " + symbol.name();
		}
		s += " ::";
		for (Symbol symbol : expected) {
			s += " " + symbol.name();
		}
		return s;
	}
	
	// Tricky functions
	/** add to right */
	public void add(Symbol symbol){
		right.add(symbol);
	}
	
	public void insert(FinalSymbol symbol){
		expected.add(symbol);
	}
	
	/** insert to expected */
	public void insert(HashSet<FinalSymbol> expectedSet){
		if(expectedSet == null)
			throw new NullPointerException();
		for(FinalSymbol expectedSym : expectedSet){
			if(!expectedSym.isFinal()){
				throw new IllegalArgumentException();
			}else{
				expected.add(expectedSym);					
			}
		}
	}
	
	public boolean similar(Term other) {
		return ruleID == other.ruleID 
				&& left.equals(other.left)
				&& right.equals(other.right);
	}
	
	/** 将other的期待集并入此对象 */
	public boolean union(Term other) {
		return Define.union(this.expected, other.expected);
	}
	
	@Override
	public boolean equals(Object obj) {
		Term other = (Term)obj;
		return left.equals(other.left) && right.equals(other.right)
				&& ruleID == other.ruleID && expected.equals(other.expected);
	}
	
	@Override
	public int hashCode() {
		// FIXME: optimize hashCode
		int ans = 0;
		ans += ruleID;
		ans += left.index();
		for(Symbol sym : right){
			ans += sym.index();
		}
		for(Symbol sym : expected){
			ans += sym.index();
		}
		return ans;
	}
	
	public void setLeft(CombineSymbol left) {
		this.left = left;
	}

	public boolean isEmpty() {
		return right.isEmpty();
	}
	
	/**  @return the head of right, or null if right is empty */
	public Symbol peek() {
		return right.peek();
	}
	
	/**  @return the head of right, or null if right is empty */
	public Symbol poll() {
		return right.poll();
	}
}
