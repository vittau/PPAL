package controller;

import model.evaluable.BasicEvaluationFunction;
import model.evaluable.BasicProposition;
import model.evaluable.EvaluationFunction;
import model.evaluable.Proposition;
import model.societies.BasicPopulation;
import model.societies.Society;
import model.structures.*;
import org.testng.annotations.Test;

/**
 * Created by Vitor on 12/07/2015.
 */
public class BasicSimulationStateTest {

	@Test(expectedExceptions = IllegalStateException.class)
	public void societiesNotAddedTwice() {

		final EvaluationFunction evf = new BasicEvaluationFunction();

		final Proposition p = new BasicProposition("p", evf);
		final Proposition q = new BasicProposition("q", evf);

		final State s0 = new BasicState(p);
		final State s1 = new BasicState(p, q);
		final State s2 = new BasicState(p, q);

		final StateModel stm = new BasicStateModel(s0, s1, s2);
		final SocietyModel sm = new BasicSocietyModel(stm);


		Society society = new BasicPopulation("soc", "soc", sm, 20);

		sm.insertEdge(society, s0, s1);
		sm.insertEdge(society, s0, s2);

		SimulationState simState = new BasicSimulationState();
		simState.insertSociety(society);
		simState.insertSociety(society);
	}
}
