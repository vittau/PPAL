package model.structures;

import model.evaluable.Evaluable;
import model.societies.Society;

import java.util.*;

/**
 * This class implements a basic society model of the PPAL logic.
 */
public class BasicSocietyModel implements SocietyModel {

	private StateModel stateModel;
	private Map<String, Map<State, Set<State>>> edgeMatrix;
	private Map<String, Society> socMap;

	/**
	 * Constructs a basic society model out of a state model.
	 * @param stateModel A state model.
	 */
	public BasicSocietyModel(StateModel stateModel) throws IllegalArgumentException {
		if(stateModel.getStates().size() == 0)
			throw new IllegalArgumentException("Must have at least one state");
		this.stateModel = stateModel;
		edgeMatrix = new HashMap<String, Map<State, Set<State>>>();
		socMap = new HashMap<String, Society>();
	}

	@Override public Set<State> getStates() {
		return stateModel.getStates();
	}

	@Override public Set<State> getNeighbourStates(Society society, State state) throws  IllegalArgumentException {
		if(!edgeMatrix.containsKey(society.getName())) {
			throw new IllegalArgumentException("Society not present in this model.");
		}
		if(!stateModel.getStates().contains(state)) {
			throw new IllegalArgumentException("State not present in this model.");
		}
		Map<State, Set<State>> societyEdges = edgeMatrix.get(society.getName());

		if(!societyEdges.containsKey(state)) {
			return new HashSet<State>();
		}

		return societyEdges.get(state);
	}

	@Override public void insertEdge(Society society, State s1, State s2) {
		if(!edgeMatrix.containsKey(society.getName())) {
			edgeMatrix.put(society.getName(), new HashMap<State, Set<State>>());
			socMap.put(society.getName(), society);
		}
		Map<State, Set<State>> societyEdges = edgeMatrix.get(society.getName());

		if(!societyEdges.containsKey(s1)) {
			societyEdges.put(s1, new HashSet<State>());
		}
		if(!societyEdges.containsKey(s2)) {
			societyEdges.put(s2, new HashSet<State>());
		}
		Set<State> edgesS1 = societyEdges.get(s1);
		Set<State> edgesS2 = societyEdges.get(s2);

		edgesS1.add(s2);
		edgesS2.add(s1);
	}

	@Override public boolean equals(Object o) {
		if(o instanceof SocietyModel) {
			SocietyModel sm = (SocietyModel) o;
			for(Society s : socMap.values()) {
				for(State st : getStates()) {
					try {
						//Checking if the neighbour states for every state for every society exists in the other model as well.
						if(!getNeighbourStates(s, st).equals(sm.getNeighbourStates(s, st))) {
							return false;
						}
					}
					catch(IllegalArgumentException e) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override public void trimEdges(Society society, Evaluable ev) {
		Map<State, Set<State>> edges = edgeMatrix.get(society.getName());
		Iterator<Map.Entry<State, Set<State>>> iteratorEdges = edges.entrySet().iterator();
		while(iteratorEdges.hasNext()) {
			State st = iteratorEdges.next().getKey();
			Set<State> edges2 = edges.get(st);
			Iterator<State> iterator = edges2.iterator();
			while(iterator.hasNext()) {
				State st2 = iterator.next();
				if(ev.eval(society, st) != 1.0 || ev.eval(society, st2) != 1.0) {
					iterator.remove();
				}
			}
			if(edges2.isEmpty()) {
				iteratorEdges.remove();
			}
		}
	}

	@Override public BasicSocietyModel clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		BasicSocietyModel basicSocietyModel = new BasicSocietyModel(stateModel);

		basicSocietyModel.edgeMatrix = new HashMap<String, Map<State, Set<State>>>();
		for(String socName : edgeMatrix.keySet()) {
			Map<State, Set<State>> new_map = new HashMap<State, Set<State>>();
			for(State st : edgeMatrix.get(socName).keySet()) {
				Set<State> new_set = new HashSet<State>(edgeMatrix.get(socName).get(st));
				new_map.put(st, new_set);
			}
			basicSocietyModel.edgeMatrix.put(socName, new_map);
		}

		basicSocietyModel.stateModel = stateModel;

		return basicSocietyModel;
	}

	@Override public void replaceSociety(Society oldSociety, Society newSociety) {
		edgeMatrix.put(newSociety.getName(), edgeMatrix.remove(oldSociety.getName()));
		socMap.put(newSociety.getName(), socMap.remove(oldSociety.getName()));
	}
}
