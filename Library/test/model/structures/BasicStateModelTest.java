package model.structures;

import org.testng.annotations.*;

/**
 * Created by Vitor on 06/05/2015.
 */
public class BasicStateModelTest {

	@BeforeTest
	public void setUpTest() {

	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void mustHaveAtLeastOneState() {
		new BasicStateModel();
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
