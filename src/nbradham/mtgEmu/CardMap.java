package nbradham.mtgEmu;

/**
 * Holds details on a single card.
 */
public final record CardMap(Zone zone, Type type, byte count, short[] origins) {
	static final byte IND_X = 0, IND_Y = 1;
}