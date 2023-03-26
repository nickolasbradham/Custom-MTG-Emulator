package nbradham.mtgEmu;

import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Implementing classes are able to hold GameCard instances.
 * 
 * @author Nickolas S. Bradham
 *
 */
public interface Holder {

	/**
	 * Removes {@code gameCard} from the holder.
	 * 
	 * @param gameCard The GameCard to remove.
	 */
	void remove(GameCard gameCard);
}