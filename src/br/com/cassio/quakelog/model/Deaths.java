package br.com.cassio.quakelog.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Deals with the death count for a given Player.
 */
public class Deaths {

	private final Player player;

	private final Map<DeathType, Integer> deathTypes;

	/**
	 * The constructor.
	 * 
	 * @param player
	 *            The given {@link Player}.
	 */
	public Deaths(final Player player) {
		this.player = player;
		this.deathTypes = new HashMap<>();
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
	 * Gets the {@link DeathType} {@link Map}.
	 * 
	 * @return The {@link DeathType} {@link Map}.
	 */
	public Map<DeathType, Integer> getDeathTypes() {
		return this.deathTypes;
	}

	/**
	 * Increments the count by 1.
	 */
	public void addDeathType(final DeathType deathType) {
		if (!this.deathTypes.containsKey(deathType)) {
			this.deathTypes.put(deathType, 0);
		}

		final Integer previousValue = this.deathTypes.get(deathType);
		this.deathTypes.put(deathType, previousValue + 1);
	}
}