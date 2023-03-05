package nbradham.mtgEmu;

import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import nbradham.mtgEmu.builder.DeckBuilder;
import nbradham.mtgEmu.players.LocalPlayer;

final class Launcher {

	private final JFrame frame = new JFrame("Custom MTG Emulator");

	private void createGUI() {
		SwingUtilities.invokeLater(() -> {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

			addButton("Singlplayer", e -> {
				new LocalPlayer().start();
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

	private void addButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		button.setAlignmentX(.5f);
		frame.add(button);
	}

	public static void main(String[] args) {
		new Launcher().createGUI();
	}
}