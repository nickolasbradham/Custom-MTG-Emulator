package nbradham.mtgEmu;

import java.awt.Point;
import java.awt.image.BufferedImage;

record BuildCard(BufferedImage bi, Point loc) {

	BuildCard(BufferedImage img) {
		this(img, new Point());
	}
}
