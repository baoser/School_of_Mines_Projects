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

public class gameActionTests {
private static Board testBoard;
	
	@BeforeClass
	public static void setUp() {
		testBoard = Board.getInstance();
		testBoard.setConfigFiles("ClueLayout.csv", "ClueRooms.txt");
		testBoard.initialize();
	}
	/**
	 * test if no room in list, choose a target randomly
	 * a location with 3 possible targets is being tested.
	 * run a loop a larget number of times and check if all
	 * possible targets are picked.
	 */
	@Test
	public void testRandomTarget() {
		ComputerPlayer comp = new ComputerPlayer("Comp1", Color.red, 20, 16);
		// pick a location and a path length
		testBoard.calcTargets(20, 16, 2);
		
		// boolean to track targets
		boolean loc_19_15 = false;
		boolean loc_18_16 = false;
		boolean loc_19_17 = false;
		
		//run the loop 100 times
		for (int i = 0; i < 100; i++) {
			BoardCell selected = comp.pickLocation(testBoard.getTargets());
			// if selected matches one of the targets, set to true
			if (selected == testBoard.getCellAt(19, 15))
				loc_19_15 = true;
			else if (selected == testBoard.getCellAt(18, 16))
				loc_18_16 = true;
			else if (selected == testBoard.getCellAt(19, 17))
				loc_19_17 = true;
			else
				fail("Invalid target selected");
		}
		// check if all targets have been chosen
		assertTrue(loc_19_15);
		assertTrue(loc_19_17);
		assertTrue(loc_18_16);
	}
	
	/**
	 * test if a doorway is in the list and the room is not
	 * previously visited, choose that door way as the target
	 */
	@Test
	public void testDoorwaySelection() {
		ComputerPlayer comp = new ComputerPlayer("Comp1", Color.red, 12, 19);
		testBoard.calcTargets(12, 19, 5);
		
		boolean doorway = false;
		
		BoardCell selected = comp.pickLocation(testBoard.getTargets());
		if (selected == testBoard.getCellAt(13, 18)) {
			doorway = true;
		}
		
		assertTrue(doorway);
		
	}
	
	/**
	 * test if a room was previously visited, the doorway will be randomly
	 * selecled among with other targets.
	 */
	@Test
	public void testRandomDoorway() {
		ComputerPlayer comp = new ComputerPlayer("Comp1", Color.red, 18, 6);
		testBoard.calcTargets(18, 6, 2);
		
		boolean loc_17_5 = false; // track doorway
		boolean loc_16_6 = false;
		boolean loc_17_7 = false;
		boolean loc_19_7 = false;
		boolean loc_20_6 = false;
		
		// run this loop 200 times to make sure all targets are selected
		for (int i = 0; i < 200; i++) {
			BoardCell selected = comp.pickLocation(testBoard.getTargets());
			// set to true if a target is selected
			if (selected == testBoard.getCellAt(17, 5))
				loc_17_5 = true;
			else if (selected == testBoard.getCellAt(16, 6))
				loc_16_6 = true;
			else if (selected == testBoard.getCellAt(17, 7))
				loc_17_7 = true;
			else if (selected == testBoard.getCellAt(19, 7))
				loc_19_7 = true;
			else if (selected == testBoard.getCellAt(20, 6))
				loc_20_6 = true;
			else
				fail("Invalid target selected");
		}
		// check if all targets have been chosen
		assertTrue(loc_17_5);
		assertTrue(loc_16_6);
		assertTrue(loc_17_7);
		assertTrue(loc_19_7);
		assertTrue(loc_20_6);
	}
	
