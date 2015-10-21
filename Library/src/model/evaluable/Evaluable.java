package model.evaluable;

import model.societies.Society;
import model.structures.State;

/**
 * This interface represents a structure of the PPDEL logic capable of evaluation to a real number, given a Society (which contains a SocietyModel) and a State.
 */
public interface Evaluable {

	/**
	 * Evaluates the structure.
	 * @param soc Society to evaluate on.
	 * @param s State to evaluate on.
	 * @return Evaluation. A real value ranging from 0.0 to 1.0.
	 */
	double eval(Society soc, State s);
}
