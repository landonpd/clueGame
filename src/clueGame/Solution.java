package clueGame;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;
	
	public Card getRoom() {
		return room;
	}
	public void setRoom(Card room) {
		this.room = room;
	}
	public Card getPerson() {
		return person;
	}
	public void setPerson(Card person) {
		this.person = person;
	}
	public Card getWeapon() {
		return weapon;
	}
	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}
	public Solution(Card room, Card person, Card weapon) {
		super();
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}
	
	public Solution() {
		super();
	}
	
}
