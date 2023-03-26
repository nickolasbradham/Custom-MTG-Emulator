package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

import nbradham.mtgEmu.Holder;
import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.gameObjects.GameCard.CardType;
import nbradham.mtgEmu.players.Player;

/**
 * Represents the library in the game.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class Library extends GameObject implements Holder {

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
		c.setHolder(this);
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
		CardType t;
		for (byte i = 0; i < stack.size(); ++i)
			if ((t = (c = stack.get(i)).getType()) == CardType.TOKEN || c.getOwnerID() != play.getID())
				stack.remove(i--);
			else if (t == CardType.COMMANDER) {
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
	public void remove(GameCard gameCard) {
		stack.remove(gameCard);
	}

	@Override
	public void onObjectDropped(GameObject o) {
		if (o instanceof GameCard)
			putOnTop((GameCard) o);
	}
}