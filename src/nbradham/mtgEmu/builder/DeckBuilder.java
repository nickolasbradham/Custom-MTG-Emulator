package nbradham.mtgEmu.builder;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class DeckBuilder {

	private static short L_IMG_W = 200;

	private final ArrayList<Card> cards = new ArrayList<>();
	private final JFileChooser chooser = new JFileChooser();
	private final JFrame parent;
	private int commanders, dupes, singles, tokens;
	private byte id = -1, smallest = 0;

	public DeckBuilder(JFrame parentFrame) {
		parent = parentFrame;
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
	}

	public void start() {
		try {
			commanders = promptCards("Select Commander card(s). Cancel to skip.", f -> 1);
			dupes = promptCards("Select Library card(s) with duplicates (ex: Basic Lands). Cancel to skip.", img -> {
				JLabel label = new JLabel("quantity (>0)?", new ImageIcon(img.getScaledInstance(L_IMG_W,
						L_IMG_W * img.getHeight() / img.getWidth(), BufferedImage.SCALE_SMOOTH)), JLabel.LEFT);
				byte count = 0;
				while (count < 1)
					try {
						count = Byte.parseByte(JOptionPane.showInputDialog(label));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(parent, "Must enter a number.");
					}
				return count;
			});
			singles = promptCards("Select remaining Library card(s). Cancel to skip.", f -> 1);
			tokens = promptCards("Select Token/Special card(s). Cancel to skip.", f -> 1);

			Card smallCard = cards.get(smallest);
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

			BufferedImage stitched = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics stitchG = stitched.createGraphics();
			cards.forEach(c -> {
				Point loc = c.loc();
				stitchG.drawImage(c.img(), loc.x, loc.y, loc.x + w, loc.y + h, 0, 0, c.getWidth(), c.getHeight(), null);
			});
			
			ImageIO.write(stitched, "png", new File("Stitched.png"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private int promptCards(String prompt, CardCountGetter ccg) throws IOException {
		chooser.setDialogTitle(prompt);
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File[] fs = chooser.getSelectedFiles();
			BufferedImage buf;
			Card c;
			for (File f : fs) {
				cards.add(c = new Card(++id, buf = ImageIO.read(f), ccg.getCount(buf)));
				smallest = c.getWidth() < cards.get(smallest).getWidth() ? id : smallest;
			}
			return fs.length;
		}
		return 0;
	}
}