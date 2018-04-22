package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import dataType.FinalSymbol;
import dataType.Symbol;
import dataType.SymbolList;
import debuger.Debuger;
	
/**
 * 根据Define里的规则
 * 接收符号跳转
 */
public class Table {
	// 打印建表过程
	private static final Debuger debuger = new Debuger(true); 
	
	// Attributes
	private ArrayList<Row> table;
	private int ID;
	protected static final int INDEX_WIDTH = 4;
	
	// Constructors
	public Table() {
		ID = 0;
		table = new ArrayList<Row>();		
		buildTable(Define.srcTerm); 
	}
	
	// Functions
	/**
	 * @param currStatus
	 * @param received
	 * @return null: 若row[symbol]为空
	 */
	public Status jump(int currID, Symbol received) {
		Row currRow = table.get(currID);
		Status targStatus = currRow.get(received);
		return targStatus;
	}
	
	public String toString() {
		String indexForm = "%-" + INDEX_WIDTH + "s";
		String headerForm = "%-" + Row.CELL_WIDTH + "s";
		// 表头
		String s = "Table:\n";
		s += String.format(indexForm, " ");
		for(Symbol symbol : SymbolList.values()){
			String header = String.format(headerForm, symbol.name()); 
			s += header;
		}
		s += "\n";
		// 表格
		int size = table.size();
		for(int i = 0; i < size; i++) {
			s += String.format(indexForm, i + ":");
			Row row = table.get(i); 
			s += row.toString() + "\n";
		}
		return s;
	}
	
	/**
	 * 利用term构建块，分配ID,并用dfs递归构建后续块(构建同时创建源块的表)
	 * @param term 最外层的规则
	 */
	private void buildTable(final Term term0){
		debuger.println("建表中...");
		Block block0 = new Block();
		block0.add(term0);
		block0.closure();
		HashMap<Block, Integer> blockMap = new HashMap<Block, Integer>(); // 赋值ID
		blockMap.put(block0, ID++);
		Queue<Block> blockQue = new NotNullLinkedList<Block>(); 
		blockQue.add(block0);
		debuger.print(block0.toString());
		while (!blockQue.isEmpty()) {
			Block front = blockQue.poll(); // 待建表块
			Row row = new Row();
			// 归约
			ArrayList<Term> terms = front.getTerms();
			for (Term term : terms) {
				if (term.isEmpty()) {
					HashSet<FinalSymbol> validSymbol = term.expected;
					for(FinalSymbol sym : validSymbol){ 
						Status status;
						if (term.ruleID == 0) { // 假设归约到规则0是到达接收状态
							status = Status.endStatus();
						} else {
							status = new Status(Action.reduce, term.ruleID);
						}
						row.put(sym, status);
					}
				}
			}
			// 转移 
			for (Symbol eaten : SymbolList.values()) { // 对于front,当接受一个symbol
				Block nextBlock = front.nextBlock(eaten);
				if (nextBlock != null) { // 存在经由被吃掉的symbol到达的块
					if(!blockMap.containsKey(nextBlock)) {
						blockMap.put(nextBlock, ID++);
						blockQue.add(nextBlock);
						debuger.print(nextBlock.toString());
					}
					Action action = eaten.isFinal()? Action.shift : Action.goTo;
					int target = blockMap.get(nextBlock);
					Status status = new Status(action, target);
					row.put(eaten, status);
				}
			}
			table.add(row);
		}
		debuger.println(this.toString());
	}
	
	public static void main(String[] args) {
		new Table();
	}
}

class Status {
	Action action;
	int target;
	
	public Status(Action action, int target) {
		this.action = action;
		this.target = target;
	}
	
	public String toString(){
		return action.name() + target;
	}
	
	public static Status endStatus(){
		return new Status(Action.accept, 0);
	}
}

class Row {
	private Status[] row;
	private static final int SIZE = Define.ROW_SIZE;
	protected static final int CELL_WIDTH = 8;
	
	public Row() {
		row = new Status[SIZE]; 
		for(int i = 0;i < SIZE; i++){
			row[i] = null;
		}
	}
	
	/** row[symbol] = status */
	public void put(Symbol symbol, Status status){
		row[symbol.index()] = status;
	}
	
	/**
	 * @param symbol
	 * @return null: 若row[symbol]为空
	 */
	public Status get(Symbol symbol) {
		return row[symbol.index()];
	}
	
	public String toString() {
		String s = "";
		String form = "%-" + CELL_WIDTH + "s";
		for(Status status : row){
			String addIn;
			if(status == null){
				addIn = String.format(form, "null");
			}else{
				addIn = String.format(form, status.toString());
			}
			s += addIn;
		}
		return s;
	}
}
