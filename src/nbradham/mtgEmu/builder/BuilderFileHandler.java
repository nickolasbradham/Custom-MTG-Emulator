package nbradham.mtgEmu.builder;

import java.io.File;

@FunctionalInterface
interface BuilderFileHandler {
	void handle(File f);
}