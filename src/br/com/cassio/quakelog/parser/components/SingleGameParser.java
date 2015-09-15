package br.com.cassio.quakelog.parser;

import static br.com.cassio.quakelog.parser.GameReferences.CLIENT_USER_INFO_PATTERN;
import static br.com.cassio.quakelog.parser.GameReferences.KILL_INFO_PATTERN;
import static br.com.cassio.quakelog.parser.GameReferences.WORLD_KILLER_ID;
import static br.com.cassio.quakelog.parser.GameReferences.buildLinePatternFor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.cassio.quakelog.model.DeathType;
import br.com.cassio.quakelog.model.Deaths;
import br.com.cassio.quakelog.model.Game;
import br.com.cassio.quakelog.model.Kills;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.SingleGameLog;

public class SingleGameParser {

	/**
	 * Parse the {@link SingleGameLog}s into {@link Game}s.
	 * 
	 * @param singleGameLogs
	 *            The given {@link SingleGameLog} {@link List}.
	 * @return A {@link Game} {@link List}.
	 */
	public static List<Game> parse(final List<SingleGameLog> singleGameLogs) {
		return new SingleGameParser().parseSingleGameLogs(singleGameLogs);
	}

	/**
	 * Parse the {@link SingleGameLog}s into {@link Game}s.
	 * 
	 * @param singleGameLogs
	 *            The given {@link SingleGameLog} {@link List}.
	 * @return A {@link Game} {@link List}.
	 */
	private List<Game> parseSingleGameLogs(final List<SingleGameLog> singleGameLogs) {
		final List<Game> games = new ArrayList<>();

		for (final SingleGameLog singleGameLog : singleGameLogs) {
			games.add(parseSingleGameLog(singleGameLog));
		}

		return games;
	}

	/**
	 * Parse the {@link SingleGameLog} into a {@link Game}.
	 * 
	 * @param singleGameLog
	 *            The given {@link SingleGameLog}.
	 * @return A {@link Game}.
	 */
	private Game parseSingleGameLog(final SingleGameLog singleGameLog) {
		final Map<String, Player> playerByUserId = new HashMap<>();
		final Map<String, Player> playerByName = new HashMap<>();
		final Map<String, Kills> killByName = new HashMap<>();
		final Map<String, Deaths> deathByName = new HashMap<>();

		final List<String> logLines = singleGameLog.getLogLines();
		parseLines(logLines, playerByUserId, playerByName, killByName, deathByName);

		final String name = singleGameLog.getName();
		final List<Player> players = new ArrayList<>(playerByUserId.values());
		final List<Kills> kills = new ArrayList<>(killByName.values());
		final List<Deaths> deaths = new ArrayList<>(deathByName.values());

		return new Game(name, players, kills, deaths);
	}

	/**
	 * Parses the given lines and adds its information to the given maps.
	 * 
	 * @param logLine
	 *            The given log line.
	 * @param playerByUserId
	 *            A {@link Map} with the {@link Player}s for a given user id.
	 * @param playerByName
	 *            A {@link Map} with the {@link Player}s for a given name.
	 * @param killByName
	 *            A {@link Map} with the {@link Kills} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link Deaths} for a given name.
	 */
	private void parseLines(final List<String> logLines, final Map<String, Player> playerByUserId,
			final Map<String, Player> playerByName, final Map<String, Kills> killByName,
			final Map<String, Deaths> deathByName) {

		for (final String logLine : logLines) {
			parseLine(logLine, playerByUserId, playerByName, killByName, deathByName);
		}
	}

	/**
	 * Parses the given line and adds its information to the given maps.
	 * 
	 * @param logLine
	 *            The given log line.
	 * @param playerByUserId
	 *            A {@link Map} with the {@link Player}s for a given user id.
	 * @param playerByName
	 *            A {@link Map} with the {@link Player}s for a given name.
	 * @param killByName
	 *            A {@link Map} with the {@link Kills} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link Deaths} for a given name.
	 */
	private void parseLine(final String logLine, final Map<String, Player> playerByUserId,
			final Map<String, Player> playerByName, final Map<String, Kills> killByName,
			final Map<String, Deaths> deathByName) {

		final Matcher userInfoMatcher = buildLinePatternFor(CLIENT_USER_INFO_PATTERN).matcher(logLine);
		if (userInfoMatcher.matches()) {
			final String userInfo = userInfoMatcher.group(3).trim();
			parseUserInfoLine(userInfo, playerByUserId, playerByName);

			return;
		}

		final Matcher killInfoMatcher = buildLinePatternFor(KILL_INFO_PATTERN).matcher(logLine);
		if (killInfoMatcher.matches()) {
			final String killInfo = killInfoMatcher.group(3).trim();
			parseKillInfoLine(killInfo, playerByUserId, killByName, deathByName);

			return;
		}
	}

