package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

public final class CardZone extends GameObject {

	private final ArrayList<Card> cards = new ArrayList<>();
	private int w, shift;

	public CardZone(int objX, int objY, int width, int hoverShift) {
		x = objX;
		y = objY;
		w = width;
		shift = hoverShift;
	}

	public void add(Card card) {
		cards.add(card);
		int div = w / (cards.size() + 2);
		for (byte i = 0; i < cards.size(); i++)
			cards.get(i).setPos(x + (i + 1) * div, y);
	}
}