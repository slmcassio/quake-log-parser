package br.com.cassio.quakelog.parser.components.analyzers;

import java.util.Map;
import java.util.Optional;

import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.PlayerDeathInfo;
import br.com.cassio.quakelog.model.PlayerKillInfo;

public interface AnalyzerChain {

	/**
	 * Analyze
	 * 
	 * @param killerId
	 *            The given killer id.
	 * @param killedId
	 *            The given killed id.
	 * @param deathTypeId
	 *            The given death type id.
	 * @param playerByUserId
	 *            A {@link Map} with the players for a given user id.
	 * @param killByName
	 *            A {@link Map} with the {@link PlayerKillInfo} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	void resolve(final String killerId, final String killedId, final String deathTypeId,
			final Map<String, Player> playerByUserId, final Map<String, PlayerKillInfo> killByName,
			final Map<String, PlayerDeathInfo> deathByName);

	/**
	 * Calls the next Analyzer.
	 * 
	 * @param next
	 *            The next Analyzer.
	 * @param killerId
	 *            The given killer id.
	 * @param killedId
	 *            The given killed id.
	 * @param deathTypeId
	 *            The given death type id.
	 * @param playerByUserId
	 *            A {@link Map} with the players for a given user id.
	 * @param killByName
	 *            A {@link Map} with the {@link PlayerKillInfo} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	default void next(final Optional<AnalyzerChain> next, final String killerId, final String killedId,
			final String deathTypeId, final Map<String, Player> playerByUserId,
			final Map<String, PlayerKillInfo> killByName, final Map<String, PlayerDeathInfo> deathByName) {

		if (null != next) {
			next.ifPresent(analyzer -> analyzer.resolve(killerId, killedId, deathTypeId, playerByUserId, killByName,
					deathByName));
		}
	}
}
