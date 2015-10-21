package model.evaluable;

/**
 * This interface represents a logic proposition of the PPDEL logic.
 */
public interface Proposition extends Evaluable {

	/**
	 * Informs whether this proposition is negated or not.
	 * @return True if it is negated, false otherwise.
	 */
	boolean isNeg();

	/**
	 * The name of the proposition, independent of being negated or not.
	 * @return A String representing the name of the proposition.
	 */
	String getName();

	/**
	 * The negation of this proposition.
	 * @return A proposition.
	 */
	Proposition getNeg();
}