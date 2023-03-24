package nbradham.mtgEmu.players;

import java.awt.Graphics;
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
import nbradham.mtgEmu.gameObjects.CardZone;
import nbradham.mtgEmu.gameObjects.GameCard;
import nbradham.mtgEmu.gameObjects.GameObject;
import nbradham.mtgEmu.gameObjects.Library;

/**
 * Holds code shared by local and remote players.
 * 
 * @author Nickolas S. Bradham
 *
 */
public abstract class Player {

	private final BufferedImage bufImg = new BufferedImage(GPanel.WIDTH, GPanel.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	private final Graphics bufG = bufImg.createGraphics();
	private final ArrayList<GameObject> objects = new ArrayList<>();
	private final GPanel gameView = new GPanel(this);
	private final CardZone commandZone = new CardZone(this, 0, 700, 200), handZone = new CardZone(this, 200, 700, 900);
	private final Library lib = new Library(this);
	private final int id;

	private GameObject drag;
	private byte fieldEnd;
	private boolean redrawFlag = true;

	/**
	 * Creates a new Player instance.
	 * 
	 * @param playerID The ID of the player.
	 */
	protected Player(int playerID) {
		id = playerID;
		moveToGUI(commandZone);
		moveToGUI(handZone);
		moveToGUI(lib);
	}

	/**
	 * Starts the player and builds the GUI.
	 */
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
								commandZone.add(c);
								break;
							case LIBRARY:
								lib.putOnTop(c);
								break;
							case TOKEN:
								// TODO add to tokens.
							}
						redraw();
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

	/**
	 * Draws game elements.
	 * 
	 * @param g The Graphics to draw to.
	 */
	public final void drawGame(Graphics g) {
		if (redrawFlag) {
			bufG.clearRect(0, 0, GPanel.WIDTH, GPanel.HEIGHT);
			objects.forEach(o -> {
				if (o != drag)
					o.draw(bufG);
			});
			redrawFlag = false;
		}
		g.drawImage(bufImg, 0, 0, null);
		if (drag != null)
			drag.draw(g);
	}

	/**
	 * Moves a GameObject to the GUI layer.
	 * 
	 * @param obj The object to move.
	 */
	public final void moveToGUI(GameObject obj) {
		remove(obj);
		obj.setIndex(objects.size());
		objects.add(obj);
	}

	/**
	 * Removes a GameObject from the view.
	 * 
	 * @param obj The object to remove.
	 */
	public final void remove(GameObject obj) {
		int i = obj.getIndex();
		if (i > -1) {
			if (i < fieldEnd && objects.contains(obj))
				--fieldEnd;
			objects.remove(i);
		}
	}

	/**
	 * Retrieves the ID of this player.
	 * 
	 * @return The numeric player ID.
	 */
	public final int getID() {
		return id;
	}

	/**
	 * Schedules a game redraw.
	 */
	protected final void redraw() {
		redrawFlag = true;
		gameView.repaint();
	}
}