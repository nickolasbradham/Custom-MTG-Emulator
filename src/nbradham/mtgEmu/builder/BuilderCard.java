package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import nbradham.mtgEmu.Type;
import nbradham.mtgEmu.Zone;
import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Holds card information.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class BuilderCard {

	private CardImage cfgA = new CardImage(), cfgB = new CardImage();
	private Zone zone = Zone.Library;
	private Type type = Type.Simple;
	private byte count = 1;

	/**
	 * Constructs a new BuilderCard.
	 * 
	 * @param image The image for card config a.
	 */
	BuilderCard(Image image) {
		setAimg(image);
	}

	/**
	 * Constructs a new BuilderCard.
	 * 
	 * @param setZone The card zone.
	 * @param setType The card type.
	 * @param qty     The card quantity.
	 * @param imgA    The card config a image.
	 * @param imgB    The card config b image.
	 */
	BuilderCard(Zone setZone, Type setType, byte qty, BufferedImage imgA, BufferedImage imgB) {
		zone = setZone;
		type = setType;
		count = qty;
		cfgA.setImg(imgA);
		cfgB.setImg(imgB);
	}

	/**
	 * Sets the config a image.
	 * 
	 * @param i The new image.
	 */
	void setAimg(Image i) {
		cfgA.setImg(i);
	}

	/**
	 * Retrieves the a config image.
	 * 
	 * @return The image for config a.
	 */
	Image getAimg() {
		return cfgA.getImg();
	}

	/**
	 * Sets the config b image.
	 * 
	 * @param i The new image.
	 */
	void setBimg(Image i) {
		cfgB.setImg(i);
		type = i == null ? Type.Simple : Type.Custom;
	}

	/**
	 * Retrieves the b config image.
	 * 
	 * @return The image for config b.
	 */
	Image getBimg() {
		return cfgB.getImg();
	}

	/**
	 * Flips the config a image by 180 and sets to b.
	 * 
	 * @return The rotated image.
	 */
	Image flipForB() {
		Image i = cfgA.getImg();
		int w = i.getWidth(null), h = i.getHeight(null);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
		bi.getGraphics().drawImage(i, w, h, -w, -h, null);
		cfgB.setImg(bi);
		type = Type.Flipped;
		return bi;
	}

	/**
	 * Retrieves the config a CardImage.
	 * 
	 * @return The a config CardImage.
	 */
	CardImage getCIa() {
		return cfgA;
	}

	/**
	 * Retrieves the config b CardImage.
	 * 
	 * @return The b config CardImage.
	 */
	CardImage getCIb() {
		return cfgB;
	}

	/**
	 * Retrieves the card type.
	 * 
	 * @return The card type.
	 */
	Type getType() {
		return type;
	}

	/**
	 * Sets the card zone.
	 * 
	 * @param setZone The new card zone.
	 */
	void setZone(Zone setZone) {
		zone = setZone;
	}

	/**
	 * Retrieves the card zone.
	 * 
	 * @return The card zone.
	 */
	Zone getZone() {
		return zone;
	}

	/**
	 * Sets the card quantity.
	 * 
	 * @param value The new quantity.
	 */
	void setQty(byte value) {
		count = value;
	}

	/**
	 * Retrieves the card quantity.
	 * 
	 * @return The card quantity.
	 */
	byte getQty() {
		return count;
	}

	/**
	 * Loads a card image file.
	 * 
	 * @param f The file to load.
	 * @return The scaled card image.
	 */
	static Image loadImg(File f) {
		try {
			return ImageIO.read(f).getScaledInstance(GameCard.LG_WIDTH, GameCard.LG_HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}