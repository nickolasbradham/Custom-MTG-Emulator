package nbradham.mtgEmu.builder;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Handles the deck compilation process.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class DeckBuilder {

	private static final String PRMPT_BACK = "Select card back image. Cancel to use default.";
	private static final short L_IMG_W = 200;
	private static final byte F_CANCEL = -1;

	private final ArrayList<BuildCard> cards = new ArrayList<>();
	private final JFileChooser chooser = new JFileChooser();
	private final JFrame parent;
	private int commanders, dupes, singles, tokens;
	private byte id = -1, smallest = 0;

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
		try {
			promptCards(PRMPT_BACK, f -> 1);
			id = -1;
			chooser.setMultiSelectionEnabled(true);
			commanders = promptCards("Select Commander card(s). Cancel to skip.");
			if ((dupes = promptCards("Select Library card(s) with duplicates (ex: Basic Lands). Cancel to skip.",
					img -> {
						JLabel label = new JLabel(
								"quantity (>0)?", new ImageIcon(img.getScaledInstance(L_IMG_W,
										L_IMG_W * img.getHeight() / img.getWidth(), BufferedImage.SCALE_SMOOTH)),
								JLabel.LEFT);
						byte count = 0;
						String response;
						while (count < 1)
							try {
								response = JOptionPane.showInputDialog(label);
								if (response == null)
									return F_CANCEL;
								count = Byte.parseByte(response);
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(parent, "Must enter a number.");
							}
						return count;
					})) == F_CANCEL)
				return;
			singles = promptCards("Select remaining Library card(s). Cancel to skip.");
			tokens = promptCards("Select Token/Special card(s). Cancel to skip.");

			if (cards.size() <= 1) {
				JOptionPane.showMessageDialog(parent, "No cards selected.", "Build Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			BuildCard smallCard = cards.get(smallest);
			int w = smallCard.getWidth(), h = smallCard.getHeight(), x = 0, y = h;
			byte moveID = 0;

			while (moveID < cards.size()) {
				while (x + w <= y + h && moveID < cards.size()) {
					for (short py = 0; py < y && moveID < cards.size(); py += h)
						cards.get(moveID++).loc().setLocation(x, py);
					x += w;
				}
				while (y + h <= x + w && moveID < cards.size()) {
					for (short px = 0; px < x && moveID < cards.size(); px += w)
						cards.get(moveID++).loc().setLocation(px, y);
					y += h;
				}
			}

			BufferedImage stitched = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
			Graphics2D stitchG = stitched.createGraphics();
			stitchG.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			cards.forEach(c -> {
				Point loc = c.loc();
				stitchG.drawImage(c.img(), loc.x, loc.y, loc.x + w, loc.y + h, 0, 0, c.getWidth(), c.getHeight(), null);
			});

			chooser.setDialogTitle("Select where to save deck.");
			chooser.resetChoosableFileFilters();
			chooser.setFileFilter(new FileNameExtensionFilter("Custom Deck Format File", "cdf"));
			chooser.setSelectedFile(new File("Deck.cdf"));

			if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
				ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(chooser.getSelectedFile()));

				zipOut.putNextEntry(new ZipEntry("cards.png"));
				ImageIO.write(stitched, "png", zipOut);
				zipOut.closeEntry();

				zipOut.putNextEntry(new ZipEntry("info.bin"));
				DataOutputStream dos = new DataOutputStream(zipOut);
				dos.writeShort(w);
				dos.writeShort(h);
				id = 0;
				writeCards(dos, commanders);

				dos.writeByte(dupes);
				BuildCard c;
				Point cLoc;
				for (byte n = 0; n < dupes; n++) {
					cLoc = (c = cards.get(++id)).loc();
					dos.writeByte(c.count());
					dos.writeShort(cLoc.x);
					dos.writeShort(cLoc.y);
				}

				writeCards(dos, singles);
				writeCards(dos, tokens);

				zipOut.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Prompts the user to select card images and adds them to the deck.
	 * 
	 * @param prompt The prompt text.
	 * @return Returns how many cards were loaded.
	 * @throws IOException Thrown by {@link #promptCards(String, CardCountGetter)}.
	 */
	private int promptCards(String prompt) throws IOException {
		return promptCards(prompt, f -> 1);
	}

	/**
	 * Prompts the user to select card images and adds them to the deck.
	 * 
	 * @param prompt The prompt text.
	 * @param ccg    The CardCountGetter instance to get number of cards.
	 * @return Returns how many cards were loaded.
	 * @throws IOException Thrown by {@link ImageIO#read(File)}.
	 */
	private int promptCards(String prompt, CardCountGetter ccg) throws IOException {
		chooser.setDialogTitle(prompt);
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File[] fs = chooser.getSelectedFiles();
			for (File f : fs)
				addCard(ImageIO.read(f), ccg);
			return fs.length;
		}
		if (prompt == PRMPT_BACK) {
			addCard(ImageIO.read(DeckBuilder.class.getResource("/back.png")), ccg);
			return 1;
		}
		return 0;
	}

	/**
	 * Adds a new {@link BuildCard} to the card list and updates {@code smallest} if
	 * applicable.
	 * 
	 * @param buf The card image.
	 * @param ccg The CardCountGetter for this card.
	 */
	private void addCard(BufferedImage buf, CardCountGetter ccg) {
		BuildCard c = new BuildCard(buf, ccg.getCount(buf));
		cards.add(c);
		smallest = c.getWidth() < cards.get(smallest).getWidth() ? id : smallest;
	}

	/**
	 * Writes {@code numCards} number of cards to {@code dos} starting at
	 * {@code id}.
	 * 
	 * @param dos      The DataOutputStream to write to.
	 * @param numCards The number of cards to write.
	 * @throws IOException Thrown by {@link DataOutputStream#writeByte(int)} and
	 *                     {@link DataOutputStream#writeShort(int)}.
	 */
	private void writeCards(DataOutputStream dos, int numCards) throws IOException {
		dos.writeByte(numCards);
		Point cLoc;
		for (byte n = 0; n < numCards; n++) {
			cLoc = cards.get(++id).loc();
			dos.writeShort(cLoc.x);
			dos.writeShort(cLoc.y);
		}
	}
}