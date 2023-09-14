package nbradham.mtgEmu.builder;

import java.io.File;

/**
 * Used to handle files during the build process.
 * 
 * @author Nickolas S. Bradham
 *
 */
@FunctionalInterface
interface BuilderFileHandler {

	/**
	 * Handles target file.
	 * 
	 * @param f The file to handle.
	 */
	void handle(File f);
}