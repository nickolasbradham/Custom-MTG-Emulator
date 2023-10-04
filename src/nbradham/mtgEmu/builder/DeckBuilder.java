package nbradham.mtgEmu.builder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
			JFrame frame = new JFrame("Deck Builder");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1366, 750);
			JPanel pane = new JPanel();
			pane.setPreferredSize(new Dimension(-1, -1));
			BuilderCard bc;
			for (File f : chooser.getSelectedFiles()) {
				cards.add(bc = new BuilderCard(f));
				pane.add(new CardEditor(bc));
			}
			pane.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					Component[] cs = pane.getComponents();
					Component c = cs[cs.length - 1];
					pane.setPreferredSize(new Dimension(-1, c.getY() + c.getHeight()
							+ ((FlowLayout) pane.getLayout()).getVgap() + pane.getInsets().bottom));
				}
			});
			frame.setContentPane(new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			frame.setVisible(true);
		});
	}
}