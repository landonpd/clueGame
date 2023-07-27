package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Board;

class FileInitTest {


	static final int ROWS=19;//18 + 0th row
	static final int COLS=33;//32 + 0th row

	private static Board board;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		board=Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files

		board.initialize();
	}

	//run 
	//ensure your layout and setup files are loaded correctly (correct number of rooms, test several entries including first and last in file)
	//@Test
	public void filesLoading(){
		assertEquals("Hangar", board.getRoom('H').getName() );
		assertEquals("Medbay", board.getRoom('M').getName() );
		assertEquals("Reactor", board.getRoom('R').getName() );
		assertEquals("Dormitories", board.getRoom('D').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() ); //don't know why this is in init306, walkway isn't a room? is everything going to be a room?
	}

	//ensure the correct number of rows/columns have been read
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(ROWS, board.getNumRows());
		assertEquals(COLS, board.getNumColumns());
	}

	//verify at least one doorway in each direction. Also verify cells that don't contain doorways return false for isDoorway().
	@Test
	public void doorwayDirections(){
		BoardCell cell = board.getCell(6, 3);//top engine room
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(11, 7);//bottom door to reactor
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(4, 20);//upper turret door
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(14, 23);//left door to hangar
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(9,3); //random walkway
		assertFalse(cell.isDoorway());
	}

	//check that the correct number of doors have been loaded.
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(17, numDoors);
	}

	//check some of the cells to ensure they have the correct initial
	@Test
	public void testCellsInitial(){
		assertEquals(board.getCell(6,13).getInitial(),'W');
		assertEquals(board.getCell(7,13).getInitial(),'X');
		assertEquals(board.getCell(0,0).getInitial(),'X');
		assertEquals(board.getCell(18,32).getInitial(),'X');
		assertEquals(board.getCell(8,7).getInitial(),'R');
		assertEquals(board.getCell(14,0).getInitial(),'E');
		assertEquals(board.getCell(10,20).getInitial(),'M');
		assertEquals(board.getCell(8,28).getInitial(),'D');
		assertEquals(board.getCell(13,29).getInitial(),'C');
		assertEquals(board.getCell(11,32).getInitial(),'X');
	}
	//check that rooms have the proper center cell and label cell.
	@Test
	public void testRoomsLabel(){
		assertEquals(board.getRoom('B').getLabelCell(),board.getCell(1,6));
		assertEquals(board.getRoom('C').getLabelCell(),board.getCell(12,29));
		assertEquals(board.getRoom('D').getLabelCell(),board.getCell(6,29));
		assertEquals(board.getRoom('E').getLabelCell(),board.getCell(4,0));
		assertEquals(board.getRoom('H').getLabelCell(),board.getCell(16,20));
		assertEquals(board.getRoom('M').getLabelCell(),board.getCell(8,21));
		assertEquals(board.getRoom('R').getLabelCell(),board.getCell(8,7));
		assertEquals(board.getRoom('S').getLabelCell(),board.getCell(15,7));
		assertEquals(board.getRoom('T').getLabelCell(),board.getCell(2,22));
	}
	@Test
	public void testRoomsCenter(){
		assertEquals(board.getRoom('B').getCenterCell(),board.getCell(1,8));
		assertEquals(board.getRoom('C').getCenterCell(),board.getCell(13,29));
		assertEquals(board.getRoom('D').getCenterCell(),board.getCell(7,29));
		assertEquals(board.getRoom('E').getCenterCell(),board.getCell(9,1));
		assertEquals(board.getRoom('H').getCenterCell(),board.getCell(17,20));
		assertEquals(board.getRoom('M').getCenterCell(),board.getCell(9,21));
		assertEquals(board.getRoom('R').getCenterCell(),board.getCell(9,7));
		assertEquals(board.getRoom('S').getCenterCell(),board.getCell(16,7));
		assertEquals(board.getRoom('T').getCenterCell(),board.getCell(3,22));

	}

}
