package nbradham.mtgEmu.builder;

import java.io.File;

/**
 * Used to get the quantity of a card.
 * 
 * @author Nickolas S. Bradham
 *
 */
@FunctionalInterface
interface BuilderFileHandler {

	/**
	 * Retreives how many of this card is in the deck.
	 * 
	 * @param buf The image of the card.
	 * @return The quantity of the card.
	 */
	void handle(File f);
}