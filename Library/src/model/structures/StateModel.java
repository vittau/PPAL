package model.structures;

import java.util.Set;

/**
 * This interface represents a state model of the PPDEL logic.
 */
public interface StateModel {

	/**
	 * States contained in this model.
	 * @return A set of the states contained in this model.
	 */
	Set<State> getStates();
}
