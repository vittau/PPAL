package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Created by Vitor on 08/05/2015.
 */
public class BasicEvaluationFunctionTest {

	@BeforeTest
	public void setUpTest() {

	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test
	public void returnOneIfPropBelongsToStateInModel() {
		final Proposition p = new Proposition() {
			@Override public double eval(Society soc, State s) {
				return 0; //Not tested.
			}
			@Override public String getName() {
				return "p";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public boolean isNeg() {
				return false; //Not tested.
			}
		};
		final State state = new State() {
			@Override public Set<Proposition> getPropositions() {
				HashSet<Proposition> hashSet = new HashSet<Proposition>();
				hashSet.add(p);
				return hashSet;
			}
		};
		SocietyModel societyModel = new SocietyModel() {
			@Override
			public Society getSociety(String name) {
				return null;
			}

			@Override public Set<State> getNeighbourStates(Society society, State state) {
				return null; //This is not tested.
			}

			@Override public void insertEdge(Society society, State s1, State s2) {
				//This is not tested.
			}

			@Override
			public void trimEdges(Society society, Evaluable ev) {
				//Not tested.
			}

			@Override public SocietyModel clone() { return null; }

			@Override
			public void replaceSociety(Society oldSociety, Society newSociety) {
				//Not tested.
			}

			@Override public Set<State> getStates() {
				Set<State> states = new HashSet<State>();
				states.add(state);
				return states;
			}
		};

		EvaluationFunction ev = new BasicEvaluationFunction();
		double result = ev.eval(p, societyModel, state);
		assertEquals(result, 1.0);
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
