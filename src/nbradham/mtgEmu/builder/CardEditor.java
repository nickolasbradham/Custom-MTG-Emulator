package nbradham.mtgEmu.builder;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import nbradham.mtgEmu.Type;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
	private final DeckBuilder db;
	private final JLabel aLab, bLab = new JLabel();

	/**
	 * Constructs a new CardEditor.
	 * 
	 * @param builderCard The BuilderCard that this editor handles.
	 * @param deckBuilder The controlling Builder.
	 */
	CardEditor(BuilderCard builderCard, DeckBuilder deckBuilder) {
		super(new BorderLayout());
		card = builderCard;
		db = deckBuilder;
		add(aLab = new JLabel(new ImageIcon(card.getCfgA())), BorderLayout.LINE_START);
		add(bLab, BorderLayout.LINE_END);
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

	/**
	 * Removes this CardEditor and related card.
	 */
	void delete() {
		db.remove(this);
	}

	/**
	 * Retrieves the BuilderCard of this CardEditor.
	 * 
	 * @return The BuilderCard
	 */
	BuilderCard getCard() {
		return card;
	}

	/**
	 * Retrieves the DeckBuilder.
	 * 
	 * @return The DeckBuilder.
	 */
	DeckBuilder getBuilder() {
		return db;
	}

	/**
	 * Replaces the card in this editor.
	 * 
	 * @param f The new image to load.
	 */
	void replaceCard(File f) {
		card.loadA(f);
		aLab.setIcon(new ImageIcon(card.getCfgA()));
	}

	/**
	 * Sets the B config.
	 * 
	 * @param f The image to load.
	 */
	void setB(File f) {
		card.loadB(f);
		bLab.setIcon(f == null ? null : new ImageIcon(card.getCfgB()));
	}

	/**
	 * Sets the B config.
	 * 
	 * @param img The image to use.
	 */
	void setBimg(Image img) {
		card.setB(img);
		bLab.setIcon(new ImageIcon(img));
	}
}