	/**
	 * Parse a kill info line.
	 * 
	 * @param killInfo
	 *            The given user info.
	 * @param playerByUserId
	 *            A {@link Map} with the players for a given user id.
	 * @param killByName
	 *            A {@link Map} with the {@link Kills} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link Deaths} for a given name.
	 */
	private void parseKillInfoLine(final String killInfo, final Map<String, Player> playerByUserId,
			final Map<String, Kills> killByName, final Map<String, Deaths> deathByName) {

		final Matcher matcher = Pattern.compile("([0-9]*)\\s([0-9]*)\\s([0-9]*)(.*)").matcher(killInfo);
		if (!matcher.matches()) {
			return;
		}

		// Killer
		final String killerId = matcher.group(1);
		final Player killer = playerByUserId.get(killerId);

		// Killed
		final String killedId = matcher.group(2);
		final Player killed = playerByUserId.get(killedId);

		// World killed player
		if (isWorldKiller(killerId)) {
			processWorldDeath(killed, killByName);
			return;
		}

		// Player killed player
		processKillers(killer, killByName);

		try {
			final String deathTypeId = matcher.group(3);
			final Integer deathTypeValue = Integer.valueOf(deathTypeId);
			final DeathType deathType = DeathType.getByValue(deathTypeValue);

			processDeath(killed, deathType, deathByName);

		} catch (final Exception exception) {
			System.out.println("Error parsing death type.");
		}

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

	/**
	 * Adds the given killer to the kills map (if not already there). Increment
	 * kill count.
	 * 
	 * @param killer
	 *            The given killer.
	 * @param killByName
	 *            A {@link Map} with the {@link Kills} for a given name.
	 */
	private void processKillers(final Player killer, final Map<String, Kills> killByName) {
		final String killerName = killer.getName();
		if (!killByName.containsKey(killerName)) {
			killByName.put(killerName, new Kills(killer));
		}

		killByName.get(killerName).incrementCount();
	}

	/**
	 * Adds the given killed to the kills map (if not already there). Decrement
	 * kill count.
	 * 
	 * @param killed
	 *            The given killed.
	 * @param killByName
	 *            A {@link Map} with the {@link Kills} for a given name.
	 */
	private void processWorldDeath(final Player killed, final Map<String, Kills> killByName) {
		final String killedName = killed.getName();
		if (!killByName.containsKey(killedName)) {
			killByName.put(killedName, new Kills(killed));
		}

		killByName.get(killedName).decrementCount();
	}

	/**
	 * Adds the given killed to the deaths map (if not already there). Increment
	 * death count by {@link DeathType}.
	 * 
	 * @param killed
	 *            The given killed.
	 * @param deathType
	 *            The given {@link DeathType}.
	 * @param deathByName
	 *            A {@link Map} with the {@link Deaths} for a given name.
	 */
	private void processDeath(final Player killed, final DeathType deathType, final Map<String, Deaths> deathByName) {
		final String killedName = killed.getName();
		if (!deathByName.containsKey(killedName)) {
			deathByName.put(killedName, new Deaths(killed));
		}

		deathByName.get(killedName).addDeathType(deathType);
	}

	/**
	 * Parse a user info line.
	 * 
	 * @param userInfo
	 *            The given user info.
	 * @param playerByUserId
	 *            A {@link Map} with the players for a given user id.
	 * @param playerByName
	 *            A {@link Map} with the players for a given name.
	 */
	private void parseUserInfoLine(final String userInfo, final Map<String, Player> playerByUserId,
			final Map<String, Player> playerByName) {

		final String userId = parseUserId(userInfo);
		final String playerName = parsePlayerName(userInfo);

		if (!playerByName.containsKey(playerName)) {
			playerByName.put(playerName, new Player(playerName));
		}

		final Player player = playerByName.get(playerName);
		playerByUserId.put(userId, player);
	}

	/**
	 * Parses the user id from the given user info {@link Matcher}.
	 * 
	 * e.g.<br/>
	 * <b>Log Line:</b> 21:17 ClientUserinfoChanged: 2 n\Isgalamido\t<br/>
	 * <b>Result:</b> 2
	 * 
	 * @param userInfo
	 *            The given user info.
	 */
	private String parseUserId(final String userInfo) {
		if (userInfo.length() <= 0) {
			return "";
		}

		return userInfo.substring(0, 1).trim();
	}

	/**
	 * Parses the user id from the given user info {@link Matcher}.
	 * 
	 * e.g.<br/>
	 * <b>Log Line:</b> 21:17 ClientUserinfoChanged: 2 n\Isgalamido\t<br/>
	 * <b>Result:</b> Isgalamido
	 * 
	 * @param userInfo
	 *            The given user info.
	 */
	private String parsePlayerName(final String userInfo) {
		if (userInfo.length() <= 0) {
			return "";
		}

		final int playerNameStart = userInfo.indexOf("n\\");
		if (playerNameStart <= 0) {
			return "";
		}

		final int playerNameEnd = userInfo.indexOf("\\t\\0\\model");
		if (playerNameEnd <= 0) {
			return "";
		}

		return userInfo.substring(playerNameStart + 2, playerNameEnd).trim();
	}
}