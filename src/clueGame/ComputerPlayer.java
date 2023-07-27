package clueGame;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;



public class ComputerPlayer extends Player {
	
	//private boolean makeAccusation;
	
	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		super.isHuman = false;
		//this.makeAccusation=false;
	}

	

	/*
	 * selectTarget: Randomly select where to move, prioritizes going to rooms
	 * @param: targets is the list of possible targets
	 *@Param: roomMap list of rooms to favor moving into a room 
	 *
	 * https://www.baeldung.com/java-iterate-set: used to iterate over set
	 * */
	@Override
	public BoardCell selectTarget(Set<BoardCell> targets, Map<Character,Room> roomMap) { 
		//go through set and grab all room ones that are not in seen, remove those rooms too,
		//if room list is not empty randomly pick one, otherwise randomly select from what is left in set, add what isn't a room into a list too
		int randCell;
		Random rand=new Random();
		ArrayList<BoardCell> unseenRooms=new ArrayList<BoardCell>(); //rooms that haven't been seen
		ArrayList<BoardCell> seenAndNonRooms=new ArrayList<BoardCell>(); //cells that are seen rooms or just not rooms
		Iterator<BoardCell> targetIt = targets.iterator(); 
		BoardCell next;
		while(targetIt.hasNext()) {
			next=targetIt.next();
			
			if(next.isRoom()) {
				Card tempCard=new Card(roomMap.get(next.getInitial()).getName(), CardType.ROOM);
				if(!seenCards.contains(tempCard)) {
					unseenRooms.add(next);
				}else {
					seenAndNonRooms.add(next);
				}

			}else{
				seenAndNonRooms.add(next);
			}
		}
		//if there are unseen rooms that the computer can get to pick one randomly and go to it.
		if(unseenRooms.size()!=0) {
			randCell=rand.nextInt(unseenRooms.size());
			return unseenRooms.get(randCell);

		}else { //if there is not a room that is unseen randomly choose one from what is left.
			randCell=rand.nextInt(seenAndNonRooms.size());
			return seenAndNonRooms.get(randCell);
		}
	}
	
	/*
	 *CreateSuggestion: Randomly generating a suggestion based on where the computer is and what cards have not been seen by the computer.
	 * @param: cards is the deck of possible cards
	 * @param: roomName is the name of the room that the player is in
	 * 
	 * */
	@Override
	public Solution createSuggestion(Set<Card> cards,String roomName) {
		Solution suggestion=new Solution();
		ArrayList<Card> cardList = new ArrayList<Card>(cards);
		int randCard;
		Random rand=new Random();
		boolean playerFound=false,weaponFound=false;
		while(true) {
			randCard=rand.nextInt(cardList.size());
			if(!seenCards.contains(cardList.get(randCard))) {
				if(cardList.get(randCard).getCardType()==CardType.PERSON) {
					suggestion.setPerson(cardList.get(randCard));
					playerFound=true;
				}else if(cardList.get(randCard).getCardType()==CardType.WEAPON) {
					suggestion.setWeapon(cardList.get(randCard));
					weaponFound=true;
				}
			}
			if(weaponFound&&playerFound) {
				break;
			}
		}
		
		suggestion.setRoom(new Card(roomName,CardType.ROOM));
		return suggestion;
	}
	
	/*
	 * canMakeAccusation checks to see if the computer can make an accusation because they have learned all that there is to learn
	 * */
	@Override
	public boolean canMakeAccusation(int deckSize) {
		if(seenCards.size()==deckSize-3) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * makeAccusation finds the not seen cards from the deck to make an accusation
	 * */
	@Override
	public Solution makeAccusation(Set<Card> deck) {
		Solution answer=new Solution();
		for (Card card: deck) {
			if (!seenCards.contains(card)) {
				if(card.getCardType()==CardType.PERSON) {
					answer.setPerson(card);
				}else if(card.getCardType()==CardType.WEAPON) {
					answer.setWeapon(card);
				}else if(card.getCardType()==CardType.ROOM) {
					answer.setRoom(card);
				}
				
			}
			
		}
		return answer;
	}
	
}
