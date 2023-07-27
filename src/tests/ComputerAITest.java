package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.*;

//import java.util.Map;
//import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class ComputerAITest {
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
	void testComputerTargetSelectRoom() {
		board.calcTargets(board.getCell(13, 18), 3);//Can get to hangar, so should always go into hangar
		ComputerPlayer testPlayer=new ComputerPlayer("Test","Test",13,18);
		BoardCell hangar=new BoardCell(17,20,'H'); //room to get to 
		BoardCell selectedTarget= testPlayer.selectTarget(board.getTargets(),board.getRoomMap());
		assertTrue(hangar.equals(selectedTarget)); 


		//maybe add one where there are multiple rooms not in seen list, make sure the room is randomly selected
		int medbayCount=0,hangarCount=0,total=100000;
		board.calcTargets(board.getCell(13, 18), 5);
		BoardCell medbay=new BoardCell(9,21,'M');

		for(int i=0;i<total;i++) {
			selectedTarget= testPlayer.selectTarget(board.getTargets(),board.getRoomMap());
			if(selectedTarget.equals(medbay)) {

				medbayCount++;
			}else if(selectedTarget.equals(hangar)) {
				//System.out.println("hi");
				hangarCount++;
			}
		}
		assertTrue(hangarCount>0&&medbayCount>0);

		//now should only go into hangar some of the time instead of each time.
		testPlayer.updateSeenCards(new Card("Hangar", CardType.ROOM)); 
		board.calcTargets(board.getCell(14, 17), 1);
		int count1=0,count2=0,count3=0,count4=0;
		BoardCell expected1=new BoardCell(17,20,'H'); //spaces player can get to 
		BoardCell expected2=new BoardCell(14,16,'W');
		BoardCell expected3=new BoardCell(14,18,'W');
		BoardCell expected4=new BoardCell(13,17,'W');
		for(int i=0;i<total;i++) {
			selectedTarget= testPlayer.selectTarget(board.getTargets(),board.getRoomMap());
			if(selectedTarget.equals(expected1)) {
				count1++;
			}else if(selectedTarget.equals(expected2)) {
				count2++;
			}else if(selectedTarget.equals(expected3)) {
				count3++;
			}else if(selectedTarget.equals(expected4)) {
				count4++;
			}

		}
		//System.out.println(count1+" "+count2+" "+count3+" "+count4);//+" "+missCount);
		assertTrue(count1>0&&count2>0&&count3>0&&count4>0);
	}

	@Test
	void testComputerTargetSelectNoRooms() {
		int count1=0,count2=0,count3=0,count4=0,total=100000;
		board.calcTargets(board.getCell(12, 14), 1);
		ComputerPlayer testPlayer=new ComputerPlayer("Test","Test",12,14);
		BoardCell expected1=new BoardCell(12,13,'W'); //spaces player can get to 
		BoardCell expected2=new BoardCell(13,14,'W');
		BoardCell expected3=new BoardCell(12,15,'W');
		BoardCell expected4=new BoardCell(11,14,'W');
		BoardCell selectedTarget;
		//testing to check randomness
		for(int i=0;i<total;i++) {
			selectedTarget= testPlayer.selectTarget(board.getTargets(),board.getRoomMap());
			if(selectedTarget.equals(expected1)) {
				count1++;
			}else if(selectedTarget.equals(expected2)) {
				count2++;
			}else if(selectedTarget.equals(expected3)) {
				count3++;
			}else if(selectedTarget.equals(expected4)) {
				count4++;
			}
		}
		assertTrue(count1>0&&count2>0&&count3>0&&count4>0);

	}

	@Test
	void testMakeSuggestion() {
		Solution correctSuggestion=new Solution();
		correctSuggestion.setPerson(new Card("Mauve Major",CardType.PERSON));
		correctSuggestion.setWeapon(new Card("Portal Gun",CardType.WEAPON));
		correctSuggestion.setRoom(new Card("Hangar",CardType.ROOM));
		ComputerPlayer testPlayer=new ComputerPlayer("Test","Test",17,20); //set current location to be a room
		//not updating hand, all hand cards will be in seen cards as well so don't need to update that one
		//weapons
		testPlayer.updateSeenCards(new Card("Light Saber",CardType.WEAPON)); //minimum, these would be what is in the hand, all weapons		
		testPlayer.updateSeenCards(new Card("Ray Gun",CardType.WEAPON));
		testPlayer.updateSeenCards(new Card("Old Slug Thrower",CardType.WEAPON));
		testPlayer.updateSeenCards(new Card("Assassin Bug",CardType.WEAPON));
		
		//players
		testPlayer.updateSeenCards(new Card("Crimson Captain",CardType.PERSON));
		testPlayer.updateSeenCards(new Card("Peachy Private",CardType.PERSON));
		testPlayer.updateSeenCards(new Card("Lemon Lieutenant",CardType.PERSON));
		testPlayer.updateSeenCards(new Card("Teal Technician",CardType.PERSON));
		
		Solution generatedSuggestion=testPlayer.createSuggestion(board.getCards(),"Hangar");
		
		//Room matches current location
		assertEquals(board.getRoomMap().get(generatedSuggestion.getRoom().getCardName().charAt(0)).getCenterCell().getCol(),testPlayer.getCol());
		assertEquals(board.getRoomMap().get(generatedSuggestion.getRoom().getCardName().charAt(0)).getCenterCell().getRow(),testPlayer.getRow());
		
		//If multiple weapons not seen, one of them is randomly selected
		//If multiple persons not seen, one of them is randomly selected
		//for loop again, pick between two people and two weapons
		int total=1000,majorCount=0,portalCount=0,sageCount=0,ventCount=0;//,missCount=0;
		
		for(int i=0;i<total;i++) {
			generatedSuggestion=testPlayer.createSuggestion(board.getCards(),"Hangar");
			if(generatedSuggestion.getPerson().equals(correctSuggestion.getPerson())) {
				majorCount++;
			}else if(generatedSuggestion.getPerson().equals(new Card("Sage Sergeant",CardType.PERSON))) {
				sageCount++;
				
			}
			if(generatedSuggestion.getWeapon().equals(correctSuggestion.getWeapon())) {
				portalCount++;
			}else if(generatedSuggestion.getWeapon().equals(new Card("Vent Cover",CardType.WEAPON))) {
				ventCount++;
			}
			
			
		}
		//System.out.println(majorCount+" "+portalCount+" "+sageCount+" "+ventCount);//+" "+missCount);
		assertTrue(majorCount>0&&portalCount>0&&sageCount>0&&ventCount>0);
		//If only one weapon not seen, it's selected
		//If only one person not seen, it's selected (can be same test as weapon)
		
		//one more player and one more weapon seen, 
		testPlayer.updateSeenCards(new Card("Vent Cover",CardType.WEAPON));
		testPlayer.updateSeenCards(new Card("Sage Sergeant",CardType.PERSON));
		generatedSuggestion=testPlayer.createSuggestion(board.getCards(),"Hangar");
		//System.out.println(generatedSuggestion.getWeapon()+" "+correctSuggestion.getWeapon());
		assertTrue(correctSuggestion.getWeapon().equals(generatedSuggestion.getWeapon()));
		assertTrue(correctSuggestion.getPerson().equals(generatedSuggestion.getPerson()));
		
		
		
	}

}
