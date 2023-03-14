package nbradham.mtgEmu.gameObjects;

import java.util.Stack;

public final class Library extends GameObject {

	private final Stack<Card> stack = new Stack<>();

	public Library() {
		setPos(1100, 700);
	}

	public final void putOnTop(Card c) {
		stack.push(c);
	}
}