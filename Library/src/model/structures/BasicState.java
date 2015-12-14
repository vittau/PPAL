package model.structures;

import model.evaluable.Proposition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements a basic state of the PPAL logic.
 */
public class BasicState implements State {

	private Set<Proposition> propositions;

	/**
	 * Constructs a basic state out of an array of propositions.
	 * @param prop An array of propositions. Must have at least one element.
	 */
	public BasicState(Proposition ... prop) throws IllegalArgumentException {
		if(prop.length == 0)
			throw new IllegalArgumentException("Must have at least one proposition");

		this.propositions = new HashSet<Proposition>(prop.length);
		for(Proposition p : prop) {
			if(propositions.contains(p.getNeg())) {
				throw new IllegalArgumentException("Inserted the negation of an already inserted proposition");
			}
			else {
				propositions.add(p);
			}
		}
	}

	@Override public Set<Proposition> getPropositions() {
		return propositions;
	}

	@Override public boolean equals(Object o) {
		if(o instanceof State) {
			State st = (State) o;
			return propositions.equals(st.getPropositions());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(Proposition p : propositions) {
			hash += p.hashCode();
		}
		return hash;
	}

	@Override public String toString() {
		String result =  "{";
		Iterator<Proposition> iterator = propositions.iterator();
		while(true) {
			result += iterator.next().getName();
			if(iterator.hasNext()) {
				result += ", ";
			}
			else {
				result += "}";
				break;
			}
		}
		return  result;
	}
}
