package nbradham.mtgEmu.builder;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Small {@link JFileChooser} wrapper for added functionality.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class FileChooser extends JFileChooser {
	static final FileNameExtensionFilter FILTER_IMAGE = new FileNameExtensionFilter("Image Files", "jpg", "png");
	private static final long serialVersionUID = 1L;
	private static final File NULL_FILE = new File("");

	private final Component par;

	/**
	 * Constructs a new FileChooser.
	 * 
	 * @param parent The parent Component.
	 */
	FileChooser(Component parent) {
		super();
		par = parent;
	}

	/**
	 * Shows the open dialog.
	 * 
	 * @param title The title of the dialog.
	 * @param multi Allow multiple file selection?
	 * @return the value returned by {@link #showOpenDialog(Component)}.
	 */
	int showOpenDialog(String title, boolean multi, FileNameExtensionFilter filter) {
		config(title, multi, filter);
		setSelectedFile(NULL_FILE);
		return showOpenDialog(par);
	}

	/**
	 * Prompts for a file selection.
	 * 
	 * @param title   The title of the dialog.
	 * @param multi   Allow multiple file selection?
	 * @param handler Handles file on successful selection.
	 */
	void prompt(String title, boolean multi, FileHandler handler) {
		prompt(title, multi, FILTER_IMAGE, handler);
	}

	void prompt(String title, boolean multi, FileNameExtensionFilter filter, FileHandler handler) {
		if (showOpenDialog(title, multi, filter) != JFileChooser.APPROVE_OPTION)
			return;
		handler.hande(getSelectedFile());
	}

	void config(String title, boolean multi, FileNameExtensionFilter filter) {
		setDialogTitle(title);
		setMultiSelectionEnabled(multi);
		setSelectedFile(NULL_FILE);
		setFileFilter(filter);
	}
}