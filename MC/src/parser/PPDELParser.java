package parser;

import controller.BasicSimulationState;
import controller.SimulationState;
import nu.xom.ParsingException;

import java.io.File;
import java.io.IOException;

/**
 * This interface represents a PPDEL input file parser.
 */
public interface PPDELParser {
	/**
	 * Checks if a document is valid.
	 * @param file Document file.
	 * @return True if the document is valid, false otherwise.
	 * @throws IOException When the document cannot be accessed.
	 */
	boolean isDocumentValid(File file) throws IOException;

	/**
	 * Parses a PPDEL input document.
	 * @param file Document file.
	 * @return A SimulationState resultant of the parsed document.
	 * @throws IllegalArgumentException When the document is not valid.
	 * @throws IOException When the document cannot be accessed.
	 */
	SimulationState parseDocument(File file) throws IllegalArgumentException, IOException;
}
