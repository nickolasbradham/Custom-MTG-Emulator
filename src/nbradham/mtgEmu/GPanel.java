package nbradham.mtgEmu;

import java.awt.Graphics;

import javax.swing.JPanel;

import nbradham.mtgEmu.players.Player;

public final class GPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Player player;

	public GPanel(Player setPlayer) {
		player = setPlayer;
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		player.drawGame(g);
	}
}