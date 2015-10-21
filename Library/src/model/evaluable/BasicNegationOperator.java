package model.evaluable;

import model.societies.Society;
import model.structures.State;

/**
 * This class implements a fuzzy negation unary operator of the PPDEL logic.
 */
public class BasicNegationOperator implements UnaryOperator {

	private Evaluable ev;

	public BasicNegationOperator(Evaluable ev) {
		this.ev = ev;
	}

	@Override public double eval(Society soc, State s) {
		return (1D - ev.eval(soc, s));
	}
}
