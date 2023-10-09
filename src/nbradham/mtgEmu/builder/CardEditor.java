package nbradham.mtgEmu.builder;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nbradham.mtgEmu.Zone;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		add(aLab = new JLabel(new ImageIcon(card.getAimg())), BorderLayout.LINE_START);
		add(bLab, BorderLayout.LINE_END);
		JPanel foot = new JPanel();
		foot.add(new JLabel("Zone:"));
		JComboBox<Zone> zBox = new JComboBox<>(Zone.values());
		zBox.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(ActionEvent e) {
				card.setZone((Zone) zBox.getSelectedItem());
			}
		});
		foot.add(zBox);
		foot.add(new JLabel("Qty:"));
		JSpinner spin = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
		spin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				card.setQty(((Integer) spin.getValue()).byteValue());
			}
		});
		foot.add(spin);
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
	 * @param img The new image to load.
	 */
	void replaceCard(Image img) {
		setB(null);
		card.setAimg(img);
		aLab.setIcon(new ImageIcon(card.getAimg()));
	}

	void flipForB() {
		bLab.setIcon(new ImageIcon(card.flipForB()));
	}

	void setB(Image img) {
		card.setBimg(img);
		bLab.setIcon(img == null ? null : new ImageIcon(img));
	}
}