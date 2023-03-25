package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.util.Stack;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.players.Player;

/**
 * Represents the library in the game.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class Library extends GameObject {

	private final Stack<GameCard> stack = new Stack<>();
	private final Player play;

	/**
	 * Constructs a new Library assigned to {@code player}.
	 * 
	 * @param player The owner of this library.
	 */
	public Library(Player player) {
		play = player;
		setPos(1100, 700);
	}

	/**
	 * Places {@code c} on the top of the library.
	 * 
	 * @param c The GameCard to put on top.
	 */
	public void putOnTop(GameCard c) {
		play.remove(c);
		stack.push(c);
	}

	/**
	 * Removes all cards and children from this object.
	 */
	public void clear() {
		stack.clear();
		clearChildren();
	}

	@Override
	public void draw(Graphics g) {
		Main.CARD_MANAGER.drawBack(g, play.getID(), getX(), getY());
	}
}