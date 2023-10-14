package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Holds image and map cords of card.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class CardImage {

	static Image DEFAULT_BACK;
	static {
		try {
			DEFAULT_BACK = ImageIO.read(CardImage.class.getResource("/nbradham/mtgEmu/back.png"))
					.getScaledInstance(GameCard.LG_WIDTH, GameCard.LG_HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final Point loc = new Point();
	private Image img = DEFAULT_BACK;

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