package model.societies;

import model.evaluable.Evaluable;
import model.structures.SocietyModel;

/**
 * This class implements an announcement of the PPDEL logic.
 */
public class BasicAnnouncement {

	/**
	 * Splits a population into a group with two populations, to be used by the announcement operator.
	 *  @param pop   Population to be split.
	 * @param pre   Pre-condition of the announcement. Used to trim the model.
	 * @param ratio Ratio of the population which will receive the announcement.
	 * @return Group containing two populations, one that received the announcement, and one that did not.
	 * @throws IllegalArgumentException If the ration is not between 0.0 and 1.0 (both inclusive).
	 */
	public static Group announce(Population pop, Evaluable pre, double ratio) throws IllegalArgumentException {

		if(ratio < 0D || ratio > 1D) {
			throw new IllegalArgumentException("Ratio must be between 0.0 and 1.0 (both inclusive).");
		}

		SocietyModel clonedSmOld = pop.getSocietyModel().clone();
		Population p_old = new BasicPopulation(pop.getName() + "_o", clonedSmOld, pop.getSize() * (1D - ratio));
		clonedSmOld.replaceSociety(pop, p_old);

		SocietyModel clonedSmNew = pop.getSocietyModel().clone();
		Population p_new = new BasicPopulation(pop.getName() + "_n", clonedSmNew, pop.getSize() * ratio);
		clonedSmNew.replaceSociety(pop, p_new);
		clonedSmNew.trimEdges(p_new, pre);

		return new BasicGroup(pop.getName(), p_old, p_new);
	}
}
