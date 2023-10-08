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

	private Image cfgA, cfgB;
	private Type type = Type.Library;
	private byte count = 1;

	/**
	 * Constructs a new BuilderCard with {@code f} as Card Config A.
	 * 
	 * @param f
	 */
	BuilderCard(File f) {
		loadA(f);
	}

	void loadA(File f) {
		cfgA = load(f);
	}

	/**
	 * Retrieves Config A Image
	 * 
	 * @return The image of this cards A config.
	 */
	Image getCfgA() {
		return cfgA;
	}

	void loadB(File f) {
		cfgB = load(f);
	}

	void setB(Image i) {
		cfgB = i;
	}

	private static Image load(File f) {
		if (f != null)
			try {
				Image i = ImageIO.read(f);
				return i.getScaledInstance(i.getWidth(null) * GameCard.LG_HEIGHT / i.getHeight(null),
						GameCard.LG_HEIGHT, BufferedImage.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	Image getCfgB() {
		return cfgB;
	}
}