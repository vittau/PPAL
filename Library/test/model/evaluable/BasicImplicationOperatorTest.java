package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;
import org.testng.annotations.*;

import java.util.Set;

import static org.testng.Assert.assertTrue;

/**
 * Created by Vitor on 16/06/2015.
 */
public class BasicImplicationOperatorTest {

	private static Society      soc;
	private static SocietyModel m;
	private static State        s;

	@BeforeTest public void setUpTest() {
		m = new SocietyModel() {
			@Override public Set<State> getNeighbourStates(Society society, State state) {
				return null; //Not tested.
			}

			@Override public void insertEdge(Society society, State s1, State s2) {
				//Not tested.
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
				return null; //Not tested.
			}
		};
		soc = new Society() {
			@Override public String getName() {
				return "soc";
			}
			@Override public String getId() {
				return "soc";
			}
			@Override public double getSize() {
				return 42; //Not tested.
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return m;
			}
		};
		s = new State() {
			@Override public Set<Proposition> getPropositions() {
				return null; //Not tested.
			}
		};
	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test
	public void definition1() {
		double z = 0.5;
		Evaluable ev = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0.2;
			}
		};
		Evaluable ev2 = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0.5;
			}
		};
		Evaluable ev3 = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0.7;
			}
		};
		BinaryOperator andOp13 = new BasicImplicationOperator(z, ev, ev3);
		BinaryOperator andOp23 = new BasicImplicationOperator(z, ev2, ev3);
		assertTrue(andOp13.eval(soc, s) >= andOp23.eval(soc, s));
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
