package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;

import nbradham.mtgEmu.Main;

public final class GameCard extends GameObject {
	public static enum CardType {
		COMMANDER, LIBRARY, TOKEN
	};

	static final short SM_HEIGHT = 300;

	private final CardType type;
	private final int pID;
	private final byte cID, iID;

	public GameCard(int playerID, byte cardID, CardType cardType, byte imageID) {
		pID = playerID;
		cID = cardID;
		type = cardType;
		iID = imageID;
	}

	public CardType getType() {
		return type;
	}

	@Override
	public void draw(Graphics g) {
		Main.CARD_MANAGER.drawCard(g, pID, iID, getX(), getY());
	}
}