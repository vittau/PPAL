package model.evaluable;

import model.societies.Society;
import model.structures.State;

/**
 * This class implements a fuzzy conjunction binary operator of the PPDEL logic.
 */
public class BasicConjunctionOperator implements BinaryOperator {

	private Evaluable ev;
	private Evaluable ev2;

	public BasicConjunctionOperator(Evaluable ev, Evaluable ev2) {
		this.ev = ev;
		this.ev2 = ev2;
	}

	@Override public double eval(Society soc, State s) {
		double evD = ev.eval(soc, s);
		double ev2D = ev2.eval(soc, s);
		return Math.min(evD, ev2D);
	}
}
