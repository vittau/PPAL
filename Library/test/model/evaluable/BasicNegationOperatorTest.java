package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;
import org.testng.annotations.*;

import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Created by Vitor on 10/06/2015.
 */
public class BasicNegationOperatorTest {

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
		final Society soc = new Society() {
			@Override public String getName() {
				return "soc";
			}
			@Override public String getId() {
				return "soc";
			}
			@Override public double getSize() {
				return 42;
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
	public void negationOfTheNegationMustEqualItself() {
		Evaluable ev = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0;
			}
		};
		UnaryOperator negOp = new BasicNegationOperator(ev);
		UnaryOperator negOp2 = new BasicNegationOperator(negOp);
		assertEquals(negOp2.eval(soc, s), ev.eval(soc, s));
	}

	@Test
	public void negationMustNotEqualItself() {
		Evaluable ev = new Evaluable() {
			@Override public double eval(Society soc, State s) {
				return 0;
			}
		};
		UnaryOperator negOp = new BasicNegationOperator(ev);
		assertFalse(negOp.eval(soc, s) == ev.eval(soc, s));
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
