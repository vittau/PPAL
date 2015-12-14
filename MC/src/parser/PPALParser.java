package parser;

import controller.SimulationState;

import java.io.File;
import java.io.IOException;

/**
 * This interface represents a PPAL input file parser.
 */
public interface PPALParser {
	/**
	 * Checks if a document is valid.
	 * @param file Document file.
	 * @return True if the document is valid, false otherwise.
	 * @throws IOException When the document cannot be accessed.
	 */
	boolean isDocumentValid(File file) throws IOException;

	/**
	 * Parses a PPAL input document.
	 * @param file Document file.
	 * @return A SimulationState resultant of the parsed document.
	 * @throws IllegalArgumentException When the document is not valid.
	 * @throws IOException When the document cannot be accessed.
	 */
	SimulationState parseDocument(File file) throws IllegalArgumentException, IOException;
}
