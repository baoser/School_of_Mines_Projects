/**
 * @author Cassandra VanDeventer
 * @author Bao Nguyen
 * CSCI 306 - A
 */
package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGame extends JFrame {
	Board board;
	private DetectiveNotes Detective;
	private static String startMessage;
	private static String title;
	
	private ClueGUI gui;
	private MyCardsGUI myCards;
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private ArrayList<Player> players;
	
	private boolean humanMustFinish = true;
	private boolean initialClick = true;
	private boolean submitDialog = true;
	private int roll;
	private Random rand;
	private int currentPlayer = 0;
	private String stringSuggestion;
	private String response;
	private String aPlayerWins;
	private String winTitle = "Winner!";
	
	private GuessDialog guessDialog;
	/**
	 * Constructor
	 */
	public ClueGame() {
		setTitle("Clue Game");
		setSize(825, 825);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//make a new board with all data
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueRooms.txt");
		board.initialize();
		players = board.getPeople();
		rand = new Random();
		gui = new ClueGUI();
		gui.nextPlayer.addActionListener(new NextPlayerClicked());
		gui.makeAccusation.addActionListener(new MakeAccusationClicked());
		myCards = new MyCardsGUI(board.getPeople().get(0));
		myCards.setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));
		//add board to the center of the frame
		add(board, BorderLayout.CENTER);
		//add control panel to south
		add(gui, BorderLayout.SOUTH);
		//add my cards to east
		add(myCards, BorderLayout.EAST);
		//create file menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(FileMenu());
		// message for prompt start splash screen
		title = "Welcome to Clue!";
		startMessage = "You are " + board.getPeople().get(0).getName() + ". Press Next Player to begin the game.";
		addMouseListener(new MoveHumanPlayer());
		for(Card c : board.getSolution()) {
			System.out.println(c.getCardName());
		}
	}
	
	/**
	 * create a file drop menu with detective notes option and exit program option
	 */
	private JMenu FileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(DetectiveOption());
		menu.add(ExitOption());
		return menu;
	}
	
	/**
	 * create detective notes item for a drop menu
	 * that will show detective notes dialog when clicked
	 * @return 
	 */
	
	private JMenuItem DetectiveOption() {
		JMenuItem option = new JMenuItem("Detective Notes");
		
		class MenuItemListener implements ActionListener {
			
			@Override
			public void actionPerformed(ActionEvent a) {
				Detective = new DetectiveNotes();
				Detective.setVisible(true);
			}
		}
		option.addActionListener(new MenuItemListener());
		return option;
	}
	
	/**
	 * exit program when clicked
	 * @return
	 */
	private JMenuItem ExitOption() {
		JMenuItem option = new JMenuItem("Exit");
		
		class MenuItemListener implements ActionListener {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		option.addActionListener(new MenuItemListener());
		return option;
	}
	
	/**
	 * this class handles an event when the Next Player button is clicked
	 * if the current player is human, it must finish the turn before
	 * moving on to the next player in the list or an error message will be
	 * displayed. A Computer player can also make a suggestion if it enters a room and an 
	 * accusation if their suggestion is not disproved in their previous turn
	 */
	class NextPlayerClicked implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// player must submit suggestion before ending their turn
			if (!submitDialog) {
				JOptionPane errorPane = new JOptionPane();
				errorPane.showMessageDialog(new JFrame(), "You must submit your suggestion", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			// if human hasn't finished their turn
			else if (humanMustFinish && !initialClick) {
				JOptionPane errorPane = new JOptionPane();
				errorPane.showMessageDialog(new JFrame(), "You must finish your turn", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				// reset guess text to empty
				stringSuggestion = "";
				response = "";
				gui.guess.setText(stringSuggestion);
				gui.guessResponse.setText(response);
				// random roll
				roll = rand.nextInt(6) + 1;
				// create a temp player and set it to the current player
				Player tempP = players.get(currentPlayer);
				// calculate targets for the current player
				board.calcTargets(tempP.getRow(), tempP.getColumn(), roll);
				// clear targets list
				targets.clear();
				// add targets from the board to this target list
				for (BoardCell c : board.getTargets()) {
					targets.add(c);
				}
				// if it's human player's turn, display targets in a different color on the board
				if (players.get(currentPlayer) instanceof HumanPlayer) {
					for (BoardCell c : targets) {
						c.setATarget(true);
					}
					humanMustFinish = true;
				}
				else {
					// create a temporary computer player and set it to current player
					ComputerPlayer tempC = (ComputerPlayer) players.get(currentPlayer);
					// if make accusation flag is true then call make accusation method from ComputerPlayer
					if (tempC.correctGuess == true) {
						// check with solution
						ArrayList<Card> accusation = tempC.makeAccusation();
						boolean computerWins = board.checkAccusation(accusation);
						if (computerWins) {
							// computer wins, displays answer
							String answer = board.getSolution().get(0).getCardName() + ", " +  board.getSolution().get(1).getCardName() + ", " +  board.getSolution().get(2).getCardName();
							aPlayerWins = tempC.getName() + " wins!" + " Answer: " + answer;
							JOptionPane winnerPane = new JOptionPane();
							winnerPane.showMessageDialog(new JFrame(), aPlayerWins, winTitle, JOptionPane.INFORMATION_MESSAGE);		
							System.exit(0);
						}
						else {	
							// displays computer incorrect accusation
							String incorrectAccusation = accusation.get(0).getCardName() + ", " + accusation.get(1).getCardName() + ", " + accusation.get(2).getCardName() + ".";
							JOptionPane pane = new JOptionPane();
							pane.showMessageDialog(new JFrame(), incorrectAccusation, "Incorrect Accustaion", JOptionPane.INFORMATION_MESSAGE);
							return;
						}	
					}
					
					// call pickLocation method for computer player passing in the targets list
					BoardCell loc = tempC.pickLocation(targets);
					// reset the current player's location 
					players.get(currentPlayer).setColumn(loc.getColumn());
					players.get(currentPlayer).setRow(loc.getRow());
					// turn off the display for targets
					for (BoardCell c : targets) {
						c.setATarget(false);
					}
					// create a temporary board cell and give it the current player's location
					BoardCell tempCell = board.getCellAt(players.get(currentPlayer).getRow(), players.get(currentPlayer).getColumn());
					// check if it is a room
					if (tempCell.getInitial() != 'W') {
						// computer player create suggestion
						tempC.createSuggestion();
						stringSuggestion = tempC.toStringSuggestion();
						// if none can disprove 
						if (board.handleSuggestion(tempC.suggestion, players.get(currentPlayer)) == null) {
							// set flag to make accusation for next turn
							tempC.correctGuess = true;
							// displays no new clue message
							gui.guessResponse.setText("No new clue");
						}
						else {
							// display response
							response = board.handleSuggestion(tempC.suggestion, players.get(currentPlayer)).getCardName();
							gui.guessResponse.setText(response);
						}
						// set computer suggestion as text
						gui.guess.setText(stringSuggestion);
						// move suggested player into room
						for (Player p : players) {
							for (Card c : tempC.suggestion) {
								if (p.getName().equals(c.getCardName())) {
										p.setRow(tempC.getRow());
										p.setColumn(tempC.getColumn());
								}
							}
						}
					}
				}
				// display die roll and current player's name
				String s = Integer.toString(roll);
				gui.dieRoll.setText(s);
				gui.turn.setText(players.get(currentPlayer).getName());
				// reset currentPlayer counter
				currentPlayer++;
				if(currentPlayer == 6) {	
					currentPlayer = 0;
				}
			}
			//reset initialClick and repaint
			initialClick = false;
			board.repaint();
		}
	}
	
	/**
	 * this class will move the human player according to the
	 * player's mouse click. if human player enters a room, a dialog
	 * will pop up for the player to make a suggestion.
	 */
	class MoveHumanPlayer implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// if not human player, return
			if (currentPlayer != 1)
				return;
			// booleans to keep track of unfinished turn and wrong location
			boolean notFinished = true;
			boolean wrongLocation = true;
			// loop through the target list, if the click is within a valid cell then change
			// player's location
			for (BoardCell c : targets) {
				if (e.getX() >= c.getColumn() * 25 + 8 && e.getX() <= (c.getColumn() * 25) + 32) {
					if (e.getY() >= c.getRow() * 25 + 53 && e.getY() <= (c.getRow() * 25) + 80) {
						players.get(0).setColumn(c.getColumn());
						players.get(0).setRow(c.getRow());
						for (BoardCell cell : targets) {
							cell.setATarget(false);
						}
						wrongLocation = false;
						notFinished = false;
						board.repaint();
						// if enter a room, display a dialog for making a suggestion
						if (board.getCellAt(c.getRow(), c.getColumn()).getInitial() != 'W') {
							submitDialog = false;
							BoardCell temp = board.getCellAt(players.get(0).getRow(), players.get(0).getColumn());
							String roomName = board.getLegend().get(temp.getInitial());
							guessDialog = new GuessDialog(roomName);
							// ADD ACTIONLISTENER TO THE SUBMIT BUTTONS
							guessDialog.submit.addActionListener(new SubmitSuggestionClicked());
							guessDialog.setVisible(true);
						}
					}
				}
			}
			humanMustFinish = notFinished;
			// display an error message if an invalid location is clicked
			if (wrongLocation) {
				JOptionPane errorPane = new JOptionPane();
				errorPane.showMessageDialog(new JFrame(), "Invalid Location", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * ActionListener for submit button in suggestion dialog.
	 *
	 */
	class SubmitSuggestionClicked implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			submitDialog = true;
			// set flag to enable the next player button
			humanMustFinish = false;
			// create a copy of the human player
			HumanPlayer player = (HumanPlayer) players.get(0);
			// human player create suggestion
			guessDialog.personGuess = (String) guessDialog.playersDropdown.getSelectedItem();
			guessDialog.weaponGuess = (String) guessDialog.weaponsDropdown.getSelectedItem();
			BoardCell temp = board.getCellAt(player.getRow(), player.getColumn());
			guessDialog.roomGuess = board.getLegend().get(temp.getInitial());
			player.createSuggestion(guessDialog.personGuess, guessDialog.weaponGuess, guessDialog.roomGuess);
			stringSuggestion = player.toStringSuggestion();
			// close the dialog
			guessDialog.setVisible(false);
			// if none can disprove 
			if (board.handleSuggestion(player.suggestion, player) == null) {
				// displays no new clue message
				gui.guessResponse.setText("No new clue");
			}
			else {
				// display response
				response = board.handleSuggestion(player.suggestion, player).getCardName();
				gui.guessResponse.setText(response);
			}
			// set suggestion as text
			gui.guess.setText(stringSuggestion);
			// move suggested player into room
			for (Player p : players) {
				for (Card c : player.suggestion) {
					if (p.getName().equals(c.getCardName())) {
						p.setRow(player.getRow());
						p.setColumn(player.getColumn());
					}
				}
			}
			board.repaint();
		}
	}
	
	/**
	 * ActionListener for submit button in accusation dialog
	 */
	class SubmitAccusationClicked implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			// create a copy of the human player
			HumanPlayer player = (HumanPlayer) players.get(0);
			// human player create suggestion
			guessDialog.personGuess = (String) guessDialog.playersDropdown.getSelectedItem();
			guessDialog.weaponGuess = (String) guessDialog.weaponsDropdown.getSelectedItem();
			guessDialog.roomGuess = (String) guessDialog.roomsDropdown.getSelectedItem();
			player.createAccusation(guessDialog.personGuess, guessDialog.weaponGuess, guessDialog.roomGuess);
			// close the dialog
			guessDialog.setVisible(false);
			boolean win = board.checkAccusation(player.getAccusation());
			
			// player wins
			if (win) {
				aPlayerWins = "You Win!";
				JOptionPane winnerPane = new JOptionPane();
				winnerPane.showMessageDialog(new JFrame(), aPlayerWins, winTitle, JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
			else {
				JOptionPane wrongAccusationPane = new JOptionPane();
				wrongAccusationPane.showMessageDialog(new JFrame(), "Your accusation is incorrect.", "Incorrect Accusation Made", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	}
	
	/**
	 * action for make accusation button
	 */
	class MakeAccusationClicked implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// accusation can only be made at the beginning of the turn
			if (humanMustFinish && !initialClick) {
				// new dialog pops up
				guessDialog = new GuessDialog();
				// actions for submit and cancel buttons
				guessDialog.submit.addActionListener(new SubmitAccusationClicked());
				guessDialog.cancel.addActionListener(new CancelAccusationClicked());
				guessDialog.setVisible(true);
			}
			else {
				JOptionPane cannotAccuse = new JOptionPane();
				String errorMessage = "You can only make an accusation at the beggining of your turn.";
				String errorTitle = "Cannot Make Accusation";
				cannotAccuse.showMessageDialog(new JFrame(), errorMessage, errorTitle, JOptionPane.INFORMATION_MESSAGE);
			}
			initialClick = false;		
		}	
	}
	
	/**
	 * Action for cancel button in accusation dialog
	 */
	class CancelAccusationClicked implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			guessDialog.setVisible(false);
		}
	}
	
	/**
	 * MAIN
	 * @param args
	 */
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
		JOptionPane pane = new JOptionPane();
		pane.showMessageDialog(game, startMessage, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
}
