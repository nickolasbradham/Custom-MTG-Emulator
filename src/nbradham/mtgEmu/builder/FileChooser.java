package nbradham.mtgEmu.builder;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

final class FileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;
	private static final File NULL_FILE = new File("");

	private final Component par;

	FileChooser(Component parent) {
		super();
		par = parent;
		setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
	}

	int showOpenDialog(String title, boolean multi) {
		setDialogTitle(title);
		setMultiSelectionEnabled(multi);
		setSelectedFile(NULL_FILE);
		return showOpenDialog(par);
	}

	void prompt(String title, boolean multi, FileHandler handler) {
		if (showOpenDialog(title, multi) != JFileChooser.APPROVE_OPTION)
			return;
		handler.hande(getSelectedFile());
	}
}