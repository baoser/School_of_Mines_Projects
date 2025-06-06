/**
 * Bao Nguyen
 * Cassandra Vandeventer
 * CSCI306-A
 */
package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public class HumanPlayer extends Player{

	public HumanPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * create a suggestion
	 * @param personCardName
	 * @param weaponCardName
	 * @param roomCardName
	 */
	public void createSuggestion(String personCardName, String weaponCardName, String roomCardName) {
		// clear suggestion
		suggestion.clear();
		// create new cards
		Card personCard = new Card(CardType.PERSON, personCardName);
		Card weaponCard = new Card(CardType.WEAPON, weaponCardName);
		Card roomCard = new Card(CardType.ROOM, roomCardName);
		// add to suggestion
		suggestion.add(personCard);
		suggestion.add(weaponCard);
		suggestion.add(roomCard);	
	}
	
	/**
	 * create accusation
	 * @param personCardName
	 * @param weaponCardName
	 * @param roomCardName
	 */
	public void createAccusation(String personCardName, String weaponCardName, String roomCardName) {
		// clear accusation
		accusation.clear();
		// create new cards
		Card personCard = new Card(CardType.PERSON, personCardName);
		Card weaponCard = new Card(CardType.WEAPON, weaponCardName);
		Card roomCard = new Card(CardType.ROOM, roomCardName);
		// add to accusation
		accusation.add(personCard);
		accusation.add(weaponCard);
		accusation.add(roomCard);
	}
	
	public ArrayList<Card> getAccusation() {
		return accusation;
	}
}
