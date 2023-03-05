package nbradham.mtgEmu.builder;

import java.awt.image.BufferedImage;

/**
 * Used to get the quantity of a card.
 * 
 * @author Nickolas S. Bradham
 *
 */
@FunctionalInterface
interface CardCountGetter {

	/**
	 * Retreives how many of this card is in the deck.
	 * 
	 * @param buf The image of the card.
	 * @return The quantity of the card.
	 */
	byte getCount(BufferedImage buf);
}