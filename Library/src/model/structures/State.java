package model.structures;

import model.evaluable.Proposition;

import java.util.Set;

/**
 * This interface represents a state of the PPAL logic.
 */
public interface State {

	/**
	 * Propositions valid in this state.
	 * @return A set of the propositions valid in this state.
	 */
	Set<Proposition> getPropositions();
}
