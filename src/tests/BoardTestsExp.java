/**
 * TestBoardCell: testing code for boardCell class
 * Authors: Landon Dixon and Charles Hulongbayan
 */

package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

//import org.junit.Before; wrong library to import
import org.junit.jupiter.api.BeforeEach; //correct library for beforeEach
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;


/*
 * BoardTestExp tests the creation of a cells adjacency list and tests the calculation of the target list.
 * 
 * */
class BoardTestsExp {
	TestBoard board;
	
	@BeforeEach // BeforeEach doesn't work? Fixed, importing the wrong thing
	void setUpBeforeEach() throws Exception {
		board = new TestBoard();
		
	}
	
	/*
	 * testAdjList00 tests creating adjlst from the top left corner
	 * 
	 */

	@Test
	void testAdjList00(){//top left
		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> adjList = cell.getAdjList();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();//corrList: correct list
		//add correct adjacent locations to corrList 
		corrList.add(board.getCell(0,1));
		corrList.add(board.getCell(1,0));
		assertEquals(adjList, corrList);//should end up as exactly the same, unless comparisons don't work on sets.
	}
	
	/*
	 * testAdjList33 tests creating adjlst from the bottom right corner
	 * 
	 */
	
	@Test
	void testAdjList33(){//bottom right
		TestBoardCell cell = board.getCell(3,3);
		Set<TestBoardCell> adjList = cell.getAdjList();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();
		corrList.add(board.getCell(3,2));
		corrList.add(board.getCell(2,3));
		assertEquals(adjList, corrList);
	}
	
	/*
	 * testAdjList13 tests creating adjlst from the right edge of the board
	 * 
	 */
	
	@Test
	void testAdjList13(){//right edge
		TestBoardCell cell = board.getCell(1,3);
		Set<TestBoardCell> adjList = cell.getAdjList();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();
		corrList.add(board.getCell(0,3));
		corrList.add(board.getCell(2,3));
		corrList.add(board.getCell(1,2));
		assertEquals(adjList, corrList);
	}
	
	/*
	 * testAdjList30 tests creating adjlst from the left edge of the board
	 */
	
	@Test
	void testAdjList30(){//left edge
		TestBoardCell cell = board.getCell(3,0);
		Set<TestBoardCell> adjList = cell.getAdjList();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();
		corrList.add(board.getCell(2,0));
		corrList.add(board.getCell(3,1));
		assertEquals(adjList, corrList);
	}
	
	/*
	 * testAdjList22 tests creating adjlst from the middle of the board
	 * 
	 */
	
	@Test
	void testAdjList22(){//middle
		TestBoardCell cell = board.getCell(2,2);
		Set<TestBoardCell> adjList = cell.getAdjList();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();
		corrList.add(board.getCell(1,2));
		corrList.add(board.getCell(3,2));
		corrList.add(board.getCell(2,1));
		corrList.add(board.getCell(2,3));
		assertEquals(adjList, corrList);
	}
	
	/*
	 * testTargetsEmpty003 tests making the targlst for an empty board, starting in the top left corner with 3 steps.
	 * 
	 */	
	@Test
	void testTargetsEmpty003(){//empty board from (0,0) 3 steps
		TestBoardCell cell = board.getCell(0,0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targList = board.getTargets();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();//corrList: correct list
		corrList.add(board.getCell(3,0));//missed
		corrList.add(board.getCell(2,1));
		corrList.add(board.getCell(0,1));//missed
		corrList.add(board.getCell(1,2));
		corrList.add(board.getCell(0,3));
		corrList.add(board.getCell(1,0));
		assertEquals(targList, corrList);
	}
	
	/*
	 * testTargetsEmpty236 tests making the targlst for an empty board, starting from (2,3) with 6 steps.
	 * 
	 */	
	@Test
	void testTargetsEmpty236(){//fail
		TestBoardCell cell = board.getCell(2,3);
		board.calcTargets(cell, 6);
		Set<TestBoardCell> targList = board.getTargets();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();//corrList: correct list
		corrList.add(board.getCell(3,0));//got it
		corrList.add(board.getCell(2,1));//got it
		corrList.add(board.getCell(0,1));//got it
		corrList.add(board.getCell(1,2));//got it
		corrList.add(board.getCell(0,3));//got it
		corrList.add(board.getCell(1,0));//got it
		corrList.add(board.getCell(3,2));//got it
		//also got (2,3) (starting position)
		assertEquals(targList, corrList);
	}
	
	
	/*
	 * testTargetsOccupied334 tests making the targlst for a board with occupied spaces starting from (3,3) with 4 steps.
	 * 
	 */
	@Test
	void testTargetsOccupied334(){//fail
		//occupied cells
		board.getCell(0, 3).setOccupied(true);
		board.getCell(1, 0).setOccupied(true);
		TestBoardCell cell = board.getCell(3,3);
		board.calcTargets(cell, 4);
		Set<TestBoardCell> targList = board.getTargets();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();//corrList: correct list
		//TODO:i don't want to figure out the movement for this so this list is wrong. Done
		corrList.add(board.getCell(2,0));//got it
		corrList.add(board.getCell(1,1));//got it
		corrList.add(board.getCell(3,1));//got it
		corrList.add(board.getCell(0,2));//got it
		corrList.add(board.getCell(2,2));//got it
		corrList.add(board.getCell(1,3));//got it
		//got (3,3), (starting position)
		assertEquals(targList, corrList);
	}
	
	/*
	 * testTargetsEmpty236 tests making the targlst for board with rooms, starting from (2,1) with 5 steps.
	 * 
	 */
	
	@Test
	void testTargetsRoom215(){//fail
		//rooms
		board.getCell(0, 3).setRoom(true);
		board.getCell(1, 0).setRoom(true);
		TestBoardCell cell = board.getCell(2,1);
		board.calcTargets(cell, 5);
		Set<TestBoardCell> targList = board.getTargets();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();//corrList: correct list
		//TODO:i don't want to figure out the movement for this so this list is wrong. Done
		corrList.add(board.getCell(0,0));//got it
		corrList.add(board.getCell(1,0));//didn't get this
		corrList.add(board.getCell(1,1));//got it
		corrList.add(board.getCell(3,1));//got it
		corrList.add(board.getCell(0,2));//got it
		corrList.add(board.getCell(2,2));//got it
		corrList.add(board.getCell(0,3));//didn't get this
		corrList.add(board.getCell(1,3));//got it
		corrList.add(board.getCell(3,3));//got it
		corrList.add(board.getCell(2,0));
		//got (2,0)
		assertEquals(targList, corrList);
	}
	
	/*
	 * testTargetsEmpty236 tests making the targlst for a board with occupied cells and rooms,
	 * starting from (0,3) with 3 steps.
	 * 
	 */
	
	@Test
	void testTargetsMixed033(){//fail
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setRoom(true);
		TestBoardCell cell = board.getCell(0,3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targList = board.getTargets();
		Set<TestBoardCell> corrList = new HashSet<TestBoardCell>();//corrList: correct list
		corrList.add(board.getCell(1,2));//didn't get this
		corrList.add(board.getCell(2,2));//got it
		corrList.add(board.getCell(3,3));//got it
		//got (1,3)
		assertEquals(targList, corrList);
	}
	
}
