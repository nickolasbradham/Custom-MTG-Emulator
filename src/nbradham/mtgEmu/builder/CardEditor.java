package nbradham.mtgEmu.builder;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;

final class CardEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final ImageIcon CFGB_SET = new ImageIcon(CardEditor.class.getResource("/nbradham/mtgEmu/builder/setb.png"));

	private final BuilderCard card;

	CardEditor(BuilderCard builderCard) {
		card = builderCard;
		add(new JLabel(new ImageIcon(card.getCfgA())), BorderLayout.LINE_START);
		add(new JLabel(CFGB_SET), BorderLayout.LINE_END);
	}
}