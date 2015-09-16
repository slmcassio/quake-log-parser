package br.com.cassio.quakelog.parser.components.analyzers;

import java.util.Map;
import java.util.Optional;

import br.com.cassio.quakelog.model.DeathType;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.PlayerDeathInfo;
import br.com.cassio.quakelog.model.PlayerKillInfo;

public class DeathAnalyzer implements AnalyzerChain {

	private final Optional<AnalyzerChain> next;

	/**
	 * The constructor.
	 * 
	 * @param nextAnalizer
	 *            The next chain {@link AnalyzerChain}.
	 */
	public DeathAnalyzer(final Optional<AnalyzerChain> nextAnalizer) {
		this.next = nextAnalizer;
	}

	@Override
	public void resolve(final String killerId, final String killedId, final String deathTypeId,
			final Map<String, Player> playerByUserId, final Map<String, PlayerKillInfo> killByName,
			final Map<String, PlayerDeathInfo> deathByName) {

		process(killedId, deathTypeId, playerByUserId, deathByName);
		next(this.next, killerId, killedId, deathTypeId, playerByUserId, killByName, deathByName);
	}

	/**
	 * Process a death info.
	 * 
	 * @param killedId
	 *            The given killed id.
	 * @param deathTypeId
	 *            The given death type id.
	 * @param playerByUserId
	 *            A {@link Map} with the players for a given user id.
	 * @param deathByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	private void process(final String killedId, final String deathTypeId, final Map<String, Player> playerByUserId,
			final Map<String, PlayerDeathInfo> deathByName) {
		final Player killed = playerByUserId.get(killedId);
		if (null == killed) {
			return;
		}

		final String killedName = killed.getName();
		final DeathType deathType = getDeathType(deathTypeId);

		if (!deathByName.containsKey(killedName)) {
			deathByName.put(killedName, new PlayerDeathInfo(killed));
		}

		deathByName.get(killedName).addDeathType(deathType);
	}

	/**
	 * Gets the {@link DeathType} basing on the given id.
	 * 
	 * @param deathTypeId
	 *            The given {@link DeathType} id.
	 * @return The found {@link DeathType} or {@link DeathType#NOT_FOUND}.
	 */
	private DeathType getDeathType(final String deathTypeId) {
		try {
			final Integer deathTypeValue = Integer.valueOf(deathTypeId);
			return DeathType.getByValue(deathTypeValue);

		} catch (final Exception exception) {
			System.out.println("Error parsing death type.");
			return DeathType.NOT_FOUND;
		}
	}
}
