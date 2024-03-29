package br.com.cassio.quakelog.parser.components.analyzers;

import java.util.Map;
import java.util.Optional;

import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.PlayerDeathInfo;
import br.com.cassio.quakelog.model.PlayerKillInfo;

public class KillAnalyzer implements AnalyzerChain {

	private final Optional<AnalyzerChain> next;

	/**
	 * The constructor.
	 * 
	 * @param nextAnalizer
	 *            The next chain {@link AnalyzerChain}.
	 */
	public KillAnalyzer(final Optional<AnalyzerChain> nextAnalizer) {
		this.next = nextAnalizer;
	}

	@Override
	public void resolve(final String killerId, final String killedId, final String deathTypeId,
			final Map<String, Player> playerByUserId, final Map<String, PlayerKillInfo> killByName,
			final Map<String, PlayerDeathInfo> deathByName) {

		if (!isSuicide(killerId, killedId)) {
			process(killerId, playerByUserId, killByName);
		}

		next(this.next, killerId, killedId, deathTypeId, playerByUserId, killByName, deathByName);
	}

	/**
	 * Process a death info.
	 * 
	 * @param killerId
	 *            The given killer id.
	 * @param playerByUserId
	 *            A {@link Map} with the players for a given user id.
	 * @param killByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	private void process(final String killerId, final Map<String, Player> playerByUserId,
			final Map<String, PlayerKillInfo> killByName) {
		final Player killer = playerByUserId.get(killerId);
		if (null == killer) {
			return;
		}

		final String killerName = killer.getName();

		if (!killByName.containsKey(killerName)) {
			killByName.put(killerName, new PlayerKillInfo(killer));
		}

		killByName.get(killerName).incrementCount();
	}

	/**
	 * Checks if the given player killed himself.
	 * 
	 * @param killerId
	 *            The given killer id.
	 * @param killedId
	 *            The given killed id.
	 * @return True if player committed suicide. Otherwise false.
	 */
	private boolean isSuicide(final String killerId, final String killedId) {
		return killerId.equals(killedId);
	}
}
