package nbradham.mtgEmu;

public final class Card extends GameObject {
	public static enum CardType {
		COMMANDER, LIBRARY, TOKEN
	};

	private final CardType type;
	private final byte iID;

	public Card(CardType cardType, byte imageID) {
		type = cardType;
		iID = imageID;
	}

	public CardType getType() {
		return type;
	}
}