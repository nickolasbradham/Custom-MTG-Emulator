package nbradham.mtgEmu.gameObjects;

import java.awt.Graphics;
import java.util.Stack;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.players.Player;

public final class Library extends GameObject {

	private final Stack<GameCard> stack = new Stack<>();
	private final Player play;

	public Library(Player player) {
		play = player;
		setPos(1100, 700);
	}

	public final void putOnTop(GameCard c) {
		play.remove(c);
		stack.push(c);
	}

	@Override
	public final void draw(Graphics g) {
		Main.CARD_MANAGER.drawBack(g, play.getID(), getX(), getY());
	}
}