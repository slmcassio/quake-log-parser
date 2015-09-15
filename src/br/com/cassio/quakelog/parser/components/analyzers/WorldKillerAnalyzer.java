package br.com.cassio.quakelog.parser.components.analyzers;

import static br.com.cassio.quakelog.parser.components.GameReferences.WORLD_KILLER_ID;

import java.util.Map;
import java.util.Optional;

import br.com.cassio.quakelog.model.Deaths;
import br.com.cassio.quakelog.model.Kills;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.Suicides;

public class WorldKillerAnalyzer implements AnalyzerChain {

    private final Optional<AnalyzerChain> next;

    /**
     * The constructor.
     * 
     * @param nextAnalizer
     *            The next chain {@link AnalyzerChain}.
     */
    public WorldKillerAnalyzer(final Optional<AnalyzerChain> nextAnalizer) {
        this.next = nextAnalizer;
    }

    @Override
    public void resolve(final String killerId, final String killedId, final String deathTypeId, final Map<String, Player> playerByUserId,
            final Map<String, Kills> killByName, final Map<String, Deaths> deathByName, final Map<String, Suicides> suicideByName) {

        if (isWorldKiller(killerId)) {
            process(killedId, playerByUserId, killByName, suicideByName);
        }

        next(this.next, killerId, killedId, deathTypeId, playerByUserId, killByName, deathByName, suicideByName);
    }

    /**
     * Process a world killed info.
     * 
     * @param killedId
     *            The given killed id.
     * @param playerByUserId
     *            A {@link Map} with the players for a given user id.
     * @param killByName
     *            A {@link Map} with the {@link Kills} for a given name.
     * @param suicideByName
     *            A {@link Map} with the {@link Suicides} for a given name.
     */
    private void process(final String killedId, final Map<String, Player> playerByUserId, final Map<String, Kills> killByName,
            final Map<String, Suicides> suicideByName) {

        final Player killed = playerByUserId.get(killedId);
        final String killedName = killed.getName();

        // Punishment for <world> death
        if (!killByName.containsKey(killedName)) {
            killByName.put(killedName, new Kills(killed));
        }
        killByName.get(killedName).decrementCount();
    }

    /**
     * Checks if the 'world' is the killer.
     * 
     * @param killerId
     *            The given killer id.
     * @return True if the 'world' is the killer. Otherwise false.
     */
    private boolean isWorldKiller(final String killerId) {
        return WORLD_KILLER_ID.equalsIgnoreCase(killerId);
    }
}
