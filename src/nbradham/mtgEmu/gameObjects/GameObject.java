package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Holds code used by all game objects.
 * 
 * @author Nickolas S. Bradham
 *
 */
public class GameObject {

	private final ArrayList<GameObject> children = new ArrayList<>();
	private final Rectangle space = new Rectangle();

	private int index = -1;

	/**
	 * Sets the position of this object.
	 * 
	 * @param newX The new X coordinate.
	 * @param newY The new Y coordinate.
	 */
	public final void setPos(int newX, int newY) {
		int dx = newX - space.x, dy = newY - space.y;
		space.setLocation(newX, newY);
		children.forEach(c -> c.translate(dx, dy));
	}

	/**
	 * Sets the size of this object.
	 * 
	 * @param width  The new width of the object.
	 * @param height The new height of the object.
	 */
	public final void setSize(int width, int height) {
		space.setSize(width, height);
	}

	/**
	 * Translates this object by ({@code dx}, {@code dy}).
	 * 
	 * @param dx The difference in X.
	 * @param dy The difference in Y.
	 */
	private void translate(int dx, int dy) {
		space.translate(dx, dy);
		children.forEach(c -> c.translate(dx, dy));
	}

	/**
	 * Default draw code. Should be overrided by subclasses with visible elements.
	 * 
	 * @param g The Graphics to draw to.
	 */
	public void draw(Graphics g) {
	}

	/**
	 * Sets the index of this object.
	 * 
	 * @param newIndex The new index.
	 */
	public final void setIndex(int newIndex) {
		index = newIndex;
	}

	/**
	 * Retrieves the index of this object.
	 * 
	 * @return The index of this object.
	 */
	public final int getIndex() {
		return index;
	}

	/**
	 * Retrieves the X coordinate of this object.
	 * 
	 * @return The X coordinate of this object.
	 */
	protected final int getX() {
		return space.x;
	}

	/**
	 * Retrieves the Y coordinate of this object.
	 * 
	 * @return The Y coordinate of this object.
	 */
	protected final int getY() {
		return space.y;
	}

	/**
	 * Retrieves the width coordinate of this object.
	 * 
	 * @return The width coordinate of this object.
	 */
	protected final int getWidth() {
		return space.width;
	}

	/**
	 * Adds a child to this object.
	 * 
	 * @param child The child to add.
	 */
	protected final void addChild(GameObject child) {
		children.add(child);
	}
}