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

	BuilderCard(Image image) {
		setAimg(image);
	}

	BuilderCard(Zone setZone, Type setType, byte qty, BufferedImage imgA, BufferedImage imgB) {
		zone = setZone;
		type = setType;
		count = qty;
		cfgA.setImg(imgA);
		cfgB.setImg(imgB);
	}

	void setAimg(Image i) {
		cfgA.setImg(i);
	}

	Image getAimg() {
		return cfgA.getImg();
	}

	void setBimg(Image i) {
		cfgB.setImg(i);
		type = i == null ? Type.Simple : Type.Custom;
	}

	Image flipForB() {
		Image i = cfgA.getImg();
		int w = i.getWidth(null), h = i.getHeight(null);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
		bi.getGraphics().drawImage(i, w, h, -w, -h, null);
		cfgB.setImg(bi);
		type = Type.Flipped;
		return bi;
	}

	CardImage getCIa() {
		return cfgA;
	}

	Type getType() {
		return type;
	}

	CardImage getCIb() {
		return cfgB;
	}

	Zone getZone() {
		return zone;
	}

	static Image loadImg(File f) {
		try {
			return ImageIO.read(f).getScaledInstance(GameCard.LG_WIDTH, GameCard.LG_HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	void setZone(Zone setZone) {
		zone = setZone;
	}

	void setQty(byte value) {
		count = value;
	}

	byte getQty() {
		return count;
	}

	Image getBimg() {
		return cfgB.getImg();
	}
}