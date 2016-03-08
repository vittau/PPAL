package model.societies;

import model.structures.SocietyModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements a basic group of the PPAL logic.
 */
public class BasicGroup implements Group {

	private final String name;
	private final String id;
	private Set<Society> societies;

	/**
	 * Constructs a basic group out of an array of societies.
	 * @param name The name of this group.
	 * @param societies An array of societies. Can be empty.
	 */
	public BasicGroup(String name, String id, Society... societies) {
		this.name = name;
		this.id = id;
		this.societies = new HashSet<Society>(societies.length);
		Collections.addAll(this.societies, societies);
	}

	@Override public double getSize() {
		double size = 0;
		for(Society society : societies) {
			size += society.getSize();
		}
		return size;
	}

	@Override public Set<Society> getSocieties() {
		return societies;
	}

	@Override public SocietyModel getSocietyModel() {
		return null;
	}

	@Override public String getName() {
		return name;
	}

	@Override public String getId() {
		return id;
	}

	@Override public String toString() {
		String result = name;
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_DOWN);
		result += " (size = " + df.format(getSize()) + ")\n";
		Iterator<Society> iterator = societies.iterator();
		while(iterator.hasNext()) {
			result += name + "." + iterator.next();
			if(iterator.hasNext()) {
				result += "\n";
			}
		}
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
