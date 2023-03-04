package nbradham.mtgEmu.builder;

import java.awt.Point;
import java.awt.image.BufferedImage;

record Card(int id, BufferedImage img, byte count, Point loc) {

	Card(int id, BufferedImage img, byte count) {
		this(id, img, count, new Point());
	}
	
	int getWidth() {
		return img.getWidth();
	}
	
	int getHeight() {
		return img.getHeight();
	}
}
