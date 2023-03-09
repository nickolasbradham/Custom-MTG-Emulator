package nbradham.mtgEmu;

final class CardUVs {

	private final int mapW, mapH;
	private final short w, h;
	private final short[][] uvs;
	private int offset = 0;

	CardUVs(int mapWidth, int mapHeight, short width, short height, short[][] cardUVs) {
		mapW = mapWidth;
		mapH = mapHeight;
		w = width;
		h = height;
		uvs = cardUVs;
	}

	int getOffset() {
		return offset;
	}

	void setOffset(int newOffset) {
		offset = newOffset;
	}

	int getMapWidth() {
		return mapW;
	}

	int getMapHeight() {
		return mapH;
	}
}