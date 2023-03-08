package nbradham.mtgEmu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import nbradham.mtgEmu.Card.CardType;

public final class CardManager {

	private static final double PI_2 = Math.PI / 2;

	private final ArrayList<CardUVs> cardUVs = new ArrayList<>();

	private BufferedImage cardImgs;
	private byte id = -1;

	public void load(int player, File deckFile) throws ZipException, IOException {
		ZipFile zFile = new ZipFile(deckFile);
		BufferedImage img = ImageIO.read(zFile.getInputStream(zFile.getEntry("cards.png")));
		int w = img.getWidth(), h = img.getHeight();
		BufferedImage loadedImages = new BufferedImage(w + h, Math.max(h, w), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = loadedImages.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.translate(w + (h - w) / 2, (w - h) / 2);
		g.rotate(PI_2, w / 2, h / 2);
		g.drawRenderedImage(img, null);

		DataInputStream info = new DataInputStream(zFile.getInputStream(zFile.getEntry("info.bin")));
		short cw = info.readShort(), ch = info.readShort();
		ArrayList<Card> gameCards = new ArrayList<>();
		ArrayList<short[]> uvOrigins = new ArrayList<>();
		addCards(info, gameCards, uvOrigins, CardType.COMMANDER);
		byte count = info.readByte();
		for (byte i = 0; i < count; ++i) {
			byte dupes = info.readByte();
			short[] tuv = new short[] { info.readShort(), info.readShort() };
			uvOrigins.add(tuv);
			++id;
			for (byte n = 0; n < dupes; n++)
				gameCards.add(new Card(CardType.LIBRARY, id));
		}
		addCards(info, gameCards, uvOrigins, CardType.LIBRARY);
		addCards(info, gameCards, uvOrigins, CardType.TOKEN);
		zFile.close();
		cardUVs.add(new CardUVs(cw, ch, uvOrigins.toArray(new short[0][])));
	}

	private void addCards(DataInputStream info, ArrayList<Card> gameCards, ArrayList<short[]> uvOrigins, CardType ct)
			throws IOException {
		byte count = info.readByte();
		for (byte i = 0; i < count; ++i) {
			gameCards.add(new Card(ct, ++id));
			uvOrigins.add(new short[] { info.readShort(), info.readShort() });
		}
	}
}