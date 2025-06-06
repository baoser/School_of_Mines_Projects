/**
 * @author Cassandra VanDeventer
 * @author Bao Nguyen
 * CSCI 306 - A
 */
package clueGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MyCardsGUI extends JPanel{
	private String personCard, weaponCard, roomCard;
	JTextArea peopleText, weaponsText, roomsText;
	
	/**
	 * constructor takes in human player and access its cards
	 * @param p
	 */
	public MyCardsGUI(Player p) {
		// store player's card in a local ArrayList
		ArrayList<Card> temp = new ArrayList<Card>();
		temp = p.getMyCards();
		// get the cards' names
		for (Card c : temp) {
			if (c.getType() == CardType.PERSON) {
				personCard = c.getCardName();
			}
			else if (c.getType() == CardType.WEAPON) {
				weaponCard = c.getCardName();
			}
			else if (c.getType() == CardType.ROOM) {
				roomCard = c.getCardName();
			}
		}
		
		//create a panel and add 3 sub panels to it: person, weapon, and room
		setLayout(new GridLayout(3, 1));
		setSize(300, 300);
		
		JPanel panel = PeoplePanel();
		add(panel);
		panel = weaponsPanel();
		add(panel);
		panel = roomsPanel();
		add(panel);
	}
	
	/**
	 * create a panel for person card
	 * @return
	 */
	private JPanel PeoplePanel() {
		//create a local panel
		JPanel people = new JPanel();
		// create a new text field and add person card
		peopleText = new JTextArea(10, 10);
		peopleText.setText(personCard);
		peopleText.setEditable(true);
		peopleText.setBorder(new TitledBorder (new EtchedBorder(), "Person"));
		people.add(peopleText);
		
		//return local panel
		return people;
	}
	
	/**
	 * create a panel for person card
	 * @return
	 */
	private JPanel weaponsPanel() {
		//create a local panel
		JPanel weapon = new JPanel();
		// create a new text field and add weapon card
		weaponsText = new JTextArea(10, 10);
		weaponsText.setText(weaponCard);
		weaponsText.setEditable(true);
		weaponsText.setBorder(new TitledBorder (new EtchedBorder(), "Weapon"));
		weapon.add(weaponsText);
		
		//return local panel
		return weapon;
	}
	
	/**
	 * create a panel for person card
	 * @return
	 */
	private JPanel roomsPanel() {
		//create a local panel
		JPanel room = new JPanel();
		// create a new text field and add room card
		roomsText = new JTextArea(10, 10);
		roomsText.setText(roomCard);
		roomsText.setEditable(true);
		roomsText.setBorder(new TitledBorder (new EtchedBorder(), "Room"));
		room.add(roomsText);
		//return local panel
		return room;
	}
}
