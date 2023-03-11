package nbradham.mtgEmu.gameObjects;

public final class Card extends GameObject {
	public static enum CardType {
		COMMANDER, LIBRARY, TOKEN
	};

	private final CardType type;
	private final byte cID, iID;

	public Card(byte cardID, CardType cardType, byte imageID) {
		cID = cardID;
		type = cardType;
		iID = imageID;
	}

	public CardType getType() {
		return type;
	}
}