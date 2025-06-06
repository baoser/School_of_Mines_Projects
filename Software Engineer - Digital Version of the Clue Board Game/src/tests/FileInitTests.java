/**
 * @Author Bao Nguyen
 * @author Cassandra Vandeventer
 * 
 * CSCI 306-A
 * C13A-2
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class FileInitTests {
	
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 21;
	public static final int NUM_COLUMNS = 24;
	
	private static Board board;
	
	/**
	 * set up file names and the board
	 */
	@BeforeClass
	public static void load() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueRooms.txt");
		board.initialize();
	}
	
	/**
	 * test if values match keys
	 */
	@Test
	public void testLegend() {
		assertEquals(board.getLegend().size(), LEGEND_SIZE);
		assertEquals(board.getLegend().get('T'), "Theater"); //first in file
		assertEquals(board.getLegend().get('G'), "Garden");
		assertEquals(board.getLegend().get('W'), "Walkway"); //last in file

	}
	
	/**
	 * test for correct number of rows and columns
	 */
	@Test
	public void testSize() {
		assertEquals(board.getNumRows(), NUM_ROWS);
		assertEquals(board.getNumColumns(), NUM_COLUMNS);
	}
	
	/**
	 * test for correct door directions and if the cell
	 * is a room piece or walkway piece
	 */
	@Test
	public void testDoorDirection() {
		BoardCell test = board.getCellAt(3, 4); 
		
		assertTrue(test.isDoorway());
		assertEquals(DoorDirection.RIGHT, test.getDoorDirection());
		test = board.getCellAt(4, 8);
		
		assertTrue(test.isDoorway());
		assertEquals(DoorDirection.DOWN, test.getDoorDirection());
		test = board.getCellAt(3, 19);
		
		assertTrue(test.isDoorway());
		assertEquals(DoorDirection.LEFT, test.getDoorDirection());
		test = board.getCellAt(14, 11);
		
		assertTrue(test.isDoorway());
		assertEquals(DoorDirection.UP, test.getDoorDirection());
		test = board.getCellAt(0, 0); // room piece
		assertFalse(test.isDoorway());
		test = board.getCellAt(0, 4); // walk way piece
		assertFalse(test.isDoorway());
	}
	
	/**
	 * test for correct number of doors
	 */
	@Test
	public void testNumberOfDoors() {
		int testDoor = 0;
		for (int rows = 0; rows < board.getNumRows(); rows++)
			for (int columns = 0; columns < board.getNumColumns(); columns++) {
				BoardCell door = board.getCellAt(rows, columns);
				if (door.isDoorway())
					testDoor++;
			}
		Assert.assertEquals(13, testDoor); //arbitrary
	}
	
	/**
	 * test for correct cell's initials
	 */
	@Test
	public void testInitials() {
		assertEquals('K', board.getCellAt(0, 0).getInitial()); //entered in arbitrary values here as well
		assertEquals('H', board.getCellAt(8, 18).getInitial());
		assertEquals('T', board.getCellAt(20, 3).getInitial());
		assertEquals('G', board.getCellAt(20, 20).getInitial());
		assertEquals('X', board.getCellAt(10, 13).getInitial());
		assertEquals('B', board.getCellAt(7, 3).getInitial());

	}

	
}
