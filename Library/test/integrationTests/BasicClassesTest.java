package integrationTests;

import controller.BasicSimulationState;
import controller.SimulationState;
import model.evaluable.BasicEvaluationFunction;
import model.evaluable.BasicProposition;
import model.evaluable.EvaluationFunction;
import model.evaluable.Proposition;
import model.societies.BasicPopulation;
import model.societies.Society;
import model.structures.*;
import org.testng.annotations.Test;

/**
 * Created by Vitor on 22/07/2015.
 */
public class BasicClassesTest {

	@Test
	public void someTest() {

		final EvaluationFunction evf = new BasicEvaluationFunction();

		final Proposition p = new BasicProposition("p", evf);
		final Proposition q = new BasicProposition("q", evf);
		final Proposition r = new BasicProposition("r", evf);

		final State s0 = new BasicState(p);
		final State s1 = new BasicState(p, q);
		final State s2 = new BasicState(p, r);

		final StateModel stm = new BasicStateModel(s0, s1, s2);
		final SocietyModel sm = new BasicSocietyModel(stm);

		Society society = new BasicPopulation("soc", "soc", sm, 20);

		sm.insertEdge(society, s0, s1);
		sm.insertEdge(society, s0, s2);

		SimulationState simState = new BasicSimulationState();
		simState.insertSociety(society);

	}

	//TODO: Develop a proper integration test.
}
