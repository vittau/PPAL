package model.evaluable;

import model.structures.SocietyModel;
import model.structures.State;

/**
 * This interface represents an evaluation function of the PPDEL logic.
 */
public interface EvaluationFunction {

	/**
	 * Evaluates a proposition in a model given a state.
	 * @param p Proposition to evaluate.
	 * @param societyModel Model to evaluate onto.
	 * @param state State where the proposition is to be evaluated.
	 * @return A real number between 0 and 1.
	 */
	double eval(Proposition p, SocietyModel societyModel, State state);
}
