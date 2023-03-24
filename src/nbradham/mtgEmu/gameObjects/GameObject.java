package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class GameObject {

	protected final ArrayList<GameObject> children = new ArrayList<>();

	private final Rectangle space = new Rectangle();

	private int index = -1;

	public final void setPos(int newX, int newY) {
		int dx = newX - space.x, dy = newY - space.y;
		space.setLocation(newX, newY);
		children.forEach(c -> c.translate(dx, dy));
	}

	public final void setWH(int width, int height) {
		space.setSize(width, height);
	}

	private void translate(int dx, int dy) {
		space.translate(dx, dy);
		children.forEach(c -> c.translate(dx, dy));
	}

	public void draw(Graphics g) {
		g.drawRect(space.x, space.y, space.width, space.height);
	}

	public final void setIndex(int newIndex) {
		index = newIndex;
	}

	public final int getIndex() {
		return index;
	}

	protected final int getX() {
		return space.x;
	}

	protected final int getY() {
		return space.y;
	}

	protected final int getWidth() {
		return space.width;
	}
}