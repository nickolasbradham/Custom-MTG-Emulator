package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public final class CardZone extends GameObject {

	private final ArrayList<GameCard> cards = new ArrayList<>();

	public CardZone(int objX, int objY, int width) {
		setLayer(Layer.GUI);
		setPos(objX, objY);
		setWH(width, GameCard.SM_HEIGHT);
	}

	public void add(GameCard card) {
		cards.add(card);
		children.add(card);
		int div = getWidth() / (cards.size() + 2), x = getX(), y = getY();
		for (byte i = 0; i < cards.size(); i++)
			cards.get(i).setPos(x + (i + 1) * div, y);
	}
}