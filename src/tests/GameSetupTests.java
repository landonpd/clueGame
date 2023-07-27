package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;


class GameSetupTests {
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
	void LoadingPeople() throws BadConfigFormatException{//tests loading of people, and that players are properly loaded as human and computer
		ArrayList<Player> correctPlayers=new ArrayList<Player>();
		ArrayList<Player> foundPlayers = board.getPlayers();
		Player first=new HumanPlayer("Crimson Captain","Red",3,0);
		correctPlayers.add(first);
		Player second=new ComputerPlayer("Peachy Private","Orange",8,14);
		correctPlayers.add(second);
		Player third=new ComputerPlayer("Lemon Lieutenant", "Yellow",15,1);
		correctPlayers.add(third);
		Player fourth=new ComputerPlayer("Teal Technician", "Blue", 17, 13);
		correctPlayers.add(fourth);
		Player fifth=new ComputerPlayer("Sage Sergeant", "Green", 15, 28);
		correctPlayers.add(fifth);
		Player sixth=new ComputerPlayer("Mauve Major", "Purple", 10, 31);
		correctPlayers.add(sixth);
		assertTrue(correctPlayers.get(0).equals(foundPlayers.get(0)));
		assertTrue(correctPlayers.get(1).equals(foundPlayers.get(1)));
		assertTrue(correctPlayers.get(2).equals(foundPlayers.get(2)));
		assertTrue(correctPlayers.get(3).equals(foundPlayers.get(3)));
		assertTrue(correctPlayers.get(4).equals(foundPlayers.get(4)));
		assertTrue(correctPlayers.get(5).equals(foundPlayers.get(5)));
	}
	
	@Test
	void LoadingCard() throws BadConfigFormatException {//tests loading of card deck
		
		Set<Card> correctCards = new HashSet<Card>();
		Set<Card> foundCards = board.getCards();
		
		correctCards.add(new Card("Light Saber",CardType.WEAPON));
		correctCards.add(new Card("Ray Gun",CardType.WEAPON));
		correctCards.add(new Card("Old Slug Thrower",CardType.WEAPON));
		correctCards.add(new Card("Assassin Bug",CardType.WEAPON));
		correctCards.add(new Card("Vent Cover",CardType.WEAPON));
		correctCards.add(new Card("Portal Gun",CardType.WEAPON));
		
		correctCards.add(new Card("Hangar",CardType.ROOM));
		correctCards.add(new Card("Bridge",CardType.ROOM));
		correctCards.add(new Card("Reactor",CardType.ROOM));
		correctCards.add(new Card("Dormitories",CardType.ROOM));
		correctCards.add(new Card("Engine Room",CardType.ROOM));
		correctCards.add(new Card("Turret",CardType.ROOM));
		correctCards.add(new Card("Storage",CardType.ROOM));
		correctCards.add(new Card("Cafeteria",CardType.ROOM));
		correctCards.add(new Card("Medbay",CardType.ROOM));
		
		correctCards.add(new Card("Crimson Captain",CardType.PERSON));
		correctCards.add(new Card("Peachy Private",CardType.PERSON));
		correctCards.add(new Card("Lemon Lieutenant",CardType.PERSON));
		correctCards.add(new Card("Teal Technician",CardType.PERSON));
		correctCards.add(new Card("Sage Sergeant",CardType.PERSON));
		correctCards.add(new Card("Mauve Major",CardType.PERSON));
		
		assertTrue(correctCards.size()==foundCards.size());
		assertTrue(correctCards.containsAll(foundCards));
		assertTrue(foundCards.containsAll(correctCards));
		

	}
	
	//Tests if the solution is a weapon person and room and checks it is size 3.
	@Test
	void checkingSolution()throws BadConfigFormatException {
		board.deal();
		Solution generatedSolution=board.getSolution();
		assertTrue(generatedSolution.getPerson().getCardType()==CardType.PERSON);
		assertTrue(generatedSolution.getWeapon().getCardType()==CardType.WEAPON);
		assertTrue(generatedSolution.getRoom().getCardType()==CardType.ROOM);	
	}
	
	//Tests that cards are delt correctly
	@Test
	void cardsDeltToPlayers()throws BadConfigFormatException {
		board.deal();
		int numOfCards=3;
		Set<Card> deck=new HashSet<Card>();
		ArrayList<Player> playerLst=board.getPlayers();
		for(int i=0;i<playerLst.size();i++) {
			assertEquals(numOfCards,playerLst.get(i).getHand().length);
			for(int j=0;j<numOfCards;j++) {
				assertTrue(deck.add(playerLst.get(i).getHand()[j]));
			}
		}	
	}
}
