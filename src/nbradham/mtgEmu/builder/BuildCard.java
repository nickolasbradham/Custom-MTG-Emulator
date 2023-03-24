package nbradham.mtgEmu.builder;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Holds card information during the deck build process.
 * 
 * @author Nickolas S. Bradham
 *
 */
record BuildCard(int id, BufferedImage img, byte count, Point loc) {

	/**
	 * Creates a new Card with default {@link Point}.
	 * 
	 * @param id    The card ID.
	 * @param img   The image of the card.
	 * @param count The quantity of this card.
	 */
	BuildCard(int id, BufferedImage img, byte count) {
		this(id, img, count, new Point());
	}

	/**
	 * Retrieves the width of the card image.
	 * 
	 * @return The image width.
	 */
	int getWidth() {
		return img.getWidth();
	}

	/**
	 * Retrieves the height of the card image.
	 * 
	 * @return The image height.
	 */
	int getHeight() {
		return img.getHeight();
	}
}