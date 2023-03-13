package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public final class CardZone extends GameObject {

	private final ArrayList<Card> cards = new ArrayList<>();
	private int w, shift;

	public CardZone(int objX, int objY, int width, int hoverShift) {
		setLayer(Layer.GUI);
		setPos(objX, objY);
		w = width;
		shift = hoverShift;
	}

	public void add(Card card) {
		cards.add(card);
		children.add(card);
		int div = w / (cards.size() + 2);
		for (byte i = 0; i < cards.size(); i++)
			cards.get(i).setPos(x + (i + 1) * div, y);
	}
}