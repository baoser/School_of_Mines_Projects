/**
 * @Author Bao Nguyen
 * @author Cassandra Vandeventer
 * 
 * CSCI 306-A
 * C13A-2
 * 
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;

public final class Board extends JPanel {
	int numRows = 0;
	int numColumns = 0;
	
	public final int MAX_BOARD_SIZE = 50;
	
	private BoardCell [][] board;
	private static Map<Character, String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private String boardConfigFile;
	private String roomConfigFile;
	private String peopleConfigFile;
	private String weaponConfigFile;
	private ArrayList<Player> people;
	private ArrayList<Card> deck;
	private ArrayList<Card> solution;
	ArrayList<Card> peopleDeck = new ArrayList<Card>();
	ArrayList<Card> weaponDeck = new ArrayList<Card>();
	ArrayList<Card> roomDeck = new ArrayList<Card>();

	
	// variable used for singleton pattern
	// Usually singletons are used for centralized management of internal or external resources
	// and they provide a global point of access to themselves
	// This involves only one class which is responsible to instantiate itself, to make sure
	// that it doesn't create more than one instance
	private static Board theInstance = new Board();
	
	private Board() {
		legend = new HashMap<Character, String>();
		board = new BoardCell [MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		people = new ArrayList<Player>();
		deck = new ArrayList<Card>();
		solution = new ArrayList<Card>();
		
	} // constructor is private to ensure only one can be created
	
	public static Board getInstance() { // this method returns the only Board
		return theInstance;
	}
	
	/**
	 * call functions in the correct order
	 */
	public void initialize() {
		loadRoomConfig();
		loadBoardConfig();
		loadPeopleConfig();
		loadDeckConfig();
		dealCards();
		calcAdjacencies();
	}
	
	/**
	 * clear legend first
	 * then read in each line from roomConfigFile and store as a string in an array
	 * using split method for a comma and a space. Each array should have 3 elements
	 * or it's an exception. Also every third element needs to be either card or other.
	 * If no exceptions are thrown, then populate the map.
	 */
	public void loadRoomConfig() {
		legend.clear();
		Scanner reader;
		try {
			reader = new Scanner (new File(roomConfigFile));
			while (reader.hasNextLine()) {
				String tempo = reader.nextLine();
				String[] rooms = tempo.split(", ");
				if (rooms.length != 3 || rooms[0].length() != 1) {
					throw new BadConfigFormatException ("Legend not formated correctly");
				}

				if (!rooms[2].equalsIgnoreCase("Card") && !rooms[2].equalsIgnoreCase("other")) {
					throw new BadConfigFormatException ("Legend not formated correctly");
				}
				legend.put(rooms[0].charAt(0), rooms[1]);
			}
			reader.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * reinitialize numRows and numColumns.
	 * read in each line as a string with split method on a comma and store in an array.
	 * check for exceptions on the right number of columns every time.
	 * create a board cell based on what kind of cell it is (is doorway and door direction and symbol)
	 * put the cell on the board
	 * 
	 */
	public void loadBoardConfig() {
		numRows = 0;
		numColumns = 0;
		Scanner reader;
		try {
			reader = new Scanner(new File(boardConfigFile));
			int i = 0;
			while(reader.hasNextLine()) {
				numRows++;
				String tempo = reader.nextLine();
				String[] rooms = tempo.split(",");
				if (i == 0) {
					numColumns = rooms.length;
				}
				else {
					if (rooms.length != numColumns) {
						throw new BadConfigFormatException ("invalid number of columns");
					}
				}
				for (int j = 0; j < rooms.length; j++) {
					BoardCell cell = new BoardCell(i, j);
					// check if it's a doorway, if so then give a direction
					if (rooms[j].length() > 1) {
						DoorDirection d;
						switch (rooms[j].charAt(1)) {
						case 'U':
							d = DoorDirection.UP;
							cell.setDirection(d);
							break;
						case 'D':
							d = DoorDirection.DOWN;
							cell.setDirection(d);
							break;
						case 'L':
							d = DoorDirection.LEFT;
							cell.setDirection(d);
							break;
						case 'R':
							d = DoorDirection.RIGHT;
							cell.setDirection(d);
							break;
						default:
							break;
						}
					} else {
						DoorDirection d = DoorDirection.NONE;
						cell.setDirection(d);
					}
					if(legend.containsKey(rooms[j].charAt(0))) {
						cell.setInitial(rooms[j].charAt(0));
					}
					else {
						throw new BadConfigFormatException("Board contains invalid symbols");
					}
					//put the cell on the board
					board [i][j] = cell;
				}
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * load people method:
	 * read in each line for people.txt file and store the contents
	 * of each line in a string array using the split method with ", "
	 * the array should have 4 elements for name, color, row, and column.
	 * Then create 1 human player and the rest are computer players.
	 */
	public void loadPeopleConfig() {
		people.clear();
		Scanner reader;
		int tempRow = 0;
		int tempCol = 0;
		Color tempColor;
		try {
			reader = new Scanner (new File("data/people.txt"));
			while (reader.hasNextLine()) {
				String tempo = reader.nextLine();
				String[] playerInfo = tempo.split(", ");
				// array should have 4 elements
				if (playerInfo.length != 4) {
					throw new BadConfigFormatException ("People.txt not formated correctly");
				}
				// convert to row and columns from string
				try {
					tempRow = Integer.parseInt(playerInfo[2]);
					tempCol = Integer.parseInt(playerInfo[3]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new BadConfigFormatException ("Players' locations not formated correctly");
				}
				// check for boundaries
				if (tempRow < 0 || tempRow > numRows || tempCol < 0 || tempCol > numColumns) {
					throw new BadConfigFormatException ("locations are out of bounds");
				}
				// convert to color from string
				try {
				    Field field = Class.forName("java.awt.Color").getField(playerInfo[1]);
				    tempColor = (Color)field.get(null);
				} catch (Exception e) {
				    throw new BadConfigFormatException ("Color isn't formated correctly");
				}
				// create the first player as the human player
				if (people.size() == 0) {
					HumanPlayer human = new HumanPlayer(playerInfo[0], tempColor, tempRow, tempCol);
					people.add(human);
				}
				// create computer players
				else {
					ComputerPlayer comp = new ComputerPlayer(playerInfo[0], tempColor, tempRow, tempCol);
					people.add(comp);
				}
			}
			reader.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}

	/**
	 * create the deck:
	 * create a card for each player, weapon, and room
	 * and add it to the deck. Perform one type at a time
	 */
	public void loadDeckConfig() {
		deck.clear();
		// create a new card of type PERSON for each player in the list
		for (Player p: people) {
			Card c = new Card(CardType.PERSON, p.getName());
			// add to the deck
			deck.add(c);
		}
		
		// create a new of type WEAPON for each weapon in the file
		Scanner reader;
		try {
			reader = new Scanner(new File("data/Weapon.txt"));
			while(reader.hasNextLine()) {
				String tempo = reader.nextLine();
				Card c = new Card(CardType.WEAPON, tempo);
				deck.add(c);
			}
			reader.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}

		// iterate through the map legend to get the names of the rooms
		// and create a card of type ROOM for each room in the map
		Iterator it = legend.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if ((char)pair.getKey() != 'X' && (char)pair.getKey() != 'W') {
				Card c = new Card(CardType.ROOM, (String)pair.getValue());
				deck.add(c);
			}
		}
	}
	
	/**
	 * deal cards:
	 * create 3 temporary ArrayLists for 3 different types of card.
	 * deal one pile at a time. The first card of each pile goes to
	 * solution list
	 */
	public void dealCards() {
		// adding cards to the 3 piles by types
		for (Card c: deck) {
			if (c.getType() == CardType.PERSON) {
				peopleDeck.add(c);
			}
			else if (c.getType() == CardType.WEAPON) {
				weaponDeck.add(c);
			}
			else if (c.getType() == CardType.ROOM) {
				roomDeck.add(c);
			}
		}
		//shuffle the 3 piles
		long seed = System.nanoTime();
		Collections.shuffle(peopleDeck, new Random(seed));
		Collections.shuffle(weaponDeck, new Random(seed));
		Collections.shuffle(roomDeck, new Random(seed));
		
		int playerOrder = 0; //keeps track of which player receive the next card
		// dealing people pile
		for (int i = 0; i < peopleDeck.size(); ++i) {
			// first card goes to solution
			if (solution.size() == 0) {
				solution.add(peopleDeck.get(i));
				continue;
			}
			// the rest of the cards go to players
			else {
				people.get(playerOrder).getMyCards().add(peopleDeck.get(i));
				playerOrder++;
				// reset playerOrder to the first player if the last player in the list is reached
				if (playerOrder >= people.size()) {
					playerOrder = 0;
				}
			}
		}
		// dealing weapon pile
		for (int i = 0; i < weaponDeck.size(); ++i) {
			// first card goes to solution
			if (solution.size() == 1) {
				solution.add(weaponDeck.get(i));
				continue;
			}
			// the rest goes to players
			else {
				people.get(playerOrder).getMyCards().add(weaponDeck.get(i));
				playerOrder++;
				// reset playerOrder to the first player if the last player in the list is reached
				if (playerOrder >= people.size()) {
					playerOrder = 0;
				}
			}
		}
		// dealing room pile
		for (int i = 0; i < roomDeck.size(); ++i) {
			// first card goes to solution
			if (solution.size() == 2) {
				solution.add(roomDeck.get(i));
				continue;
			}
			// the rest goes to players
			else {
				people.get(playerOrder).getMyCards().add(roomDeck.get(i));
				playerOrder++;
				// reset playerOrder to the first player if the last player in the list is reached
				if (playerOrder >= people.size()) {
					playerOrder = 0;
				}
			}
		}
	}
	
	/**
	 * calculate all possible adjacent cells for 
	 * each BoardCell. if newRow and newColumn 
	 * is less than 0 or greater than numRow or newColumn respectively, then they are
	 * invalid cells. Other wise add it to a temporary
	 * set and add the current cell and the temporary set
	 * to our map
	 */
	public void calcAdjacencies() {
		// array contains directions for column
		int adjColumn [] = {0, 1, 0, -1};
		// array contains directions for row
		int adjRow [] = {-1, 0, 1, 0};
		// variables to indicate the next row and column
		int newRow;
		int newColumn;
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numColumns; c++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				for (int k = 0; k < 4; k++) {
					newRow = r + adjRow[k];
					newColumn = c + adjColumn[k];
					if (newRow >= 0 && newRow < numRows && newColumn >= 0 && newColumn < numColumns) {
						// if the adj piece is a walkway and the current piece is not a room piece
						if (board[newRow][newColumn].isWalkway() && !board[r][c].isRoom())
							temp.add(board[newRow][newColumn]);
						
						// if the current piece is a doorway
						else if (board[r][c].isDoorway()) {
							// check for correct piece according to the door direction
							if (r + 1 == newRow && board[r][c].getDoorDirection() == DoorDirection.DOWN) {
								temp.add(board[newRow][newColumn]);
							}
							else if (r - 1 == newRow && board[r][c].getDoorDirection() == DoorDirection.UP) {
								temp.add(board[newRow][newColumn]);
							}
							else if (c + 1 == newColumn && board[r][c].getDoorDirection() == DoorDirection.RIGHT) {
								temp.add(board[newRow][newColumn]);
							}
							else if (c - 1 == newColumn && board[r][c].getDoorDirection() == DoorDirection.LEFT) {
								temp.add(board[newRow][newColumn]);
							}
						}
						// if the adj piece is a doorway
						else if (board[newRow][newColumn].isDoorway()) {
							//check for correct door direction
							if (r + 1 == newRow && board[newRow][newColumn].getDoorDirection() == DoorDirection.UP) {
								temp.add(board[newRow][newColumn]);
							}
							else if (r - 1 == newRow && board[newRow][newColumn].getDoorDirection() == DoorDirection.DOWN) {
								temp.add(board[newRow][newColumn]);
							}
							else if (c + 1 == newColumn && board[newRow][newColumn].getDoorDirection() == DoorDirection.LEFT) {
								temp.add(board[newRow][newColumn]);
							}
							else if (c - 1 == newColumn && board[newRow][newColumn].getDoorDirection() == DoorDirection.RIGHT) {
								temp.add(board[newRow][newColumn]);
							}
						}
					}
				}
				//add r, c, and temp to the map we created
				adjMatrix.put(board[r][c], temp);
			}
		}
	}
	
	/**
	 * clear the Sets targets and visited every time
	 * we need to calculate targets for a new current cell,
	 * then add current cell to visited and call the recursive
	 * function to get all possible targets for startCell 
	 * @param row
	 * @param column
	 * @param pathLength
	 */
	public void calcTargets(int row, int column, int pathLength) {
		if (adjMatrix.size() == 0) {
			calcAdjacencies();
		}
		targets.clear();
		visited.clear();
		visited.add(board[row][column]);
		findTargets(board[row][column], pathLength);
	}
	
	/**
	 * recursive function to calculate all possible targets
	 * of a current cell.
	 * @param startCell
	 * @param pathLength
	 */
	private void findTargets(BoardCell startCell, int pathLength) {
		for (BoardCell cell : adjMatrix.get(startCell)) {
			// if a cell is already in the visited list, continue
			if (visited.contains(cell)) {
				continue;
			}
			// add to visited
			visited.add(cell);
			
			// if die value goes down to one or if the current cell 
			// is a door way then add it to the targets list
			// else keep calling the function.
			if (pathLength == 1 || cell.isDoorway()) {
				targets.add(cell);
			} else {
				findTargets(cell, pathLength - 1);
			}
			visited.remove(cell);
		}
	}
	
	/**
	 * return the map legend
	 */
	public Map<Character, String> getLegend(){
		return legend;
	}
	
	/**
	 * passing in file names
	 * @param layoutFile
	 * @param legendFile
	 */
	public void setConfigFiles(String layoutFile, String legendFile) {
		boardConfigFile = "data/"+ layoutFile;
		roomConfigFile = "data/" + legendFile;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public BoardCell getCellAt(int row, int column) {
		return board[row][column];
	}
	
	public Set<BoardCell> getAdjList(int row, int col) {
		return adjMatrix.get(board[row][col]);
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public ArrayList<Player> getPeople() {
		return people;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	public ArrayList<Card> getSolution() {
		return solution;
	}
	
	public ArrayList<Card> getPeopleDeck() {
		return peopleDeck;
	}

	public ArrayList<Card> getWeaponDeck() {
		return weaponDeck;
	}

	public ArrayList<Card> getRoomDeck() {
		return roomDeck;
	}

	public boolean checkAccusation(ArrayList<Card> accusation) {
		for (Card a: accusation) {
			for (Card s: solution) {
				if (a.getType() == s.getType()) {
					if (!a.getCardName().equals(s.getCardName())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// setter for solution. this is for testing purposes only
	public void setSolution(ArrayList<Card> testSolution) {
		solution = testSolution;
	}
	
	public void setDeck(ArrayList<Card> cardList) {
		deck = cardList;
	}

	public void setPeople(ArrayList<Player> people) {
		this.people = people;
	}
	
	/**
	 * handling a suggestion
	 * @param suggestion
	 * @param suggester
	 * @return
	 */
	public Card handleSuggestion(ArrayList<Card> suggestion, Player suggester) {
		//start with the player next of suggester
		for (int i = people.indexOf(suggester) + 1; i != people.indexOf(suggester); i++){
			//if the last player is reached
			if (i >= people.size()) {
				//if at the suggester, return null
				if (people.indexOf(suggester) == 0) {
					return null;
				}
				//reset i back to first player
				i = 0;
			}
			//return result from disprove suggestion in Player class
			if (people.get(i).disproveSuggestion(suggestion) != null) {
				return people.get(i).disproveSuggestion(suggestion);
			}
		}
		return null;
	}
	
	/**
	 * Get each board cell to draw itself
	 * Draw player
	 * Draw room names
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//get each board cell to draw itself
		for(int row = 0; row < getNumRows(); row++) {
			for(int col = 0; col < getNumColumns(); col++) {
				board[row][col].drawBoardCell(g);
			}
		}
		
		//get each player to draw itself
		for(int k = 0; k < people.size(); k++) {
			people.get(k).drawPlayer(g);
		}
		
		//draw room names
		g.setColor(Color.BLUE);
		g.drawString("THEATER", 50, 450);
		g.drawString("KITCHEN", 42, 60);
		g.drawString("GARDEN", 510, 425);
		g.drawString("FAMILY ROOM", 200, 50);
		g.drawString("LIBRARY", 265, 460);
		g.drawString("STUDIO", 350, 50);
		g.drawString("PATIO", 525, 60);
		g.drawString("BILLIARD ROOM", 30, 250);
		g.drawString("HALL", 500, 225);
	}


}
