package model.structures;

import model.evaluable.Evaluable;
import model.societies.Society;

import java.util.Set;

/**
 * This interface represents a society model of the PPAL logic.
 */
public interface SocietyModel extends StateModel {

	/**
	 * Set of states connected to a given state by edges of a given society.
	 *
	 * @param society Edges representing this society will be considered.
	 * @param state   Neighbours of this state will be fetched.
	 * @return Set of neighbour states.
	 */
	Set<State> getNeighbourStates(Society society, State state);

	/**
	 * Inserts a bidirectional edge connecting state s1 and state s2.
	 *
	 * @param society Edge representing this society will be created.
	 * @param s1      One of the extremes of the edge.
	 * @param s2      Another extreme of the edge.
	 */
	void insertEdge(Society society, State s1, State s2);

	/**
	 * Removes all non-ev edges from the model.
	 * @param society Society whose edges will be trimmed.
	 * @param ev Evaluable to check for the non-ev edges.
	 */
	void trimEdges(Society society, Evaluable ev);

	/**
	 * Clones this society.
	 * @return A clone of this society.
	 */
	SocietyModel clone();

	/**
	 * To be used after cloning to replace the old society for a new one.
	 * @param oldSociety Original society.
	 * @param newSociety New society.
	 */
	void replaceSociety(Society oldSociety, Society newSociety);
}