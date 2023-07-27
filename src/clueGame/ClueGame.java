package clueGame;
import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
//import java.util.Set;

//import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ClueGame extends JFrame{
	//add board.
	Board board;
	//add gameControl Panel
	GameCardPanel cardsPanel;
	GameControlPanel controlPanel;
	public ClueGame() {
		
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize(); 
		/*cardsPanel= new GameCardPanel(user);*/cardsPanel= new GameCardPanel(board.getPlayer(0)); //board.getPlayer might be able to be gethumanPlayer
		cardsPanel.setPreferredSize(new Dimension(150,800));
		controlPanel = new GameControlPanel(); 
		board.setControlPanel(controlPanel);
		board.setCardPanel(cardsPanel);
		
		setSize(1500, 800);  // size the frame clueGUI.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		setTitle("Clue Game");
		
		// test filling in the data

		add(board,BorderLayout.CENTER);
		add(cardsPanel,BorderLayout.EAST);
		add(controlPanel,BorderLayout.SOUTH);
		Player you = board.getHumanPlayer();
		String message = "You are " +you.getName()+"\nCan you Find the solution\nbefore the wretched Computer players?";
		JOptionPane.showMessageDialog(null, message);// can position based on frame later if we want to
		board.nextFlow();//probably move this into main or its own function. Own function with some things in nextFlow moved out of board and put into here
	}
	
	
		
	public static void main(String[] args) {
		ClueGame test=new ClueGame();
		test.setVisible(true);
	}
}
