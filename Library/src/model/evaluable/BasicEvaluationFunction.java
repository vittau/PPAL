package model.evaluable;

import model.structures.SocietyModel;
import model.structures.State;

/**
 * This class implements a basic evaluation function of the PPDEL logic.
 */
public class BasicEvaluationFunction implements EvaluationFunction {

	/**
	 * Evaluates a proposition in a model given a state.
	 * @param p Proposition to evaluate.
	 * @param societyModel Model to evaluate onto.
	 * @param state State where the proposition is to be evaluated.
	 * @return 1.0 if the proposition is in the state, 0.0 otherwise.
	 * @throws IllegalArgumentException
	 */
	@Override public double eval(Proposition p, SocietyModel societyModel, State state) throws IllegalArgumentException {
		if(!societyModel.getStates().contains(state)) {
			throw new IllegalArgumentException("State not present in the given model.");
		}
		if(state.getPropositions().contains(p)) {
			return 1.0;
		}
		else {
			return 0.0;
		}
	}
}
