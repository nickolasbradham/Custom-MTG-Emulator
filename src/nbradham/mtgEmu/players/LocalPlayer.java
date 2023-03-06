package nbradham.mtgEmu.players;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import nbradham.mtgEmu.GPanel;
import nbradham.mtgEmu.CardManager;

public final class LocalPlayer extends Player {

	private final CardManager cardMan;
	private final int id;

	public LocalPlayer(CardManager cardManager, int playerID) {
		cardMan = cardManager;
		id = playerID;
	}

	public void start() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Custom MTG Emulator");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JMenuBar bar = new JMenuBar();
			JMenu gameMenu = new JMenu("Game");
			JMenuItem loadItem = new JMenuItem("Open Deck", KeyEvent.VK_O);

			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Custom Deck File", "cdf"));
			chooser.setDialogTitle("Select Deck File");

			loadItem.addActionListener(e -> {
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
						cardMan.load(id, chooser.getSelectedFile());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			gameMenu.add(loadItem);

			bar.add(gameMenu);
			frame.setJMenuBar(bar);

			GPanel gameView = new GPanel(this);
			gameView.setPreferredSize(new Dimension(1366, 750));
			frame.setContentPane(gameView);
			frame.pack();
			frame.setVisible(true);
		});
	}

	@Override
	public void drawGame(Graphics g) {
		// TODO draw the field
	}
}