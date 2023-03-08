package nbradham.mtgEmu;

final class CardUVs {

	private final short w, h;
	private final short[][] uvs;
	private short offset = 0;

	CardUVs(short width, short height, short[][] cardUVs) {
		w = width;
		h = height;
		uvs = cardUVs;
	}
}