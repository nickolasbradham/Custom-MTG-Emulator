package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import nbradham.mtgEmu.Zone;
import nbradham.mtgEmu.interfaces.GameCardHandler;
import nbradham.mtgEmu.players.Player;

/**
 * Represents a single card instance in the game.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class GameCard extends GameObject {

	public static final short SM_HEIGHT = 200, SM_WIDTH = 143, LG_HEIGHT = 350, LG_WIDTH = 250;

	private final Player control;
	private final Zone zone;
	private final int ownID;

	private GameCardHandler hold;
	private boolean tapped, flipped = false;

	/**
	 * Constructs a new GameCard assigned to player {@code playerID} with id
	 * {@code cardID}, type {@code cardType}, and image ID {@code imageID}.
	 * 
	 * @param owner    The owner of this card.
	 * @param cardID   The ID of this card.
	 * @param cardZone The Zone of this card.
	 */
	public GameCard(Player owner, Zone cardZone, int objW) {
		this(owner.getID(), cardZone, owner);
	}

	/**
	 * Constructs a new GameCard assigned to player {@code playerID} with id
	 * {@code cardID}, type {@code cardType}, and image ID {@code imageID}.
	 * 
	 * @param ownerID    The owner of this card.
	 * @param cardID     The ID of this card.
	 * @param cardZone   The Zone of this card.
	 * @param controller The controller of this card.
	 */
	public GameCard(int ownerID, Zone cardZone, Player controller) {
		control = controller;
		ownID = ownerID;
		zone = cardZone;
		setSize(SM_WIDTH, SM_HEIGHT);
	}

	/**
	 * Retrieves the CardType of this card.
	 * 
	 * @return The CardType of the card.
	 */
	public Zone getType() {
		return zone;
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
	public void setHolder(GameCardHandler holder) {
		hold = holder;
	}

	/**
	 * Sets the tap state of this GameCard.
	 * 
	 * @param tap If this GameCard is tapped.
	 */
	public void setTapped(boolean tap) {
		tapped = tap;
		setSize(tap ? SM_HEIGHT : SM_WIDTH, tap ? SM_WIDTH : SM_HEIGHT);
		control.redrawBuffer();
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (!(isTopHovering() && control.getDragging() != this))
			// TODO do.
			;
	}

	@Override
	public void drawLate(Graphics g) {
		super.drawLate(g);
		if (isTopHovering() && control.getDragging() != this)
			// TODO do.
			;
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
				hold.handle(this);
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

	@Override
	public void onTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F)
			flipped = !flipped;
		control.redrawBuffer();
	}
}