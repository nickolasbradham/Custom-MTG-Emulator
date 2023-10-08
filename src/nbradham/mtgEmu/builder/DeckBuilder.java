package nbradham.mtgEmu.builder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import nbradham.mtgEmu.Main;

/**
 * Handles the deck compilation process.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class DeckBuilder {

	private final FileChooser chooser;
	private final JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	private final JScrollPane jsp = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * Constructs a new DeckBuilder instance.
	 * 
	 * @param parentFrame The parent JFrame this is linked to.
	 */
	public DeckBuilder(JFrame parentFrame) {
		chooser = new FileChooser(parentFrame);
	}

	/**
	 * Adjusts the {@code pane} size so that there is better margins.
	 */
	private void recalcPane() {
		Component[] cs = pane.getComponents();
		if (cs.length > 0) {
			Component c = cs[cs.length - 1];
			pane.setPreferredSize(new Dimension(-1, Math.max(
					c.getY() + c.getHeight() + ((FlowLayout) pane.getLayout()).getVgap() + pane.getInsets().bottom,
					750)));
		}
	}

	/**
	 * Prompts the user to select card images and adds them to the builder.
	 */
	private void addBulk() {
		if (chooser.showOpenDialog("Select the fronts of all cards", true,
				FileChooser.FILTER_IMAGE) != FileChooser.APPROVE_OPTION)
			return;
		for (File f : chooser.getSelectedFiles())
			pane.add(new CardEditor(new BuilderCard(f), this));
		recalcPane();
		pane.revalidate();
	}

	/**
	 * Removes the CardEditor and related card.
	 * 
	 * @param cardEditor The CardEditor to remove.
	 */
	void remove(CardEditor cardEditor) {
		pane.remove(cardEditor);
		recalcPane();
		pane.revalidate();
	}

	/**
	 * Retrieves the FileChooser.
	 * 
	 * @return The FileChooser
	 */
	FileChooser getChooser() {
		return chooser;
	}

	/**
	 * Handles prompting the user for deck images and details and saves to a file.
	 */
	public void start() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Deck Builder");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(1366, 750);
			JMenuBar bar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			createItem(fileMenu, "New", () -> {
				pane.removeAll();
				addBulk();
			});
			createItem(fileMenu, "Open", () -> {
				chooser.prompt("Select custom deck file", false, Main.FILTER_DECK, f -> {
					try {
						ZipFile zFile = new ZipFile(f);
						BufferedImage img = ImageIO.read(zFile.getInputStream(zFile.getEntry(Main.FNAME_CARDS)));
						InputStream is = zFile.getInputStream(zFile.getEntry(Main.FNAME_DAT));
						// TODO Finish.
						zFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			});
			createItem(fileMenu, "Save", () -> {
				chooser.config("Select save location.", false, Main.FILTER_DECK);
				if (chooser.showSaveDialog(frame) != FileChooser.APPROVE_OPTION)
					return;
				ArrayList<CardImage> images = new ArrayList<>();
				BuilderCard bc;
				CardImage ci;
				for (Component ce : pane.getComponents()) {
					images.add((bc = ((CardEditor) ce).getCard()).getCIa());
					if (!bc.isBflip() && (ci = bc.getCIb()) != null)
						images.add(ci);
				}
				// TODO Finish.
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

	/**
	 * Creates a {@link JMenuItem} and adds it to {@code menu}.
	 * 
	 * @param menu  The JComponent to add the item to.
	 * @param label The label of the item.
	 * @param act   The action to perform when clicked.
	 */
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