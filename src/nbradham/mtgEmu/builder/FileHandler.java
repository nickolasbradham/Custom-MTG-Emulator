package nbradham.mtgEmu.builder;

import java.io.File;

@FunctionalInterface
interface FileHandler {

	void hande(File f);
}
