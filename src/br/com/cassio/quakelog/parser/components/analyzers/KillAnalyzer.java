package br.com.cassio.quakelog.parser.components.analyzers;

import java.util.Map;
import java.util.Optional;

import br.com.cassio.quakelog.model.Deaths;
import br.com.cassio.quakelog.model.Kills;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.Suicides;

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
    public void resolve(final String killerId, final String killedId, final String deathTypeId, final Map<String, Player> playerByUserId,
            final Map<String, Kills> killByName, final Map<String, Deaths> deathByName, final Map<String, Suicides> suicideByName) {

        process(killerId, playerByUserId, killByName);
        next(this.next, killerId, killedId, deathTypeId, playerByUserId, killByName, deathByName, suicideByName);
    }

    /**
     * Process a death info.
     * 
     * @param killerId
     *            The given killer id.
     * @param playerByUserId
     *            A {@link Map} with the players for a given user id.
     * @param killByName
     *            A {@link Map} with the {@link Deaths} for a given name.
     */
    private void process(final String killerId, final Map<String, Player> playerByUserId, final Map<String, Kills> killByName) {
        final Player killer = playerByUserId.get(killerId);
        if (null == killer) {
            return;
        }

        final String killerName = killer.getName();

        if (!killByName.containsKey(killerName)) {
            killByName.put(killerName, new Kills(killer));
        }

        killByName.get(killerName).incrementCount();
    }
}
