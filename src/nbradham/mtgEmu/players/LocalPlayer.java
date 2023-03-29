package nbradham.mtgEmu.players;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.gameObjects.GameObject;
import nbradham.mtgEmu.interfaces.ObjectFoundHandler;

/**
 * Handles IO from the player that is on this computer.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class LocalPlayer extends Player {

	/**
	 * Constructs a new LocalPlayer assigned to id {@code playerID}.
	 * 
	 * @param playerID The ID of the player.
	 */
	public LocalPlayer(int playerID) {
		super(playerID);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		handleFirstUnder(e.getPoint(), o -> o.onPressed(e));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GameObject o = getDragging();
		if (o != null)
			o.onReleased(e);
	}

	@Override
	protected void addMenuBarItems(JFrame frame, JMenuBar bar) {
		JMenu gameMenu = new JMenu("Game");
		JMenuItem loadItem = new JMenuItem("Open Deck", KeyEvent.VK_O);

		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Custom Deck File", "cdf"));
		chooser.setDialogTitle("Select Deck File");

		loadItem.addActionListener(e -> {
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
				loadDeck(chooser.getSelectedFile());
		});
		gameMenu.add(loadItem);

		JMenuItem reset = new JMenuItem("Reset Game", KeyEvent.VK_R);
		reset.addActionListener(e -> {
			reset();
		});
		gameMenu.add(reset);

		JMenuItem exitMain = new JMenuItem("Exit to Main", KeyEvent.VK_E);
		exitMain.addActionListener(e -> {
			frame.dispose();
			Main.main(null);
		});
		gameMenu.add(exitMain);

		bar.add(gameMenu);
	}

	@Override
	public final void startDragging(GameObject o) {
		remove(drag = o);
		redrawBuffer();
	}

	@Override
	public final void stopDragging(Point dropLoc) {
		moveToField(drag);
		if (!hovering.isEmpty())
			hovering.forEach(o -> o.onObjectDropped(drag));
		drag = null;
		redrawBuffer();
	}

	@Override
	public final void mouseMoved(Point loc) {
		super.mouseMoved(loc);
		if (drag != null) {
			drag.onMouseDragged(loc);
			gameView.repaint();
		}
	}

	@Override
	public final void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		handleFirstUnder(e.getPoint(), o -> o.onClicked(e));
	}

	/**
	 * Finds the first {@link GameObject} under {@code loc} and calls
	 * {@link ObjectFoundHandler#handle(GameObject)} on it.
	 * 
	 * @param loc The location to check.
	 * @param h   The ObjectFoundHandler to call.
	 */
	private final void handleFirstUnder(Point loc, ObjectFoundHandler h) {
		GameObject clickedObj;
		for (int i = objects.size() - 1; i > -1; --i)
			if ((clickedObj = objects.get(i)).isUnder(loc)) {
				h.handle(clickedObj);
				return;
			}
	}
}