package nbradham.mtgEmu.players;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import nbradham.mtgEmu.GPanel;

public final class LocalPlayer extends Player {

	private static final double PI_2 = Math.PI / 2;

	private BufferedImage cardMap;

	public void start() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Custom MTG Emulator");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JMenuBar bar = new JMenuBar();
			JMenu gameMenu = new JMenu("Game");
			JMenuItem loadItem = new JMenuItem("Open Deck", KeyEvent.VK_O);

			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Custom Deck File", "cdf"));
			chooser.setDialogTitle("Select Deck File");

			loadItem.addActionListener(e -> {
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					try {
						loadDeck(chooser.getSelectedFile());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			});
			gameMenu.add(loadItem);

			bar.add(gameMenu);
			frame.setJMenuBar(bar);

			GPanel gameView = new GPanel(this);
			gameView.setPreferredSize(new Dimension(1366, 750));
			frame.setContentPane(gameView);
			frame.pack();
			frame.setVisible(true);
		});
	}

	private void loadDeck(File deckFile) throws ZipException, IOException {
		ZipFile zFile = new ZipFile(deckFile);
		BufferedImage img = ImageIO.read(zFile.getInputStream(zFile.getEntry("cards.png")));
		int w = img.getWidth(), h = img.getHeight();
		cardMap = new BufferedImage(w + h, Math.max(h, w), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = cardMap.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.translate(w + (h - w) / 2, (w - h) / 2);
		g.rotate(PI_2, w / 2, h / 2);
		g.drawRenderedImage(img, null);

		DataInputStream info = new DataInputStream(zFile.getInputStream(zFile.getEntry("info.bin")));
		zFile.close();
	}

	@Override
	public void drawGame(Graphics g) {

	}
}