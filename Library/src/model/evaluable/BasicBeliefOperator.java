package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;

import java.util.Set;

/**
 * This class implements a fuzzy belief ternary operator (Populations or Groups) of the PPAL logic.
 */
public class BasicBeliefOperator implements TernaryOperator {

	private final SocietyModel sm;
	private Evaluable ev;

	public BasicBeliefOperator(SocietyModel sm, Evaluable ev ) {
		this.sm = sm;
		this.ev = ev;
	}

	@Override public double eval(Society soc, State s) {
		if(soc.getSocieties() != null) {
			return evalBG(soc, s);
		}
		else {
			return evalBP(soc, s);
		}
	}

	/**
	 * Evaluation for a group.
	 * @param soc Society being evaluated.
	 * @param s State to evaluate on.
	 * @return Evaluation. A real value ranging from 0.0 to 1.0.
	 */
	private double evalBG(Society soc, State s) {
		double result = 0.0;
		for(Society sc : soc.getSocieties()) {
			BasicBeliefOperator bbo = new BasicBeliefOperator(sm, ev);
			result += sc.getSize() * bbo.eval(sc, s);
		}
		return result / soc.getSize();
	}

	/**
	 * Evaluation for a population.
	 * @param soc Society being evaluated.
	 * @param s State to evaluate on.
	 * @return Evaluation. A real value ranging from 0.0 to 1.0.
	 */
	private double evalBP(Society soc, State s) {
		Set<State> neighbourStates = sm.getNeighbourStates(soc, s);
		double result = 0.0;
		int count = 1;
		for(State state : neighbourStates) {
			count++;
			//System.out.println(soc.getName() + ": " + ev.eval(soc, state));
			result += ev.eval(soc, state);
		}
		result += ev.eval(soc, s);
		return result/count;
	}
}
