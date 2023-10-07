package nbradham.mtgEmu.builder;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import nbradham.mtgEmu.Type;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

final class CardEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	private final BuilderCard card;

	CardEditor(BuilderCard builderCard) {
		super(new BorderLayout());
		card = builderCard;
		add(new JLabel(new ImageIcon(card.getCfgA())), BorderLayout.LINE_START);
		JPanel foot = new JPanel();
		foot.add(new JComboBox<>(Type.values()));
		foot.add(new JLabel("Qty:"));
		foot.add(new JSpinner(new SpinnerNumberModel(1, 1, 99, 1)));
		add(foot, BorderLayout.PAGE_END);
	}
}