package br.com.cassio.quakelog.parser.components;

import static br.com.cassio.quakelog.parser.components.GameReferences.CLIENT_USER_INFO_PATTERN;
import static br.com.cassio.quakelog.parser.components.GameReferences.KILL_INFO_PATTERN;
import static br.com.cassio.quakelog.parser.components.GameReferences.buildLinePatternFor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.cassio.quakelog.model.Game;
import br.com.cassio.quakelog.model.Player;
import br.com.cassio.quakelog.model.PlayerDeathInfo;
import br.com.cassio.quakelog.model.PlayerKillInfo;
import br.com.cassio.quakelog.model.SingleGameLog;
import br.com.cassio.quakelog.parser.components.analyzers.AnalyzerChain;
import br.com.cassio.quakelog.parser.components.analyzers.DeathAnalyzer;
import br.com.cassio.quakelog.parser.components.analyzers.KillAnalyzer;
import br.com.cassio.quakelog.parser.components.analyzers.WorldDeathAnalyzer;

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
		final Map<String, PlayerKillInfo> killByName = new HashMap<>();
		final Map<String, PlayerDeathInfo> deathByName = new HashMap<>();

		final List<String> logLines = singleGameLog.getLogLines();
		parseLines(logLines, playerByUserId, playerByName, killByName, deathByName);

		final String name = singleGameLog.getName();
		final List<Player> players = new ArrayList<>(playerByUserId.values());
		final List<PlayerKillInfo> playerKillInfos = new ArrayList<>(killByName.values());
		final List<PlayerDeathInfo> pplayerDeathInfos = new ArrayList<>(deathByName.values());

		return new Game(name, players, playerKillInfos, pplayerDeathInfos);
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
	 *            A {@link Map} with the {@link PlayerKillInfo} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	private void parseLines(final List<String> logLines, final Map<String, Player> playerByUserId,
			final Map<String, Player> playerByName, final Map<String, PlayerKillInfo> killByName,
			final Map<String, PlayerDeathInfo> deathByName) {

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
	 *            A {@link Map} with the {@link PlayerKillInfo} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	private void parseLine(final String logLine, final Map<String, Player> playerByUserId,
			final Map<String, Player> playerByName, final Map<String, PlayerKillInfo> killByName,
			final Map<String, PlayerDeathInfo> deathByName) {

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
	 *            A {@link Map} with the {@link PlayerKillInfo} for a given name.
	 * @param deathByName
	 *            A {@link Map} with the {@link PlayerDeathInfo} for a given name.
	 */
	private void parseKillInfoLine(final String killInfo, final Map<String, Player> playerByUserId,
			final Map<String, PlayerKillInfo> killByName, final Map<String, PlayerDeathInfo> deathByName) {

		final Matcher matcher = Pattern.compile("([0-9]*)\\s([0-9]*)\\s([0-9]*)(.*)").matcher(killInfo);
		if (!matcher.matches()) {
			return;
		}

		final String killerId = matcher.group(1);
		final String killedId = matcher.group(2);
		final String deathTypeId = matcher.group(3);

		prepareAnalyzerChain().resolve(killerId, killedId, deathTypeId, playerByUserId, killByName, deathByName);
	}

	/**
	 * Prepares an {@link AnalyzerChain}.
	 * 
	 * <p>
	 * - {@link WorldDeathAnalyzer}<br/>
	 * - {@link DeathAnalyzer}<br/>
	 * - {@link KillAnalyzer}<br/>
	 * 
	 * @return An {@link AnalyzerChain}.
	 */
	private AnalyzerChain prepareAnalyzerChain() {
		final AnalyzerChain killAnalyzer = new KillAnalyzer(Optional.empty());
		final AnalyzerChain deathAnalyzer = new DeathAnalyzer(Optional.of(killAnalyzer));
		final AnalyzerChain worldDeathAnalyzer = new WorldDeathAnalyzer(Optional.of(deathAnalyzer));

		return worldDeathAnalyzer;
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

		final int playerNameEnd = userInfo.indexOf("\\t\\");
		if (playerNameEnd <= 0) {
			return "";
		}

		return userInfo.substring(playerNameStart + 2, playerNameEnd).trim();
	}
}
