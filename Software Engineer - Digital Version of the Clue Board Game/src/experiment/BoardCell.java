/**
 * @Author Bao Nguyen
 * @author Cassandra Vandeventer
 * 
 * CSCI 306-A
 * C12A-1 Clue Paths (Clue Partner)
 * 
 */
package experiment;

public class BoardCell {
		
	private int column;
	private int row;
		
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
}