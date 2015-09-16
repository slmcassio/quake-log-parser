package br.com.cassio.quakelog.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Deals with the death count for a given Player.
 */
public class PlayerDeathInfo {

	private final Player player;

	private final Map<DeathType, Integer> countByDeathType;

	/**
	 * The constructor.
	 * 
	 * @param player
	 *            The given {@link Player}.
	 */
	public PlayerDeathInfo(final Player player) {
		this.player = player;
		this.countByDeathType = new HashMap<>();
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
	public Map<DeathType, Integer> getCountByDeathType() {
		return this.countByDeathType;
	}

	/**
	 * Increments the count by 1.
	 */
	public void addDeathType(final DeathType deathType) {
		if (!this.countByDeathType.containsKey(deathType)) {
			this.countByDeathType.put(deathType, 0);
		}

		final Integer previousValue = this.countByDeathType.get(deathType);
		this.countByDeathType.put(deathType, previousValue + 1);
	}
}