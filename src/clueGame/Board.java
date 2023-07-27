/**
 * Board: board class is used to load in the information about the game and game board from files,
 * as well as store all of the board cells and calculate possible targets for given number of steps.
 * Authors: Landon Dixon and Charles Hulongbayan
 * Date Started: Feb. 23, 2023
 */

package clueGame;

import java.awt.Color;
//import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

//import javax.swing.JDialog;
//import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * The board class is used to load in the information about the game and game board from files,
 * as well as store all of the board cells and calculate possible targets for given number of steps.
 *   
 * */
@SuppressWarnings("serial")
public class Board extends JPanel{
	int cols;//= 33;//24 for 306 tests
	int rows; //= 24;//25 for 306 tests
	int roll;
	final static int NUM_OF_CARDS_PER_PLAYER=3;//can't be static because a game might have extra rooms/weapons/players? 
	private BoardCell[][] grid;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private Set<BoardCell> clickableCells;//targetCells=new HashSet<BoardCell>();
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character,Room> roomMap;
	private ArrayList<Player> players;
	private Set<Card> cards;
	private Solution theAnswer; 
	private Boolean playerFinished;
	private Boolean playerMoved; //should not do this up here
	private Player currPlayer;
	//private Player movedPlayer;
	private GameControlPanel controlPanel;
	private GameCardPanel cardPanel;
	final static Color TARGET_COLOR=Color.cyan;

	private Boolean draw;
	private Boolean moved; //shouldn't do these here either
	//private Solution playerSolution;

	//private Solution playerSolution;
	@SuppressWarnings("unused")
	private Solution playerAccusation;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();

	// constructor is private to ensure only one can be created
	private Board() {

		super() ;
	}
	// this method returns the only Board
	public static Board getInstance() {

		return theInstance;
	}

	/*
	 * initialize the board (since we are using singleton pattern), calls both loadSetupConfig and loadLayoutConfig.
	 * Also calls calcAdjacencies
	 */
	public void initialize(){

		visited=new HashSet<BoardCell>();
		targets=new HashSet<BoardCell>();
		clickableCells=new HashSet<BoardCell>();
		theAnswer=new Solution();
		addMouseListener(new boardListener());
		draw=moved=playerMoved=false;
		playerFinished=true;
		try {

			loadSetupConfig();
			loadLayoutConfig();

		}
		catch(BadConfigFormatException e) {

			e.printStackTrace();
		}
		calcAdjacencies();
		deal();
	}

	/*
	 * calcAdjacencies: calculates what cells are considered adjacent to each other
	 */	
	private void calcAdjacencies() {
		for(int row = 0; row < rows; row++) { //iterate across grid

			for(int col = 0; col < cols; col++) { 

				if(!grid[row][col].isRoomCenter()) { //if not center

					if(row > 0) {// if not in top row

						if(grid[row][col].getDoorDirection() == DoorDirection.UP) {// if an up door 

							BoardCell center =roomMap.get(grid[row-1][col].getInitial()).getCenterCell();// add adjacencies between center of room and door
							grid[row][col].addAdjacency(center);
							center.addAdjacency(grid[row][col]);
						}
						else if(grid[row][col].getInitial() == grid[row-1][col].getInitial()){ //else if cell above in same room

							grid[row][col].addAdjacency(grid[row-1][col]); //add adjacency of cell above
						}
					}
					if(row < rows - 1) {

						if(grid[row][col].getDoorDirection() == DoorDirection.DOWN) {

							BoardCell center =roomMap.get(grid[row+1][col].getInitial()).getCenterCell();
							grid[row][col].addAdjacency(center);
							center.addAdjacency(grid[row][col]);
						}
						else if(grid[row][col].getInitial() == grid[row+1][col].getInitial()){

							grid[row][col].addAdjacency(grid[row+1][col]);
						}
					}
					if(col > 0) {

						if(grid[row][col].getDoorDirection() == DoorDirection.LEFT) {

							BoardCell center = roomMap.get(grid[row][col-1].getInitial()).getCenterCell();
							grid[row][col].addAdjacency(center);
							center.addAdjacency(grid[row][col]);
						}
						else if(grid[row][col].getInitial() == grid[row][col-1].getInitial()){

							grid[row][col].addAdjacency(grid[row][col-1]);
						}
					}
					if(col <cols - 1) {

						if(grid[row][col].getDoorDirection() == DoorDirection.RIGHT) {

							BoardCell center =roomMap.get(grid[row][col+1].getInitial()).getCenterCell();
							grid[row][col].addAdjacency(center);
							center.addAdjacency(grid[row][col]);
						}
						else if(grid[row][col].getInitial() == grid[row][col+1].getInitial()){

							grid[row][col].addAdjacency(grid[row][col+1]);
						}
					}
					if(!(grid[row][col].getSecretPassage() == '\0')) {// if secret passage

						BoardCell center1 = roomMap.get(grid[row][col].getInitial()).getCenterCell();//add adjcacencies between centers of rooms
						BoardCell center2 = roomMap.get(grid[row][col].getSecretPassage()).getCenterCell();
						center1.addAdjacency(center2);
						center2.addAdjacency(center1);
					}
				}
			}
		}
	}

