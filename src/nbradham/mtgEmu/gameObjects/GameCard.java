package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;

import nbradham.mtgEmu.Main;

/**
 * Represents a single card instance in the game.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class GameCard extends GameObject {
	public static enum CardType {
		COMMANDER, LIBRARY, TOKEN
	};

	static final short SM_HEIGHT = 300;

	private final CardType type;
	private final int pID;
	private final byte cID, iID;

	/**
	 * Constructs a new GameCard assigned to player {@code playerID} with id
	 * {@code cardID}, type {@code cardType}, and image ID {@code imageID}.
	 * 
	 * @param playerID The owner of this card.
	 * @param cardID   The ID of this card.
	 * @param cardType The CardType of this card.
	 * @param imageID  The image ID of this card.
	 */
	public GameCard(int playerID, byte cardID, CardType cardType, byte imageID) {
		pID = playerID;
		cID = cardID;
		type = cardType;
		iID = imageID;
	}

	/**
	 * Retrieves the CardType of this card.
	 * 
	 * @return The CardType of the card.
	 */
	public CardType getType() {
		return type;
	}

	@Override
	public void draw(Graphics g) {
		Main.CARD_MANAGER.drawCard(g, pID, iID, getX(), getY());
	}
}