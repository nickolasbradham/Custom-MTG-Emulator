package nbradham.mtgEmu.builder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import nbradham.mtgEmu.Type;
import nbradham.mtgEmu.gameObjects.GameCard;

final class BuilderCard {

	private BufferedImage cfgA, cfgB = null;
	private Type type = Type.LIBRARY;
	private byte count = 1;

	BuilderCard(File f) {
		try {
			cfgA = (BufferedImage) (cfgA = ImageIO.read(f)).getScaledInstance(
					cfgA.getWidth() * GameCard.LG_HEIGHT / cfgA.getHeight(), GameCard.LG_HEIGHT,
					BufferedImage.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	BufferedImage getCfgA() {
		return cfgA;
	}
}