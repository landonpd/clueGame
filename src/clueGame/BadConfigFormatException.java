/**
 * BadConfigFormatException:will deal with bad configuration formats in files the game information is loaded from.
 * Authors: Landon Dixon and Charles Hulongbayan
 * Date Started: Feb. 23, 2023
 */

package clueGame;

/*
 * BadConfigFormatException:will deal with bad configuration formats in files the game information is loaded from.
 * 
 * */
@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception {

	/**
	 * 
	 */

	public BadConfigFormatException(String message) {

		super(message);
	}

	public BadConfigFormatException() {
		
		super("The format of the configuration file was bad.");
	}
	

}
