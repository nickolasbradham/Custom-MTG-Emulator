package nbradham.mtgEmu;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import nbradham.mtgEmu.players.Player;

public final class GPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final short WIDTH = 1366, HEIGHT = 750;

	private final Player player;

	public GPanel(Player setPlayer) {
		super();
		player = setPlayer;
		setPreferredSize(new Dimension(1366, 750));
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		player.drawGame(g);
	}
}