package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest { //like given one right now
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the study that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(1, 8);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(3, 7)));
		assertTrue(testList.contains(board.getCell(7, 29)));//changed from 6 to 7, typing error
		
		// now test the storage adjacencys
		testList = board.getAdjList(16, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(13, 5)));
		assertTrue(testList.contains(board.getCell(13, 10)));
		assertTrue(testList.contains(board.getCell(3, 22)));
		
		// one more room, cafeteria
		testList = board.getAdjList(13, 29);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(12, 26)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(5, 26);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(6, 26)));//changed from 4 to 6, type
		assertTrue(testList.contains(board.getCell(5, 25)));//changed from 27 to 25, typo
		assertTrue(testList.contains(board.getCell(7, 29)));

		testList = board.getAdjList(6, 3);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(9, 1)));
		assertTrue(testList.contains(board.getCell(7, 3)));
		assertTrue(testList.contains(board.getCell(6, 4)));
		assertTrue(testList.contains(board.getCell(5, 3)));
		
		testList = board.getAdjList(17, 15);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(17, 20)));
		assertTrue(testList.contains(board.getCell(16, 15)));
		
	}
	
	// Test a variety of walkway scenarios
	// These tests are RED on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test in middle of big hallway
		Set<BoardCell> testList = board.getAdjList(5, 14);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 14)));
		assertTrue(testList.contains(board.getCell(4, 14)));
		assertTrue(testList.contains(board.getCell(5, 15)));
		assertTrue(testList.contains(board.getCell(5, 13)));
		
		// Test next to a room without door 
		testList = board.getAdjList(14, 19);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(14, 18)));
		assertTrue(testList.contains(board.getCell(14, 20)));
		assertTrue(testList.contains(board.getCell(13, 19)));

		// Test close to weird dead end
		testList = board.getAdjList(15, 13);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(14, 13)));
		assertTrue(testList.contains(board.getCell(16, 13)));
		

		// Test weird dead end
		testList = board.getAdjList(3,0);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(3, 1)));
		
	
	}
	
	
	// Tests out of walkway near weird dead end, 1, 3 and 4
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(3, 1), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(3, 2)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(3, 1), 3);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(4, 3)));
		assertTrue(targets.contains(board.getCell(3, 4)));	
			
		
		// test a roll of 4
		board.calcTargets(board.getCell(3, 1), 4);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(3,5)));
		assertTrue(targets.contains(board.getCell(4, 4)));	
		assertTrue(targets.contains(board.getCell(5, 3)));
	}
	
	// Tests out of medbay door, 1, 3 and 4
	// These are PINK on the planning spreadsheet
	
	@Test
	public void testTargetsMedbayDoor() {
		// test a roll of 1
		board.calcTargets(board.getCell(12, 21), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(9, 21)));
		assertTrue(targets.contains(board.getCell(12, 20)));	
		assertTrue(targets.contains(board.getCell(13, 21)));
		assertTrue(targets.contains(board.getCell(12, 22)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(12, 21), 3);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(9, 21)));
		assertTrue(targets.contains(board.getCell(11, 19)));	
		assertTrue(targets.contains(board.getCell(12, 24)));
		assertTrue(targets.contains(board.getCell(14, 22)));	
		
		
		// test a roll of 4
		board.calcTargets(board.getCell(12, 21), 4);
		targets= board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(9, 21)));
		assertTrue(targets.contains(board.getCell(14, 23)));	
		assertTrue(targets.contains(board.getCell(12, 17)));
		assertTrue(targets.contains(board.getCell(13, 22)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsFromDormitory() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(7, 29), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(8, 26)));
		assertTrue(targets.contains(board.getCell(5, 26)));	
		assertTrue(targets.contains(board.getCell(1, 8)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(7, 29), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(1, 8)));	
		assertTrue(targets.contains(board.getCell(6, 25)));
		assertTrue(targets.contains(board.getCell(8, 24)));	
		assertTrue(targets.contains(board.getCell(10, 26)));
	
		
		// test a roll of 4
		board.calcTargets(board.getCell(7, 29), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(1, 8)));
		assertTrue(targets.contains(board.getCell(5, 26)));
		assertTrue(targets.contains(board.getCell(7, 25)));	
		assertTrue(targets.contains(board.getCell(9, 24)));
	}
	
	// Tests out of walkway kinda toward the middle, 1, 3 and 4
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(11, 10), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(11, 11)));
		assertTrue(targets.contains(board.getCell(11, 9)));	
		assertTrue(targets.contains(board.getCell(12, 10)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(11, 10), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());//getting 9
		assertTrue(targets.contains(board.getCell(16, 7)));
		assertTrue(targets.contains(board.getCell(11, 7)));
		assertTrue(targets.contains(board.getCell(11, 11)));	
		assertTrue(targets.contains(board.getCell(11, 13)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(11, 10), 4);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(16, 7)));
		assertTrue(targets.contains(board.getCell(9, 7)));
		assertTrue(targets.contains(board.getCell(12, 13)));
		assertTrue(targets.contains(board.getCell(11, 8)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test that occupied cells block movement
		board.getCell(5, 11).setOccupied(true);
		board.calcTargets(board.getCell(5, 9), 3);
		board.getCell(5, 11).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(10, targets.size()); //changed from 9 to 10, miss counted when making test.
		assertTrue(targets.contains(board.getCell(6, 7)));
		assertTrue(targets.contains(board.getCell(4, 9)));
		assertTrue(targets.contains(board.getCell(4, 11)));	
		assertFalse( targets.contains( board.getCell(5, 12))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied, also blocked door in
		board.getCell(6, 23).setOccupied(true);
		board.getCell(3, 22).setOccupied(true);
		board.calcTargets(board.getCell(6, 20), 4);
		board.getCell(6, 23).setOccupied(false);
		board.getCell(3, 22).setOccupied(false);
		targets= board.getTargets();
		assertEquals(7, targets.size());//Changed from 6 to 7, miss counted when making test.
		assertTrue(targets.contains(board.getCell(3, 22)));	
		assertTrue(targets.contains(board.getCell(6, 16)));	
		assertTrue(targets.contains(board.getCell(5, 17)));
		assertFalse( targets.contains( board.getCell(6, 24))) ;
		
		// check leaving a room with a blocked doorway
		board.getCell(11, 7).setOccupied(true);
		board.calcTargets(board.getCell(9, 7), 2);
		board.getCell(11, 7).setOccupied(false);//replaced 1 with 11, another typo
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(9, 1)));
		assertTrue(targets.contains(board.getCell(6, 6)));	
		assertTrue(targets.contains(board.getCell(6, 8)));
		assertFalse( targets.contains( board.getCell(12, 7))) ;
		

	}
}
