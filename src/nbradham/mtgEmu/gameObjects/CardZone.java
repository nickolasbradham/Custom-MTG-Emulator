package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public final class CardZone extends GameObject {

	private final ArrayList<Card> cards = new ArrayList<>();
	private int w;

	public CardZone(int objX, int objY, int width) {
		setLayer(Layer.GUI);
		setPos(objX, objY);
		w = width;
	}

	public void add(Card card) {
		cards.add(card);
		children.add(card);
		int div = w / (cards.size() + 2), x = getX(), y = getY();
		for (byte i = 0; i < cards.size(); i++)
			cards.get(i).setPos(x + (i + 1) * div, y);
	}
}