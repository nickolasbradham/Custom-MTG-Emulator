package nbradham.mtgEmu.builder;

import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Handles the deck compilation process.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class DeckBuilder {

	private final JFrame parent;
	private final JFileChooser chooser = new JFileChooser();

	private final ArrayList<BuilderCard> cards = new ArrayList<>();

	/**
	 * Constructs a new DeckBuilder instance.
	 * 
	 * @param parentFrame The parent JFrame this is linked to.
	 */
	public DeckBuilder(JFrame parentFrame) {
		parent = parentFrame;
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
	}

	/**
	 * Handles prompting the user for deck images and details and saves to a file.
	 */
	public void start() {
		chooser.setDialogTitle("Select the fronts of all cards");
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
			return;
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new FlowLayout());
			BuilderCard bc;
			for (File f : chooser.getSelectedFiles()) {
				cards.add(bc = new BuilderCard(f));
				frame.add(new CardEditor(bc));
			}
		});
	}
}