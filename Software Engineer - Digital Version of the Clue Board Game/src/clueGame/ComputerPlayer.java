/**
 * Bao Nguyen
 * Cassandra Vandeventer
 * CSCI306-A
 */
package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{
	private char previousRoom = ' ';
	private ArrayList<Card> deck;
	private ArrayList<Card> seenCards;
	boolean correctGuess = false;
	
	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
		seenCards = new ArrayList<Card>();
	}
	
	/**
	 * pick a location for computer
	 * @param targets
	 * @return
	 */
	public BoardCell pickLocation(Set<BoardCell> targets) {
		
		// pick a random number from 0 to the size of the targets list
		int size = targets.size();
		int item = new Random().nextInt(size);
		int i = 0;
		
		// loop through all elements in targets list
		for (BoardCell cell: targets) {
			// if it's a doorway and its initial is not the previous room initial
			// update previousRoom initial and return the cell
			if (cell.isDoorway() && previousRoom != cell.getInitial()) {
				previousRoom = cell.getInitial();
				return cell;
			}
		}
		
		// if no doorway is in the target list, return a random cell from the list
		for (BoardCell cell: targets) {
			if (i == item) {
				return cell;
			}
			i++;
		}
		return null;
	}
	
	/**
	 * create a suggestion for computer
	 */
	public void createSuggestion() {
		//clear suggestion every time is called
		suggestion.clear();
		//access to the board's deck
		Board board = Board.getInstance();
		Collections.shuffle(board.getDeck());
		for (Card c: board.getDeck()) {
			// if player's hand contains a card from the deck, go to next iteration
			if (myCards.contains(c)){
				continue;
			}
			// if it's a person card and suggestion is empty, add it to suggestion
			else if (c.getType() == CardType.PERSON && suggestion.isEmpty() && !seenCards.contains(c)) {
				suggestion.add(c);
			}
			// if it's a weapon card and suggestion has one card, add it to suggestion
			else if (c.getType() == CardType.WEAPON && suggestion.size() == 1 && !seenCards.contains(c)) {
				suggestion.add(c);
			}
		}
		
		//add current room to suggestion
		char initial = board.getCellAt(row, column).getInitial();
		String currentRoomname = board.getLegend().get(initial);
		Card roomCard = new Card(CardType.ROOM, currentRoomname);
		suggestion.add(roomCard);
		
	}
	
	public ArrayList<Card> getSuggestion() {
		return suggestion;
	}
	
	public void setSeenCards(Card seenCard) {
		seenCards.add(seenCard);
	}
	
}
