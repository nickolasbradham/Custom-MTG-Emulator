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

	private CardImage cfgA = new CardImage(), cfgB = new CardImage();
	private Type type = Type.Library;
	private byte count = 1;
	private boolean flip;

	/**
	 * Constructs a new BuilderCard with {@code f} as Card Config A.
	 * 
	 * @param f
	 */
	BuilderCard(File f) {
		loadA(f);
	}

	/**
	 * Loads an image into config A.
	 * 
	 * @param f The image to load.
	 */
	void loadA(File f) {
		cfgA.setImg(load(f));
	}

	/**
	 * Retrieves Config A Image
	 * 
	 * @return The image of this cards A config.
	 */
	Image getCfgA() {
		return cfgA.getImg();
	}

	/**
	 * Loads an image into config B.
	 * 
	 * @param f The image to load.
	 */
	void loadB(File f) {
		cfgB.setImg(load(f));
	}

	/**
	 * Sets an image into config B.
	 * 
	 * @param i The image to set.
	 */
	void setB(Image i) {
		cfgB.setImg(i);
	}

	/**
	 * Retrieves config B image,
	 * 
	 * @return The config B image.
	 */
	Image getCfgB() {
		return cfgB.getImg();
	}

	void setFlipped(boolean b) {
		flip = b;
	}

	CardImage getCIa() {
		return cfgA;
	}

	CardImage getCIb() {
		return cfgB;
	}

	boolean isBflip() {
		return flip;
	}

	/**
	 * Reads an image file.
	 * 
	 * @param f The file to read.
	 * @return The loaded image.
	 */
	private static Image load(File f) {
		if (f != null)
			try {
				Image i = ImageIO.read(f);
				return i.getScaledInstance(GameCard.LG_WIDTH, GameCard.LG_HEIGHT, BufferedImage.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}
}