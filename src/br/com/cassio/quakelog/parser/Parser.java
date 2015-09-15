package br.com.cassio.quakelog.parser;

import static br.com.cassio.quakelog.parser.Reader.readFile;
import static br.com.cassio.quakelog.parser.Printer.printGames;
import static br.com.cassio.quakelog.parser.components.GameSplitter.split;
import static br.com.cassio.quakelog.parser.components.SingleGameParser.parse;

import java.io.IOException;
import java.util.List;

import br.com.cassio.quakelog.model.Game;
import br.com.cassio.quakelog.model.SingleGameLog;

public class Parser {

    /**
     * Begin..
     * 
     * @param args
     *            The given arguments.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new Parser().process();
    }

    /**
     * Process file.
     */
    private void process() {
        final List<String> allLogLines = readFile("/Users/vntcamo/Desktop/games-2.log");
        final List<SingleGameLog> singleGameLogs = split(allLogLines);
        final List<Game> games = parse(singleGameLogs);

        printGames(games);
    }
}
