package controller;

import model.societies.Society;
import model.structures.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a PPAL simulation state.
 */
public class BasicSimulationState implements SimulationState {

	private Map<String, Society> societiesMap;
	private Set<Society> societies;
	private State realState;

	@Override public void insertSociety(Society society) throws IllegalStateException {
		if(societies == null) {
			societies = new HashSet<Society>();
		}
		if(societiesMap == null) {
			societiesMap = new HashMap<String, Society>();
		}
		if(societiesMap.containsKey(society.getId()) || societies.contains(society)) {
			throw new IllegalStateException("Society already inserted.");
		}
		societiesMap.put(society.getId(), society);
		societies.add(society);
	}

	@Override public void removeSociety(Society society) {
		societies.remove(society);
		societiesMap.remove(society.getId());
	}

	@Override public Set<Society> getSocieties() {
		return societies;
	}

	//TODO: Appears to be returning null where it shouldn't.
	@Override public Society getSociety(String id) {
		return societiesMap.get(id);
	}

	@Override public void setRealState(State realState) {
		this.realState = realState;
	}

	@Override public State getRealState() {
		return realState;
	}
}
