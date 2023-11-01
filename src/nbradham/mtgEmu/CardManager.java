package nbradham.mtgEmu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.ZipException;
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
	private static final byte MAPY = 0, MAPW = 1, MAPH = 2, UVY = 1;
	private final HashMap<GameCard, short[]> cardUVs = new HashMap<>();
	private ArrayList<int[]> maps = new ArrayList<>();
	private BufferedImage imageMap = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);

	/**
	 * Loads a deck file into the texture map and generates the game deck.
	 * 
	 * @param player   The player to tie this deck to.
	 * @param deckFile The file to load.
	 * @return An array holding all the cards.
	 * @throws ZipException Thrown by {@link DeckFile#load(File)}.
	 * @throws IOException  Thrown by {@link DeckFile#load(File)}.
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
		int pID = player.getID();
		ArrayList<int[]> newMaps = new ArrayList<>(maps);
		int[] tmpA = { pID == 0 ? 0 : (tmpA = newMaps.get(pID - 1))[MAPY] + tmpA[MAPH], loadedImages.getWidth(),
				loadedImages.getHeight() }, tmpB;
		if (pID < newMaps.size())
			newMaps.set(pID, tmpA);
		else
			newMaps.add(tmpA);
		int nw = 0, nms = newMaps.size();
		for (int[] m : newMaps)
			nw = Math.max(nw, m[MAPW]);
		for (int i = pID + 1; i < nms; ++i) {
			tmpA = Arrays.copyOf(tmpA = newMaps.get(i), tmpA.length);
			tmpA[MAPY] = (tmpB = newMaps.get(i - 1))[MAPY] + tmpB[MAPH];
			newMaps.set(i, tmpA);
		}
		img = new BufferedImage(nw, (tmpA = newMaps.get(nms - 1))[MAPY] + tmpA[MAPH], BufferedImage.TYPE_INT_ARGB_PRE);
		g = img.createGraphics();
		if (pID > 0)
			g.drawImage(imageMap, 0, 0, nw, (tmpA = newMaps.get(pID))[MAPY], 0, 0, nw, tmpA[MAPY], null);
		g.drawImage(loadedImages, 0, tmpA[MAPY], null);
		if (pID < maps.size() - 1)
			g.drawImage(imageMap, 0, tmpA[MAPY] + tmpA[MAPH], imageMap.getWidth(), img.getHeight(), 0,
					(tmpA = maps.get(pID + 1))[MAPY] + tmpA[MAPH], imageMap.getWidth(), tmpA[MAPY] + tmpA[MAPH], null);
		imageMap = img;
		maps = newMaps;
		ArrayList<GameCard> gameCards = new ArrayList<>();
		byte c;
		GameCard tc;
		for (CardMap cm : df.details()) {
			c = cm.count();
			cardUVs.put(tc = new GameCard(player, cm.zone(), pID), cm.origins());
			for (byte i = 0; i < c; ++i)
				gameCards.add(tc);
		}
		return gameCards.toArray(new GameCard[0]);
	}

	/**
	 * Draws card image {@code iID} from player {@code pID} to Graphics {@code g} at
	 * point ({@code x},{@code y}).
	 * 
	 * @param g The Graphics to draw to.
	 * @param c The card to draw.
	 */
	public void drawCard(Graphics g, GameCard c) {
		short[] cuvs = cardUVs.get(c);
		int x = c.getX(), y = c.getY(), sy = maps.get(c.getOwnerID())[MAPY] + cuvs[UVY];
		g.drawImage(imageMap, x, y, x + c.getWidth(), y + c.getHeight(), cuvs[CardMap.IND_X], sy,
				cuvs[CardMap.IND_X] + GameCard.LG_WIDTH, sy + GameCard.LG_HEIGHT, null);
	}
}