package model.societies;

import model.structures.SocietyModel;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Vitor on 01/05/2015.
 */
public class BasicGroupTest {

	@BeforeTest
	public void setUpTest() {

	}

	@BeforeMethod
	public void setUpMethod() {

	}

	@Test
	public void emptyGroupMustHaveSizeZero() {
		Group group = new BasicGroup("group", "group");
		assertEquals(group.getSize(), 0D);
	}
	@Test
	public void sizesOfSocietiesMustEqualGroupSize() {
		final double SIZE_P1 = 7;
		final double SIZE_P2 = 13;

		Society p1 = new Population() {
			@Override public String getName() {
				return "p1";
			}
			@Override public String getId() {
				return "p1";
			}
			@Override public double getSize() {
				return SIZE_P1;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return null;
			}
		};

		Society p2 = new Population() {
			@Override public String getName() {
				return "p2";
			}
			@Override public String getId() {
				return "p2";
			}
			@Override public double getSize() {
				return SIZE_P2;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return null;
			}
		};

		Group group = new BasicGroup("group", "group", p1, p2);
		assertEquals(group.getSize(), SIZE_P1 + SIZE_P2);
	}

	@Test
	public void containsItsSocieties() {
		final int SIZE_P1 = 7;
		final int SIZE_P2 = 13;

		Society p1 = new Population() {
			@Override public String getName() {
				return "p1";
			}
			@Override public String getId() {
				return "p1";
			}
			@Override public double getSize() {
				return SIZE_P1;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return null; //Not tested.
			}
		};

		Society p2 = new Population() {
			@Override public String getName() {
				return "p2";
			}
			@Override public String getId() {
				return "p2";
			}
			@Override public double getSize() {
				return SIZE_P2;
			}
			@Override public Set<Society> getSocieties() {
				return null;
			}
			@Override public SocietyModel getSocietyModel() {
				return null; //Not tested.
			}
		};

		Group group = new BasicGroup("group", "group", p1, p2);
		assertTrue(group.getSocieties().containsAll(Arrays.asList(p1, p2)));
		assertEquals(group.getSocieties().size(), 2);
	}

	@AfterMethod
	public void tearDownMethod() {

	}

	@AfterTest
	public void tearDownTest() {

	}
}
