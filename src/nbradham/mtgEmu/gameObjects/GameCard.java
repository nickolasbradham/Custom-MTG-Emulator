package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.players.Player;

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

	public static final short SM_HEIGHT = 200, LG_HEIGHT = 350;

	private final Player control;
	private final CardType type;
	private final int ownID;
	private final byte cID, iID;
	private boolean hovering;

	/**
	 * Constructs a new GameCard assigned to player {@code playerID} with id
	 * {@code cardID}, type {@code cardType}, and image ID {@code imageID}.
	 * 
	 * @param owner    The owner of this card.
	 * @param cardID   The ID of this card.
	 * @param cardType The CardType of this card.
	 * @param imageID  The image ID of this card.
	 */
	public GameCard(Player owner, byte cardID, CardType cardType, byte imageID, int objW) {
		this(owner.getID(), cardID, cardType, imageID, objW, owner);
	}

	/**
	 * Constructs a new GameCard assigned to player {@code playerID} with id
	 * {@code cardID}, type {@code cardType}, and image ID {@code imageID}.
	 * 
	 * @param ownerID    The owner of this card.
	 * @param cardID     The ID of this card.
	 * @param cardType   The CardType of this card.
	 * @param imageID    The image ID of this card.
	 * @param controller The controller of this card.
	 */
	public GameCard(int ownerID, byte cardID, CardType cardType, byte imageID, int objW, Player controller) {
		control = controller;
		ownID = ownerID;
		cID = cardID;
		type = cardType;
		iID = imageID;
		setSize(objW, SM_HEIGHT);
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
		Main.CARD_MANAGER.drawCard(g, ownID, iID, getX(), getY(), false);
	}

	@Override
	public void drawLate(Graphics g) {
		if (hovering)
			Main.CARD_MANAGER.drawCard(g, ownID, iID, getX(), getY(), true);
	}

	@Override
	public void onMouseOverTop() {
		hovering = true;
		control.redrawBuffer();
	}

	@Override
	public void onMouseExit() {
		hovering = false;
		control.redrawBuffer();
	}
}