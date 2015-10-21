package controller;

import model.societies.Society;
import model.structures.State;

import java.util.Set;

/**
 * This interface represents a PPDEL simulation state.
 */
public interface SimulationState {

	/**
	 * Inserts a society in the simulation.
	 * @param society A society.
	 * @throws IllegalStateException When a society is already inserted.
	 */
	void insertSociety(Society society) throws IllegalStateException;

	/**
	 * Removes a society from the simulation.
	 * @param society A society.
	 */
	void removeSociety(Society society);

	/**
	 * Get all societies in the simulation.
	 * @return A Set of societies.
	 */
	Set<Society> getSocieties();

	/**
	 * Fetches a society by it's name.
	 * @param name Name of the society to fetch.
	 * @return A society with the given name, or null if no such society exists.
	 */
	Society getSociety(String name);

	/**
	 * Sets the real state.
	 * @param realState A State.
	 */
	void setRealState(State realState);

	/**
	 * Gets the real state.
	 * @return A State.
	 */
	State getRealState();
}
