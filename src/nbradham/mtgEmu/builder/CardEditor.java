package nbradham.mtgEmu.builder;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;

final class CardEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	private final BuilderCard card;

	CardEditor(BuilderCard builderCard) {
		card = builderCard;
		add(new JLabel(new ImageIcon(card.getCfgA())), BorderLayout.LINE_START);
		
	}
}