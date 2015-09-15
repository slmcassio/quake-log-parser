package br.com.cassio.quakelog.parser;

import static java.util.stream.Collectors.*;

import java.util.Iterator;
import java.util.List;

import br.com.cassio.quakelog.model.Game;
import br.com.cassio.quakelog.model.Kills;
import br.com.cassio.quakelog.model.Player;

public class GamePrinter {

	private static final String TAB = "    ";

	public static void print(final List<Game> games) {
		new GamePrinter().printGames(games);
	}

	private void printGames(final List<Game> games) {
		for (final Game game : games) {
			printGameName(game);
			printTotalKills(game);
			printPlayers(game);
			printKills(game);

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
		final Long totalCount = game.getKills().stream().map(Kills::getCount).mapToLong(Long::longValue).count();
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
		if (game.getKills().isEmpty()) {
			return;
		}

		System.out.println(TAB + "Kills: {");
		final Iterator<Kills> kills = game.getKills().iterator();

		while (kills.hasNext()) {
			final Kills kill = kills.next();
			final Player player = kill.getPlayer();

			System.out.print(TAB + TAB + "\"" + player.getName() + "\": " + kill.getCount());

			if (kills.hasNext()) {
				System.out.println(",");

			} else {
				System.out.println();
			}
		}

		System.out.println(TAB + "}");
	}

	/**
	 * appends double quotes to the given {@link String}.
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
