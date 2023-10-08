package nbradham.mtgEmu.builder;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import nbradham.mtgEmu.Type;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 * Handles the GUI of a single card in the editor.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class CardEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final EditMenu POPUP = new EditMenu();

	private final BuilderCard card;
	private final CardEditor self = this;

	/**
	 * Constructs a new CardEditor.
	 * 
	 * @param builderCard The BuilderCard that this editor handles.
	 */
	CardEditor(BuilderCard builderCard) {
		super(new BorderLayout());
		card = builderCard;
		add(new JLabel(new ImageIcon(card.getCfgA())), BorderLayout.LINE_START);
		JPanel foot = new JPanel();
		foot.add(new JComboBox<>(Type.values()));
		foot.add(new JLabel("Qty:"));
		foot.add(new JSpinner(new SpinnerNumberModel(1, 1, 99, 1)));
		add(foot, BorderLayout.PAGE_END);
		addMouseListener(new MouseAdapter() {
			@Override
			public final void mouseClicked(MouseEvent e) {
				POPUP.showFor(self);
			}
		});
	}
}