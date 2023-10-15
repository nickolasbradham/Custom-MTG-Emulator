package nbradham.mtgEmu;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

/**
 * Handles loading a deck from a file.
 */
public final record DeckFile(BufferedImage image, CardMap[] details) {

	/**
	 * Loads a deck from a file and puts it in a DeckFile instance.
	 * 
	 * @param file The file to load.
	 * @return A new DeckFile instance with the data loaded.
	 * @throws ZipException thrown by {@link ZipFile#ZipFile(File)}.
	 * @throws IOException  thrown by {@link ZipFile#ZipFile(File)},
	 *                      {@link ImageIO#read(java.io.InputStream)},
	 *                      {@link ZipFile#getInputStream(java.util.zip.ZipEntry)},
	 *                      {@link DataInputStream#available()},
	 *                      {@link DataInputStream#readByte()},
	 *                      {@link DataInputStream#readShort()}, and
	 *                      {@link ZipFile#close()}.
	 */
	public static DeckFile load(File file) throws ZipException, IOException {
		ZipFile zFile = new ZipFile(file);
		BufferedImage img = ImageIO.read(zFile.getInputStream(zFile.getEntry(Main.FNAME_CARDS)));
		DataInputStream dis = new DataInputStream(zFile.getInputStream(zFile.getEntry(Main.FNAME_DAT)));
		dis.readBoolean();
		Zone[] zones = Zone.values();
		Type[] types = Type.values();
		Zone z;
		Type t;
		byte c;
		short x, y;
		ArrayList<CardMap> cards = new ArrayList<>();
		while (dis.available() > 0) {
			z = zones[dis.readByte()];
			t = types[dis.readByte()];
			c = dis.readByte();
			x = dis.readShort();
			y = dis.readShort();
			cards.add(t == Type.Custom ? new CardMap(z, t, c, new short[] { x, y, dis.readShort(), dis.readShort() })
					: new CardMap(z, t, c, new short[] { x, y }));
		}
		zFile.close();
		return new DeckFile(img, cards.toArray(new CardMap[0]));
	}
}