package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.interfaces.Holder;
import nbradham.mtgEmu.players.Player;

/**
 * Represents a single card instance in the game.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class GameCard extends GameObject {

	/**
	 * Used to determine card return location.
	 * 
	 * @author Nickolas S. Bradham
	 *
	 */
	public static enum CardType {
		COMMANDER, LIBRARY, TOKEN
	};

	public static final short SM_HEIGHT = 200, LG_HEIGHT = 350;

	private final Player control;
	private final CardType type;
	private final int ownID, normW;
	private final byte cID, iID;

	private Holder hold;
	private boolean tapped;

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
		setSize(normW = objW, SM_HEIGHT);
	}

	/**
	 * Retrieves the CardType of this card.
	 * 
	 * @return The CardType of the card.
	 */
	public CardType getType() {
		return type;
	}

	/**
	 * Retrieves the ID of the owner of this GameCard.
	 * 
	 * @return The owner ID of this GameCard.
	 */
	public int getOwnerID() {
		return ownID;
	}

	/**
	 * Sets the Holder of this GameCard.
	 * 
	 * @param holder The new Holder of this GameCard.
	 */
	public void setHolder(Holder holder) {
		hold = holder;
	}

	/**
	 * Sets the tap state of this GameCard.
	 * 
	 * @param tap If this GameCard is tapped.
	 */
	public void setTapped(boolean tap) {
		tapped = tap;
		setSize(tap ? SM_HEIGHT : normW, tap ? normW : SM_HEIGHT);
		control.redrawBuffer();
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (!(isTopHovering() && control.getDragging() != this))
			Main.CARD_MANAGER.drawCard(g, ownID, iID, getX(), getY(), false, tapped);
	}

	@Override
	public void drawLate(Graphics g) {
		super.drawLate(g);
		if (isTopHovering() && control.getDragging() != this)
			Main.CARD_MANAGER.drawCard(g, ownID, iID, getX(), getY(), true, tapped);
	}

	@Override
	public void onMouseOverTop() {
		super.onMouseOverTop();
		control.redrawBuffer();
	}

	@Override
	public void onMouseExitTop() {
		super.onMouseExitTop();
		control.redrawBuffer();
	}

	@Override
	public void onPressed(MouseEvent e) {
		super.onPressed(e);
		if (e.getButton() == MouseEvent.BUTTON1) {
			control.startDragging(this);
			if (hold != null) {
				hold.remove(this);
				setHolder(null);
			}
		}
	}

	@Override
	public void onReleased(MouseEvent e) {
		super.onReleased(e);
		if (e.getButton() == MouseEvent.BUTTON1 && control.getDragging() == this)
			control.stopDragging(e.getPoint());
		control.mouseMoved(e.getPoint());
	}

	@Override
	public void onMouseDragged(Point loc) {
		super.onMouseDragged(loc);
		setPos(loc.x - getWidth() / 2, loc.y - getHeight() / 2);
	}

	@Override
	public void onClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			setTapped(!tapped);
			control.mouseMoved(e.getPoint());
		}
	}
}