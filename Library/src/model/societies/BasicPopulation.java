package model.societies;

import model.structures.SocietyModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;

/**
 * This class implements a basic population of the PPAL logic..
 */
public class BasicPopulation implements Population {

	private final String name;
	private final SocietyModel sm;
	private final double size;

	/**
	 * Constructs a basic population with a size.
	 * @param name The name of this population.
	 * @param size The size of the population. Must be greater than zero.
	 */
	public BasicPopulation(String name, SocietyModel sm, double size) throws  IllegalArgumentException {
		if(size <= 0)
			throw new IllegalArgumentException("A population must have size greater than zero.");
		this.name = name;
		this.sm = sm;
		this.size = size;
	}

	@Override public double getSize() {
		return size;
	}

	@Override public Set<Society> getSocieties() {
		return null;
	}

	@Override public SocietyModel getSocietyModel() {
		return sm;
	}

	@Override public String getName() {
		return name;
	}

	@Override public String toString() {
		String result = name;
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_DOWN);
		result += " (size = " + df.format(size) + ")";
		return result;
	}

	@Override public boolean equals(Object o) {
		if(o instanceof Society) {
			Society society = (Society) o;
			String socName = society.getName();
			return name.equals(socName);
		}
		else {
			return false;
		}
	}

	@Override public int hashCode() {
		return name.hashCode();
	}
}
