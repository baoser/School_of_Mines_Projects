/**
 * @author Cassandra VanDeventer
 * @author Bao Nguyen
 * CSCI 306 - A
 * C20A
 */
package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ClueGUI extends JPanel {
	JTextField turn;
	JTextField dieRoll;
	JTextField guess;
	JTextField guessResponse;
	JButton nextPlayer;
	JButton makeAccusation;

	/**
	 * Our main constructor.
	 */
	
	public ClueGUI() {
		// overall size of the JFrame
		setSize(new Dimension(700, 200));
		
		// top panel with buttons is (2, 1) GridLayout from description
		setLayout(new GridLayout(2, 1));
				
		// incorporates the functions below
		JPanel buttons = createButtons();
		add(buttons);
		JPanel bottom = guessInfo();
		add(bottom);
		
	}
		
	/**
	 * This method is to create the buttons on the panel and give them names
	 * within themselves.
	 * 
	 */
	private JPanel createButtons() {
		// titles the "Whose turn?" section of the panel
		TitledBorder title;
		title = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Whose turn?");
		title.setTitleJustification(TitledBorder.CENTER);
		
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		
		//no layout specified, so this is flow
		nextPlayer = new JButton("Next player");
		makeAccusation = new JButton("Make an accusation");

		// this is a text field that we don't want to be able to edit
		turn = new JTextField(15);
		turn.setEditable(false);
		
		panel2.add(turn, BorderLayout.CENTER);
		panel2.setBorder(title);
		
		
		// bottom half of the panel is in a (1, 3) GridLayout
		panel.setLayout(new GridLayout(1, 3));
		
		panel.add(panel2);
		panel.add(nextPlayer);
		panel.add(makeAccusation);
		
		return panel;
	}
	
	/**
	 * create the second row of the panel for the die, guess, and guess result
	 */
	
	private JPanel guessInfo() {
		// panel for die/roll
		JPanel panel = new JPanel();
		JLabel dieRollLabel = new JLabel("Roll");
		dieRoll = new JTextField(5);
		dieRoll.setEditable(false);
		
		// panel for guess/guess
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 1));
		JLabel guessLabel = new JLabel("Guess");
		guess = new JTextField(30);
		guess.setEditable(false);
		
		// panel for guess result/response
		JPanel panel3 = new JPanel();
		JLabel guessResultLabel = new JLabel("Response");
		guessResponse = new JTextField(10);
		guessResponse.setEditable(false);
		
		// adding the components to each panel
		panel.add(dieRollLabel);
		panel.add(dieRoll);
		panel2.add(guessLabel);
		panel2.add(guess);
		panel3.add(guessResultLabel);
		panel3.add(guessResponse);
		
		// titles of each of the bottom 3 panels in the bottom half of the panel
		panel.setBorder(new TitledBorder("Die"));
		panel2.setBorder(new TitledBorder("Guess"));
		panel3.setBorder(new TitledBorder("Guess Result"));
		
		// adding created panels above to overall frame
		JPanel mainPanel = new JPanel();
		mainPanel.add(panel);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		
		return mainPanel;
		
	}
	
	
//	public static void main(String[] args) {	
//		// creates the JPanel and adds it to the JFrame
//		ClueGUI gui = new ClueGUI();
//		// makes viewable
//		gui.setVisible(true);
//	}

}