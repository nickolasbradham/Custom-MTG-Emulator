package nbradham.mtgEmu.builder;

import java.awt.Image;

/**
 * Handles File operations.
 * 
 * @author Nickolas S. Bradham
 *
 */
@FunctionalInterface
interface FileHandler {

	/**
	 * Handles a file.
	 * 
	 * @param image The file to handle.
	 */
	void hande(Image image);
}
