package nbradham.mtgEmu.interfaces;

import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Handles a {@link GameCard}.
 */
@FunctionalInterface
public interface GameCardHandler {

	/**
	 * Handles the card .
	 * 
	 * @param c The GameCard to handle.
	 */
	void handle(GameCard c);
}
