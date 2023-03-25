package nbradham.mtgEmu.players;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import nbradham.mtgEmu.GPanel;
import nbradham.mtgEmu.gameObjects.CardZone;
import nbradham.mtgEmu.gameObjects.GameObject;
import nbradham.mtgEmu.gameObjects.Library;

/**
 * Holds code shared by local and remote players.
 * 
 * @author Nickolas S. Bradham
 *
 */
public abstract class Player {

	protected final CardZone commandZone = new CardZone(this, 0, 700, 200),
			handZone = new CardZone(this, 200, 700, 900);
	protected final Library lib = new Library(this);

	private final BufferedImage bufImg = new BufferedImage(GPanel.WIDTH, GPanel.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	private final Graphics bufG = bufImg.createGraphics();
	private final ArrayList<GameObject> objects = new ArrayList<>(), hovering = new ArrayList<>();
	private final GPanel gameView = new GPanel(this);
	private final int id;

	private GameObject drag, hoverTop;
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
			addMenuBarItems(frame, bar);
			frame.setJMenuBar(bar);
			frame.setContentPane(gameView);
			frame.pack();
			frame.setVisible(true);
		});
	}

	protected void addMenuBarItems(JFrame frame, JMenuBar bar) {
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
			objects.forEach(o -> {
				if (o != drag)
					o.drawLate(bufG);
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
	public final void redrawBuffer() {
		redrawFlag = true;
		gameView.repaint();
	}

	/**
	 * Handles when the mouse is moved on the game view.
	 * 
	 * @param loc The location of the mouse.
	 */
	public final void mouseMoved(Point loc) {
		GameObject test;
		for (byte i = 0; i < hovering.size(); ++i)
			if (!(test = hovering.get(i)).isUnder(loc)) {
				hovering.remove(i--);
				test.onMouseExit();
				if (test == hoverTop)
					test.onMouseExitTop();
			}

		boolean first = true;
		for (int i = objects.size() - 1; i > -1; --i) {
			if ((test = objects.get(i)).isUnder(loc)) {
				if (first) {
					if (test != hoverTop) {
						if (hoverTop != null)
							hoverTop.onMouseExitTop();
						hoverTop = test;
					}
					test.onMouseOverTop();
					first = false;
				}
				if (!hovering.contains(test)) {
					test.onMouseOver();
					hovering.add(test);
				}
			}
		}
	}
}