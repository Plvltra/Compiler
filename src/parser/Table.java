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
 * ����Define��Ĺ���
 * ���շ�����ת
 */
public class Table {
	// ��ӡ�������
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
	 * @return null: ��row[symbol]Ϊ��
	 */
	public Status jump(int currID, Symbol received) {
		Row currRow = table.get(currID);
		Status targStatus = currRow.get(received);
		return targStatus;
	}
	
	public String toString() {
		String indexForm = "%-" + INDEX_WIDTH + "s";
		String headerForm = "%-" + Row.CELL_WIDTH + "s";
		// ��ͷ
		String s = "Table:\n";
		s += String.format(indexForm, " ");
		for(Symbol symbol : SymbolList.values()){
			String header = String.format(headerForm, symbol.name()); 
			s += header;
		}
		s += "\n";
		// ���
		int size = table.size();
		for(int i = 0; i < size; i++) {
			s += String.format(indexForm, i + ":");
			Row row = table.get(i); 
			s += row.toString() + "\n";
		}
		return s;
	}
	
	/**
	 * ����term�����飬����ID,����dfs�ݹ鹹��������(����ͬʱ����Դ��ı�)
	 * @param term �����Ĺ���
	 */
	private void buildTable(final Term term0){
		debuger.println("������...");
		Block block0 = new Block();
		block0.add(term0);
		block0.closure();
		HashMap<Block, Integer> blockMap = new HashMap<Block, Integer>(); // ��ֵID
		blockMap.put(block0, ID++);
		Queue<Block> blockQue = new NotNullLinkedList<Block>(); 
		blockQue.add(block0);
		debuger.print(block0.toString());
		while (!blockQue.isEmpty()) {
			Block front = blockQue.poll(); // �������
			Row row = new Row();
			// ��Լ
			ArrayList<Term> terms = front.getTerms();
			for (Term term : terms) {
				if (term.isEmpty()) {
					HashSet<FinalSymbol> validSymbol = term.expected;
					for(FinalSymbol sym : validSymbol){ 
						Status status;
						if (term.ruleID == 0) { // �����Լ������0�ǵ������״̬
							status = Status.endStatus();
						} else {
							status = new Status(Action.reduce, term.ruleID);
						}
						row.put(sym, status);
					}
				}
			}
			// ת�� 
			for (Symbol eaten : SymbolList.values()) { // ����front,������һ��symbol
				Block nextBlock = front.nextBlock(eaten);
				if (nextBlock != null) { // ���ھ��ɱ��Ե���symbol����Ŀ�
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
	 * @return null: ��row[symbol]Ϊ��
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
