package nbradham.mtgEmu.players;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import nbradham.mtgEmu.Main;
import nbradham.mtgEmu.gameObjects.GameCard;

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
	protected void addMenuBarItems(JFrame frame, JMenuBar bar) {
		JMenu gameMenu = new JMenu("Game");
		JMenuItem loadItem = new JMenuItem("Open Deck", KeyEvent.VK_O);

		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Custom Deck File", "cdf"));
		chooser.setDialogTitle("Select Deck File");

		loadItem.addActionListener(e -> {
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				try {
					for (GameCard c : Main.CARD_MANAGER.load(this, chooser.getSelectedFile()))
						switch (c.getType()) {
						case COMMANDER:
							commandZone.add(c);
							break;
						case LIBRARY:
							lib.putOnTop(c);
							break;
						case TOKEN:
							// TODO add to tokens.
						}
					redrawBuffer();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		gameMenu.add(loadItem);

		JMenuItem exitMain = new JMenuItem("Exit to Main", KeyEvent.VK_E);
		exitMain.addActionListener(e -> {
			frame.dispose();
			Main.main(null);
		});
		gameMenu.add(exitMain);

		bar.add(gameMenu);
	}
}