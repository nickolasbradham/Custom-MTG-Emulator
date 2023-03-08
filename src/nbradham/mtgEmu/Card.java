package nbradham.mtgEmu;

final class Card {
	static enum CardType {
		COMMANDER, LIBRARY, TOKEN
	};

	private final CardType type;
	private final byte iID;

	public Card(CardType cardType, byte imageID) {
		type = cardType;
		iID = imageID;
	}
}