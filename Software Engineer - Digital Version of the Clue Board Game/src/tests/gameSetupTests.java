/**
 * Bao Nguyen
 * Cassandra Vandeventer
 * CSCI306-A
 */
package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.Player;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;

public class gameSetupTests {
	private static Board testBoard;
	
	@BeforeClass
	public static void setUp() {
		testBoard = Board.getInstance();
		testBoard.setConfigFiles("ClueLayout.csv", "ClueRooms.txt");
		testBoard.initialize();
	}
	
	/**
	 * load people test:
	 * Number of Players
	 * Name
	 * Color
	 * Human/Computer
	 * Starting location
	 */
	@Test
	public void testLoadPeople() {
		boolean harry = false, snape = false;
		ArrayList<Player> test = testBoard.getPeople();
		// test for correct number of players
		assertEquals(6, test.size());
		for(Player p: test) {
			// tests for the right name
			if (p.getName().equals("Harry Potter")) {
				harry = true;
				// test for human player
				assertTrue(p instanceof HumanPlayer);
				// test for color
				assertEquals(p.getColor(), Color.red);
				// test of starting row
				assertEquals(p.getRow(), 0);
				// test for starting column
				assertEquals(p.getColumn(), 5);
			}		
			else if (p.getName().equals("Severus Snape")) {
				snape = true;
				// test for computer player
				assertTrue(p instanceof ComputerPlayer);
				assertEquals(p.getColor(), Color.green);
				assertEquals(p.getRow(), 5);
				assertEquals(p.getColumn(), 23);
			}
		}
		// test for existence of players
		assertTrue(harry);
		assertTrue(snape);
	}
	
	/**
	 * test loading deck of cards:
	 * deck contains the correct total number of cards
	 * deck contains correct number of each type of card
	 * deck contains the right cards (one for each type)
	 */
	@Test
	public void testDeckCards() {
		int numPlayers = 0, numWeapons = 0, numRooms = 0;
		boolean ron = false, knife = false, library = false;
		ArrayList<Card> testDeck = testBoard.getDeck();
		//test for the correct total number of cards in the deck
		assertEquals(21, testDeck.size());
		for (Card c: testDeck) {
			// increment each type of card and check if it's the right card
			if (c.getType() == CardType.PERSON) {
				++numPlayers;
				if (c.getCardName().equals("Ron Weasley") ) {
					ron = true;
				}
			}
			else if (c.getType() == CardType.WEAPON) {
				++numWeapons;
				if (c.getCardName().equals("Knife")) {
					knife = true;
				}
			}
			else if (c.getType() == CardType.ROOM) {
				++numRooms;
				if (c.getCardName().equals("Library")) {
					library = true;
				}
			}
			
		}
		// tests for correct number of cards of each type
		assertEquals (6, numPlayers);
		assertEquals (6, numWeapons);
		assertEquals (9, numRooms);
		// tests for correct cards
		assertTrue (ron);
		assertTrue (knife);
		assertTrue (library);
		
	}
	/**
	 * test dealing cards:
	 * All cards are deal
	 * Player's number of cards should not differ by greater than 1
	 * Player should not have the same card
	 */
	@Test
	public void testDeal() {
		int totalCards = 0;
		int handSize = -1;
		boolean person = false, weapon = false, room = false;
		HashSet<Card> testSet = new HashSet<Card>();
		ArrayList<Player> people = testBoard.getPeople();
		ArrayList<Card> testSolution = testBoard.getSolution();
		//test for the correct number of card in solution
		assertEquals(3, testSolution.size());
		//test for uniqueness in solution
		for (Card c: testSolution) {
			if (c.getType() == CardType.PERSON) {
				person = true;
			}
			else if (c.getType() == CardType.WEAPON) {
				weapon = true;
			}
			else if (c.getType() == CardType.ROOM) {
				room = true;
			}
		}
		assertTrue(person == weapon == room == true);
		for (Player p: people) {
			// if it's the first player's hand, assign the size
			if (handSize == -1) {
				handSize = p.getMyCards().size();
			}
			// compare current player's hand size the the previous player's
			else {
				if (handSize != p.getMyCards().size() + 1 && handSize != p.getMyCards().size() - 1 && handSize != p.getMyCards().size()) {
					fail();
				}
			}
			//add up cards from players' hands
			totalCards = totalCards + p.getMyCards().size();
			for (int i = 0; i < p.getMyCards().size(); ++i){
				testSet.add(p.getMyCards().get(i));
			}
			
		}
		// add solution cards to total cards
		totalCards += testBoard.getSolution().size();
		for (int i = 0; i < testBoard.getSolution().size(); ++i){
			testSet.add(testBoard.getSolution().get(i));
		}
		//test for all cards are dealt
		assertEquals(21, totalCards);
		//test for uniqueness in testSet, if there are 23 unique cards in the set
		assertEquals(21, testSet.size());
	}
}
