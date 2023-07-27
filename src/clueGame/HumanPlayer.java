package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import javax.swing.JPanel;
import javax.swing.JTextField;



public class HumanPlayer extends Player {
	Board board; 
	Solution solution;
	Set<Card>weapon;
	Set<Card>person;
	Set<Card>room;
	JComboBox<String> weaponCombo;
	JComboBox<String> personCombo;
	JComboBox<String> roomCombo;
	JDialog solutionDialog;
	boolean jaccuse=false;
	//Boolean pressSubmit=false, pressCancel = false;

	public HumanPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		super.isHuman = true;
		board = Board.getInstance();
	}


	//Not used
	@Override
	public BoardCell selectTarget(Set<BoardCell> targets, Map<Character, Room> roomMap) {
		return null;
	}


	

	/*
	 * createSuggestion: Human player version of create suggestion, calls solutionDialog function with same args.
	 * @param: cards is the deck of possible cards
	 * @param: roomName is the name of the room that the player is in
	 * */
	@Override
	public Solution createSuggestion(Set<Card> cards, String roomName) {
		this.solutionDialog(cards, roomName);
		//board.setPlayerFinished(true);
		return solution;
		
	}
	
	//not used
	@Override
	protected boolean canMakeAccusation(int deckSize) {
		return false;
	}


	/*
	 * createSuggestion: Human player version of create suggestion, calls solutionDialog function with deck and null, which defines this to solutionDIalog as an accusation.
	 * @param: deck is the deck of possible cards
	 * 
	 * */
	@Override
	protected Solution makeAccusation(Set<Card> deck) {
		this.solutionDialog(deck, null);
		return solution;
	}
	
	
	/* 
	 * solutionDialog:creates a dialog box where the human player can select their suggestions and accusations  
	 * @param: cards is the deck of possible cards
	 * @param: roomName is the name of the room that the player is in. If null, this is interpreted as an accusation
	 * */
	private  void solutionDialog(Set<Card> cards, String roomName) {
		solution = new Solution();

		weapon= new HashSet<Card>();
		person= new HashSet<Card>();
		room= new HashSet<Card>();
		for(Card card :cards) {
			switch(card.getCardType()) {
			case WEAPON:
				weapon.add(card);
				break;
			case PERSON:
				person.add(card);
				break;
			case ROOM:
				room.add(card);
				break;
			}

		}


		solutionDialog = new JDialog();
		
		solutionDialog.setSize(200, 200);
		
		solutionDialog.setLayout(new GridLayout(4,2));
		solutionDialog.setModal(true);
		solutionDialog.setAlwaysOnTop(true);
		//sugg.set
		
		//room row
		JLabel roomLabel = new JLabel("Current Room:");
		solutionDialog.add(roomLabel);
		if(roomName==(null)) {//accusation lets you pick room
			jaccuse = true;
			roomCombo = new JComboBox<String>();
			for(Card card :room) {
				roomCombo.addItem(card.getCardName());
				
			}
			for(Card card :room) {
				if(card.getCardName() == roomCombo.getSelectedItem()) {
					solution.setRoom(card);
				}
			}
			roomCombo.addItemListener(new RoomCheckboxListener());
			solutionDialog.add(roomCombo);
			solutionDialog.setTitle("Make an Accusation");
			
		}else {
			JTextField roomTextField = new JTextField(roomName);//suggestion uses room youre in
			roomTextField.setEditable(false);
			solutionDialog.add(roomTextField);
			for(Card card :room) {
				if(card.getCardName() == roomName) {
					solution.setRoom(card);
				}
				
			}
			solutionDialog.setTitle("Make a Suggestion");
			
		}
		
		
		
		//person row
		JLabel personLabel = new JLabel("Person:");
		solutionDialog.add(personLabel);
		personCombo = new JComboBox<String>();
		for(Card card :person) {
			personCombo.addItem(card.getCardName());
		}
		for(Card card :person) {
			if(card.getCardName() == personCombo.getSelectedItem()) {
				solution.setPerson(card);
			}
		}
		personCombo.addItemListener(new PersonCheckboxListener());
		solutionDialog.add(personCombo);
		
		//weapon row
		JLabel weaponLabel = new JLabel("Weapon:");
		solutionDialog.add(weaponLabel);
		weaponCombo = new JComboBox<String>();
		for(Card card :weapon) {
			weaponCombo.addItem(card.getCardName());
		}
		for(Card card :weapon) {
			if(card.getCardName() == weaponCombo.getSelectedItem()) {
				solution.setWeapon(card);
			}
		}
		weaponCombo.addItemListener(new WeaponCheckboxListener());
		solutionDialog.add(weaponCombo);
		
		//buttons
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new SubmitListener());
		solutionDialog.add(submitButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener());
		solutionDialog.add(cancelButton);
		
		solutionDialog.repaint();	
		solutionDialog.setVisible(true);
	}
	
	
	/* 
	 * WeaponCheckboxListener: detects the input from associated checkbox in solutionDialog   
	 * */
	private class WeaponCheckboxListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			for(Card card :weapon) {
				if(card.getCardName() == weaponCombo.getSelectedItem()) {
					solution.setWeapon(card);
				}
			}
		}
	}
	/* 
	 * PersonCheckboxListener: detects the input from associated checkbox in solutionDialog   
	 * */
	private class PersonCheckboxListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			for(Card card :person) {
				if(card.getCardName() == personCombo.getSelectedItem()) {
					solution.setPerson(card);
				}
			}
		}
	}
	/* 
	 * RoomCheckboxListener: detects the input from associated checkbox in solutionDialog   
	 * */
	private class RoomCheckboxListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			for(Card card :room) {
				if(card.getCardName() == roomCombo.getSelectedItem()) {
					solution.setRoom(card);
				}
			}
		}
	}
	/* 
	 * SubmitListener: detects the input from submitButton and triggers various methods from other classes  
	 * */
	private class SubmitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			solutionDialog.setVisible(false);
			//pressSubmit = true;
			
			
			if(jaccuse) {
				if(board.checkAccusation(solution)) {
					String message = "You guessed it was "+solution.getPerson().getCardName()+" with the "+solution.getWeapon().getCardName()+" in "+solution.getRoom().getCardName()+"\nYou are win!";
					JOptionPane.showMessageDialog(null, message);
				}else{
					String message = "You guessed it was "+solution.getPerson().getCardName()+" with the "+solution.getWeapon().getCardName()+" in "+solution.getRoom().getCardName()+"\nYou are lose :(";
					JOptionPane.showMessageDialog(null, message);
				}
				
			}else {
				board.printSuggestion(solution);
				for (Player player: board.getPlayers()) {//This for loop needs to happen when the player makes a suggestion
					System.out.println(player.getName());
					if(solution.getPerson().getCardName()==player.getName()) {
						
						player.setMoved(true);
						break;
					}

				}
			}
			board.setPlayerFinished(true);
		}

	}
	/* 
	 * CancelListener: detects the input from cancelButton and resets variables so that player can back out of actions  
	 * */
	private class CancelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			solutionDialog.setVisible(false);
			//pressCancel = true;
			solution = null;
			board.setPlayerFinished(true);
			jaccuse = false;
			
		}

	}


	
	





}
