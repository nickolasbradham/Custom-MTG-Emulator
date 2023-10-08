package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.Point;

final class CardImage {

	private final Point loc = new Point();
	private Image img;

	void setImg(Image image) {
		img = image;
	}

	Image getImg() {
		return img;
	}

	public Point getLoc() {
		return loc;
	}
}