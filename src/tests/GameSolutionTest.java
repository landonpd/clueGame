package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSolutionTest {
	// We make the Board static because we can load it one time and 
		// then do all the tests. 
		private static Board board;
	
	@BeforeAll
	static void setUp() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	@Test
	void testSolution() {
		Card person = new Card("Crimson Captain",CardType.PERSON);
		Card weapon = new Card("Light Saber",CardType.WEAPON);
		Card room = new Card("Hangar",CardType.ROOM);
		Card wrongPerson = new Card("Mauve Major",CardType.PERSON);
		Card wrongWeapon =new Card("Portal Gun",CardType.WEAPON);
		Card wrongRoom = new Card("Engine Room",CardType.ROOM);
		
		Solution correctAccusation = new Solution(person,weapon,room);
		board.setSolution(correctAccusation);
		assertTrue(board.checkAccusation(correctAccusation));
		assertFalse(board.checkAccusation(new Solution(wrongPerson,weapon,room)));//wrong person
		assertFalse(board.checkAccusation(new Solution(person,wrongWeapon,room)));//wrong weapon
		assertFalse(board.checkAccusation(new Solution(person,weapon,wrongRoom)));//wrong room	
	}
	
	@Test
	void testPlayerSuggestionHandling() {
		Player testPlayer = new ComputerPlayer("Testy Technician","gray",8,14);
		Card person = new Card("Crimson Captain",CardType.PERSON);
		Card weapon = new Card("Light Saber",CardType.WEAPON);
		Card room = new Card("Hangar",CardType.ROOM);
		Card wrongPerson = new Card("Mauve Major",CardType.PERSON);
		Card wrongWeapon =new Card("Portal Gun",CardType.WEAPON);
		Card wrongRoom = new Card("Engine Room",CardType.ROOM);
		testPlayer.updateHand(person,weapon,room);
		
		Solution suggestion = new Solution(wrongPerson,wrongWeapon, wrongRoom);
		assertEquals(testPlayer.disproveSuggestion(suggestion),null);
		suggestion = new Solution(person,wrongWeapon, wrongRoom);
		assertEquals(testPlayer.disproveSuggestion(suggestion),person);
		suggestion = new Solution(wrongPerson,wrongWeapon, wrongRoom);
		assertEquals(testPlayer.disproveSuggestion(suggestion),null);
		suggestion = new Solution(wrongPerson,weapon, wrongRoom);
		assertEquals(testPlayer.disproveSuggestion(suggestion),weapon);
		suggestion = new Solution(wrongPerson,wrongWeapon, room);
		assertEquals(testPlayer.disproveSuggestion(suggestion),room);
		
		int aHappens = 0;
		int bHappens = 0;
		int cHappens = 0;
		Card output;
		suggestion = new Solution(person,weapon,room);
		for(int i = 0; i<100; i++) {
			output = testPlayer.disproveSuggestion(suggestion);
			if(output == person) {
				aHappens++;
			}
			if(output == weapon) {
				bHappens++;
			}
			if(output == room) {
				cHappens++;
			}
		}
		assertFalse(aHappens == 0 ||bHappens == 0 ||cHappens == 0 );
	}
	
	@Test
	void testHandleSuggestion() {
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new HumanPlayer("Testy Technician0","gray",8,14));
		testPlayers.add(new ComputerPlayer("Testy Technician1","gray",8,14));
		testPlayers.add(new ComputerPlayer("Testy Technician2","gray",8,14));
		//cards for test player 0
		Card crim = new Card("Crimson Captain",CardType.PERSON);//yes i know the names are stupid, it makes it more readable i hope
		Card lit = new Card("Light Saber",CardType.WEAPON);
		Card hungy = new Card("Hangar",CardType.ROOM);
		testPlayers.get(0).updateHand(crim, lit, hungy);
		//cards for test player 1
		Card pea = new Card("Peachy Private",CardType.PERSON);
		Card ray = new Card("Ray Gun",CardType.WEAPON);
		Card brij = new Card("Bridge",CardType.ROOM);
		testPlayers.get(1).updateHand(pea, ray, brij);
		//cards for test player 2
		Card lem = new Card("Lemon Lieutenant",CardType.PERSON);
		Card ost = new Card("Old Slug Thrower",CardType.WEAPON);
		Card rea = new Card("Reactor",CardType.ROOM);
		testPlayers.get(2).updateHand(lem, ost, rea);
		//cards no one has
		Card tea = new Card("Teal Technician",CardType.PERSON);
		Card bug = new Card("Assassin Bug",CardType.WEAPON);
		Card dorm = new Card("Dormitories",CardType.ROOM);
		
		
		
		board.setPlayers(testPlayers);
		Solution suggestion = new Solution(tea,bug,dorm);
		assertEquals(board.handleSuggestion(testPlayers.get(0),suggestion),null);// no disprove
		suggestion = new Solution(crim,lit,hungy);
		assertEquals(board.handleSuggestion(testPlayers.get(0),suggestion),null);// sugg disprove
		suggestion = new Solution(pea,bug,dorm);
		assertEquals(board.handleSuggestion(testPlayers.get(0),suggestion),pea);// 1 disprove
		suggestion = new Solution(lem,bug,dorm);
		assertEquals(board.handleSuggestion(testPlayers.get(0),suggestion),lem);// 2 disprove
		suggestion = new Solution(pea,ost,dorm);
		assertEquals(board.handleSuggestion(testPlayers.get(0),suggestion),pea);// both 1 and 2 disprove (1 returns)
		suggestion = new Solution(crim,ost,dorm);
		assertEquals(board.handleSuggestion(testPlayers.get(1),suggestion),ost);// both 0 and 2 disprove (2 returns)
		suggestion = new Solution(crim,ost,dorm);
		assertEquals(board.handleSuggestion(testPlayers.get(2),suggestion),crim);// 0 disprove
	}
}
