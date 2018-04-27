package lexer.token;

public class Position implements Cloneable{
	
	private int rowNum;
	
	// Constructors
	public Position(int rowNum) {
		this.rowNum = rowNum;
	}
	
	// Functions
	public Position nextRow() {
		Position ans = this.clone();
		ans.rowNum++;
		return ans;
	}
	
	public static Position unknown() {
		return new Position(0);
	}
	
	@Override
	public String toString() {
		return "rowNum: " + rowNum;
	}
	
	@Override
	public Position clone() {
		Position p = null;
		try {
			p = (Position)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return p;
	}
}
