package nbradham.mtgEmu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import nbradham.mtgEmu.gameObjects.GameCard;
import nbradham.mtgEmu.gameObjects.GameCard.CardType;
import nbradham.mtgEmu.players.Player;

/**
 * Handles card textures.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class CardManager {

	private static final double PI_2 = Math.PI / 2;

	private ArrayList<CardUVs> cardUVs = new ArrayList<>();
	private BufferedImage imageMap;
	private byte cardID, imgID;

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
		ZipFile zFile = new ZipFile(deckFile);

		BufferedImage img = ImageIO.read(zFile.getInputStream(zFile.getEntry("cards.png")));
		int w = img.getWidth(), h = img.getHeight(), mw = w + h, mh = Math.max(h, w);
		BufferedImage loadedImages = new BufferedImage(mw, mh, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = loadedImages.createGraphics();

		g.drawImage(img, 0, 0, null);
		g.translate(w + (h - w) / 2, (w - h) / 2);
		g.rotate(PI_2, w / 2, h / 2);
		g.drawRenderedImage(img, null);

		DataInputStream info = new DataInputStream(zFile.getInputStream(zFile.getEntry("info.bin")));
		short cw = info.readShort(), ch = info.readShort();
		ArrayList<GameCard> gameCards = new ArrayList<>();
		ArrayList<short[]> uvOrigins = new ArrayList<>();
		int scaleW = CardUVs.scaleWidth(cw, GameCard.SM_HEIGHT, ch), pID = player.getID();
		cardID = -1;
		imgID = -1;

		addCards(info, player, gameCards, uvOrigins, CardType.COMMANDER, scaleW);
		byte count = info.readByte();
		for (byte i = 0; i < count; ++i) {
			byte dupes = info.readByte();
			short[] tuv = new short[] { info.readShort(), info.readShort() };
			uvOrigins.add(tuv);
			++imgID;
			for (byte n = 0; n < dupes; ++n)
				gameCards.add(new GameCard(player, ++cardID, CardType.LIBRARY, imgID, scaleW));
		}
		addCards(info, player, gameCards, uvOrigins, CardType.LIBRARY, scaleW);
		addCards(info, player, gameCards, uvOrigins, CardType.TOKEN, scaleW);

		zFile.close();

		ArrayList<CardUVs> newCardUVs = new ArrayList<>(cardUVs);
		CardUVs newSet = new CardUVs(mw, mh, cw, ch, uvOrigins.toArray(new short[0][]), scaleW);
		if (pID < newCardUVs.size())
			newCardUVs.set(pID, newSet);
		else
			newCardUVs.add(newSet);

		int widest = -1;
		for (CardUVs uv : newCardUVs)
			widest = Math.max(widest, uv.getMapWidth());

		CardUVs last = newCardUVs.get(0), cur;
		int end = newCardUVs.size();
		for (byte i = 1; i < end; ++i) {
			(cur = newCardUVs.get(i)).setMapOffset(last.getMapOffset() + last.getMapHeight());
			last = cur;
		}

		BufferedImage newImageMap = new BufferedImage(widest, last.getMapOffset() + last.getMapHeight(),
				BufferedImage.TYPE_4BYTE_ABGR_PRE);
		Graphics newG = newImageMap.createGraphics();

		int newOff, lastOff;
		for (byte i = 0; i < end; i++)
			if (i == pID)
				newG.drawImage(loadedImages, 0, newSet.getMapOffset(), null);
			else
				newG.drawImage(imageMap, 0, newOff = (cur = newCardUVs.get(i)).getMapOffset(), cur.getMapWidth(),
						newOff + cur.getMapHeight(), 0, lastOff = (last = newCardUVs.get(i)).getMapOffset(),
						last.getMapWidth(), lastOff + last.getMapHeight(), null);
		imageMap = newImageMap;
		cardUVs = newCardUVs;
		return gameCards.toArray(new GameCard[0]);
	}

	/**
	 * Reads the next category of cards from {@code inStream}. Stores game info in
	 * {@code gameCards} and UV info in {@code uvOrigins}.
	 * 
	 * @param inStream  The DataInputStream to read from.
	 * @param player    The player to assign the cards to.
	 * @param gameCards The ArrayList to add the {@link GameCard}s to.
	 * @param uvOrigins The ArrayList to add the UV origins to.
	 * @param cardType  The type of card to assign.
	 * @throws IOException Thrown by {@link DataInputStream#readByte()} and
	 *                     {@link DataInputStream#readShort()}.
	 */
	private void addCards(DataInputStream inStream, Player player, ArrayList<GameCard> gameCards,
			ArrayList<short[]> uvOrigins, CardType cardType, int scaleW) throws IOException {
		byte count = inStream.readByte();
		for (byte i = 0; i < count; ++i) {
			gameCards.add(new GameCard(player, ++cardID, cardType, ++imgID, scaleW));
			uvOrigins.add(new short[] { inStream.readShort(), inStream.readShort() });
		}
	}

	/**
	 * Draws card image {@code iID} from player {@code pID} to Graphics {@code g} at
	 * point ({@code x},{@code y}).
	 * 
	 * @param g   The Graphics to draw to.
	 * @param pID The player ID of the card.
	 * @param iID The image ID of the card.
	 * @param x   The x coordinate of the card.
	 * @param y   The y coordinate of the card.
	 */
	public void drawCard(Graphics g, int pID, byte iID, int x, int y, boolean fullClamp) {
		CardUVs uvs = cardUVs.get(pID);
		short[] uvxy = uvs.getUV(iID);
		int lgW = uvs.getLargeWidth();
		int dx = fullClamp ? Math.max(0, Math.min(x, GPanel.WIDTH - lgW)) : x,
				dy = fullClamp ? Math.max(0, Math.min(y, GPanel.HEIGHT - GameCard.LG_HEIGHT)) : y;
		g.drawImage(imageMap, dx, dy, dx + (fullClamp ? lgW : uvs.getSmallWidth()),
				dy + (fullClamp ? GameCard.LG_HEIGHT : GameCard.SM_HEIGHT), uvxy[0], uvxy[1], uvxy[0] + uvs.getWidth(),
				uvxy[1] + uvs.getHeight(), null);
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
		if (pID < cardUVs.size()) {
			CardUVs uvs = cardUVs.get(pID);
			g.drawImage(imageMap, x, y, x + uvs.getSmallWidth(), y + GameCard.SM_HEIGHT, 0, 0, uvs.getWidth(),
					uvs.getHeight(), null);
		}
	}
}