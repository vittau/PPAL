package model.evaluable;

import model.societies.Society;
import model.structures.State;

/**
 * This class implements a basic implication function of the PPAL logic.
 */
public class BasicImplicationOperator implements BinaryOperator {

	private Evaluable ev;
	private Evaluable ev2;
	private double    z;

	@Override public double eval(Society soc, State s) {
		double evD = ev.eval(soc, s);
		double ev2D = ev2.eval(soc, s);
		return Math.max(z / Math.max(evD, z), ev2D);
	}

	public BasicImplicationOperator(double z, Evaluable ev, Evaluable ev2) {
		this.z = z;
		this.ev = ev;
		this.ev2 = ev2;
	}
}
