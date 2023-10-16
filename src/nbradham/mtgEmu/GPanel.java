package nbradham.mtgEmu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import nbradham.mtgEmu.players.Player;

/**
 * The GUI view to draw game elements to.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class GPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final short WIDTH = 1366, HEIGHT = 750;

	private final Player player;

	/**
	 * Creates a new GPanel instance assigned to {@code setPlayer}.
	 * 
	 * @param setPlayer
	 */
	public GPanel(Player setPlayer) {
		super();
		player = setPlayer;
		setPreferredSize(new Dimension(1366, 750));
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public final void mouseMoved(MouseEvent e) {
				player.mouseMoved(e.getPoint());
			}

			@Override
			public final void mouseDragged(MouseEvent e) {
				player.mouseMoved(e.getPoint());
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public final void mousePressed(MouseEvent e) {
				player.mousePressed(e);
			}

			@Override
			public final void mouseReleased(MouseEvent e) {
				player.mouseReleased(e);
			}

			@Override
			public final void mouseClicked(MouseEvent e) {
				player.mouseClicked(e);
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public final void keyTyped(KeyEvent e) {
				player.keyTyped(e);
			}
		});
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		player.drawGame((Graphics2D) g);
	}
}