	/*
	 *loadLayoutConfig: Loads the layout file configuration information from a file. Throws an exception if the file formatting is incorrect.
	 *Also Initializes grid and inputs all of the cells into it.
	 * */
	public void loadLayoutConfig() throws BadConfigFormatException{

		try {

			FileReader reader = new FileReader("data/"+ layoutConfigFile);
			Scanner in = new Scanner(reader);
			rows = 0;
			cols = 0;
			ArrayList<String[]> csvStrArray = new ArrayList<String[]>(); 
			while(in.hasNextLine()){

				String line = in.nextLine();
				String[] splitLine =  line.split(",");
				if(rows==0) {

					cols = splitLine.length;
				}
				if(splitLine.length!=cols) {

					in.close();
					throw new BadConfigFormatException();
				}
				rows++;
				csvStrArray.add(splitLine);
			}
			in.close();
			initializeGrid(csvStrArray);
		}catch(FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	/*
	 *loadSetupConfig: Loads the setup file configuration information from a file. Throws an exception if the file formatting is incorrect.
	 *Also initializes roomMap and puts all the rooms (and Spaces) into it.
	 * */
	public void loadSetupConfig() throws BadConfigFormatException{

		players=new ArrayList<Player>();
		roomMap = new HashMap<Character,Room>();
		cards = new HashSet<Card>();
		try {

			FileReader reader = new FileReader("data/"+ setupConfigFile);
			Scanner in = new Scanner(reader);
			while(in.hasNextLine()){

				String line = in.nextLine();
				if(!line.substring(0,2).equals("//")) {//make newRoom somewhere up here insteade of down there each time maybe?

					String[] splitLine =line.split(", ");
					if(splitLine[0].equals("Room")) {

						Room newRoom = new Room(splitLine[1],true);
						roomMap.put(splitLine[2].charAt(0), newRoom); //causing a nullptr error in exceptions
						newRoom.setColor(Color.white);
						Card newCard = new Card(splitLine[1],CardType.ROOM);
						cards.add(newCard);
					}else if(splitLine[0].equals("Space")) {

						Room newRoom = new Room(splitLine[1],false); //not real room so set to false
						if(splitLine[1].equals("Unused")) {

							newRoom.setColor(Color.gray);
						}else {

							newRoom.setColor(Color.yellow);
						}
						roomMap.put(splitLine[2].charAt(0), newRoom);	
					}else if(splitLine[0].equals("Weapon")) {

						Card newCard = new Card(splitLine[1],CardType.WEAPON);
						cards.add(newCard);
					}else if (splitLine[0].equals("Player")){

						Player player;
						if (splitLine[3].equals("Human")) {

							player=new HumanPlayer(splitLine[1],splitLine[2],Integer.parseInt(splitLine[4]),Integer.parseInt(splitLine[5]));
						}else {

							player=new ComputerPlayer(splitLine[1],splitLine[2],Integer.parseInt(splitLine[4]),Integer.parseInt(splitLine[5]));
						}
						players.add(player);
						Card newCard = new Card(splitLine[1],CardType.PERSON);
						cards.add(newCard);
					}else {

						in.close();
						throw new BadConfigFormatException();
					}	
				}
			}
			in.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	/*
	 * initilizeGrid initializes the grid that represents the map of the game based on information gotten from the layoutConfigFile. 
	 * Also checks things like if a certain cell is a door or not and updates the cell appropriately.
	 * @param csvStrArray: array from loadlayoutconfig that contains cell data from csv 
	 * */
	private void initializeGrid(ArrayList<String[]> csvStrArray) throws BadConfigFormatException{

		grid=new BoardCell[rows][cols];
		for(int row = 0; row < rows; row++) {// iterate through grid

			for(int col = 0; col < cols; col++) {

				if(!roomMap.containsKey(csvStrArray.get(row)[col].charAt(0))) {// if csv has chars that are not rooms in setup txt
					throw new BadConfigFormatException("Bad spot: "+row+", "+col);
				}
				grid[row][col] = new BoardCell(row,col, csvStrArray.get(row)[col].charAt(0));
				grid[row][col].setColor(roomMap.get(csvStrArray.get(row)[col].charAt(0)).getColor());
				if(roomMap.get(csvStrArray.get(row)[col].charAt(0)).isRealRoom()) { //checking to see if its a room or not
					grid[row][col].setRoom(true);
				}

				if(csvStrArray.get(row)[col].length() == 2) {// if the cell has a second character ( extra property)

					if(csvStrArray.get(row)[col].charAt(1)== '^') {// if has up arrow as second char

						grid[row][col].setDoorDirection(DoorDirection.UP);// set doordir to up 
					}
					else if(csvStrArray.get(row)[col].charAt(1)== '>') {

						grid[row][col].setDoorDirection(DoorDirection.RIGHT);
					}
					else if(csvStrArray.get(row)[col].charAt(1)== '<') {

						grid[row][col].setDoorDirection(DoorDirection.LEFT);
					}
					else if(csvStrArray.get(row)[col].charAt(1)== 'v') {

						grid[row][col].setDoorDirection(DoorDirection.DOWN);
					}
					else if(csvStrArray.get(row)[col].charAt(1)== '#') {//#=label

						grid[row][col].setRoomLabel(true);
						roomMap.get(csvStrArray.get(row)[col].charAt(0)).setLabel(grid[row][col]);
					}
					else if(csvStrArray.get(row)[col].charAt(1)== '*') {//*=center

						grid[row][col].setRoomCenter(true);
						roomMap.get(csvStrArray.get(row)[col].charAt(0)).setCenterCell(grid[row][col]);
					}
					else {// if not one of the chars above its a secret passage 

						grid[row][col].setSecretPassage(csvStrArray.get(row)[col].charAt(1));
					}
				}
			}
		}
	}

	/*
	 * calcTargets: Wrapper for recursiveCalcTargets, clears the sets used and then calls the recursive function.
	 * @Param startCell tells the board where to start calculating from.
	 * @Param pathLength tells how far the player can go.
	 * */
	public void calcTargets(BoardCell startCell, int pathLength) {

		//Clearing targets and visited so that there are no leftover values from previous calculations
		targets.clear();
		visited.clear();
		clickableCells.clear();
		//maybe reset the color for all of the cells?
		if(currPlayer != null) { // target tests do bad things
			draw=currPlayer.isHuman();
		}
		if(moved) {

			targets.add(startCell);
			clickableCells.add(startCell);
			if(draw) {


				startCell.setColor(TARGET_COLOR);
				for(int row = 0; row < rows; row++) {// iterate through grid

					for(int col = 0; col < cols; col++) {

						if(grid[row][col].getInitial()==startCell.getInitial()) {		
							clickableCells.add(grid[row][col]);
							grid[row][col].setColor(TARGET_COLOR);
						}
					}
				}
			}
			moved=false;
		}
		recursiveCalcTargets( startCell,  pathLength);
	}

	/**
	 * @brief recursiveCalcTargets: calculates where a player can get to from a certain starting position with a certain path length recursively.
	 * @Param startCell: tells the board where to start calculating from.
	 * @Param pathLength: tells how far the player can go.
	 * */
	private void recursiveCalcTargets(BoardCell startCell, int pathLength) { //be able to stay in room if you were telaported in their, might have to pass in another bool.

		if(pathLength==0) {

			if(draw) {
				clickableCells.add(startCell);//only need to click if it is going to be drawn
				startCell.setColor(TARGET_COLOR);
			}
			//only color if current player is human
			targets.add(startCell);
			return;
		}
		visited.add(startCell);

		Set<BoardCell> adjLst=startCell.getAdjList();
		for(BoardCell adjCell : adjLst) {
			if(startCell.isRoomCenter()&&adjCell.isRoomCenter()) {
				targets.add(adjCell);//for secret passage
				//Going through the entire grid to change the color of room cells that are targets
				if(draw) {
					clickableCells.add(adjCell);
					adjCell.setColor(TARGET_COLOR);
					for(int row = 0; row < rows; row++) {// iterate through grid

						for(int col = 0; col < cols; col++) {

							if(grid[row][col].getInitial()==adjCell.getInitial()) {		
								clickableCells.add(grid[row][col]);
								grid[row][col].setColor(TARGET_COLOR);
							}
						}
					}
				}
				visited.add(adjCell);
			}
			//can't add a room that is in visited already, only effects when we start in a room, 
			//previously was adding the room itself to its own target list.
			else if (adjCell.isRoomCenter()&&!visited.contains(adjCell)) {

				targets.add(adjCell);
				//Going through the entire grid to change the color of room cells that are targets
				if(draw) {
					clickableCells.add(adjCell);
					adjCell.setColor(TARGET_COLOR);
					for(int row = 0; row < rows; row++) {// iterate through grid

						for(int col = 0; col < cols; col++) {

							if(grid[row][col].getInitial()==adjCell.getInitial()) {		
								clickableCells.add(grid[row][col]);
								grid[row][col].setColor(TARGET_COLOR);
							}
						}
					}
				}
				visited.add(adjCell);
				//getRoom(adjCell).getName();//?
			}
			else if(!adjCell.getOccupied()) {

				if(!visited.contains(adjCell)) {

					visited.add(adjCell);
					recursiveCalcTargets(adjCell,pathLength-1);
					visited.remove(adjCell);
				}
			}
		}
	}


	/*
	 * deal: Creates the final solution of the game randomly and deals the remaining cards randomly to the players
	 * */
	public void deal() {

		int randCard;
		boolean foundWeapon=false, foundPlayer=false,foundRoom=false;
		Random rand=new Random();
		ArrayList<Card> cardList = new ArrayList<Card>(cards);
		//randomly generating the final solution
		while(!foundPlayer||!foundWeapon||!foundRoom) {

			randCard=rand.nextInt(cardList.size());
			if(cardList.get(randCard).getCardType()==CardType.ROOM && !foundRoom) {

				theAnswer.setRoom(cardList.get(randCard));
				cardList.remove(cardList.get(randCard));
				foundRoom=true;
			}else if(cardList.get(randCard).getCardType()==CardType.WEAPON && !foundWeapon) {

				theAnswer.setWeapon(cardList.get(randCard));
				cardList.remove(cardList.get(randCard));
				foundWeapon=true;
			}else if(cardList.get(randCard).getCardType()==CardType.PERSON && !foundPlayer) {

				theAnswer.setPerson(cardList.get(randCard));
				cardList.remove(cardList.get(randCard));
				foundPlayer=true;
			}
		}
		//dealing the cards to the players
		for(Player player: players) {

			for(int j=0;j<NUM_OF_CARDS_PER_PLAYER;j++) {

				randCard=rand.nextInt(cardList.size()); 
				//changes the color for the card to display the holder color in the GUI
				cardList.get(randCard).setHolderColor(player.getColor()); 
				player.updateHand(cardList.get(randCard),j);
				player.updateSeenCards(cardList.get(randCard));
				cardList.remove(randCard);
			}
		}
	}

	/*
	 * checkAccusation: checks and inputed accusation against theAnswer, returns boolean
	 * @Param accusation is the passed in accusation
	 * */
	public boolean checkAccusation(Solution accusation) {

		if(this.theAnswer.getPerson() == accusation.getPerson() && this.theAnswer.getWeapon() == accusation.getWeapon() && this.theAnswer.getRoom()== accusation.getRoom()) {

			return true;
		}else {
			return false;
		}

	}


	/*
	 * handleSuggestion: runs disproveSuggestion for each player in a circle
	 * @Param player is suggesting player
	 * @Param suggestion is the suggestion the player is suggesting
	 * */
	public Card handleSuggestion(Player player, Solution suggestion) {
		//moves the accused player to the room of the accusing player
		String playerToMoveName=suggestion.getPerson().getCardName();
		for(Player playerToMove: players) {
			if (playerToMove.getName().equals(playerToMoveName)) {
				playerToMove.setPosition(getCell(player.getRow(),player.getCol()));
				//setDrawnPos here to probably
			}
		}
		int suggPlayerIndex =this.players.indexOf(player);
		int i = suggPlayerIndex;// start with suggester
		Card returnCard = null;
		while(true) {

			i++;//increment to next player
			if (i >=players.size()){

				i = 0;// loops back to first player
			}
			if(i == suggPlayerIndex) {

				return null;// return null when you get back to suggester
			}
			returnCard = players.get(i).disproveSuggestion(suggestion);

			if(returnCard != null) {

				return returnCard;// return card if found
			}
		}
	}


	/*
	 * paintComponent: overriding this function to draw the board
	 * 
	 * @Param g: Allow function to actually draw
	 * */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(getGraphics());
		int cellSizeX = getWidth()/cols;
		int cellSizeY = getHeight()/rows;

		//drawing each cell 
		for(int row = 0; row < rows; row++) {

			for(int col = 0; col < cols; col++) {

				grid[row][col].drawCell(cellSizeX,cellSizeY, g);
			}
		} 

		//drawing grid border lines, separate so that rooms don't cover them
		for(int row = 0; row < rows; row++) {

			for(int col = 0; col < cols; col++) {

				grid[row][col].drawBorder(cellSizeX,cellSizeY, g);
			}
		}

		//drawing the room labels
		for(int row = 0; row < rows; row++) {

			for(int col = 0; col < cols; col++) {

				grid[row][col].drawLabel(cellSizeX,cellSizeY, g,roomMap);
			}
		}

		//drawing doors
		for(int row = 0; row < rows; row++) {

			for(int col = 0; col < cols; col++) {

				grid[row][col].drawDoor(cellSizeX,cellSizeY, g);
			}
		}

		//need to draw where there are secret passages hereish at some point.

		//drawing players, checking if there are players on top of each other so they can be drawn offset from eachother so they can be seen
		boolean alone=true;
		int matchesCount=0;
		for (Player player :players) {
			matchesCount=0;
			alone=true;
			for(Player playerPos:players) {
				if(playerPos.getRow()==player.getRow()&&playerPos.getCol()==player.getCol()) {

					matchesCount++;
				}
				if(matchesCount>1) {

					alone=false;

				}
				//check if playerPos matches player twice
			}
			player.drawPlayer(cellSizeX,cellSizeY, g,alone);
		}

	}

	/*
	 * nextFlow: deals with game flow control, runs whenever the next button is pressed, 
	 * */
	public void nextFlow() {
		//check if human finished
		controlPanel.setGuessResult(""); //reseting controlPanel to blank
		controlPanel.setGuess("");
		if(!playerFinished&&!playerMoved) {
			//error here
			String message = "Error: move token before next";
			JOptionPane.showMessageDialog(null, message);

		}else {
			//yes
			//update player
			if(currPlayer == null || players.indexOf(currPlayer)==players.size()-1) {
				currPlayer = players.get(0);
			}
			else {
				currPlayer = players.get(players.indexOf(currPlayer)+1);
			}

			//roll
			Random rand=new Random();
			roll = rand.nextInt(6)+1; 
			//computer do accuse here, don't need to calc targets
			//calcTargets

			moved=currPlayer.isMoved();
			calcTargets(getCell(currPlayer.getRow(), currPlayer.getCol()),roll);
			moved=false;//resetting moved and the moved variable for the current player,
			currPlayer.setMoved(false);
			controlPanel.setTurn(currPlayer, roll);
			if(currPlayer.isHuman){
				//showTarg
				//set these booleans to false so that the action listeners will work
				playerMoved=false;
				playerFinished = false;
			}else{
				//not a human, check if computer can make an accusation
				if(currPlayer.canMakeAccusation(cards.size())) {
					Solution answer =currPlayer.makeAccusation(cards); 
					String message = "Computer won, it was "+answer.getPerson().getCardName()+" with the "+answer.getWeapon().getCardName()+" in "+answer.getRoom().getCardName()+"\nYou are lose!";
					JOptionPane.showMessageDialog(null, message);

				}

				//computer do move
				currPlayer.setPosition(currPlayer.selectTarget(targets, roomMap));

				if (getCell(currPlayer.getRow(),currPlayer.getCol()).isRoom()) { //if after move the computer is in a room make suggestion

					Solution suggestion=currPlayer.createSuggestion(cards, getRoom(getCell(currPlayer.getRow(),currPlayer.getCol()).getInitial()).getName());

					//display guess
					controlPanel.setGuess("It was "+suggestion.getPerson().getCardName()+" with the "+suggestion.getWeapon().getCardName()+" in "+suggestion.getRoom().getCardName());
					Card disprovingCard=handleSuggestion(currPlayer,suggestion);

					for (Player player: players) {//finding out who moved so that on their turn they can stay in the same spot

						if(suggestion.getPerson().getCardName()==player.getName()) {

							player.setMoved(true);
							break;
						}

					}
					if(disprovingCard!=null) {

						Color playerColor=disprovingCard.getHolderColor();
						for (Player player: players) {//going through all the players to find who disproved the card
							if (player.getColor()==playerColor) {
								controlPanel.setGuessResult("Disproven by "+player.getName());
								break;

							}
							if(suggestion.getPerson().getCardName()==player.getName()) {
								player.setMoved(true);
							}

						}
						currPlayer.updateSeenCards(disprovingCard);
					}else {
						controlPanel.setGuessResult("Suggestion not disproven");

					}
				}
			}
			repaint();

		}
	}


	/*
	 * boardListener: Class that implements MouseListener so that the player can click on the board. This allows the user to 
	 * select a square to move to.
	 * */
	private class boardListener implements MouseListener {


		/*
		 * mousePressed: when the mouse is pressed this function decides if the user has clicked on a viable spot 
		 * and if so updates playerFinished so that the next player can go.
		 * 
		 * @Param e: MouseEvent to listen for the mouse press and to get the coordinates of where it was when pressed
		 * */
		@Override
		public void mousePressed(MouseEvent e) {

			int cellSizeX = getWidth()/cols;
			int cellSizeY = getHeight()/rows;
			if(playerMoved) {
				return;
			}
			//checking to see if the click was in any of the possible cells
			for(BoardCell cell:clickableCells) {
				if(cell.clickedOn(e.getX(),e.getY(),cellSizeX,cellSizeY) && !playerFinished){

					BoardCell newCell=cell;
					if (cell.isRoom()) {
						newCell=getRoom(cell).getCenterCell();
					}

					currPlayer.setPosition(newCell);
					if(newCell.isRoom()) {
						currPlayer.createSuggestion(cards, getRoom(getCell(currPlayer.getRow(),currPlayer.getCol()).getInitial()).getName());

					}

					playerMoved=true;
					break;
				}
			}
			//remove color from targets after the player has moved
			if(playerMoved==true) {
				for(BoardCell coloredCell: clickableCells) { 

					if(!coloredCell.isRoom()) {

						coloredCell.setColor(Color.yellow);
					}else {

						coloredCell.setColor(Color.white);
					}
				}
				repaint();
			}
		}

		//below functions not used but had to be added
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

	}

	/*
	 * print suggestion is the code that runs when someone makes a suggestion, whether they are a human or computer
	 * 
	 * @Param suggestion: suggestion is the three cards that were suggested
	 * 
	 * */
	public void printSuggestion(Solution suggestion) {
		controlPanel.setGuess("It was "+suggestion.getPerson().getCardName()+" with the "+suggestion.getWeapon().getCardName()+" in "+suggestion.getRoom().getCardName());
		Card disprovingCard=handleSuggestion(currPlayer,suggestion);
		if(disprovingCard!=null) {
			//display disproving player here somehow, get disprovingCard.getHolderColor() and use the color to find the correct player
			Color playerColor=disprovingCard.getHolderColor();
			for (Player player: players) {
				if (player.getColor()==playerColor) {
					controlPanel.setGuessResult("Disproven by "+player.getName());
					break;
				}
			}
			currPlayer.updateSeenCards(disprovingCard);
			if(currPlayer.isHuman()) {
				cardPanel.updatePanel(currPlayer, disprovingCard.getCardType());

			}
			cardPanel.validate();
		}else {
			controlPanel.setGuessResult("Suggestion not disproven");
		}
		cardPanel.repaint();
	}

	
	/*
	 * accuseFlow is the logic for if the make accusation button is going to work, otherwise an error box is displayed
	 * 
	 * */
	public void accuseFlow() {
		if(currPlayer.isHuman){
			playerAccusation = getHumanPlayer().makeAccusation(cards);
		}else {
			String message = "Error: can only accuse during your turn";
			JOptionPane.showMessageDialog(null, message);
		}
	}



	//setters and getters
	public void setConfigFiles(String layout,String setup){

		this.layoutConfigFile = layout;
		this.setupConfigFile= setup;
	}

	
	public void setSolution(Solution solution) {

		this.theAnswer = solution;
	}

	public void setControlPanel(GameControlPanel controlPanel) {

		this.controlPanel = controlPanel;
	}

	public void setCardPanel(GameCardPanel cardPanel) {

		this.cardPanel = cardPanel;
	}
	
	
	public void setPlayers(ArrayList<Player> players) {

		this.players = players;
	}

	public void setPlayerFinished(Boolean playerFinished) {
		this.playerFinished = playerFinished;
	}

	
	public ArrayList<Player> getPlayers(){

		return players;
	}

	public Solution getSolution() {

		return theAnswer;
	}

	public BoardCell getCell(int row, int col) {

		return grid[row][col];
	}

	public Set<BoardCell> getTargets(){

		return targets;
	}

	//Two getters for a room, one that takes a cell one that takes a character.
	public Room getRoom(Character character) {

		return roomMap.get(character);
	}

	public Room getRoom(BoardCell cell) {

		return roomMap.get(cell.getInitial());
	}

	public Map<Character,Room> getRoomMap(){

		return roomMap;
	}

	public int getNumRows() {

		return rows;
	}

	public int getNumColumns() {

		return cols;
	}

	public Player getPlayer(int ind) {

		return players.get(ind);
	}

	public Set<BoardCell>getAdjList(int row,int col){

		return grid[row][col].getAdjList();
	}

	public Set<Card> getCards() {

		return cards;
	}

	
	public Player getHumanPlayer() {

		for(Player player : players) {

			if(player.isHuman) {

				return player;
			}
		}
		return null; 
	}
	

}
