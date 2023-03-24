package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

import nbradham.mtgEmu.players.Player;

/**
 * Handles card drop zones like the hand and command zone.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class CardZone extends GameObject {

	private final ArrayList<GameCard> cards = new ArrayList<>();
	private final Player play;

	/**
	 * Constructs a new CardZone instance assigned to {@code player} at location
	 * ({@code x}, {@code y}) with width of {@code width}.
	 * 
	 * @param player The Player this belongs to.
	 * @param objX   The X coordinate of this zone.
	 * @param objY   The Y coordinate of this zone.
	 * @param width  The width of this zone.
	 */
	public CardZone(Player player, int objX, int objY, int width) {
		play = player;
		setPos(objX, objY);
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
		children.add(card);
		int div = getWidth() / (cards.size() + 2), x = getX(), y = getY();
		for (byte i = 0; i < cards.size(); i++)
			cards.get(i).setPos(x + (i + 1) * div, y);
	}
}