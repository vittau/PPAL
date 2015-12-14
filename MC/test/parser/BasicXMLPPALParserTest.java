package parser;

import controller.SimulationState;
import model.evaluable.BasicEvaluationFunction;
import model.evaluable.BasicProposition;
import model.evaluable.EvaluationFunction;
import model.evaluable.Proposition;
import model.structures.BasicState;
import model.structures.State;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

/**
 * Created by Vitor on 01/09/2015.
 */
public class BasicXMLPPALParserTest {

    @Test
    public void ppalExampleTest() {
        final EvaluationFunction evf = new BasicEvaluationFunction();

        PPALParser parser = new BasicXMLPPALParser(evf);
        try {
            assertTrue(parser.isDocumentValid(new File("./Examples./PPALExample.xml")), "The document is invalid.");
        } catch (IOException e) {
            fail("Failed to read the document.");
        }


        SimulationState simulationState = null;
        try {
            simulationState = parser.parseDocument(new File("./Examples/PPALExample.xml"));
        } catch (IOException e) {
            fail("Failed to read the document.");
        }

        State realState = simulationState.getRealState();

        Proposition ah0 = new BasicProposition("a.h0", evf);
        Proposition bh1 = new BasicProposition("b.h1", evf);
        Proposition ch2 = new BasicProposition("c.h2", evf);
        State expectedRealState = new BasicState(ah0, bh1, ch2);

        assertEquals(realState, expectedRealState);

        //TODO: Implement the rest of this test.
        fail("Implement me.");
    }
}
