package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.util.ArrayList;

import nbradham.mtgEmu.GPanel;
import nbradham.mtgEmu.Holder;
import nbradham.mtgEmu.players.Player;

/**
 * Handles card drop zones like the hand and command zone.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class CardZone extends GameObject implements Holder {

	private final ArrayList<GameCard> cards = new ArrayList<>();
	private final Player play;
	private final String text;
	private final int originY;

	/**
	 * Constructs a new CardZone instance assigned to {@code player} at location
	 * ({@code x}, {@code y}) with width of {@code width}.
	 * 
	 * @param player The Player this belongs to.
	 * @param objX   The X coordinate of this zone.
	 * @param objY   The Y coordinate of this zone.
	 * @param width  The width of this zone.
	 */
	public CardZone(Player player, int objX, int objY, int width, String label) {
		play = player;
		text = label;
		setPos(objX, originY = objY);
		setSize(width, GameCard.SM_HEIGHT);
	}

	/**
	 * Adds {@code card} to this zone.
	 * 
	 * @param card The GameCard to add to this zone.
	 */
	public void add(GameCard card) {
		play.moveToGUI(card);
		cards.add(card);
		addChild(card);
		card.setHolder(this);
		distributeCards();
	}

	/**
	 * removes all cards and children from this zone.
	 */
	public void clear() {
		cards.clear();
		clearChildren();
	}

	/**
	 * Retrieves all GameCards from this zone.
	 * 
	 * @return All cards taken from the zone.
	 */
	public ArrayList<GameCard> takeAll() {
		cards.forEach(c -> play.remove(c));
		ArrayList<GameCard> arr = new ArrayList<>(cards);
		clear();
		return arr;
	}

	/**
	 * Removes {@code c} from this zone.
	 * 
	 * @param c The GameCard to remove.
	 */
	public void remove(GameCard c) {
		cards.remove(c);
		removeChild(c);
		distributeCards();
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.drawRect(getX(), getY(), getWidth(), getHeight());
		if (isHovering())
			g.drawString(text, getX() + 3, getY() + g.getFontMetrics().getMaxAscent());
	}

	@Override
	public void onMouseOver() {
		super.onMouseOver();
		setPos(getX(), GPanel.HEIGHT - getHeight());
		play.redrawBuffer();
	}

	@Override
	public void onMouseExit() {
		super.onMouseExit();
		setPos(getX(), originY);
		play.redrawBuffer();
	}

	@Override
	public void onObjectDropped(GameObject o) {
		super.onObjectDropped(o);
		if (o instanceof GameCard)
			add((GameCard) o);
	}

	/**
	 * Updates the position of all cards in this zone.
	 */
	private void distributeCards() {
		int div = getWidth() / (cards.size() + 1), x = getX(), y = getY();
		GameCard c;
		for (byte i = 0; i < cards.size(); i++) {
			(c = cards.get(i)).setPos(x + (i + 1) * div - c.getWidth() / 2, y);
		}
	}
}