package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import nbradham.mtgEmu.Type;
import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Holds card information.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class BuilderCard {

	private Image cfgA, cfgB = null;
	private Type type = Type.Library;
	private byte count = 1;

	/**
	 * Constructs a new BuilderCard with {@code f} as Card Config A.
	 * 
	 * @param f
	 */
	BuilderCard(File f) {
		try {
			cfgA = (cfgA = ImageIO.read(f)).getScaledInstance(
					cfgA.getWidth(null) * GameCard.LG_HEIGHT / cfgA.getHeight(null), GameCard.LG_HEIGHT,
					BufferedImage.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves Config A Image
	 * 
	 * @return The image of this cards A config.
	 */
	Image getCfgA() {
		return cfgA;
	}

	/**
	 * Sets the Config A of this card.
	 * 
	 * @param i The new image.
	 */
	void setCfgA(Image i) {
		cfgA = i;
	}
}