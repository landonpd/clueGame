/**
 * BoardCell: class holds information about the status of board cells.
 * Authors: Landon Dixon and Charles Hulongbayan
 * Date Started: Feb. 23, 2023
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
//import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/*
 * BoardCell: class holds information about the status of a board cells.
 * 
 * */
public class BoardCell {

	private int row, col;
	private Boolean isRoom, isOccupied, roomLabel, roomCenter ;
	private char initial, secretPassage;
	private DoorDirection doorDirection;
	Set<BoardCell> adjList;
	private Color color;

	
	public BoardCell(int row, int col, char initial) {

		this.row = row;
		this.col = col;
		this.initial = initial;
		secretPassage = '\0';
		adjList = new HashSet<BoardCell>();
		isOccupied =roomLabel = roomCenter = isRoom= false;
		this.doorDirection = DoorDirection.NONE;
		color= Color.WHITE;
	}

	

	/*
	 * addAdjecency: adds a cell to this cells adjacency list
	 * 
	 * @Param cell is the cell to be added to the adjacency list
	 * 
	 * */
	public void addAdjacency(BoardCell cell) {

		adjList.add(cell);
	}
	
	/*
	 * clickedOn: returns true if the given x y coordinates are within the borders of the cell
	 * 
	 * @Param x: mouses x position
	 * @Param y: mouses y position
	 * @Param cellSizeX: the width of the cell
	 * @Param cellSizeY: the height of the cell
	 * 
	 * */
	public boolean clickedOn(int x,int y,int cellSizeX,int cellSizeY) {
		if (x>col*cellSizeX&&x<(col+1)*cellSizeX&& y>row*cellSizeY && y<(row+1)*cellSizeY) { //terrible for getting room, need same for loop as earlier in 
			return true;
		}
		return false;
	}
	
	
	/*
	 * drawCell: Draws the cell by filling its area with it color
	 * 
	 * @Param cellSizeX: the width of the cell
	 * @Param cellSizeY: the height of the cell
	 * @Param g: Used to allow the function to actually draw
	 * 
	 * */
	public void drawCell(int cellSizeX,int cellSizeY,Graphics g) {
		
		g.setColor(color);
		g.fillRect(cellSizeX*col, cellSizeY*row, cellSizeX, cellSizeY);
	}
	
	/*
	 * drawBorder: Draws the border of cells that are not rooms. This is separate so that when a room is drawn it doesn't 
	 * cover another cells outline.
	 * 
	 * @Param cellSizeX: the width of the cell
	 * @Param cellSizeY: the height of the cell
	 * @Param g: Used to allow the function to actually draw
	 * 
	 * */
	public void drawBorder(int cellSizeX,int cellSizeY,Graphics g) {
		if(!isRoom) {
			g.setColor(Color.BLACK);
			g.drawRect(cellSizeX*col, cellSizeY*row, cellSizeX, cellSizeY);
		}
	}
	
	/*
	 * drawDoor: Draws the door with a blue rectangle in the right spot based on door direction
	 * 
	 * @Param cellSizeX: the width of the cell
	 * @Param cellSizeY: the height of the cell
	 * @Param g: Used to allow the function to actually draw
	 * 
	 * */
	public void drawDoor(int cellSizeX,int cellSizeY,Graphics g) {
		if(doorDirection!=DoorDirection.NONE) {
			if(doorDirection==DoorDirection.UP) {
				g.setColor(Color.BLUE);
				g.fillRect(cellSizeX*col, cellSizeY*row-cellSizeY/5, cellSizeX, cellSizeY/5);//can replace 5 with a doorThickness in refactor
			}
			if(doorDirection==DoorDirection.DOWN) {
				g.setColor(Color.BLUE);
				g.fillRect(cellSizeX*col, cellSizeY*(row+1), cellSizeX, cellSizeY/5);
			}
			if(doorDirection==DoorDirection.LEFT) {
				g.setColor(Color.BLUE);
				g.fillRect(cellSizeX*col-cellSizeX/5, cellSizeY*row, cellSizeX/5, cellSizeY);
			}
			if(doorDirection==DoorDirection.RIGHT) {
				g.setColor(Color.BLUE);
				g.fillRect(cellSizeX*(col+1), cellSizeY*row, cellSizeX/5, cellSizeY);
			}
		}
		
	}

	
	/*
	 * drawLabel: Draws the label of a room for room labels
	 * 	 
	 * @Param cellSizeX: the width of the cell
	 * @Param cellSizeY: the height of the cell
	 * @Param g: Used to allow the function to actually draw
	 * @Param roomMap: gets the room map from board to get the name of the room
	 * 
	 * */
	public void drawLabel(int cellSizeX,int cellSizeY,Graphics g,Map<Character,Room> roomMap) {
		if(roomLabel) {
			char[] label=roomMap.get(initial).getName().toCharArray();
			int labelSize=roomMap.get(initial).getName().length();
			g.setColor(Color.BLACK);
			g.drawChars(label,0, labelSize, cellSizeX*col,cellSizeY*(row+1)); 
		}else if(secretPassage!='\0') {
			String labelStr="To \n "+roomMap.get(secretPassage).getName();//can't include \n? maybe something with toCharArray
			char[] labelChar=labelStr.toCharArray();
			int labelSize=labelStr.length();
			g.setColor(Color.BLACK);
			g.drawChars(labelChar,0, labelSize, cellSizeX*col,cellSizeY*(row+1));
		}
	}
	
	
	/*
	 * equals: overriding equals to compare cells to each other. The row,column and initial have to match
	 * 
	 * @Param target: The second object being compared
	 * */
	@Override
	public boolean equals(Object target) {
		if (target == this) {
            return true;
       }
       if (target == null) {
          return false;
       }
       if (target instanceof BoardCell) {
            BoardCell arg = (BoardCell) target;
            if (arg.row==this.row&&arg.col==this.col&&arg.initial==this.initial) {
                return true;
            }
        }
       return false;
		
	}

	
	//setters and getters
	public void setRoom(boolean isRoom) {

		this.isRoom = isRoom;
	}

	public void setSecretPassage(char secretPassage) {

		this.secretPassage = secretPassage;
	}

	public void setDoorDirection(DoorDirection doorDirection) {

		this.doorDirection = doorDirection;
	}

	public void setRoomLabel(Boolean roomLabel) {

		this.roomLabel = roomLabel;
	}

	public void setRoomCenter(Boolean roomCenter) {

		this.roomCenter = roomCenter;
	}

	public void setOccupied(boolean occupied) {

		isOccupied = occupied;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isDoorway() {

		if(doorDirection != DoorDirection.NONE) {

			return true;
		}
		return false;
	}


	public boolean isLabel() {

		return roomLabel;
	}

	public boolean isRoomCenter() {

		return roomCenter;
	}
	
	public boolean isRoom() {

		return isRoom;
	}

	public DoorDirection getDoorDirection() {

		return doorDirection;
	}

	public char getSecretPassage() {

		return secretPassage;
	}

	public char getInitial() {

		return initial;
	}

	public Set<BoardCell> getAdjList(){

		return adjList;

	}
	
	public boolean getOccupied() {

		return isOccupied;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}



	public Color getColor() {
		return color;
	}



	
	
}
