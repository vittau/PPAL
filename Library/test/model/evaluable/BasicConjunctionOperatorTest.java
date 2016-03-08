package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;
import org.testng.annotations.*;

import java.util.Set;

import static org.testng.Assert.assertTrue;

/**
 * Created by Vitor on 15/06/2015.
 */
public class BasicConjunctionOperatorTest {

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
	public void symmetry() {
		Evaluable ev = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0.5;
			}
		};
		Evaluable ev2 = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0.5;
			}
		};
		BinaryOperator andOp = new BasicConjunctionOperator(ev, ev2);
		BinaryOperator andOpSym = new BasicConjunctionOperator(ev2, ev);
		assertTrue(andOpSym.eval(soc, s) == andOp.eval(soc, s));
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
