package nbradham.mtgEmu;

import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import nbradham.mtgEmu.builder.DeckBuilder;
import nbradham.mtgEmu.players.LocalPlayer;

/**
 * Handles main menu GUI.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class Main {

	public static final CardManager CARD_MANAGER = new CardManager();
	public static final String FNAME_CARDS = "cards.png", FNAME_DAT = "info.bin", FNAME_EXT = "cdf";
	public static final FileNameExtensionFilter FILTER_DECK = new FileNameExtensionFilter("Custom Deck File",
			FNAME_EXT);

	private static final String R_VERSION = "v0.4";

	private final JFrame frame = new JFrame("Custom MTG Emulator");

	/**
	 * Creates and shows the GUI on the AWT thread.
	 */
	private void createGUI() {
		SwingUtilities.invokeLater(() -> {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

			addButton("Singlplayer", e -> {
				new LocalPlayer(0).start();
				frame.dispose();
			});

			addButton("Host Game", e -> {
				new EmuServer().start();
				new EmuClient("localhost").start();
				frame.dispose();
			}).setEnabled(false);

			addButton("Join Game", e -> {
				new EmuClient(JOptionPane.showInputDialog("Enter host address:")).start();
				frame.dispose();
			}).setEnabled(false);

			addButton("Deck Builder", e -> {
				new DeckBuilder(frame).start();
			});

			frame.pack();
			frame.setVisible(true);
		});
		try {
			String s = new String(
					new URI("https://api.github.com/repos/nickolasbradham/Custom-MTG-Emulator/releases/latest").toURL()
							.openStream().readAllBytes());
			int st = s.indexOf("\"body\":\"") + 8, e = s.indexOf('"', st), ns = s.indexOf("\"tag_name\":\"") + 12,
					ne = s.indexOf('"', ns);
			if (!s.substring(ns, ne).equals(R_VERSION) && JOptionPane.showConfirmDialog(frame,
					"There is a new version released. Would you like to download it? Here are the release notes:"
							+ System.lineSeparator() + s.substring(st, e).replace("\\r\\n", System.lineSeparator()),
					"Update Available", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				Desktop.getDesktop()
						.browse(new URI("https://github.com/nickolasbradham/Custom-MTG-Emulator/releases/latest"));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a {@link JButton} with set text and listener to the {@link JFrame}
	 * 
	 * @param text     The button text.
	 * @param listener The listener for on click.
	 * @return The newly create JButton.
	 */
	private JButton addButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		button.setAlignmentX(.5f);
		frame.add(button);
		return button;
	}

	/**
	 * Constructs and starts a new Launcher instance.
	 * 
	 * @param args Ignored.
	 */
	public static void main(String[] args) {
		new Main().createGUI();
	}
}