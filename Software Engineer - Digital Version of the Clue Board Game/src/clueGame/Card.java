/**
 * Bao Nguyen
 * Cassandra Vandeventer
 * CSCI306-A
 */
package clueGame;

public class Card {
	private String cardName;
	private CardType type;
	
	public Card(CardType type, String cardName) {
		super();
		this.type = type;
		this.cardName = cardName;
	}
	
	public CardType getType() {
		return type;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public boolean equals() {
		return false;
	}
}
