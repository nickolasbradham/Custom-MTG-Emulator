package nbradham.mtgEmu.builder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	private final JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	private final JScrollPane jsp = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * Constructs a new DeckBuilder instance.
	 * 
	 * @param parentFrame The parent JFrame this is linked to.
	 */
	public DeckBuilder(JFrame parentFrame) {
		parent = parentFrame;
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
		chooser.setDialogTitle("Select the fronts of all cards");
		chooser.setMultiSelectionEnabled(true);
	}

	/**
	 * Adjusts the {@code pane} size so that there is better margins.
	 */
	private void recalcPane() {
		Component[] cs = pane.getComponents();
		if (cs.length > 0) {
			Component c = cs[cs.length - 1];
			pane.setPreferredSize(new Dimension(-1,
					c.getY() + c.getHeight() + ((FlowLayout) pane.getLayout()).getVgap() + pane.getInsets().bottom));
		}
	}

	/**
	 * Prompts the user to select card images and adds them to the builder.
	 */
	private void addBulk() {
		if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
			return;
		BuilderCard bc;
		for (File f : chooser.getSelectedFiles()) {
			cards.add(bc = new BuilderCard(f));
			pane.add(new CardEditor(bc));
		}
		recalcPane();
		pane.revalidate();
	}

	/**
	 * Handles prompting the user for deck images and details and saves to a file.
	 */
	public void start() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Deck Builder");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1366, 750);
			JMenuBar bar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			createItem(fileMenu, "New", () -> {
				cards.clear();
				pane.removeAll();
				addBulk();
			});
			bar.add(fileMenu);
			createItem(bar, "Add Cards", () -> addBulk());
			frame.setJMenuBar(bar);
			pane.setPreferredSize(new Dimension(-1, 700));
			pane.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					recalcPane();
				}
			});
			jsp.getVerticalScrollBar().setUnitIncrement(20);
			frame.setContentPane(jsp);
			frame.setVisible(true);
		});
	}

	private static void createItem(JComponent menu, String label, Runnable act) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				act.run();
			}
		});
		menu.add(item);
	}
}