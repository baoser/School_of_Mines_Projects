/**
 * @author Bao Nguyen
 * @author Cassandra VanDeventer
 * CSCI306-A
 * C14A-1 Clue Board III (Clue Pair)
 * 
 */
package tests;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {
	/** We make the Board static because we can load it one time and 
	* then do all the tests.
	*/ 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueRooms.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	/** Ensure that player does not move around within room
	* These cells are ORANGE and Purple on the planning spreadsheet
	*/
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		Set<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());
		testList = board.getAdjList(2, 1);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(20, 23);
		assertEquals(0, testList.size());
	}

	/** Ensure that the adjacency list from a doorway is only the
	* walkway. NOTE: This test could be merged with door 
	* direction test. 
	* These tests are bright blue on the planning spreadsheet
	*/ 
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(9, 6);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(9, 7)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(8, 16);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 15)));
		
	}
	
	/** Test adjacency at entrance to rooms
	 * These tests are PINK in planning spreadsheet
	 *
	 */
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		Set<BoardCell> testList = board.getAdjList(3, 5);
		assertTrue(testList.contains(board.getCellAt(2, 5)));
		assertTrue(testList.contains(board.getCellAt(3, 6)));
		assertTrue(testList.contains(board.getCellAt(4, 5)));
		assertTrue(testList.contains(board.getCellAt(3, 4)));
		assertEquals(4, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(5, 8);
		assertTrue(testList.contains(board.getCellAt(4, 8)));
		assertTrue(testList.contains(board.getCellAt(5, 9)));
		assertTrue(testList.contains(board.getCellAt(6, 8)));
		assertTrue(testList.contains(board.getCellAt(5, 7)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(3, 18);
		assertTrue(testList.contains(board.getCellAt(2, 18)));
		assertTrue(testList.contains(board.getCellAt(3, 19)));
		assertTrue(testList.contains(board.getCellAt(4, 18)));
		assertTrue(testList.contains(board.getCellAt(3, 17)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(5, 20);
		assertTrue(testList.contains(board.getCellAt(5, 21)));
		assertTrue(testList.contains(board.getCellAt(6, 20)));
		assertTrue(testList.contains(board.getCellAt(5, 19)));
		assertEquals(3, testList.size());
	}

	/** Test a variety of walkway scenarios
	* These tests are DARK GREEN on the planning spreadsheet
	*/
	@Test
	public void testAdjacencyWalkways()
	{
		// test for only adj walkways
		Set<BoardCell> testList = board.getAdjList(15, 16);
		assertTrue(testList.contains(board.getCellAt(14, 16)));
		assertTrue(testList.contains(board.getCellAt(15, 17)));
		assertTrue(testList.contains(board.getCellAt(16, 16)));
		assertTrue(testList.contains(board.getCellAt(15, 15)));
		assertEquals(4, testList.size());

		// test for top edge
		testList = board.getAdjList(0, 18);
		assertTrue(testList.contains(board.getCellAt(0, 17)));
		assertTrue(testList.contains(board.getCellAt(1, 18)));
		assertEquals(2, testList.size());
		
		//test for right edge
		testList = board.getAdjList(5, 23);
		assertTrue(testList.contains(board.getCellAt(5, 22)));
		assertEquals(1, testList.size());
		
	}
	
	
	/** Tests of just walkways, includes on edge of board
	* and beside room
	* Have already tested adjacency lists on all four edges, will
	* These are LIGHT GREEN on the planning spreadsheet
	*/
	@Test
	public void testTargetsSteps() {
		// Test target one step
		board.calcTargets(0, 12, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(1, 12)));	
		
		// Test target two steps
		board.calcTargets(20, 16, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 16)));
		assertTrue(targets.contains(board.getCellAt(19, 17)));	
		assertTrue(targets.contains(board.getCellAt(19, 15)));
		
		// Test target four steps
		board.calcTargets(18, 15, 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCellAt(20, 17)));
		assertTrue(targets.contains(board.getCellAt(20, 15)));	
		assertTrue(targets.contains(board.getCellAt(18, 17)));
		assertTrue(targets.contains(board.getCellAt(14, 15)));
		assertTrue(targets.contains(board.getCellAt(15, 16)));
		assertTrue(targets.contains(board.getCellAt(16, 17)));
		assertTrue(targets.contains(board.getCellAt(17, 16)));
		assertTrue(targets.contains(board.getCellAt(16, 15)));
		assertTrue(targets.contains(board.getCellAt(19, 16)));
		
		// Test target six step
		board.calcTargets(12, 0, 6);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 6)));
		assertTrue(targets.contains(board.getCellAt(13, 5)));
		
	}
	

	/** Test getting into a room
	 *These are BROWN on the planning spreadsheet
	*/
	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(12, 11, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 9)));
		assertTrue(targets.contains(board.getCellAt(13, 10)));
		assertTrue(targets.contains(board.getCellAt(14, 11)));
		assertTrue(targets.contains(board.getCellAt(13, 12)));
		assertTrue(targets.contains(board.getCellAt(12, 13)));
		
		// Door is 4 steps away, but can enter with 5 or 6
		// Testing for 5
		board.calcTargets(20, 6, 5);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 7)));
		assertTrue(targets.contains(board.getCellAt(17, 6)));
		assertTrue(targets.contains(board.getCellAt(19, 6)));
		assertTrue(targets.contains(board.getCellAt(18, 7)));
		assertTrue(targets.contains(board.getCellAt(17, 5)));
		assertTrue(targets.contains(board.getCellAt(15, 6)));
		assertTrue(targets.contains(board.getCellAt(20, 7)));
	}

	/** Test getting out of a room
	 * These are RED(excluding X) on the planning spreadsheet
	*/
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(17, 5, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(17, 6)));
		// Take two steps
		board.calcTargets(17, 5, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 6)));
		assertTrue(targets.contains(board.getCellAt(17, 7)));
		assertTrue(targets.contains(board.getCellAt(18, 6)));
	}

}
