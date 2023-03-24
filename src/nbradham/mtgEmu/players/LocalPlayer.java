package nbradham.mtgEmu.players;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import nbradham.mtgEmu.GPanel;
import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.gameObjects.GameCard;
import nbradham.mtgEmu.gameObjects.CardZone;
import nbradham.mtgEmu.gameObjects.GameObject;
import nbradham.mtgEmu.gameObjects.Library;

public final class LocalPlayer extends Player {

	public static enum Layer {
		BATTLEFIELD, GUI
	}

	private final GPanel gameView = new GPanel(this);
	private final BufferedImage fieldImg = new BufferedImage(GPanel.WIDTH, GPanel.HEIGHT,
			BufferedImage.TYPE_4BYTE_ABGR),
			guiImg = new BufferedImage(GPanel.WIDTH, GPanel.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	private final Graphics2D fieldG = fieldImg.createGraphics(), guiG = guiImg.createGraphics();
	private final ArrayList<GameObject> objects = new ArrayList<>();
	private final CardZone commandZone = new CardZone(0, 700, 200), handZone = new CardZone(200, 700, 900);
	private final Library lib = new Library();
	private final int id;
	private GameObject drag;
	private boolean redrawField = true, redrawGui = true;

	public LocalPlayer(int playerID) {
		id = playerID;
		objects.add(commandZone);
		objects.add(handZone);
		objects.add(lib);
	}

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
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
						for (GameCard c : Main.CARD_MANAGER.load(id, chooser.getSelectedFile()))
							switch (c.getType()) {
							case COMMANDER:
								objects.add(c);
								commandZone.add(c);
								break;
							case LIBRARY:
								lib.putOnTop(c);
								break;
							case TOKEN:
								// TODO add to tokens.
							}
						redrawField = redrawGui = true;
						gameView.repaint();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			gameMenu.add(loadItem);

			bar.add(gameMenu);
			frame.setJMenuBar(bar);
			frame.setContentPane(gameView);
			frame.pack();
			frame.setVisible(true);
		});
	}

	@Override
	public void drawGame(Graphics g) {
		redrawLayer(redrawField, fieldG, Layer.BATTLEFIELD, g, fieldImg);
		redrawField = false;
		//redrawLayer(redrawGui, guiG, Layer.GUI, g, guiImg);
		redrawGui = false;
		if (drag != null)
			drag.draw(g);
	}

	private void redrawLayer(boolean flag, Graphics2D bufG, Layer layer, Graphics viewG, BufferedImage buf) {
		if (flag) {
			//bufG.setComposite(AlphaComposite.Clear);
			bufG.fillRect(0, 0, GPanel.WIDTH, GPanel.HEIGHT);
			//bufG.setComposite(AlphaComposite.Src);
			objects.forEach(o -> {
				if (o.getLayer() == layer && o != drag)
					o.draw(bufG);
			});
			flag = false;
		}
		viewG.drawImage(buf, 0, 0, null);
	}
}