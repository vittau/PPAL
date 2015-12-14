package model.societies;

import model.structures.SocietyModel;

import java.util.Set;

/**
 * This interface represents a society of the PPAL logic.
 */
public interface Society {

	/**
	 * Size of the society.
	 * @return A double representing the size of the society.
	 */
	double getSize();

	/**
	 * Returns the societies contained in this society.
	 * @return A set containing the societies contained in this society, or null if there aren't any.
	 */
	Set<Society> getSocieties();

	/**
	 * Returns this society's model, or null if it contains other societies.
	 * @return This society's model.
	 */
	SocietyModel getSocietyModel();

	/**
	 * Returns the name of this society.
	 * @return The name of this society.
	 */
	String getName();
}
