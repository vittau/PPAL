package model.evaluable;

import model.societies.Society;
import model.structures.State;

/**
 * This class implements a fuzzy disjunction binary operator of the PPDEL logic.
 */
public class BasicDisjunctionOperator implements BinaryOperator {

	private Evaluable ev;
	private Evaluable ev2;

	public BasicDisjunctionOperator(Evaluable ev, Evaluable ev2) {
		this.ev = ev;
		this.ev2 = ev2;
	}

	@Override public double eval(Society soc, State s) {
		double evD = ev.eval(soc, s);
		double ev2D = ev2.eval(soc, s);
		return Math.max(evD, ev2D);
	}
}
