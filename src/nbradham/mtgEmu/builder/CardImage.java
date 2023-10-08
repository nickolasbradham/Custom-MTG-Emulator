package nbradham.mtgEmu.builder;

import java.awt.Image;

final class CardImage {

	private Image img;

	void setImg(Image image) {
		img = image;
	}

	Image getImg() {
		return img;
	}
}