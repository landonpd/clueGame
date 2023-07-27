/**
 * TestBoardCell: testing code for board class
 * Authors: Landon Dixon and Charles Hulongbayan
 */
package experiment;

import java.util.HashSet;
import java.util.Set;

/*
 * TestBoard class used to test the methods that the board will use.
 * Board will be used eventually to hold information about the game board
 * 
 * */

public class TestBoard {


	final static int COLS = 4;
	final static int ROWS = 4;
	private TestBoardCell[][] grid;

	private Set<TestBoardCell> visited;
	private Set<TestBoardCell> targets;




	public TestBoard() {
		//initialize grid with COLS ROWS, 
		grid=new TestBoardCell[4][4];
		visited=new HashSet<TestBoardCell>();
		targets=new HashSet<TestBoardCell>();
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i,j);
			}
		}
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {

				if(i > 0) {
					grid[i][j].addAdjacency(grid[i-1][j]);
				}
				if(i < ROWS - 1) {
					grid[i][j].addAdjacency(grid[i+1][j]);
				}
				if(j > 0) {
					grid[i][j].addAdjacency(grid[i][j-1]);
				}
				if(j <COLS - 1) {
					grid[i][j].addAdjacency(grid[i][j+1]);
				}
			}
		}
	}





	/*
	 * calcTargets: calculates where a player can get to from a certain starting position with a certain path length.
	 * parameters: startCell tells the board where to start calculating form, pathLength tells how far the player can go
	 * 
	 * 
	 * */
	public void calcTargets(TestBoardCell startCell, int pathLength) {//sometimes add startCell to targets, never add a room, get weird extra positions when there is a room

		if(pathLength==0) {
			if(!targets.contains(startCell)){
				System.out.println(startCell);
			}
			targets.add(startCell);
			
			return;

		}
		visited.add(startCell);

		//boolean visitedBool=false;
		Set<TestBoardCell> adjLst=startCell.getAdjList();

		for(TestBoardCell adjCell : adjLst) {
			if(!visited.contains(adjCell)) {
				visited.add(adjCell);
				if (adjCell.isRoom()) {
					targets.add(adjCell);
				}
				else if(!adjCell.getOccupied()) {
					calcTargets(adjCell,pathLength-1);
					visited.remove(adjCell);
				}
			}
		}

	}



	//getters and setters

	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];

	}

	public Set<TestBoardCell> getTargets(){
		return targets;

	}

}
