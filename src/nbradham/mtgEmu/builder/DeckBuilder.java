package nbradham.mtgEmu.builder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import nbradham.mtgEmu.CardMap;
import nbradham.mtgEmu.DeckFile;
import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.Type;
import nbradham.mtgEmu.gameObjects.GameCard;

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
	private final CardImage sleeve = new CardImage();

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
		pane.revalidate();
	}

	/**
	 * Prompts the user to select card images and adds them to the builder.
	 * 
	 * @param reset
	 */
	private void addBulk(boolean reset) {
		if (chooser.showOpenDialog("Select the fronts of all cards", true,
				FileChooser.FILTER_IMAGE) != FileChooser.APPROVE_OPTION)
			return;
		if (reset)
			reset();
		for (File f : chooser.getSelectedFiles())
			pane.add(new CardEditor(new BuilderCard(BuilderCard.loadImg(f)), this));
		recalcPane();
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
			fileMenu.setMnemonic(KeyEvent.VK_F);
			createItem(fileMenu, "New", KeyEvent.VK_N, () -> {
				addBulk(true);
			});
			createItem(fileMenu, "Open", KeyEvent.VK_O, () -> {
				chooser.config("Select custom deck file", false, Main.FILTER_DECK);
				if (chooser.showOpenDialog(frame) != FileChooser.APPROVE_OPTION)
					return;
				reset();
				try {
					DeckFile df = DeckFile.load(chooser.getSelectedFile());
					BufferedImage img = df.image();
					Type t;
					short[] xys;
					for (CardMap cmd : df.details()) {
						pane.add(new CardEditor(new BuilderCard(cmd.zone(), t = cmd.type(), cmd.count(),
								img.getSubimage((xys = cmd.origins())[0], xys[1], GameCard.LG_WIDTH,
										GameCard.LG_HEIGHT),
								t == Type.Custom
										? img.getSubimage(xys[2], xys[3], GameCard.LG_WIDTH, GameCard.LG_HEIGHT)
										: null),
								this));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				recalcPane();
			});
			createItem(fileMenu, "Save", KeyEvent.VK_S, () -> {
				chooser.config("Select save location.", false, Main.FILTER_DECK);
				if (chooser.showSaveDialog(frame) != FileChooser.APPROVE_OPTION)
					return;
				ArrayList<CardImage> images = new ArrayList<>();
				images.add(sleeve);
				ArrayList<BuilderCard> cards = new ArrayList<>();
				BuilderCard bc;
				CardImage ci;
				for (Component ce : pane.getComponents()) {
					images.add((bc = ((CardEditor) ce).getCard()).getCIa());
					cards.add(bc);
					if (bc.getType() != Type.Flipped && (ci = bc.getCIb()).getImg() != CardImage.DEFAULT_BACK)
						images.add(ci);
				}
				int x = 0, y = GameCard.LG_HEIGHT;
				byte moveID = 0;

				while (moveID < images.size()) {
					while (x + GameCard.LG_WIDTH <= y + GameCard.LG_HEIGHT && moveID < images.size()) {
						for (short py = 0; py < y && moveID < images.size(); py += GameCard.LG_HEIGHT)
							images.get(moveID++).getLoc().setLocation(x, py);
						x += GameCard.LG_WIDTH;
					}
					while (y + GameCard.LG_HEIGHT <= x + GameCard.LG_WIDTH && moveID < images.size()) {
						for (short px = 0; px < x && moveID < images.size(); px += GameCard.LG_WIDTH)
							images.get(moveID++).getLoc().setLocation(px, y);
						y += GameCard.LG_HEIGHT;
					}
				}
				BufferedImage stitched = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
				Graphics2D stitchG = stitched.createGraphics();
				stitchG.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				images.forEach(c -> {
					Point loc = c.getLoc();
					stitchG.drawImage(c.getImg(), loc.x, loc.y, loc.x + GameCard.LG_WIDTH, loc.y + GameCard.LG_HEIGHT,
							0, 0, GameCard.LG_WIDTH, GameCard.LG_HEIGHT, null);
				});
				File out = chooser.getSelectedFile();
				String ext = "." + Main.FNAME_EXT;
				if (!out.getName().endsWith(ext))
					out = new File(out.getParent(), out.getName() + ext);
				try {
					ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(out));
					zipOut.putNextEntry(new ZipEntry(Main.FNAME_CARDS));
					ImageIO.write(stitched, "png", zipOut);
					zipOut.closeEntry();
					zipOut.putNextEntry(new ZipEntry(Main.FNAME_DAT));
					DataOutputStream dos = new DataOutputStream(zipOut);
					dos.writeBoolean(sleeve.getImg() == CardImage.DEFAULT_BACK);
					cards.forEach(c -> {
						Type t = c.getType();
						try {
							dos.writeByte(c.getZone().ordinal());
							dos.writeByte(t.ordinal());
							dos.writeByte(c.getQty());
							writeCI(dos, c.getCIa());
							if (t == Type.Custom)
								writeCI(dos, c.getCIb());
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					zipOut.closeEntry();
					zipOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			bar.add(fileMenu);
			JMenu sleeveMenu = new JMenu("Sleeves");
			sleeveMenu.setMnemonic(KeyEvent.VK_S);
			createItem(sleeveMenu, "Set Sleeves", KeyEvent.VK_S,
					() -> chooser.prompt("Select sleeve image", false, i -> sleeve.setImg(i)));
			createItem(sleeveMenu, "Default Sleeves", KeyEvent.VK_D, () -> sleeve.setImg(CardImage.DEFAULT_BACK));
			bar.add(sleeveMenu);
			createItem(bar, "Add Cards", KeyEvent.VK_A, () -> addBulk(false));
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
	 * Clears all card editors and the sleeve.
	 */
	private void reset() {
		pane.removeAll();
		sleeve.setImg(null);
	}

	/**
	 * Writes a CardImage to {@code dos}.
	 * 
	 * @param dos The DataOutputStream to write to.
	 * @param ci  The CardImage to write.
	 * @throws IOException Thrown by {@link DataOutputStream#writeShort(int)}.
	 */
	private void writeCI(DataOutputStream dos, CardImage ci) throws IOException {
		Point p = ci.getLoc();
		dos.writeShort(p.x);
		dos.writeShort(p.y);
	}

	/**
	 * Creates a {@link JMenuItem} and adds it to {@code menu}.
	 * 
	 * @param menu  The JComponent to add the item to.
	 * @param label The label of the item.
	 * @param act   The action to perform when clicked.
	 */
	private static void createItem(JComponent menu, String label, int accel, Runnable act) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				act.run();
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke(accel, InputEvent.CTRL_DOWN_MASK));
		item.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(label.charAt(0)));
		menu.add(item);
	}
}