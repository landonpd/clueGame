package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	protected String name;
	//private Color color;
	protected Color color;
	protected int row,col;
	//protected int drawnRow,drawnCol;//consider drawRow and drawCol to draw differently in rooms, not terrible to implement if I do it this way
	protected Card[] hand;
	protected Set<Card> seenCards; //change to set 
	protected boolean isHuman;
	protected boolean moved;
	protected static final int DRAWING_OFFSET_MAX=7;
	protected static final int DRAWING_OFFSET_MIN=2;


	public Player(String name, String color, int row, int col) {
		super();
		this.name = name;
		this.row = row;//this.drawnRow=row;
		this.col = col;//this.drawnCol=col;
		this.hand=new Card[3];
		seenCards=new HashSet<Card>();
		switch (color.toLowerCase()) {
	    case "blue":
	    	this.color = Color.BLUE;
	        break;
	    case "green":
	    	 this.color = Color.GREEN;
	        break;
	    case "yellow":
	    	this.color = Color.YELLOW;
	        break;
	    case "magenta":
	    	this.color = Color.MAGENTA;
	        break;
	    case "orange":
	    	this.color = Color.ORANGE;
	        break;
	    case "purple":
	    	this.color = Color.MAGENTA;
	    	break;
	    case "red":
	    	this.color = Color.RED;
	        break;
	        }
	}
	
	/*
	 * drawPlayer: Draws the border of cells that are not rooms. This is separate so that when a room is drawn it doesn't 
	 * cover another cells outline.
	 * 
	 * @Param playerSizeX: the width of the player circle
	 * @Param playerSizeY: the height of the player circle
	 * @Param g: Used to allow the function to actually draw
	 * 
	 * */
	public void drawPlayer(int playerSizeX,int playerSizeY,Graphics g,boolean alone) {
		g.setColor(color);
		int drawnCol=col*playerSizeX,drawnRow=row*playerSizeY;
		if(!alone){
			Random rand=new Random();
			int drawingOffset=rand.nextInt(DRAWING_OFFSET_MAX)+DRAWING_OFFSET_MIN;
			if(rand.nextBoolean()) {
				drawnCol+=drawingOffset;//choose offset randomly so that they aren't still on top of each other, or randomly decide to add or subtract
				drawnRow+=drawingOffset;
			}else {
				drawnCol-=drawingOffset;//choose offset randomly so that they aren't still on top of each other, or randomly decide to add or subtract
				drawnRow-=drawingOffset;
			}
			//System.out.println(name+" is overlapping with someone");
			
			
			//System.out.println(playerSizeX+" "+playerSizeY);
			//System.out.println(drawnRow+" "+drawnCol);
		}
		g.fillOval(drawnCol, drawnRow, playerSizeX, playerSizeY); //consider updating to move slightly around in rooms, can't change col and row because need to be at center cell to move correctly
		g.setColor(Color.BLACK);
		g.drawOval(drawnCol, drawnRow, playerSizeX, playerSizeY);
		//(cellSizeX*col, cellSizeY*row, cellSizeX, cellSizeY);
	}


	
	/*
	 * equals: overriding equals to compare players to each other. The color row, column and name need to match. Used for testing purposes.
	 * 
	 * @Param target: The second object being compared
	 * */
	public boolean equals(Player target) { 
	
		if(this.name.equals(target.getName()) && this.color.equals(target.getColor()) &&this.row==target.getRow()&&this.col==target.getCol()) {
			return true;
		}
		return false;
				
	}
	
	/*
	 * disproveSuggestion: Checks if the player has a card in the solution and returns a random card that it has.
	 * 
	 * @Param suggestion: The suggested solution that is being compared to the players hand
	 * */
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> disprovableCards = new ArrayList<Card>();
		
		for( int i = 0; i < hand.length;i++) {
			
			if(suggestion.getPerson() == hand[i]) {
				disprovableCards.add(suggestion.getPerson());
			}
			if(suggestion.getWeapon() == hand[i]) {
				disprovableCards.add(suggestion.getWeapon());
			}
			if(suggestion.getRoom() == hand[i]) {
				disprovableCards.add(suggestion.getRoom());
			}
		}
		if(disprovableCards.size() == 1) {
			return disprovableCards.get(0);
		}else if(disprovableCards.size() > 1){
			int randCard;

			Random rand=new Random();
			
			randCard=rand.nextInt(disprovableCards.size());
			return disprovableCards.get(randCard);
		}
		
		
		return null;
	}

	
	/*
	 * selectTarget:Function to select the target that the player will move to. 
	 * Consider making this not abstract and overloading it for human player, would fix some problems
	 * 
	 * @Param targets: List of possible targets
	 * @Param roomMap: The room map to be able to prioritize moving into a room, 
	 * */
	public abstract BoardCell selectTarget(Set<BoardCell> targets, Map<Character,Room> roomMap);
	

	
	/*
	 * CreateSuggestion:Function to create the suggestion for the player
	 * Consider making this not abstract and overloading it for human player, would fix some problems
	 * 
	 * @Param cards: List of all cards
	 * @Param roomName: The room the player is currently in 
	 * */
	public abstract Solution createSuggestion(Set<Card> cards, String roomName);
	
	
	/*
	 * canMakeAccusation used to check if a computer player can make an accusation
	 * 
	 * @param: deckSize is needed to determine if the computer can make an accusation 
	 * */
	protected abstract boolean canMakeAccusation(int deckSize); 

	/*
	 * makeAccusation makes an accusation for a player, different between humans and computers so abstract
	 * 
	 * @param: deck, pass in the deck of cards so that the player can see all of the options or the computer can pick the right answer
	 * */
	protected abstract Solution makeAccusation(Set<Card> deck);
	
	
	//setters and getters
	public void updateSeenCards(Card card) {
		seenCards.add(card);
	}
	
	public void updateHand(Card card, int position) {
		this.hand[position]=card;
	}
	
	public void updateHand(Card card1, Card card2, Card card3) {
		this.hand[0] = card1;
		this.hand[1] = card2;
		this.hand[2] = card3;
	}

	

	public void setPosition(BoardCell newLocation) {
		this.row = newLocation.getRow();
		this.col = newLocation.getCol();
	}
	
	public void setMoved(boolean move) {
		moved=move;
	}
	

	public Color getColor() {
		return color;
	}


	public int getRow() {
		return row;
	}
	

	public int getCol() {
		return col;
	}
	
	public boolean isMoved() {
		return moved;
	}
	
	public Card[] getHand() {
		return hand;
	}
	
	public Set<Card> getSeenCards(){
		return seenCards;
	}
	
	
	public String getName() {
		return name;
	}

	public boolean isHuman() {
		return isHuman;
	}

	

	

	
	
}
