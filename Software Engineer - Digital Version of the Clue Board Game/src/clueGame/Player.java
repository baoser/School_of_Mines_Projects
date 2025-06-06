/**
 * Bao Nguyen
 * Cassandra Vandeventer
 * CSCI306-A
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public abstract class Player {
	private String playerName;
	protected int row;
	protected int column;
	private Color color;
	protected ArrayList<Card> myCards;
	protected ArrayList<Card> suggestion;
	protected ArrayList<Card> accusation;
	
	
	public Player(String name, Color color, int row, int column) {
		playerName = name;
		this.color = color;
		this.row = row;
		this.column = column;
		myCards = new ArrayList<Card>();
		suggestion = new ArrayList<Card>();
		accusation = new ArrayList<Card>();
	}
	
	/**
	 * disprove a suggestion
	 * if a card from player's hand matching a card from suggestion
	 * add it to the matchingCards list
	 * if there are 1 or more matching cards, shuffle the list and pick the first one
	 * if there are no matching cards, return null
	 * @param suggestion
	 * @return
	 */
	public Card disproveSuggestion(ArrayList<Card> suggestion) {
		//make a list for matching cards
		ArrayList<Card> matchingCards = new ArrayList<Card>();
		//nested for loop to check for matching cards from two lists
		for (Card s: suggestion) {
			for (Card p: myCards) {
				//if there is a match, add the card to matching cards list
				if (s.getCardName().equals(p.getCardName())) {
					matchingCards.add(p);
				}
			}
		}
		// if there is at least 1 matching card, shuffle and return the first one
		if (matchingCards.size() >= 1) {
			Collections.shuffle(matchingCards);
			return matchingCards.get(0);
		}
		// no matching card
		else{
			return null;
		}
	}
	
	/**
	 * convert card names in suggestion to strings
	 * @return
	 */
	public String toStringSuggestion() {
		return suggestion.get(0).getCardName() + ", " + suggestion.get(1).getCardName() + ", " + suggestion.get(2).getCardName();
	}
	
	/**
	 * make accusation method. Add cards from suggestion to accusation
	 * most likely only used by computers
	 * @return
	 */
	public ArrayList<Card> makeAccusation() {
		accusation.clear();
		for (Card c : suggestion) {
			accusation.add(c);
		}
		return accusation;
	}
	
	public String getName() {
		return playerName;
	}
	public int getRow() {
		// TODO Auto-generated method stub
		return row;
	}
	public int getColumn() {
		// TODO Auto-generated method stub
		return column;
	}
	public Color getColor() {
		// TODO Auto-generated method stub
		return color;
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ArrayList<Card> getMyCards() {
		return myCards;
	}
	
	// testing purposes
	public void setHand(String person, String weapon, String room) {
		myCards.clear();
		Card card1 = new Card(CardType.PERSON, person);
		Card card2 = new Card(CardType.WEAPON, weapon);
		Card card3 = new Card(CardType.ROOM, room);
		
		myCards.add(card1);
		myCards.add(card2);
		myCards.add(card3);
	}
	
	public void drawPlayer(Graphics g) {
		g.setColor(this.color);
		g.drawOval(column * 25, row * 25, 25, 25);
		g.fillOval(column * 25, row * 25, 25, 25);
		g.setColor(Color.black);
		g.drawOval(column * 25, row * 25, 25, 25);
	}
}
