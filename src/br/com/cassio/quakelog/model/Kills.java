package br.com.cassio.quakelog.model;

/**
 * Deals with the kill count for a given Player.
 */
public class Kills {

	private final Player player;

	private Long count;

	/**
	 * The constructor.
	 * 
	 * @param player
	 *            The given {@link Player}.
	 */
	public Kills(final Player player) {
		this.player = player;
		this.count = 0l;
	}

	/**
	 * Gets the {@link Player}.
	 * 
	 * @return A {@link Player}.
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Gets the count.
	 * 
	 * @return The count.
	 */
	public Long getCount() {
		return this.count;
	}

	/**
	 * Increments the count by 1.
	 */
	public void incrementCount() {
		this.count++;
	}

	/**
	 * Decrements the count by 1.
	 */
	public void decrementCount() {
		this.count--;
	}
}