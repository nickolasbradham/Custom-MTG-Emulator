package nbradham.mtgEmu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import nbradham.mtgEmu.gameObjects.GameCard;
import nbradham.mtgEmu.players.Player;

/**
 * Handles card textures.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class CardManager {

	private static final double PI_2 = Math.PI / 2;
	private final HashMap<Byte, Rectangle> maps = new HashMap<>();
	private BufferedImage imageMap;

	/**
	 * Loads a deck file into the texture map and generates the game deck.
	 * 
	 * @param player   The player to tie this deck to.
	 * @param deckFile The file to load.
	 * @return An array holding all the cards.
	 * @throws ZipException Thrown by {@link ZipFile#ZipFile(File)}.
	 * @throws IOException  Thrown by {@link ZipFile#ZipFile(File)},
	 *                      {@link ImageIO#read(java.io.InputStream)},
	 *                      {@link ZipFile#getInputStream(java.util.zip.ZipEntry)},
	 *                      {@link DataInputStream#readShort()},
	 *                      {@link #addCards(DataInputStream, int, ArrayList, ArrayList, CardType)},
	 *                      and {@link DataInputStream#readByte()}.
	 */
	public GameCard[] load(Player player, File deckFile) throws ZipException, IOException {
		DeckFile df = DeckFile.load(deckFile);
		BufferedImage img = df.image();
		int w = img.getWidth(), h = img.getHeight(), mw = w + h, mh = Math.max(h, w);
		BufferedImage loadedImages = new BufferedImage(mw, mh, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = loadedImages.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.translate(w + (h - w) / 2, (w - h) / 2);
		g.rotate(PI_2, w / 2, h / 2);
		g.drawRenderedImage(img, null);

		ArrayList<GameCard> gameCards = new ArrayList<>();
		int pID = player.getID();
		byte c;
		for (CardMap cm : df.details()) {
			c = cm.count();
			for (byte i = 0; i < c; ++i)
				gameCards.add(new GameCard(player, cm.zone(), pID));
		}
		return gameCards.toArray(new GameCard[0]);
	}

	/**
	 * Draws card image {@code iID} from player {@code pID} to Graphics {@code g} at
	 * point ({@code x},{@code y}).
	 * 
	 * @param g         The Graphics to draw to.
	 * @param pID       The player ID of the card.
	 * @param iID       The image ID of the card.
	 * @param x         The x coordinate of the card.
	 * @param y         The y coordinate of the card.
	 * @param fullClamp If the image should be large and clamped to the screen
	 *                  dimensions.
	 * @param tapped    If the card is tapped.
	 * @param flipped   If the card is in the second config.
	 */
	public void drawCard(Graphics g, GameCard c) {
		// TODO Do.
	}

	/**
	 * Draws card back from player {@code pID} to Graphics {@code g} at point
	 * ({@code x},{@code y}).
	 * 
	 * @param g   The Graphics to draw to.
	 * @param pID The player ID of the card back.
	 * @param x   The x coordinate of the card.
	 * @param y   The y coordinate of the card.
	 */
	public void drawBack(Graphics g, int pID, int x, int y) {
		// TODO Do.
	}
}