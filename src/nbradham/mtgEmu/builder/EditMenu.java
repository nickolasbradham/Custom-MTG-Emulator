package nbradham.mtgEmu.builder;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Handles individual BuilderCard edit tasks.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class EditMenu {

	private final JPopupMenu menu = new JPopupMenu();
	private CardEditor call;

	/**
	 * Constructs a new EditMenu.
	 */
	EditMenu() {
		createMenuItem("Delete", () -> call.delete());
		createMenuItem("Replace",
				() -> call.getBuilder().getChooser().prompt("Select new card front", false, f -> call.replaceCard(f)));
		JMenu bFig = new JMenu("Set 2nd Config");
		createMenuItem(bFig, "Custom...",
				() -> call.getBuilder().getChooser().prompt("Select 2nd config image", false, f -> call.setB(f)));
		createMenuItem(bFig, "None", () -> call.setB(null));
		createMenuItem(bFig, "Rotate 180", () -> {
			call.flipForB();
		});
		menu.add(bFig);
	}

	/**
	 * Creates a new {@link JMenuItem} and adds it to {@code menu}.
	 * 
	 * @param label The label of the item.
	 * @param act   The action to take when clicked.
	 */
	private void createMenuItem(String label, Runnable act) {
		createMenuItem(menu, label, act);
	}

	/**
	 * Creates a new {@link JMenuItem} and adds it to {@code menu}.
	 * 
	 * @param menu  The menu to add the item to.
	 * @param label The label of the item.
	 * @param act   The action to take when clicked.
	 */
	private void createMenuItem(JComponent menu, String label, Runnable run) {
		JMenuItem custom = new JMenuItem(label);
		custom.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(ActionEvent e) {
				run.run();
			}
		});
		menu.add(custom);
	}

	/**
	 * Shows the menu for a specific CardEditor.
	 * 
	 * @param caller The calling CardEditor.
	 */
	void showFor(CardEditor caller) {
		menu.show(call = caller, 0, 0);
	}
}