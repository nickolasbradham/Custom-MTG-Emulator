package nbradham.mtgEmu.interfaces;

import nbradham.mtgEmu.gameObjects.GameCard;

@FunctionalInterface
public interface DropHandler {

	void handle(GameCard c);
}
