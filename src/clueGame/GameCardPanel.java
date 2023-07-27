
package clueGame;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
//import java.util.ArrayList;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


/*
 * GameCardPanel: This class creates the JPanel that shows the user the cards in their hand as well as the cards that they have seen.
 * 
 * */
@SuppressWarnings("serial")
public class GameCardPanel extends JPanel {

	private JPanel peoplePanel;
	private JPanel weaponPanel;
	private JPanel roomPanel;


	/**
	 * Constructor: Creates the three types of panels and adds them to itself.
	 * 
	 *  @Param user: passed in to get the initial hand cards
	 */
	public GameCardPanel(Player user)  {
		setLayout(new GridLayout(3,1)); 
		
		peoplePanel=makeCardPanel("People:",user,CardType.PERSON);

		weaponPanel=makeCardPanel("Weapons:",user,CardType.WEAPON);
		
		roomPanel=makeCardPanel("Rooms:",user,CardType.ROOM);
		
		//setSize(300,800);
		add(peoplePanel,BorderLayout.NORTH);
		add(weaponPanel,BorderLayout.CENTER);
		add(roomPanel,BorderLayout.SOUTH);
	} 



	/**
	 * makeCardPanel: initializes a card panel with the initial cards, 
	 * 
	 * @Param name: The name of the panel that will be displayed
	 * @Param user: The player whose cards are going to be added
	 * @Param type: The type of card that the panel will be holding
	 * @return JPanel: returns the JPanel that will be added to the GameCardPanel
	 * */
	private JPanel makeCardPanel(String name,Player user,CardType type) { 
		
		JPanel cardPanel=new JPanel();

		cardPanel.setLayout(new GridLayout(2,1)); // one panel for hand, one for seen
	
		addCardstoPanel(cardPanel,user,type);
		
		TitledBorder border = BorderFactory.createTitledBorder(name);
		cardPanel.setBorder(border);

		
		return cardPanel;
	}

	
	/**
	 * addCardsToPanel: Adds all cards to the specified card panel, here is where hand and seen panels are made and added.
	 * 
	 * @Param cardPanel: which panel to add the cards to
	 * @Param user: the player whose cards we are adding
	 * @Param type: The type of cards being added
	 * 
	 * */

	private void addCardstoPanel(JPanel cardPanel, Player user,CardType type) {
		
		Border blackline = BorderFactory.createLineBorder(Color.black);

		//seen and hand panels created, can have a lot of cards so give it variable rows
		JPanel hand= new JPanel();
		JPanel seen=new JPanel();
		hand.setLayout(new GridLayout(0,1)); 
		seen.setLayout(new GridLayout(0,1));

		//title borders
		TitledBorder handBorder = BorderFactory.createTitledBorder("Hand:");
		hand.setBorder(handBorder);

		TitledBorder seenBorder = BorderFactory.createTitledBorder("Seen:");
		seen.setBorder(seenBorder);

		
		//getting all seen cards and adding them as labels either to hand or seen
		Set<Card> seenCards=user.getSeenCards();
		Card[] handCards=user.getHand();
		boolean handEmpty=true,seenEmpty=true,inHand=false; 
		for(Card card:seenCards) {
			//making label
			JLabel cardLabel=new JLabel(card.getCardName(),SwingConstants.CENTER);
			cardLabel.setBorder(blackline);
			cardLabel.setBackground(card.getHolderColor());
			cardLabel.setOpaque(true);
			
			//making sure the card goes in the right area
			if(card.getCardType()==type) { 
				//checking if the card is inside of the players hand, if so going to put in hand section instead of seen section
				for(Card handCard:handCards) {

					if(handCard.equals(card)) {
						hand.add(cardLabel);
						handEmpty=false;
						inHand=true;
					}
				}
				if(!inHand) {
					seen.add(cardLabel);
					seenEmpty=false;
				}

			}
			inHand=false;

		}
		//if no card of type is seen or in hand put placeholder none their.
		if(handEmpty) {
			JLabel noneCardLabel1=new JLabel("None",SwingConstants.CENTER); //change font/size/add spaces to force size of panel
			noneCardLabel1.setBorder(blackline);
			hand.add(noneCardLabel1);

		}
		
		if(seenEmpty) {
			JLabel noneCardLabel2=new JLabel("None",SwingConstants.CENTER);
			noneCardLabel2.setBorder(blackline);
			seen.add(noneCardLabel2);
		}
		
		cardPanel.add(hand,BorderLayout.NORTH);
		cardPanel.add(seen,BorderLayout.SOUTH);
		
	}


	/**
	 * updatePanel: Used to add cards to the GameCardPanel
	 * called when a new card is seen, empties the card panel and refills it, then adds all three card panels back to the gamecardpanel
	 * 
	 * @Param user: The player whose cards are displayed, will have the new card that is causing the update
	 * @Param type: the type of card being added
	 * */
	public void updatePanel(Player user,CardType type) { 
		JPanel cardPanel;
		if(type==CardType.PERSON) {
			cardPanel=peoplePanel;
		}else if(type==CardType.WEAPON) {
			cardPanel=weaponPanel;
		}else {
			cardPanel=roomPanel;
		}

		cardPanel.removeAll();
		
		addCardstoPanel(cardPanel,user,type);
		
		//add(peoplePanel,BorderLayout.NORTH);
		//add(weaponPanel,BorderLayout.CENTER); 
		//add(roomPanel,BorderLayout.SOUTH);
		
	}



	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		HumanPlayer user=new HumanPlayer("name", "color", 0, 0);
		user.updateHand(new Card("Scarlet",CardType.PERSON,Color.red),new Card("Plum",CardType.PERSON,Color.red),new Card("Study",CardType.ROOM,Color.red));
		user.updateSeenCards(new Card("Mustard",CardType.PERSON,Color.blue));
		user.updateSeenCards(new Card("Scarlet",CardType.PERSON,Color.red));
		user.updateSeenCards(new Card("Plum",CardType.PERSON,Color.red));
		//user.updateSeenCards(new Card("Mustard",CardType.PERSON));
		user.updateSeenCards(new Card("Study",CardType.ROOM,Color.red));
		user.updateSeenCards(new Card("Libaray",CardType.ROOM,Color.orange));
		GameCardPanel cardsPanel = new GameCardPanel(user);  // create the panel
		user.updateSeenCards(new Card("Rope",CardType.WEAPON,Color.green));
		cardsPanel.updatePanel(user,CardType.WEAPON);
		//cardsPanel.setWeaponPanel(cardsPanel.updatePanel("Weapons:",cardsPanel.getWeaponPanel(),user,CardType.WEAPON));
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(cardsPanel); // put the panel in the frame
		frame.setSize(300, 800);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

	}


}

