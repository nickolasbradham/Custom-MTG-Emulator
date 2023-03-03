package nbradham.mtgEmu;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

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
				ArrayList<BuildCard> cards = new ArrayList<>();
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Select Commander Image(s)");
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileFilter(new FileNameExtensionFilter("Image Files","jpg","png"));
				try {
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					for (File f : chooser.getSelectedFiles())
						cards.add(new BuildCard(ImageIO.read(f)));
				}catch(IOException ex) {
					JOptionPane.showMessageDialog(frame, ex);
				}
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