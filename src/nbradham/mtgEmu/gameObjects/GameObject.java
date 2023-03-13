package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public class GameObject {

	protected final ArrayList<GameObject> children = new ArrayList<>();
	protected int x, y;

	private Layer layer = Layer.BATTLEFIELD;

	public final void setPos(int newX, int newY) {
		int dx = newX - x, dy = newY - y;
		x = newX;
		y = newY;
		children.forEach(c -> c.translate(dx, dy));
	}

	protected final void setLayer(Layer newLayer) {
		layer = newLayer;
	}

	private void translate(int dx, int dy) {
		x += dx;
		y += dy;
		children.forEach(c -> c.translate(dx, dy));
	}
}