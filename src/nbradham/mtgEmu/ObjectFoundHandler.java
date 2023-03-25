package nbradham.mtgEmu;

import nbradham.mtgEmu.gameObjects.GameObject;

/**
 * Used when a object search is required in lambda form.
 * 
 * @author Nickolas S. Bradham
 *
 */
@FunctionalInterface
public interface ObjectFoundHandler {

	/**
	 * Called when target object is found.
	 * 
	 * @param clickedObj The object found.
	 */
	void handle(GameObject clickedObj);
}