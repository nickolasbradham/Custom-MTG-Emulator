package nbradham.mtgEmu;

import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
			});

			addButton("Join Game", e -> {
				new EmuClient(JOptionPane.showInputDialog("Enter host address:")).start();
				frame.dispose();
			});

			addButton("Build Deck", e -> {
				new DeckBuilder(frame).start();
			});

			frame.pack();
			frame.setVisible(true);
		});
	}

	/**
	 * Adds a {@link JButton} with set text and listener to the {@link JFrame}
	 * 
	 * @param text     The button text.
	 * @param listener The listener for on click.
	 */
	private void addButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		button.setAlignmentX(.5f);
		frame.add(button);
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