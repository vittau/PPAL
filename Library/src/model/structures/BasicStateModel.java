package model.structures;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements a basic state model of the PPAL logic.
 */
public class BasicStateModel implements StateModel {

	Set<State> states;

	/**
	 * Constructs a basic state model out of a set of states.
	 * @param states A set of states. Must have at least one element.
	 */
	public BasicStateModel(State ... states) throws IllegalArgumentException {
		if(states.length == 0)
			throw new IllegalArgumentException("Must have at least one state"); //TODO: Maybe not? How to model the corrupt politician example?
		this.states = new HashSet<State>(states.length);
		Collections.addAll(this.states, states);
	}

	@Override public Set<State> getStates() {
		return states;
	}

}
