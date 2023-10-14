package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
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

	private boolean hovering, topHovering;

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
	 * Determines if {@code loc} is over this object.
	 * 
	 * @param loc The Point to check.
	 * @return True if {@code loc} is above this object.
	 */
	public final boolean isUnder(Point loc) {
		return space.contains(loc);
	}

	/**
	 * Handles when the mouse is hovering over this object in any capacity.
	 */
	public void onMouseOver() {
		hovering = true;
	}

	/**
	 * Handles when the mouse leaves the area above this object.
	 */
	public void onMouseExit() {
		hovering = false;
	}

	/**
	 * Handles when the mouse is hovering over this object and this object is top
	 * most.
	 */
	public void onMouseOverTop() {
		topHovering = true;
	}

	/**
	 * Handles when the mouse leaves the area over this object and this object is
	 * top most.
	 */
	public void onMouseExitTop() {
		topHovering = false;
	}

	/**
	 * Handles the main draw phase for this object.
	 * 
	 * @param g The Graphics to draw to.
	 */
	public void draw(Graphics g) {
	}

	/**
	 * Handles drawing this object after the main draw phase.
	 * 
	 * @param g The Graphics to draw to.
	 */
	public void drawLate(Graphics g) {
	}

	/**
	 * Handles when the mouse is pressed on this object.
	 * 
	 * @param e Mouse event to process.
	 */
	public void onPressed(MouseEvent e) {
	}

	/**
	 * Handles when the mouse is released on this object.
	 * 
	 * @param e Mouse event to process.
	 */
	public void onReleased(MouseEvent e) {
	}

	/**
	 * Handles when an object is dropped on this object.
	 * 
	 * @param o The object being dropped.
	 */
	public void onObjectDropped(GameObject o) {
	}

	/**
	 * Handles when the mouse is dragged on this object.
	 * 
	 * @param loc Location of the mouse cursor.
	 */
	public void onMouseDragged(Point loc) {
	}

	/**
	 * Handles when the mouse is clicked on this object.
	 * 
	 * @param loc Location of the mouse cursor.
	 */
	public void onClicked(MouseEvent e) {
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
	 * Retrieves the width of this object.
	 * 
	 * @return The width of this object.
	 */
	protected final int getWidth() {
		return space.width;
	}

	/**
	 * Retrieves the height of this object.
	 * 
	 * @return The height of this object.
	 */
	protected final int getHeight() {
		return space.height;
	}

	/**
	 * Adds a child to this object.
	 * 
	 * @param child The child to add.
	 */
	protected final void addChild(GameObject child) {
		children.add(child);
	}

	/**
	 * Removes a child from this object.
	 * 
	 * @param child The child to remove.
	 */
	protected final void removeChild(GameObject child) {
		children.remove(child);
	}

	/**
	 * Removes all children from this instance.
	 */
	protected final void clearChildren() {
		children.clear();
	}

	/**
	 * Retrieves if the cursor is hovering over this object.
	 * 
	 * @return True if the cursor is over this object.
	 */
	protected final boolean isHovering() {
		return hovering;
	}

	/**
	 * Retrieves if the cursor is hovering over this object and it is the top-most
	 * object.
	 * 
	 * @return True if the cursor is over this object and it is the top-most object.
	 */
	protected final boolean isTopHovering() {
		return topHovering;
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

}