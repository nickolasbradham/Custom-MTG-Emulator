package nbradham.mtgEmu.builder;

import java.awt.image.BufferedImage;

@FunctionalInterface
interface CardCountGetter {
	byte getCount(BufferedImage buf);
}