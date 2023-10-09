package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.Point;

/**
 * Holds image and map cords of card.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class CardImage {

	private final Point loc = new Point();
	private Image img;

	/**
	 * Sets the image.
	 * 
	 * @param image The new image.
	 */
	void setImg(Image image) {
		img = image;
	}

	/**
	 * Retrieves the image.
	 * 
	 * @return The image.
	 */
	Image getImg() {
		return img;
	}

	/**
	 * Retrieves the location.
	 * 
	 * @return The Point location.
	 */
	public Point getLoc() {
		return loc;
	}
}