	/**
	 * Test checking an accusation
	 * if all 3 cards match the solution, return true
	 * if one of the cards doesn't match, return false
	 */
	@Test
	public void testAccusation() {
		ComputerPlayer comp = new ComputerPlayer("Comp1", Color.red, 18, 6);
		//create a test solution and add cards to it as known values
		ArrayList<Card> testSolution = new ArrayList<Card>();
		Card card1 = new Card(CardType.PERSON, "Albus Dumbledore");
		Card card2 = new Card(CardType.WEAPON, "Muggle Weapon");
		Card card3 = new Card(CardType.ROOM, "Garden");
		testSolution.add(card1);
		testSolution.add(card2);
		testSolution.add(card3);
		// set board's solution to test solution
		testBoard.setSolution(testSolution);
		
		// create a test accusation and add the correct cards to it
		ArrayList<Card> testAccusation = new ArrayList<Card>();
		testAccusation.add(card1);
		testAccusation.add(card2);
		testAccusation.add(card3);
		// test for correct accusation
		assertTrue(testBoard.checkAccusation(testAccusation));
		
		//remove correct person card from test accusation and add a wrong person card to it 
		testAccusation.remove(card1);
		Card wrongPerson = new Card(CardType.PERSON, "Ron Weasley");
		testAccusation.add(wrongPerson);
		//test for incorrect result
		assertFalse(testBoard.checkAccusation(testAccusation));
		//remove wrong person and add the correct card back on
		testAccusation.remove(wrongPerson);
		testAccusation.add(card1);
		
		//remove correct weapon card from test accusation and add a wrong weapon card to it 
		testAccusation.remove(card2);
		Card wrongWeapon = new Card(CardType.WEAPON, "Sword");
		testAccusation.add(wrongWeapon);
		//test for incorrect result
		assertFalse(testBoard.checkAccusation(testAccusation));
		testAccusation.remove(wrongWeapon);
		testAccusation.add(card2);
		
		//remove correct room card from test accusation and add a wrong room card to it 
		testAccusation.remove(card3);
		Card wrongRoom = new Card(CardType.ROOM, "Kitchen");
		testAccusation.add(wrongRoom);
		//test for incorrect result
		assertFalse(testBoard.checkAccusation(testAccusation));
		
	}
	
	/**
	 * test create suggestion
	 * create a test deck with 6 cards, 3 person cards, and 3 weapon cards
	 * create a list with seen cards and give it a person card and a weapon card
	 * from test deck
	 * the other two person cards and weapon cards should be selected randomly
	 * room card should be picked based on player's current location
	 */
	@Test
	public void testCreateSuggestion() {
		ComputerPlayer comp = new ComputerPlayer("Comp1", Color.red, 3, 4);
		
		//test deck with 3 person cards and 3 weapon cards
		ArrayList<Card> testDeck = new ArrayList<Card>();
		Card card1 = new Card(CardType.PERSON, "Albus Dumbledore");
		Card card2 = new Card(CardType.PERSON, "Hermoine Granger");
		Card card3 = new Card(CardType.PERSON, "Lord Voldemort");
		Card card4 = new Card(CardType.WEAPON, "Knife");
		Card card5 = new Card(CardType.WEAPON, "Sword");
		Card card6 = new Card(CardType.WEAPON, "Wand");
		testDeck.add(card1);
		testDeck.add(card2);
		testDeck.add(card3);
		testDeck.add(card4);
		testDeck.add(card5);
		testDeck.add(card6);
		testBoard.setDeck(testDeck);
		
		//seenCards with 1 person and 1 weapon from testDeck
		comp.setSeenCards(card2);
		comp.setSeenCards(card5);
		//set player's hand
		comp.setHand("Harry Potter", "Troll", "Library");
		// track each cards that are supposed to be picked
		int check_card1 = 0;
		int check_card3 = 0;
		int check_card4 = 0;
		int check_card6 = 0;
		
		//suggest 150 times
		for (int i = 0; i < 150; ++i) {
			comp.createSuggestion();
			ArrayList<Card> testSuggestion = comp.getSuggestion();
			for (int j = 0; j < testSuggestion.size(); ++j) {
				// if the right card is picked increment its counter value
				if (testSuggestion.get(j) == card1) {
					check_card1++ ;
				}
				else if (testSuggestion.get(j) == card3) {
					check_card3++;
				}
				else if (testSuggestion.get(j) == card4) {
					check_card4++;
				}
				else if (testSuggestion.get(j) == card6) {
					check_card6++;
				}
				//if a room is in a suggestion list, make sure its the room that was set
				if (testSuggestion.get(j).getType() == CardType.ROOM){
					if (!testSuggestion.get(j).getCardName().equals("Kitchen"))
						fail (testSuggestion.get(j).getCardName() + " is not a valid suggestion");
				}
			}
		}
		
		//check if each card is picked over 10 times
		assertTrue(check_card1 > 10);
		assertTrue(check_card3 > 10);
		assertTrue(check_card4 > 10);
		assertTrue(check_card6 > 10);
		
	}
	
