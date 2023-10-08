package nbradham.mtgEmu.builder;

import java.io.File;

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
	 * @param f The file to handle.
	 */
	void hande(File f);
}
