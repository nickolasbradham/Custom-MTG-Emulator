package nbradham.mtgEmu;

import nbradham.mtgEmu.gameObjects.GameCard;

/**
 * Holds card UV information.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class CardUVs {

	private final int mapW, mapH, smallW, largeW;
	private final short w, h;
	private final short[][] uvs;
	private int offset = 0;

	/**
	 * Creates a new CardUVs instance,
	 * 
	 * @param mapWidth  The image map width of this set of cards.
	 * @param mapHeight The image map height of this set of cards.
	 * @param width     The width of each card.
	 * @param height    The height of each card.
	 * @param cardUVs   A 2D array containing all the origin points of cards.
	 */
	CardUVs(int mapWidth, int mapHeight, short width, short height, short[][] cardUVs, int scaleW) {
		mapW = mapWidth;
		mapH = mapHeight;
		w = width;
		h = height;
		smallW = scaleW;
		largeW = scaleWidth(w, GameCard.LG_HEIGHT, h);
		uvs = cardUVs;
	}

	/**
	 * Retrieves the Y offset of this map in the combined map.
	 * 
	 * @return The Y offset.
	 */
	int getMapOffset() {
		return offset;
	}

	/**
	 * Sets the Y offset of this map in the combined map.
	 * 
	 * @param newOffset The new Y offset.
	 */
	void setMapOffset(int newOffset) {
		offset = newOffset;
	}

	/**
	 * Gets the width of this map.
	 * 
	 * @return The pixel width of this map.
	 */
	int getMapWidth() {
		return mapW;
	}

	/**
	 * Gets the height of this map.
	 * 
	 * @return The pixel height of this map.
	 */
	int getMapHeight() {
		return mapH;
	}

	/**
	 * Gets the width of cards in this map.
	 * 
	 * @return The pixel width of cards in this map.
	 */
	short getWidth() {
		return w;
	}

	/**
	 * Retrieves the width of the small card form.
	 * 
	 * @return The small width of cards.
	 */
	int getSmallWidth() {
		return smallW;
	}

	/**
	 * Retrieves the width of the large card form.
	 * 
	 * @return The large width of cards.
	 */
	int getLargeWidth() {
		return largeW;
	}

	/**
	 * Gets the height of cards in this map.
	 * 
	 * @return The pixel height of cards in this map.
	 */
	short getHeight() {
		return h;
	}

	/**
	 * Retrieves the UV origin of card image {@code iID}.
	 * 
	 * @param iID The ID of the card image to get.
	 * @return A short array containing the x and y of the image origin, in that
	 *         order.
	 */
	short[] getUV(byte iID) {
		return uvs[iID];
	}

	/**
	 * Calculates the scaled width of a card ({@code startW} *
	 * {@code targetScaleHeight} / {@code startH}).
	 * 
	 * @param startW            The initial card width.
	 * @param targetScaleHeight The height of the card to scale to.
	 * @param startH            The initial card height.
	 * @return The calculated scaled width.
	 */
	public static int scaleWidth(short startW, short targetScaleHeight, short startH) {
		return startW * targetScaleHeight / startH;
	}
}