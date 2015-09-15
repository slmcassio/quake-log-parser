package br.com.cassio.quakelog.model;

import java.util.List;

/**
 * Represents a single game info.
 */
public class Game {

    private final String name;

    private final List<Player> players;

    private final List<Kills> kills;

    private final List<Deaths> deaths;

    private final List<Suicides> suicides;

    /**
     * The constructor.
     * 
     * @param name
     *            The given name.
     * @param players
     *            The given {@link Player} {@link List}.
     * @param kills
     *            The given {@link Kills} {@link List}.
     * @param deaths
     *            The given {@link Deaths} {@link List}.
     * @param suicides
     *            The given {@link Suicides} {@link List}.
     */
    public Game(final String name, final List<Player> players, final List<Kills> kills, final List<Deaths> deaths, final List<Suicides> suicides) {
        this.name = name;
        this.players = players;
        this.kills = kills;
        this.deaths = deaths;
        this.suicides = suicides;
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
     * Gets the {@link Kills} {@link List}.
     * 
     * @return A {@link Kills} {@link List}.
     */
    public List<Kills> getKills() {
        return this.kills;
    }

    /**
     * Gets the {@link Deaths} {@link List}.
     * 
     * @return A {@link Deaths} {@link List}.
     */
    public List<Deaths> getDeaths() {
        return this.deaths;
    }

    /**
     * Gets the {@link Suicides} {@link List}.
     * 
     * @return A {@link Suicides} {@link List}.
     */
    public List<Suicides> getSuicides() {
        return this.suicides;
    }
}
