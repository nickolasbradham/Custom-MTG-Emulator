package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.Zone;
import nbradham.mtgEmu.interfaces.GameCardHandler;
import nbradham.mtgEmu.players.Player;

/**
 * Represents the library in the game.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class Library extends GameObject implements GameCardHandler {

	private final JPopupMenu menu = new JPopupMenu();
	private final Stack<GameCard> stack = new Stack<>();
	private final Player play;

	private GameCard dropped;

	/**
	 * Constructs a new Library assigned to {@code player}.
	 * 
	 * @param player The owner of this library.
	 */
	public Library(Player player) {
		play = player;
		setPos(1100, 700);

		createDropItem("Put on top.", c -> stack.push(c));
		createDropItem("Put X from top.", c -> stack.add(stack.size() - getNumber(), c));
		createDropItem("Shuffle in.", c -> {
			stack.push(c);
			shuffle();
		});
		createDropItem("Put X from bottom.", c -> stack.add(getNumber(), c));
		createDropItem("Put on bottom.", c -> stack.add(0, c));
	}

	/**
	 * Places {@code c} on the top of the library.
	 * 
	 * @param c The GameCard to put on top.
	 */
	public void putOnTop(GameCard c) {
		transferCard(c);
		stack.push(c);
	}

	/**
	 * Removes all cards and children from this object.
	 */
	public void clear() {
		stack.clear();
		clearChildren();
	}

	/**
	 * Recalculates the dimensions of this Library.
	 */
	public void updateSize() {
		GameCard c = stack.peek();
		setSize(c.getWidth(), c.getHeight());
	}

	/**
	 * Removes all GameCards owned by a different player, removes tokens, and
	 * removes commanders from this Library.
	 * 
	 * @return Commanders removed.
	 */
	public ArrayList<GameCard> clean() {
		ArrayList<GameCard> tmp = new ArrayList<>();
		GameCard c;
		Zone t;
		for (byte i = 0; i < stack.size(); ++i)
			if ((t = (c = stack.get(i)).getType()) == Zone.Token || c.getOwnerID() != play.getID())
				stack.remove(i--);
			else if (t == Zone.Command) {
				stack.remove(i--);
				tmp.add(c);
			}
		return tmp;
	}

	/**
	 * Shuffles the library.
	 */
	public void shuffle() {
		Collections.shuffle(stack);
	}

	@Override
	public void draw(Graphics g) {
		Main.CARD_MANAGER.drawBack(g, play.getID(), getX(), getY());
	}

	@Override
	public void onPressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			GameCard c = stack.pop();
			c.onPressed(e);
			c.onMouseDragged(e.getPoint());
		}
	}

	@Override
	public void handle(GameCard gameCard) {
		stack.remove(gameCard);
	}

	@Override
	public void onObjectDropped(GameObject o) {
		if (o instanceof GameCard) {
			dropped = (GameCard) o;
			menu.show(play.getGameView(), getX(), getY());
		}
		// putOnTop((GameCard) o);
	}

	/**
	 * Retrieves a valid number from the user.
	 * 
	 * @return The number entered.
	 */
	private byte getNumber() {
		byte n = -1;
		while (n < 1)
			try {
				n = Byte.parseByte(JOptionPane.showInputDialog("How many from top (>0)?"));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(play.getGameView(), "Must enter an integer.");
			}
		return n;
	}

	/**
	 * Removes {@code c} from play and sets the holder to this.
	 * 
	 * @param c The GameCard to work with.
	 */
	private void transferCard(GameCard c) {
		play.remove(c);
		c.setHolder(this);
	}

	/**
	 * Creates a JMenuItem for the card dropping menu and adds it to the menu.
	 * 
	 * @param label   The text of the item.
	 * @param handler The handler that moves the card to the right place.
	 */
	private void createDropItem(String label, GameCardHandler handler) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(l -> {
			transferCard(dropped);
			handler.handle(dropped);
			play.redrawBuffer();
		});
		menu.add(item);
	}
}