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

public final class CardManager {

	private static final double PI_2 = Math.PI / 2;

	private ArrayList<CardUVs> cardUVs = new ArrayList<>();
	private BufferedImage imageMap;
	private byte cardID = -1, imgID = -1;

	public GameCard[] load(int player, File deckFile) throws ZipException, IOException {
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

		addCards(info, player, gameCards, uvOrigins, CardType.COMMANDER);
		byte count = info.readByte();
		for (byte i = 0; i < count; ++i) {
			byte dupes = info.readByte();
			short[] tuv = new short[] { info.readShort(), info.readShort() };
			uvOrigins.add(tuv);
			++imgID;
			for (byte n = 0; n < dupes; ++n)
				gameCards.add(new GameCard(player, ++cardID, CardType.LIBRARY, imgID));
		}
		addCards(info, player, gameCards, uvOrigins, CardType.LIBRARY);
		addCards(info, player, gameCards, uvOrigins, CardType.TOKEN);

		zFile.close();

		ArrayList<CardUVs> newCardUVs = new ArrayList<>(cardUVs);
		CardUVs newSet = new CardUVs(mw, mh, cw, ch, uvOrigins.toArray(new short[0][]));
		if (player < newCardUVs.size())
			newCardUVs.set(player, newSet);
		else
			newCardUVs.add(newSet);

		int widest = -1;
		for (CardUVs uv : newCardUVs)
			widest = Math.max(widest, uv.getMapWidth());

		CardUVs last = newCardUVs.get(0), cur;
		int end = newCardUVs.size();
		for (byte i = 1; i < end; ++i) {
			(cur = newCardUVs.get(i)).setOffset(last.getOffset() + last.getMapHeight());
			last = cur;
		}

		BufferedImage newImageMap = new BufferedImage(widest, last.getOffset() + last.getMapHeight(),
				BufferedImage.TYPE_4BYTE_ABGR_PRE);
		Graphics newG = newImageMap.createGraphics();

		int newOff, lastOff;
		for (byte i = 0; i < end; i++)
			if (i == player)
				newG.drawImage(loadedImages, 0, newSet.getOffset(), null);
			else
				newG.drawImage(imageMap, 0, newOff = (cur = newCardUVs.get(i)).getOffset(), cur.getMapWidth(),
						newOff + cur.getMapHeight(), 0, lastOff = (last = newCardUVs.get(i)).getOffset(),
						last.getMapWidth(), lastOff + last.getMapHeight(), null);
		imageMap = newImageMap;
		cardUVs = newCardUVs;
		return gameCards.toArray(new GameCard[0]);
	}

	private void addCards(DataInputStream inStream, int player, ArrayList<GameCard> gameCards,
			ArrayList<short[]> uvOrigins, CardType cardType) throws IOException {
		byte count = inStream.readByte();
		for (byte i = 0; i < count; ++i) {
			gameCards.add(new GameCard(player, ++cardID, cardType, ++imgID));
			uvOrigins.add(new short[] { inStream.readShort(), inStream.readShort() });
		}
	}

	public void drawCard(Graphics g, int pID, byte iID, int x, int y) {
		CardUVs uvs = cardUVs.get(pID);
		short w = uvs.getWidth(), h = uvs.getHeight();
		short[] uvxy = uvs.getUV(iID);
		g.drawImage(imageMap, x, y, x + w, y + h, uvxy[0], uvxy[1], uvxy[0] + w, uvxy[1] + h, null);
	}
}