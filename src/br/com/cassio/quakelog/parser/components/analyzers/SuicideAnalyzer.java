package br.com.cassio.quakelog.parser.components.analyzers;

import java.util.Map;
import java.util.Optional;

import br.com.cassio.quakelog.model.Deaths;
import br.com.cassio.quakelog.model.Kills;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.Suicides;

public class SuicideAnalyzer implements AnalyzerChain {

    private final Optional<AnalyzerChain> next;

    /**
     * The constructor.
     * 
     * @param nextAnalizer
     *            The next chain {@link AnalyzerChain}.
     */
    public SuicideAnalyzer(final Optional<AnalyzerChain> nextAnalizer) {
        this.next = nextAnalizer;
    }

    @Override
    public void resolve(final String killerId, final String killedId, final String deathTypeId, final Map<String, Player> playerByUserId,
            final Map<String, Kills> killByName, final Map<String, Deaths> deathByName, final Map<String, Suicides> suicideByName) {

        if (isSuicide(killerId, killedId)) {
            process(killedId, playerByUserId, suicideByName);
        }

        next(this.next, killerId, killedId, deathTypeId, playerByUserId, killByName, deathByName, suicideByName);
    }

    /**
     * Process a suicide killed info.
     * 
     * @param killedId
     *            The given killed id.
     * @param playerByUserId
     *            A {@link Map} with the players for a given user id.
     * @param suicideByName
     *            A {@link Map} with the {@link Suicides} for a given name.
     */
    private void process(final String killedId, final Map<String, Player> playerByUserId, final Map<String, Suicides> suicideByName) {

        final Player killed = playerByUserId.get(killedId);
        final String killedName = killed.getName();

        // Player suicides count
        if (!suicideByName.containsKey(killedName)) {
            suicideByName.put(killedName, new Suicides(killed));
        }
        suicideByName.get(killedName).incrementCount();
    }

    /**
     * Checks if the player killed himself.
     * 
     * @param killerId
     *            The given killer id.
     * @param killedId
     *            The given killed id.
     * @return True if the 'world' is the killer. Otherwise false.
     */
    private boolean isSuicide(final String killerId, final String killedId) {
        return killerId.equalsIgnoreCase(killedId);
    }
}
