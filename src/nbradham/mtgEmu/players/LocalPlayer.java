package nbradham.mtgEmu.players;

/**
 * Handles IO from the player that is on this computer.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class LocalPlayer extends Player {

	/**
	 * Constructs a new LocalPlayer assigned to id {@code playerID}.
	 * 
	 * @param playerID The ID of the player.
	 */
	public LocalPlayer(int playerID) {
		super(playerID);
	}
}