package br.com.cassio.quakelog.model;

/**
 * Represents the player.
 */
public class Player {

	private String name;

	/**
	 * The constructor.
	 * 
	 * @param name
	 *            The given name.
	 */
	public Player(final String name) {
		this.name = name;
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
	 * Sets the given name.
	 * 
	 * @param name
	 *            The given name.
	 */
	public void setName(final String name) {
		this.name = name;
	}
}