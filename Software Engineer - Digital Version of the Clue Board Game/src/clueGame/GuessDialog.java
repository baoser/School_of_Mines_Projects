package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GuessDialog extends JDialog implements ActionListener{
	
	private JTextField currentRoom;
	private JTextField person;
	private JTextField weapon;
	String roomGuess;
	String personGuess;
	String weaponGuess;
	JComboBox<String> playersDropdown;
	JComboBox<String> weaponsDropdown;
	JComboBox<String> roomsDropdown;
	JButton submit;
	JButton cancel;
	
	Board board;
	
	/**
	 * Constructor with one parameter that is a string for
	 * the name of the room. Called when making a suggestion
	 * @param roomEntered
	 */
	public GuessDialog(String roomEntered) {
		setSize(300, 300);
		board = Board.getInstance();
		setLayout(new GridLayout(4,2 ));
		
		JLabel roomLabel = new JLabel("Your room");
		currentRoom = new JTextField(20);
		currentRoom.setText(roomEntered);
		currentRoom.setEditable(false);
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
		
		playersDropdown = new JComboBox<String>();
		playersDropdown = createPlayersDropdown(board.getPeople());
		
		weaponsDropdown = new JComboBox<String>();
		weaponsDropdown = createWeaponsDropdown(board.getWeaponDeck());
		
		submit = new JButton();
		submit = submitPanel();
		
		cancel = new JButton();
		cancel = cancelPanel();

		add(roomLabel);
		add(currentRoom);
		add(personLabel);
		add(playersDropdown);
		add(weaponLabel);
		add(weaponsDropdown);
		add(submit);
		add(cancel);	
	}
	
	/**
	 * Constructor with no parameters. Called when making an accusation
	 */
	public GuessDialog() {
		setSize(300, 300);
		board = Board.getInstance();
		setLayout(new GridLayout(4,2 ));
		
		JLabel roomLabel = new JLabel("Your room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
		
		playersDropdown = new JComboBox<String>();
		playersDropdown = createPlayersDropdown(board.getPeople());
		
		weaponsDropdown = new JComboBox<String>();
		weaponsDropdown = createWeaponsDropdown(board.getWeaponDeck());
		
		roomsDropdown = new JComboBox<String>();
		roomsDropdown = createRoomsDropdown(board.getRoomDeck());
		
		submit = new JButton();
		submit = submitPanel();
		
		JButton cancel = new JButton();
		cancel = cancelPanel();
		
		add(roomLabel);
		add(roomsDropdown);
		add(personLabel);
		add(playersDropdown);
		add(weaponLabel);
		add(weaponsDropdown);
		add(submit);
		add(cancel);	
	}
	
	/**
	 * Create a combo box for the players's names
	 * @param players
	 * @return
	 */
	public JComboBox<String> createPlayersDropdown(ArrayList<Player> players) {
		// create a local combo box
		JComboBox<String> people = new JComboBox<String>();
		// add players' names to the box
		for (Player p : players) {
			people.addItem(p.getName());
		}
		// return local combo box
		return people;
	}
	
	/**
	 * Create a combo box for weapons
	 * @param weapons
	 * @return
	 */
	public JComboBox<String> createWeaponsDropdown(ArrayList<Card> weapons) {
		// create a local combo box
		JComboBox<String> weaponBox = new JComboBox<String>();
		// add weapons to the box
		for (Card c : weapons) {
			weaponBox.addItem(c.getCardName());
		}
		return weaponBox;
	}
	
	/**
	 * Create a combo box for rooms
	 * @param rooms
	 * @return
	 */
	public JComboBox<String> createRoomsDropdown(ArrayList<Card> rooms) {
		// create a local combo box
		JComboBox<String> roomsBox = new JComboBox<String>();
		// add weapons to the box
		for (Card c : rooms) {
			roomsBox.addItem(c.getCardName());
		}
		return roomsBox;
	}
	
	private JButton submitPanel() {
		submit = new JButton("Submit");
		return submit;
	}
	
	private JButton cancelPanel() {
		cancel = new JButton("Cancel");
		return cancel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
