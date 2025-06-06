package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotes extends JDialog {
	/**
	 * Constructor
	 */
	public DetectiveNotes() {
		setSize(600, 600);
		Board board = Board.getInstance();
		//3 rows 2 columns layout
		setLayout(new GridLayout(3, 2));
		
		//create a new panel for people check boxes
		JPanel people = new JPanel();
		people = peoplePanel(board.getPeople());
		
		//panel for drop menu
		JPanel peopleGuess = new JPanel();
		peopleGuess = peopleDropDown(board.getPeople());
		
		//panel for weapons check boxes
		JPanel weapons = new JPanel();
		weapons = weaponsPanel(board.getWeaponDeck());
		
		//panel for drop menu
		JPanel weaponsGuess = new JPanel();
		weaponsGuess = weaponDropDown(board.getWeaponDeck());
		
		//panel for rooms check boxes
		JPanel room = new JPanel();
		room = roomPanel(board.getRoomDeck());
		
		//panel for drop menu
		JPanel roomGuess = new JPanel();
		roomGuess = roomDropDown(board.getRoomDeck());
		
		//add to the main dialog
		add(people);
		add(peopleGuess);
		add(weapons);
		add(weaponsGuess);
		add(room);
		add(roomGuess);
	}
	
	/**
	 * get the players list from board and create
	 * check boxes for each player
	 * @param players
	 * @return
	 */
	public JPanel peoplePanel (ArrayList<Player> players) {
		//create a local panel
		JPanel people = new JPanel();
		//set grid layout
		people.setLayout(new GridLayout(3, 2));
		//create a box for each player
		ArrayList<JCheckBoxMenuItem> peopleList = new ArrayList<JCheckBoxMenuItem>();
		for(Player p : players) {
			peopleList.add(new JCheckBoxMenuItem(p.getName()));
		}
		//add each boxes to the local panel
		for(JCheckBoxMenuItem w : peopleList) {
			people.add(w);
		}
		//set a border
		people.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		//return the local panel
		return people;
	}
	
	/**
	 * get the weapon list from board and create
	 * check boxes for each weapon
	 */
	public JPanel weaponsPanel(ArrayList<Card> weapons) {
		//create a local panel
		JPanel weapon = new JPanel();
		//set grid layout
		weapon.setLayout(new GridLayout(3, 2));
		//create a box for each weapon
		ArrayList<JCheckBoxMenuItem> weaponList = new ArrayList<JCheckBoxMenuItem>();
		//add each boxes to the local panel
		for(Card w : weapons) {
			weaponList.add(new JCheckBoxMenuItem(w.getCardName()));
		}
		//add each boxes to the local panel
		for(JCheckBoxMenuItem w : weaponList) {
			weapon.add(w);
		}
		weapon.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		return weapon;
	}
	
	/**
	 * get the room list from board and create
	 * check boxes for each room
	 */
	public JPanel roomPanel(ArrayList<Card> rooms) {
		//create local panel
		JPanel room = new JPanel();
		//set grid layout
		room.setLayout(new GridLayout(0, 2));
		//add check boxes to local panel
		for(Card r : rooms) {
			room.add(new JCheckBoxMenuItem(r.getCardName()));
		}
		//set border
		room.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		//return local panel
		return room;
	}
	
	/**
	 * create a panel for a drop down menu(combo boxes) for players
	 * @param players
	 * @return
	 */
	public JPanel peopleDropDown(ArrayList<Player> players) {
		//create a local panel
		JPanel peopleDropDown = new JPanel();
		//add each player as an option for drop down menu
		JComboBox<String> people = new JComboBox<String>();
		for(Player p : players) {
			people.addItem(p.getName());
		}
		//add the combo boxes to the panel
		peopleDropDown.add(people);
		//set border
		peopleDropDown.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
		//return local panel
		return peopleDropDown;
	}
	
	/**
	 * create a panel for a drop down menu(combo boxes) for weapons
	 */
	public JPanel weaponDropDown(ArrayList<Card> weapons) {
		//local panel
		JPanel weaponDropDown = new JPanel();
		//combo boxes for each weapon
		JComboBox<String> weaponsBoxes = new JComboBox<String>();
		for(Card w : weapons) {
			weaponsBoxes.addItem(w.getCardName());
		}
		//add the combo boxes to the panel
		weaponDropDown.add(weaponsBoxes);
		//set border
		weaponDropDown.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
		//return local panel
		return weaponDropDown;
	}
	
	/**
	 * create a panel for a drop down menu(combo boxes) for rooms
	 */
	public JPanel roomDropDown(ArrayList<Card> rooms) {
		//local panel
		JPanel roomDropDown = new JPanel();
		//combo boxes for each room
		JComboBox<String> roomsBoxes = new JComboBox<String>();
		for(Card r : rooms) {
			roomsBoxes.addItem(r.getCardName());
		}
		//add the combo boxes to the panel
		roomDropDown.add(roomsBoxes);
		roomDropDown.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess" ));
		//return local panel
		return roomDropDown;
	}
	
	public static void main(String[] args) {
		DetectiveNotes menu = new DetectiveNotes();
		menu.setVisible(true);
	}

}
