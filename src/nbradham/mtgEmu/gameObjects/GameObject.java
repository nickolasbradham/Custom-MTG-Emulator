package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public class GameObject {

	protected final ArrayList<GameObject> children = new ArrayList<>();

	private final Rectangle space = new Rectangle();

	private Layer layer = Layer.BATTLEFIELD;

	public final void setPos(int newX, int newY) {
		int dx = newX - space.x, dy = newY - space.y;
		space.setLocation(newX, newY);
		children.forEach(c -> c.translate(dx, dy));
	}

	public final void setWH(int width, int height) {
		space.setSize(width, height);
	}

	public void draw(Graphics g) {
		g.drawRect(space.x, space.y, space.width, space.height);
	}

	public Layer getLayer() {
		return layer;
	}

	protected final void setLayer(Layer newLayer) {
		layer = newLayer;
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

	private void translate(int dx, int dy) {
		space.translate(dx, dy);
		children.forEach(c -> c.translate(dx, dy));
	}
}