package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public class GameObject {

	protected final ArrayList<GameObject> children = new ArrayList<>();

	private Layer layer = Layer.BATTLEFIELD;
	private int x, y;

	public final void setPos(int newX, int newY) {
		int dx = newX - x, dy = newY - y;
		x = newX;
		y = newY;
		children.forEach(c -> c.translate(dx, dy));
	}

	public void draw(Graphics g) {
	}

	public Layer getLayer() {
		return layer;
	}

	protected final void setLayer(Layer newLayer) {
		layer = newLayer;
	}

	protected final int getX() {
		return x;
	}

	protected final int getY() {
		return y;
	}

	private void translate(int dx, int dy) {
		x += dx;
		y += dy;
		children.forEach(c -> c.translate(dx, dy));
	}
}