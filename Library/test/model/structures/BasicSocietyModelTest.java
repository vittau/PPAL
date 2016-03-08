package model.structures;

import model.evaluable.Proposition;
import model.societies.Society;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertTrue;

/**
 * Created by Vitor on 08/05/2015.
 */
public class BasicSocietyModelTest {

	@BeforeTest
	public void setUpTest() {

	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void mustHaveAtLeastOneState() {
		StateModel stateModel = new StateModel() {
			@Override public Set<State> getStates() {
				return new HashSet<State>();
			}
		};
		new BasicSocietyModel(stateModel);
	}

	@Test
	public void edgesMustBeBidirectional() {
		final Proposition p1 = new Proposition() {
			@Override public double eval(Society soc, State s) {
				return 0; //Not tested.
			}
			@Override public String getName() {
				return "p1";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public boolean isNeg() {
				return false; //Not tested.
			}
		};
		final Proposition p2 = new Proposition() {
			@Override public double eval(Society soc, State s) {
				return 0; //Not tested.
			}
			@Override public String getName() {
				return "p2";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public boolean isNeg() {
				return false; //Not tested.
			}
		};
		final State s1 = new State() {
			@Override public Set<Proposition> getPropositions() {
				HashSet<Proposition> hashSet = new HashSet<Proposition>();
				hashSet.add(p1);
				return hashSet;
			}
		};
		final State s2 = new State() {
			@Override public Set<Proposition> getPropositions() {
				HashSet<Proposition> hashSet = new HashSet<Proposition>();
				hashSet.add(p2);
				return hashSet;
			}
		};
		final StateModel stateModel = new StateModel() {
			@Override public Set<State> getStates() {
				HashSet<State> hashSet = new HashSet<State>();
				hashSet.add(s1);
				hashSet.add(s2);
				return hashSet;
			}
		};
		Society society = new Society() {
			@Override public String getName() {
				return "soc";
			}
			@Override public String getId() {
				return "soc";
			}
			@Override public double getSize() {
				return 1;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return null; //Not tested.
			}
		};
		SocietyModel basicSocietyModel = new BasicSocietyModel(stateModel);
		basicSocietyModel.insertEdge(society, s1, s2);
		Set<State> edgesS1 = basicSocietyModel.getNeighbourStates(society, s1);
		Set<State> edgesS2 = basicSocietyModel.getNeighbourStates(society, s2);
		assertTrue(edgesS1.contains(s2));
		assertTrue(edgesS2.contains(s1));
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
