package br.com.cassio.quakelogparser;

import static br.com.cassio.quakelog.parser.components.GameSplitter.split;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.cassio.quakelog.model.SingleGameLog;

public class GameParserTest {

	private static final String LINE_1 = "20:37 InitGame: \\sv_floodProtect\\";

	private static final String LINE_2 = "20:38 ClientConnect: 2";

	private static final String LINE_3 = "1:47 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0";

	private final List<String> allLogLines = new ArrayList<>();

	@Before
	public void setup() {
		this.allLogLines.clear();

		// game-1
		this.allLogLines.add(LINE_1);
		this.allLogLines.add(LINE_2);

		// game-2
		this.allLogLines.add(" " + LINE_3);
	}

	@Test
	public void shouldGenerateTwoEntries() {
		final List<SingleGameLog> splitGames = split(this.allLogLines);
		Assert.assertEquals(2, splitGames.size());
	}

	@Test
	public void shouldGenerateEntriesWithNames() {
		final List<SingleGameLog> splitGames = split(this.allLogLines);
		Assert.assertEquals("game-1", splitGames.get(0).getName());
		Assert.assertEquals("game-2", splitGames.get(1).getName());
	}

	@Test
	public void shouldGenerateEntriesWithLines() {
		final List<SingleGameLog> splitGames = split(this.allLogLines);
		Assert.assertEquals(2, splitGames.get(0).getLogLines().size());
		Assert.assertEquals(1, splitGames.get(1).getLogLines().size());

		Assert.assertEquals(LINE_1, splitGames.get(0).getLogLines().get(0));
		Assert.assertEquals(LINE_2, splitGames.get(0).getLogLines().get(1));
		Assert.assertEquals(LINE_3, splitGames.get(1).getLogLines().get(0));
	}
}
