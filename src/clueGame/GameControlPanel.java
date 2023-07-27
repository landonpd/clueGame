package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


@SuppressWarnings("serial")
public class GameControlPanel extends JPanel {//needs to give error message if it isn't human players turn, display what the accusation was along with the result, also display for computer player
	JTextField whoTurnField;
	JTextField rollField;
	JTextField guessField;
	JTextField guessResultField;
	Board board;
	
	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel()  {
		setLayout(new GridLayout(2,0));//yeah this is hard to parse
		JPanel up = new JPanel() ;
		up.setLayout(new GridLayout(1,4));
		JPanel whoTurn =new JPanel() ;
		JLabel whoTurnLabel = new JLabel("Whose Turn?");
		whoTurn.add(whoTurnLabel);
		whoTurnField = new JTextField(15);
		whoTurn.add(whoTurnField);	
		up.add(whoTurn);
		JPanel roll =new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		roll.add(rollLabel);
		rollField = new JTextField(5);
		roll.add(rollField);
		up.add(roll);
		JButton makeAccusationButton = new JButton("Make Accusation");
		makeAccusationButton.addActionListener(new AccuseListener());
		up.add(makeAccusationButton);
		JButton nextButton = new JButton("NEXT!");
		nextButton.addActionListener(new NextListener());
		up.add(nextButton);
		add(up,BorderLayout.NORTH);
		JPanel down = new JPanel() ;
		down.setLayout(new GridLayout(0,2));
		JPanel guess =new JPanel() ;
		TitledBorder guessBorder = BorderFactory.createTitledBorder("Guess");// horrible evil
		guess.setBorder(guessBorder);
		guess.setLayout(new GridLayout(1,0));
		guessField = new JTextField(10);
		guess.add(guessField,BorderLayout.CENTER);
		down.add(guess);
		JPanel guessResult =new JPanel();
		TitledBorder guessResultBorder = BorderFactory.createTitledBorder("Guess Result");
		guessResult.setBorder(guessResultBorder);
		guessResult.setLayout(new GridLayout(1,0));
		guessResultField = new JTextField(10);
		guessResult.add(guessResultField,BorderLayout.CENTER);//trust me it just makes things
		down.add(guessResult);
		add(down,BorderLayout.SOUTH);
		board = Board.getInstance();
	}
	
	/**
	 * AccuseListener: listener for accuse button
	 */
	private class AccuseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("J'Accuse!");
			board.accuseFlow();
			
		}
		
	}
	
	/**
	 * NextListener: listener for next button, advances game
	 */
	private class NextListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			board.nextFlow(); 
		}
		
	}
	



	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", "orange", 0, 0), 5);

		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}

	
	//setters n getters
	public void setGuessResult(String guessResult) {
		this.guessResultField.setText(guessResult);

	}

	public void setGuess(String guess) {
		this.guessField.setText(guess);

	}

	public void setTurn(Player player, int i) {
		this.whoTurnField.setText(player.getName());
		this.whoTurnField.setBackground(player.getColor());
		this.rollField.setText(Integer.toString(i));
		

	}
}
