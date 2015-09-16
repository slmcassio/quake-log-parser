package br.com.cassio.quakelog.model;

import java.util.List;

/**
 * Represents a single game info.
 */
public class Game {

	private final String name;

	private final List<Player> players;

	private final List<PlayerKillInfo> playerKillInfos;

	private final List<PlayerDeathInfo> playerDeathInfos;

	/**
	 * The constructor.
	 * 
	 * @param name
	 *            The given name.
	 * @param players
	 *            The given {@link Player} {@link List}.
	 * @param playerKillInfos
	 *            The given {@link PlayerKillInfo} {@link List}.
	 * @param playerDeathInfos
	 *            The given {@link PlayerDeathInfo} {@link List}.
	 */
	public Game(final String name, final List<Player> players, final List<PlayerKillInfo> playerKillInfos,
			final List<PlayerDeathInfo> playerDeathInfos) {
		this.name = name;
		this.players = players;
		this.playerKillInfos = playerKillInfos;
		this.playerDeathInfos = playerDeathInfos;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the {@link Player} {@link List}.
	 * 
	 * @return A {@link Player} {@link List}.
	 */
	public List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Gets the {@link PlayerKillInfo} {@link List}.
	 * 
	 * @return A {@link PlayerKillInfo} {@link List}.
	 */
	public List<PlayerKillInfo> getPlayerKillInfos() {
		return this.playerKillInfos;
	}

	/**
	 * Gets the {@link PlayerDeathInfo} {@link List}.
	 * 
	 * @return A {@link PlayerDeathInfo} {@link List}.
	 */
	public List<PlayerDeathInfo> getPlayerDeathInfos() {
		return this.playerDeathInfos;
	}
}
