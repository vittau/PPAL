package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;

import java.util.Set;

/**
 * This class implements a fuzzy knowledge ternary operator (Populations or Groups) of the PPAL logic.
 */
public class BasicKnowledgeOperator implements TernaryOperator {

	private final SocietyModel sm;
	private Evaluable ev;

	public BasicKnowledgeOperator(SocietyModel sm, Evaluable ev ) {
		this.sm = sm;
		this.ev = ev;
	}

	@Override public double eval(Society soc, State s) {
		if(soc.getSocieties() != null) {
			return evalKG(soc, s);
		}
		else {
			return evalKP(soc, s);
		}
	}

	/**
	 * Evaluation for a group.
	 * @param soc Society being evaluated.
	 * @param s State to evaluate on.
	 * @return Evaluation. A real value ranging from 0.0 to 1.0.
	 */
	private  double evalKG(Society soc, State s) {
		double result = 0.0;
		for(Society sc : soc.getSocieties()) {
			BasicKnowledgeOperator bko = new BasicKnowledgeOperator(sm, ev);
			result += sc.getSize() * bko.eval(sc, s);
		}
		return result / soc.getSize();
	}

	/**
	 * Evaluation for a population.
	 * @param soc Society being evaluated.
	 * @param s State to evaluate on.
	 * @return Evaluation. A real value ranging from 0.0 to 1.0.
	 */
	private double evalKP(Society soc, State s) {
		Set<State> neighbourStates = sm.getNeighbourStates(soc, s);
		for(State state : neighbourStates) {
			System.out.println(soc.getName() + ": " + ev.eval(soc, state));
			if(ev.eval(soc, state) < 1.0)
				return 0.0;
		}
		return 1.0;
	}
}
