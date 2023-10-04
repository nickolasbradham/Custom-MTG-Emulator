package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import nbradham.mtgEmu.Type;
import nbradham.mtgEmu.gameObjects.GameCard;

final class BuilderCard {

	private Image cfgA, cfgB = null;
	private Type type = Type.Library;
	private byte count = 1;

	BuilderCard(File f) {
		try {
			cfgA = (cfgA = ImageIO.read(f)).getScaledInstance(
					cfgA.getWidth(null) * GameCard.LG_HEIGHT / cfgA.getHeight(null), GameCard.LG_HEIGHT,
					BufferedImage.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Image getCfgA() {
		return cfgA;
	}
}