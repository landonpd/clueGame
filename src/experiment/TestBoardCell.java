/**
 * TestBoardCell: testing code for boardCell class
 * Authors: Landon Dixon and Charles Hulongbayan
 */

package experiment;

import java.util.HashSet;
import java.util.Set;


/*
 * TestBoardCell is the test version of the eventual class Cell.
 * The Cell class will eventually hold information about the status of board cells.
 * 
 * */
public class TestBoardCell {
	
	private int row, col;
	private Boolean isRoom, isOccupied ;
	Set<TestBoardCell> adjList;
	
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		adjList = new HashSet<TestBoardCell>();
		isRoom = isOccupied  = false;
	}
	
	/*
	 * addAdjecency: adds a cell to this cells adjacency list
	 * Parameters: cell is the cell to be added
	 * 
	 * */
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	//setters and getters
	public Set<TestBoardCell> getAdjList(){
		return adjList;
		
	}
	
	public boolean isRoom() {
		return isRoom;
	}

	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public boolean getOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
	@Override
	public String toString() {
		return "TestBoardCell [row=" + row + ", col=" + col + ", isRoom=" + isRoom + ", isOccupied=" + isOccupied + "]";
	}

}
