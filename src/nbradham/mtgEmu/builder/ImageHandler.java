package nbradham.mtgEmu.builder;

import java.awt.Image;

/**
 * Handles Image operations.
 * 
 * @author Nickolas S. Bradham
 *
 */
@FunctionalInterface
interface ImageHandler {

	/**
	 * Handles a image.
	 * 
	 * @param image The Image to handle.
	 */
	void hande(Image image);
}
