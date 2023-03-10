package nbradham.mtgEmu.players;

import java.util.ArrayList;
import nbradham.mtgEmu.Card;
import nbradham.mtgEmu.GameObject;

final class CardZone extends GameObject {

	private final ArrayList<Card> cards = new ArrayList<>();
	private final boolean inf;
	private int w, shift;

	public CardZone(int objX, int objY, int width, int hoverShift) {
		this(objX, objY, width, hoverShift, false);
	}

	public CardZone(int objX, int objY, int width, int hoverShift, boolean infinite) {
		x = objX;
		y = objY;
		w = width;
		shift = hoverShift;
		inf = infinite;
	}

	void add(Card card) {
		cards.add(card);
		int div = w / (cards.size() + 2);
		for (byte i = 0; i < cards.size(); i++)
			cards.get(i).setPos(x + (i + 1) * div, y);
	}
}