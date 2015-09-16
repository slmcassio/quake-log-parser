package br.com.cassio.quakelog.parser;

import static br.com.cassio.quakelog.parser.Printer.printGames;
import static br.com.cassio.quakelog.parser.Reader.readFile;
import static br.com.cassio.quakelog.parser.components.GameSplitter.split;
import static br.com.cassio.quakelog.parser.components.SingleGameParser.parse;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import br.com.cassio.quakelog.model.Game;
import br.com.cassio.quakelog.model.SingleGameLog;

public class Parser {

	/**
	 * Start parser.
	 * 
	 * @param args
	 *            The given arguments.
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void main(final String[] args) throws IOException {

		System.out.print("Enter the log path: ");
		final String path = new Scanner(System.in).next();

		final List<String> allLogLines = readFile(path);
		if (allLogLines.isEmpty()) {
			return;
		}

		final List<SingleGameLog> singleGameLogs = split(allLogLines);
		if (singleGameLogs.isEmpty()) {
			return;
		}

		final List<Game> games = parse(singleGameLogs);
		if (singleGameLogs.isEmpty()) {
			return;
		}

		printGames(games);
	}
}