	/**
	 * test disproving a suggestion
	 * create a computer player and set its hand with known cards
	 * create a test suggestion
	 * test for matching or not matching results and if there are multiple
	 * matching cards, select one at random
	 * 
	 */
	@Test
	public void testDisproveSuggestion() {
		ComputerPlayer comp = new ComputerPlayer("Comp1", Color.red, 3, 4);
		//set hand for comp
		comp.setHand("Ron Weasley", "Knife", "Kitchen");
		
		//create a testSuggestion
		ArrayList<Card> testSuggestion = new ArrayList<Card>();
		Card card1 = new Card(CardType.PERSON, "Ron Weasley");
		Card card2 = new Card(CardType.WEAPON, "Wand");
		Card card3 = new Card(CardType.ROOM, "Garden");
		testSuggestion.add(card1);
		testSuggestion.add(card2);
		testSuggestion.add(card3);
		//test if the cards' name matches
		assertEquals(card1.getCardName(), comp.disproveSuggestion(testSuggestion).getCardName());
		
		//reset the hand with no matching card and test for null
		comp.setHand("Lord Voldemort", "Knife", "Kitchen");
		assertEquals(null, comp.disproveSuggestion(testSuggestion));
		
		//reset the hand to have multiple matching cards and test for random selection
		comp.setHand("Ron Weasley", "Wand", "Garden");
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		for (int i = 0; i < 100; i++) {
			if (comp.disproveSuggestion(testSuggestion).getCardName().equals(card1.getCardName()))
				count1++;
			else if (comp.disproveSuggestion(testSuggestion).getCardName().equals(card2.getCardName()))
				count2++;
			else if (comp.disproveSuggestion(testSuggestion).getCardName().equals(card3.getCardName()))
				count3++;
		}
		assertTrue(count1 > 2);
		assertTrue(count2 > 2);
		assertTrue(count3 > 2);	
	}
	
	/**
	 * test handle suggestion
	 * create a list of players: 1 human and 2 computers
	 * create a test suggestion
	 * set different players' hands according to each testing scenario
	 * 
	 */
	@Test
	public void testHandleSuggest() {
		// create a human player
		HumanPlayer human = new HumanPlayer("Harry Potter", Color.red, 0, 5);
		// create 2 computer players
		ComputerPlayer comp1 = new ComputerPlayer("Lord Voldemort", Color.green, 6, 0);
		ComputerPlayer comp2 = new ComputerPlayer("Ron Weasley", Color.yellow, 20, 6);
		
		// create a testSuggestion
		ArrayList<Card> testSuggestion = new ArrayList<Card>();
		Card card1 = new Card(CardType.PERSON, "Ron Weasley");
		Card card2 = new Card(CardType.WEAPON, "Wand");
		Card card3 = new Card(CardType.ROOM, "Garden");
		testSuggestion.add(card1);
		testSuggestion.add(card2);
		testSuggestion.add(card3);
		
		//set players' hands for first testing scenario
		human.setHand("Harry Potter", "Knife", "Theater");
		comp1.setHand("Albus Dumbledore", "Sword", "Kitchen");
		comp2.setHand("Severus Snape", "Muggle Weapon", "Family Room");
		
		//add players to the people list in testBoard
		ArrayList<Player> testPeople = new ArrayList<Player>();
		testPeople.add(human);
		testPeople.add(comp1);
		testPeople.add(comp2);
		testBoard.setPeople(testPeople);
		
		// test no players can disprove
		assertEquals(null, testBoard.handleSuggestion(testSuggestion, human));
		
		//reset player's hand and test accuser can disprove but should not
		human.setHand("Ron Weasley", "Knife", "Theater");
		assertEquals(null, testBoard.handleSuggestion(testSuggestion, human));
		//test only human can disprove and human is not accuser
		assertEquals("Ron Weasley", testBoard.handleSuggestion(testSuggestion, comp1).getCardName());
		
		//reset player's hand and test human can disprove but should not
		human.setHand("Harry Potter", "Knife", "Theater");
		assertEquals(null, testBoard.handleSuggestion(testSuggestion, human));
		
		//reset player's hand and test multiple players can disprove but only one should
		comp1.setHand("Ron Weasley", "Sword", "Kitchen");
		for (int i = 0; i < 50; ++i) {
			assertEquals("Ron Weasley", testBoard.handleSuggestion(testSuggestion, human).getCardName());
		}
		
		//reset player's hand and test human and another player can disprove but only one should
		human.setHand("Ron Weasley", "Knife", "Theater");
		comp1.setHand("Albus Dumbledore", "Wand", "Kitchen");
		assertEquals("Ron Weasley", testBoard.handleSuggestion(testSuggestion, comp2).getCardName());
	}
	
}
