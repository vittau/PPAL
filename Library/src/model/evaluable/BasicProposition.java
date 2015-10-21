package model.evaluable;

import model.societies.Society;
import model.structures.State;

/**
 * This class implements a basic proposition of the PPDEL logic.
 */
public class BasicProposition implements Proposition {

	private String             name;
	private boolean            isNeg;
	private EvaluationFunction evf;

	/**
	 * Constructs a basic proposition.
	 * @param prop The proposition name.
	 */
	public BasicProposition(String prop, EvaluationFunction evf) throws IllegalArgumentException {
		if(evf == null)
			throw new IllegalArgumentException("Must have a non-null evaluation function");
		if(prop == null)
			throw new IllegalArgumentException("Must have a non-null name");
		if(prop.isEmpty())
			throw new IllegalArgumentException("Must have a non-empty name");
		if(prop.startsWith("!")) {
			this.name = prop.substring(1);
			this.isNeg = true;
		}
		else {
			this.name = prop;
			this.isNeg = false;
		}
		this.evf = evf;
	}

	@Override public boolean isNeg() {
		return isNeg;
	}

	@Override public String getName() {
		return name;
	}

	@Override public Proposition getNeg() {
		if(isNeg)
			return new BasicProposition(name, evf);
		else
			return new BasicProposition("!" + name, evf);
	}

	@Override public boolean equals(Object o) {
		if(o instanceof Proposition) {
			Proposition p = (Proposition) o;
			if(this.name.equals(p.getName())) {
				return (isNeg == p.isNeg());
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override public double eval(Society soc, State s) {
		return evf.eval(this, soc.getSocietyModel(), s);
	}
}
