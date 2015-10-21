package model.evaluable;

import model.structures.SocietyModel;
import model.structures.State;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Vitor on 03/05/2015.
 */
public class BasicPropositionTest {

	private static EvaluationFunction EVF;

	@BeforeTest public void setUpTest() {
		EVF = new EvaluationFunction() {
			@Override public double eval(Proposition p, SocietyModel societyModel, State state) {
				return 0; //Not tested.
			}
		};
	}

	@BeforeMethod public void setUpMethod() {

	}

	@Test(expectedExceptions = IllegalArgumentException.class) public void mustHaveNonEmptyId() {
		new BasicProposition("", EVF);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void mustHaveNonNullId() {
		new BasicProposition(null, EVF);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void mustHaveNonNullEvf() {
		new BasicProposition("a", null);
	}

	@Test
	public void negatedProposition() {
		Proposition bp = new BasicProposition("!a", EVF);
		assertTrue(bp.isNeg());

		Proposition bp2 = new BasicProposition("a", EVF);
		assertFalse(bp2.isNeg());

		assertEquals(bp2.getNeg(), bp);
		assertEquals(bp.getNeg(), bp2);
	}
	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
