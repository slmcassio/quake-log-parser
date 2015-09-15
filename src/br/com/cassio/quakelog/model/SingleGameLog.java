package br.com.cassio.quakelog.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single game log.
 */
public class SingleGameLog {

	private final String name;

	private List<String> logLines;

	/**
	 * The constructor.
	 * 
	 * @param name
	 *            The given name.
	 */
	public SingleGameLog(final String name) {
		this.name = name;
		this.logLines = new ArrayList<>();
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the lines.
	 * 
	 * @return A line {@link List}.
	 */
	public List<String> getLogLines() {
		return this.logLines;
	}

	/**
	 * Adds the given log line.
	 * 
	 * @param logLine
	 *            The given log line.
	 */
	public void addLogLine(final String logLine) {
		this.logLines.add(logLine);
	}
}