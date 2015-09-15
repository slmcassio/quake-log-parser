package br.com.cassio.quakelog.parser;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {

	/**
	 * Extract the lines of the file on the given path.
	 * 
	 * @param path
	 *            The given path.
	 * @return A line {@link List}.
	 */
	public static List<String> extractLines(final String path) {
		return new FileReader().getAllLines(path);
	}

	/**
	 * Reads the file on the given {@link Path}.
	 * 
	 * @param path
	 *            The given path.
	 * @return A {@link String} {@link List} with each file line.
	 */
	private List<String> getAllLines(final String path) {
		try {
			return Files.readAllLines(Paths.get(path));

		} catch (IOException e) {
			System.out.println("Error reading file");
			return emptyList();
		}
	}
}
