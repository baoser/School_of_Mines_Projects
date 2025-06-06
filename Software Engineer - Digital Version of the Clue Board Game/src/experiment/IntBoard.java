/**
 * @Author Bao Nguyen
 * @author Cassandra Vandeventer
 * 
 * CSCI 306-A
 * C12A-1 Clue Paths (Clue Partner)
 * 
 */
package experiment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	
	private Map<BoardCell, Set<BoardCell>> adjMtx;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private BoardCell[][] grid = new BoardCell[4][4];; // the game board
	
	public IntBoard() {
		super();
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				grid[r][c] = new BoardCell(r, c);
			}
		}
		adjMtx = new HashMap<BoardCell, Set<BoardCell>>();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		calcAdjacencies();
	}
	
	/**
	 * calculate all possible adjacent cells for 
	 * each BoardCell. if new row and new column 
	 * is less than 0 or greater than 3 then they are
	 * invalid cells. Other wise add it to a temporary
	 * set and add the current cell and the temporay set
	 * to our map
	 */
	public void calcAdjacencies() {
		
		int adjColumn [] = {0, 1, 0, -1};
		int adjRow [] = {-1, 0, 1, 0};
		int newRow;
		int newColumn;
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				for (int k = 0; k < 4; k++) {
					newRow = r + adjRow[k];
					newColumn = c + adjColumn[k];
					if (newRow >= 0 && newRow < 4 && newColumn >= 0 && newColumn < 4) {
						temp.add(grid[newRow][newColumn]);
					}
				}
				//add r, c, and temp to the map we created
				adjMtx.put(grid[r][c], temp);
			}
		}
	}
	
	public Set<BoardCell> getAdjList(BoardCell cell) {
//		if (adjMtx.size() == 0) {
//			calcAdjacencies();
//		}
		return adjMtx.get(cell);
//		return new HashSet<BoardCell>();
	}
	
	/**
	 * clear the Sets targets and visited every time
	 * we need to calculate targets for a new startcell,
	 * then add startCell to visited and call the recursive
	 * function to get all possible targets for startCell 
	 * @param startCell
	 * @param pathLength
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		if (adjMtx.size() == 0) {
			calcAdjacencies();
		}
		targets.clear();
		visited.clear();
		visited.add(startCell);
		findTargets(startCell, pathLength);
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public BoardCell getCell(int row, int column) {
		return grid[row][column];
	}
	
	/**
	 * recursive function that find all possible targets
	 * from startCell with pathLength. Store them in Set targets
	 * @param startCell
	 * @param pathLength
	 */
	private void findTargets(BoardCell startCell, int pathLength) {
		for (BoardCell x : adjMtx.get(startCell)) {
			if (visited.contains(x)) {
				continue;
			}
			visited.add(x);
			if (pathLength == 1) {
				targets.add(x);
			} else {
				findTargets(x, pathLength - 1);
			}
			visited.remove(x);
		}
	}
}
