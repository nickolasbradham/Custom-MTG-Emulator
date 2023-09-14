package nbradham.mtgEmu.interfaces;

import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Handles when a {@link GameCard} is dropped on another object.
 */
@FunctionalInterface
public interface DropHandler {

	/**
	 * Handles the card dropped.
	 * 
	 * @param c The GameCard dropped.
	 */
	void handle(GameCard c);
}
