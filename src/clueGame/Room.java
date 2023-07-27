/**
 * Room: Room Class holds information about a room, specifically its name and where its center and label cells are.
 * Authors: Landon Dixon and Charles Hulongbayan
 * Date Started: Feb. 23, 2023
 */
package clueGame;

import java.awt.Color;

/*
 * The Room class holds information about a room, specifically its name and where its center and label cells are.
 * 
 * */
public class Room {

	private String name;
	private BoardCell centerCell;
	private BoardCell label;
	private boolean isRealRoom;
	private Color color;

	public Room(String name,boolean isRealRoom) {

		super();
		this.name = name;
		this.isRealRoom=isRealRoom;
	}


	//setters and getters
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setCenterCell(BoardCell centerCell) {

		this.centerCell = centerCell;
	}



	public void setLabel(BoardCell label) {

		this.label = label;
	}

	
	public boolean isRealRoom() {
		return isRealRoom;
	}
	
	public Color getColor() {
		return color;
	}
	
	public BoardCell getLabelCell() {

		return label;
	}


	public BoardCell getCenterCell() {

		return centerCell;
	}

	public String getName() {

		return name;
	}

}
