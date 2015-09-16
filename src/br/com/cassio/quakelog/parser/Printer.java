package br.com.cassio.quakelog.parser;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cassio.quakelog.model.DeathType;
import br.com.cassio.quakelog.model.Game;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.PlayerDeathInfo;
import br.com.cassio.quakelog.model.PlayerKillInfo;

public class Printer {

	private static final String TAB = "    ";

	/**
	 * Prints the given {@link Game}.
	 * 
	 * @param games
	 *            The given {@link Game} {@link List}.
	 */
	public static void printGames(final List<Game> games) {
		final Printer printer = new Printer();
		printer.printOverallInfo(games);
		printer.printGameInfo(games);
	}

	/**
	 * Prints the overall result.
	 * 
	 * @param games
	 *            The given {@link Game} {@link List}.
	 */
	private void printOverallInfo(final List<Game> games) {
		System.out.println("Overall: {");

		final Map<String, Integer> killsByName = groupKillsByPlayers(games);
		for (final Entry<String, Integer> entry : killsByName.entrySet()) {
			final String playerName = entry.getKey();
			final Integer kills = entry.getValue();

			System.out.println(TAB + "\"" + playerName + "\": " + kills);
		}

		System.out.println("}");
	}

	/**
	 * Group the {@link PlayerKillInfo} count by {@link Player} name.
	 * 
	 * @param games
	 *            The given {@link Game} {@link List}.
	 * @return A count by name {@link Map}.
	 */
	private final Map<String, Integer> groupKillsByPlayers(final List<Game> games) {
		final Map<String, Integer> killsByName = new HashMap<>();

		final List<PlayerKillInfo> playerKillInfos = extractPlayerKillInfos(games);

		for (final PlayerKillInfo playerKillInfo : playerKillInfos) {

			final String playerName = playerKillInfo.getPlayer().getName();
			final Integer count = playerKillInfo.getCount();

			if (!killsByName.containsKey(playerName)) {
				killsByName.put(playerName, 0);
			}

			final Integer previousValue = killsByName.get(playerName);
			killsByName.put(playerName, previousValue + count);
		}

		return killsByName;
	}

	/**
	 * Extract all {@link PlayerKillInfo}s from the given {@link Game}s.
	 * 
	 * @param games
	 *            The given {@link Game} {@link List}.
	 * @return A {@link PlayerKillInfo} {@link List}.
	 */
	private List<PlayerKillInfo> extractPlayerKillInfos(final List<Game> games) {
		return games.stream().map(Game::getPlayerKillInfos).flatMap(List::stream).collect(toList());
	}

	/**
	 * Prints the given {@link Game}s.
	 * 
	 * @param games
	 *            The given {@link Game} {@link List}
	 */
	private void printGameInfo(final List<Game> games) {
		for (final Game game : games) {
			printGameName(game);
			printTotalKills(game);
			printPlayers(game);
			printKills(game);
			printKillsByMeans(game);

			System.out.println("}");
		}
	}

	/**
	 * Prints the given {@link Game} name.
	 * 
	 * @param game
	 *            The given {@link Game}.
	 */
	private void printGameName(final Game game) {
		System.out.println(game.getName() + ": {");
	}

	/**
	 * Prints the given {@link Game} total kills.
	 * 
	 * @param game
	 *            The given {@link Game}.
	 */
	private void printTotalKills(final Game game) {
		final Long totalCount = game.getPlayerKillInfos().stream().map(PlayerKillInfo::getCount)
				.mapToInt(Integer::intValue).count();
		System.out.println(TAB + "total_kills: " + totalCount + ";");
	}

	/**
	 * Prints the given {@link Game} players.
	 * 
	 * @param game
	 *            The given {@link Game}.
	 */
	private void printPlayers(final Game game) {
		final String players = game.getPlayers().stream().map(Player::getName).map(this::appendDoubleQuotes)
				.collect(joining(", "));

		System.out.println(TAB + "players: [" + players + "]");
	}

	/**
	 * Prints the given {@link Game} kills.
	 * 
	 * @param game
	 *            The given {@link Game}.
	 */
	private void printKills(final Game game) {
		if (game.getPlayerKillInfos().isEmpty()) {
			return;
		}

		System.out.println(TAB + "Kills: {");

		final Iterator<PlayerKillInfo> iterator = game.getPlayerKillInfos().iterator();

		while (iterator.hasNext()) {
			final PlayerKillInfo playerKillInfo = iterator.next();
			final Player player = playerKillInfo.getPlayer();

			System.out.print(TAB + TAB + "\"" + player.getName() + "\": " + playerKillInfo.getCount());

			if (iterator.hasNext()) {
				System.out.println(",");

			} else {
				System.out.println();
			}
		}

		System.out.println(TAB + "}");
	}

	/**
	 * Prints the given {@link Game} kills by means.
	 * 
	 * @param game
	 *            The given {@link Game}.
	 */
	private void printKillsByMeans(final Game game) {
		if (game.getPlayerDeathInfos().isEmpty()) {
			return;
		}

		System.out.println(TAB + "kills_by_means: {");

		final Map<DeathType, Integer> countByDeathTypes = groupByDeathType(game);
		final Iterator<Entry<DeathType, Integer>> iterator = countByDeathTypes.entrySet().iterator();

		while (iterator.hasNext()) {

			final Entry<DeathType, Integer> countByDeathType = iterator.next();
			final DeathType deathType = countByDeathType.getKey();
			final Integer count = countByDeathType.getValue();

			System.out.print(TAB + TAB + "\"" + deathType + "\": " + count);

			if (iterator.hasNext()) {
				System.out.println(",");

			} else {
				System.out.println();
			}
		}

		System.out.println(TAB + "}");
	}

	/**
	 * Group count by {@link DeathType}s.
	 * 
	 * @param game
	 *            The given {@link Game}.
	 * @return A count by {@link DeathType} {@link Map}.
	 */
	private Map<DeathType, Integer> groupByDeathType(final Game game) {

		final List<PlayerDeathInfo> playerDeathInfos = game.getPlayerDeathInfos();
		final Map<DeathType, Integer> countByDeathType = new HashMap<>();

		for (final PlayerDeathInfo playerDeathInfo : playerDeathInfos) {
			final Map<DeathType, Integer> playerCountByDeathTypes = playerDeathInfo.getCountByDeathType();

			for (final Entry<DeathType, Integer> entry : playerCountByDeathTypes.entrySet()) {
				final DeathType deathType = entry.getKey();
				final Integer count = entry.getValue();

				if (!countByDeathType.containsKey(deathType)) {
					countByDeathType.put(deathType, 0);
				}

				final Integer previous = countByDeathType.get(deathType);
				countByDeathType.put(deathType, previous + count);
			}
		}

		return countByDeathType;
	}

	/**
	 * Appends double quotes to the given {@link String}.
	 * 
	 * <p>
	 * e.g. <br/>
	 * <b>Input:</b> text<br/>
	 * <b>Output:</b> "text"
	 * 
	 * @param string
	 *            The given {@link String}.
	 * @return The given {@link String} surrounded by double quotes.
	 */
	private String appendDoubleQuotes(final String string) {
		return "\"" + string + "\"";
	}
}
