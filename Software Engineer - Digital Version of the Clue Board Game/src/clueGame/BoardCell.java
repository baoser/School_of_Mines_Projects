/**
 * @Author Bao Nguyen
 * @author Cassandra Vandeventer
 * 
 * CSCI 306-A
 * C13A-2
 * 
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class BoardCell {
		
	private int column;
	private int row;
	private char initial;
	private DoorDirection direction;
	private boolean isATarget = false;
	
	public BoardCell(int r, int c) {
		super();
		row = r;
		column = c;
		direction = DoorDirection.NONE;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public boolean isWalkway() {
		if (initial == 'W')
			return true;
		return false;
	}
	
	public boolean isRoom() {
		if (initial != 'W' && initial != 'X')
			return true;
		return false;
	}
	
	public boolean isDoorway() {
		if (direction != DoorDirection.NONE)
			return true;
		return false;
	}
	
	public DoorDirection getDoorDirection() {
		return direction; 
	}
	
	public void setDirection(DoorDirection d) {
		direction = d;
	}
	
	public void setInitial(char c) {
		initial = c;
	}

	public char getInitial() {
		return initial;
	}
	
	public boolean isATarget() {
		return isATarget;
	}
	
	public void setATarget(boolean isATarget) {
		this.isATarget = isATarget;
	}
	
	/**
	 * Draw walkway and closet and room pieces
	 * 
	 */
	public void drawBoardCell(Graphics g) {
		switch (this.initial) {
		//walkway yellow
		case 'W':
			g.setColor(Color.YELLOW);
			g.drawRect(this.column * 25, this.row * 25, 25, 25);
			g.fillRect(this.column * 25, this.row * 25, 25, 25);
			g.setColor(Color.black);
			g.drawRect(this.column * 25, this.row * 25, 25, 25);
			break;
		//closet red	
		case 'X':
			g.setColor(Color.RED);
			g.drawRect(this.column * 25, this.row * 25, 25, 25);
			g.fillRect(this.column * 25, this.row * 25, 25, 25);
			g.setColor(Color.black);
			g.drawRect(this.column * 25, this.row * 25, 25, 25);
			break;
		//anything else is gray	
		default:
			g.setColor(Color.GRAY);
			g.drawRect(this.column * 25, this.row * 25, 25, 25);
			g.fillRect(this.column * 25, this.row * 25, 25, 25);
			g.drawRect(this.column * 25, this.row * 25, 25, 25);
			break;
		}
		//draw the door with correct direction
		if(this.direction != DoorDirection.NONE) {
			if(this.direction == DoorDirection.UP) {
				g.setColor(Color.blue);
				g.drawRect(this.column * 25, this.row * 25, 25, 3);
				g.fillRect(this.column * 25, this.row * 25, 25, 3);
			}
			if(this.direction == DoorDirection.LEFT) {
				g.setColor(Color.blue);
				g.drawRect(this.column * 25, this.row * 25, 3, 25);
				g.fillRect(this.column * 25, this.row * 25, 3, 25);
			}
			if(this.direction == DoorDirection.RIGHT) {
				g.setColor(Color.blue);
				g.drawRect(this.column * 25 + 22, this.row * 25, 3, 25);
				g.fillRect(this.column * 25 + 22, this.row * 25, 3, 25);
			}
			if(this.direction == DoorDirection.DOWN) {
				g.setColor(Color.blue);
				g.drawRect(this.column * 25, this.row * 25 + 22, 25, 3);
				g.fillRect(this.column * 25, this.row * 25 + 22, 25, 3);
			}
		}
		
		if (isATarget) {
			g.setColor(Color.CYAN);
			g.drawRect(column * 25, row * 25,  25,  25);
			g.fillRect(column * 25, row * 25,  25,  25);
			g.setColor(Color.black);
			g.drawRect(column * 25, row * 25,  25,  25);
		}
	}
}