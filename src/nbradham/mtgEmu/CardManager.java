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

public final class CardManager {

	private static final double PI_2 = Math.PI / 2;

	private final ArrayList<BufferedImage> cardImgs = new ArrayList<>();
	private final ArrayList<CardUVs> cardUVs = new ArrayList<>();

	public void load(int player, File deckFile) throws ZipException, IOException {
		ZipFile zFile = new ZipFile(deckFile);
		BufferedImage img = ImageIO.read(zFile.getInputStream(zFile.getEntry("cards.png")));
		int w = img.getWidth(), h = img.getHeight();
		BufferedImage cardImages = new BufferedImage(w + h, Math.max(h, w), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = cardImages.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.translate(w + (h - w) / 2, (w - h) / 2);
		g.rotate(PI_2, w / 2, h / 2);
		g.drawRenderedImage(img, null);
		cardImgs.set(player, cardImages);

		DataInputStream info = new DataInputStream(zFile.getInputStream(zFile.getEntry("info.bin")));
		short cw = info.readShort(), ch = info.readShort();
		ArrayList<Card> gameCards = new ArrayList<>();
		ArrayList<short[]> uvOrigins = new ArrayList<>();
		// TODO load cards.
		zFile.close();
	}
}