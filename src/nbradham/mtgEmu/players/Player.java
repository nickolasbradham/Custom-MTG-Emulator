package nbradham.mtgEmu.players;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import nbradham.mtgEmu.GPanel;
import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.Zone;
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

	protected final CardZone commandZone = new CardZone(this, 0, 700, 200, "Command Zone"),
			handZone = new CardZone(this, 200, 700, 900, "Hand");
	protected final Library lib = new Library(this);
	protected final ArrayList<GameObject> objects = new ArrayList<>();
	protected final GPanel gameView = new GPanel(this);

	private final BufferedImage bufImg = new BufferedImage(GPanel.WIDTH, GPanel.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	private final Graphics bufG = bufImg.createGraphics();
	private final int id;

	protected Stack<GameObject> hovering = new Stack<>();
	protected GameObject drag;

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
			frame.setResizable(false);
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
			objects.forEach(o -> o.draw(bufG));
			objects.forEach(o -> o.drawLate(bufG));
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
		objects.add(obj);
	}

	/**
	 * Moves a GameObject to the field layer.
	 * 
	 * @param obj The object to move.
	 */
	public final void moveToField(GameObject obj) {
		remove(obj);
		objects.add(fieldEnd++, obj);
	}

	/**
	 * Removes a GameObject from the view.
	 * 
	 * @param obj The object to remove.
	 */
	public final void remove(GameObject obj) {
		int i = objects.indexOf(obj);
		if (i > -1) {
			if (i < fieldEnd)
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
	public void mouseMoved(Point loc) {
		Stack<GameObject> newHover = new Stack<>();
		objects.forEach(o -> {
			if (o.isUnder(loc)) {
				if (!hovering.contains(o))
					o.onMouseOver();
				newHover.push(o);
			}
		});
		if (!newHover.isEmpty()) {
			GameObject o = newHover.peek(), last = null;
			if (hovering.isEmpty() || (last = hovering.peek()) != o) {
				if (last != null)
					last.onMouseExitTop();
				o.onMouseOverTop();
			}
		}

		GameObject o;
		boolean first = true;
		while (!hovering.isEmpty())
			if (!newHover.contains(o = hovering.pop())) {
				if (first) {
					o.onMouseExitTop();
					first = false;
				}
				o.onMouseExit();
			}
		hovering = newHover;
	}

	/**
	 * Retrieves the object being held by the cursor.
	 * 
	 * @return The object being dragged.
	 */
	public final GameObject getDragging() {
		return drag;
	}

	/**
	 * Retrieves the GPanel game view.
	 * 
	 * @return The current GPanel.
	 */
	public GPanel getGameView() {
		return gameView;
	}

	/**
	 * Handles when a object wants to start being dragged.
	 * 
	 * @param o The object to drag.
	 */
	public void startDragging(GameObject o) {
	}

	/**
	 * Handles when a object wants to stop being dragged.
	 * 
	 * @param o The object to stop dragging.
	 */
	public void stopDragging(Point dropLoc) {
	}

	/**
	 * Handles when a mouse button is pressed.
	 * 
	 * @param e The MouseEvent to process.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Handles when a mouse button is released.
	 * 
	 * @param e The MouseEvent to process.
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Handles when a mouse button is clicked.
	 * 
	 * @param e The MouseEvent to process.
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Clears the player state and loads a new deck file.
	 * 
	 * @param file The file to load.
	 */
	protected final void loadDeck(File file) {
		commandZone.clear();
		handZone.clear();
		lib.clear();

		GameObject o;
		for (byte i = 0; i < objects.size(); i++)
			if (!((o = objects.get(i)) instanceof CardZone) && !(o instanceof Library)) {
				remove(o);
				--i;
			}

		try {
			for (GameCard c : Main.CARD_MANAGER.load(this, file))
				switch (c.getType()) {
				case Command:
					commandZone.add(c);
					break;
				case Library:
					lib.putOnTop(c);
					break;
				case Token:
					// TODO add to tokens.
				}
			lib.updateSize();
			lib.shuffle();
			redrawBuffer();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Resets all cards to all starting zones.
	 */
	protected final void reset() {
		Stack<GameCard> tmp = new Stack<>();
		tmp.addAll(commandZone.takeAll());
		tmp.addAll(handZone.takeAll());
		tmp.addAll(lib.clean());
		GameObject o;
		for (byte i = 0; i < objects.size(); ++i)
			if ((o = objects.get(i)) instanceof GameCard) {
				remove(o);
				tmp.add((GameCard) o);
				--i;
			}
		GameCard c;
		Zone t;
		while (!tmp.isEmpty())
			if ((t = (c = tmp.pop()).getType()) == Zone.Token || c.getOwnerID() != id)
				continue;
			else {
				c.setTapped(false);
				switch (t) {
				case Command:
					commandZone.add(c);
					break;
				case Library:
					lib.putOnTop(c);
				default:
					break;
				}
			}
		lib.shuffle();
		redrawBuffer();
	}

	/**
	 * Used during GUI creation to add extra elements to the JMenuBar.
	 * 
	 * @param frame The parent JFrame.
	 * @param bar   The JMenuBar to add to.
	 */
	protected void addMenuBarItems(JFrame frame, JMenuBar bar) {
	}
}