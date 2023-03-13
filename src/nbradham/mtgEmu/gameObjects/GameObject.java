package nbradham.mtgEmu.gameObjects;

import java.util.ArrayList;

import nbradham.mtgEmu.players.LocalPlayer.Layer;

public class GameObject {

	protected final ArrayList<GameObject> children = new ArrayList<>();
	protected int x, y;

	private Layer layer = Layer.BATTLEFIELD;

	public final void setPos(int newX, int newY) {
		int difX = newX - x, difY = newY - y;
		x = newX;
		y = newY;
		children.forEach(c -> c.translate(difX, difY));
	}

	protected final void setLayer(Layer newLayer) {
		layer = newLayer;
	}

	private void translate(int dx, int dy) {
		x += dx;
		y += dy;
	}
}