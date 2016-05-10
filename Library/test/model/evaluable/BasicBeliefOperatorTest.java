package model.evaluable;

import model.societies.Society;
import model.structures.SocietyModel;
import model.structures.State;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Vitor on 10/05/2016.
 */
@SuppressWarnings("Duplicates")
public class BasicBeliefOperatorTest {

	private static final double DELTA = 0.00001;

	@BeforeTest
	public void setUpTest() {

	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test
	public void definitionsBP() {
		final Proposition p = new Proposition() {
			@Override public boolean isNeg() {
				return false;
			}
			@Override public String getName() {
				return "p";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public double eval(Society soc, State s) {
				if(s.getPropositions().contains(this))
					return 1.0;
				else return 0.0;
			}
		};

		final Proposition q = new Proposition() {
			@Override public boolean isNeg() {
				return false;
			}
			@Override public String getName() {
				return "q";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public double eval(Society soc, State s) {
				if(s.getPropositions().contains(this))
					return 1.0;
				else return 0.0;
			}
		};

		final State s = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Arrays.asList(p, q));
			}
		};
		final State s2 = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Collections.singletonList(p));
			}
		};
		final State s3 = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Arrays.asList(p, q));
			}
		};

		final SocietyModel m = new SocietyModel() {
			@Override
			public Society getSociety(String name) {
				return null;
			}

			@Override public Set<State> getNeighbourStates(Society society, State state) {
				return new HashSet<State>(Arrays.asList(s2, s3));
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

			}

			@Override public Set<State> getStates() {
				return null; //Not tested.
			}
		};

		final Society sc = new Society() {
			@Override public String getName() {
				return "sc";
			}
			@Override public String getId() {
				return "sc";
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

		TernaryOperator knowOp2 = new BasicBeliefOperator(m, q);
		assertEquals(knowOp2.eval(sc, s), 0.66666, DELTA);
		TernaryOperator knowOp = new BasicBeliefOperator(m, p);
		assertEquals(knowOp.eval(sc, s), 1.0, DELTA);
	}

	@Test
	public void definitionsBG() {
		final Proposition p = new Proposition() {
			@Override public boolean isNeg() {
				return false;
			}
			@Override public String getName() {
				return "p";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public double eval(Society soc, State s) {
				if(s.getPropositions().contains(this))
					return 1.0;
				else return 0.0;
			}
		};

		final Proposition q = new Proposition() {
			@Override public boolean isNeg() {
				return false;
			}
			@Override public String getName() {
				return "q";
			}
			@Override public Proposition getNeg() {
				return null; //Not tested.
			}
			@Override public double eval(Society soc, State s) {
				if(s.getPropositions().contains(this))
					return 1.0;
				else return 0.0;
			}
		};

		final State s = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Arrays.asList(p, q));
			}
		};
		final State s2 = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Collections.singletonList(p));
			}
		};
		final State s3 = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Arrays.asList(p, q));
			}
		};
		final State s4 = new State() {
			@Override public Set<Proposition> getPropositions() {
				return new HashSet<Proposition>(Collections.singletonList(q));
			}
		};

		final SocietyModel m = new SocietyModel() {
			@Override
			public Society getSociety(String name) {
				return null;
			}

			@Override public Set<State> getNeighbourStates(Society society, State state) {
				return new HashSet<State>(Arrays.asList(s2, s3));
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

		//A population who knows p in state s.
		final Society pop1 = new Society() {
			@Override public String getName() {
				return "pop1";
			}
			@Override public String getId() {
				return "pop1";
			}
			@Override public double getSize() {
				return 70;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return m;
			}
		};

		final SocietyModel m2 = new SocietyModel() {
			@Override
			public Society getSociety(String name) {
				return null;
			}

			@Override public Set<State> getNeighbourStates(Society society, State state) {
				return new HashSet<State>(Collections.singletonList(s4));
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

		//A population who doesn't know p in state s.
		final Society pop2 = new Society() {
			@Override public String getName() {
				return "pop2";
			}
			@Override public String getId() {
				return "pop2";
			}
			@Override public double getSize() {
				return 30;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return m2;
			}
		};

		final SocietyModel gm = new SocietyModel() {
			@Override
			public Society getSociety(String name) {
				return null;
			}

			@Override public Set<State> getNeighbourStates(Society society, State state) {
				if(society == pop1)
					return pop1.getSocietyModel().getNeighbourStates(society, state);
				else
					return pop2.getSocietyModel().getNeighbourStates(society, state);
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

		//This group contains populations pop1 and pop2.
		final Society group = new Society() {
			@Override public String getName() {
				return "group";
			}
			@Override public String getId() {
				return "group";
			}
			@Override public double getSize() {
				return 100;
			}
			@Override public Set<Society> getSocieties() {
				return new HashSet<Society>(Arrays.asList(pop1, pop2));
			}
			@Override public SocietyModel getSocietyModel() {
				return null;
			}
		};

		TernaryOperator beliefOp = new BasicBeliefOperator(gm, p);
		//I test this, but it's not strictly needed by definition.
		assertEquals(beliefOp.eval(group, s), 0.85, DELTA);
		//The actual definitions are here.
		double max = beliefOp.eval(pop1, s);
		assertTrue(beliefOp.eval(group, s) <= max);
		double min = beliefOp.eval(pop2, s);
		assertTrue(beliefOp.eval(group, s) >= min);

	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
