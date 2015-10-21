package model.societies;

import org.testng.annotations.*;

import static org.testng.Assert.assertNull;

/**
 * Created by Vitor on 01/05/2015.
 */
public class BasicPopulationTest {

	@BeforeTest
	public void setUpTest() {

	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void mustHaveSizeGreaterThanZero() {
		final double SIZE_P_1 = 0;
		final double SIZE_P_2 = -10;
		new BasicPopulation("p1", null, SIZE_P_1);
		new BasicPopulation("p2", null, SIZE_P_2);
	}

	@Test
	public void containsNothing() {
		Population p = new BasicPopulation("p", null, 10);
		assertNull(p.getSocieties());
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
