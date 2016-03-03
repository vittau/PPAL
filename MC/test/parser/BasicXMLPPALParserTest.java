package parser;

import controller.SimulationState;
import model.evaluable.*;
import model.societies.BasicAnnouncement;
import model.societies.Group;
import model.societies.Population;
import model.societies.Society;
import model.structures.BasicState;
import model.structures.State;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

/**
 * Created by Vitor on 01/09/2015.
 */
public class BasicXMLPPALParserTest {

	private static final Double DELTA = 0.001;

	private static final EvaluationFunction EVF = new BasicEvaluationFunction();
	private SimulationState simulationState = null;

	@BeforeMethod
	public void beforeTest() {


		PPALParser parser = new BasicXMLPPALParser(EVF);
		try {
			File file = new File("../Examples/PPALExample.xml");
			assertTrue(parser.isDocumentValid(file), "The document is invalid.");
		} catch (IOException e) {
			fail("Failed to read the document.");
		}

		try {
			File file = new File("../Examples/PPALExample.xml");
			simulationState = parser.parseDocument(file);
		} catch (IOException e) {
			fail("Failed to read the document.");
		}
	}

	@Test
	public void realStateTest() {

		State realState = simulationState.getRealState();

		Proposition ah0 = new BasicProposition("ah0", EVF);
		Proposition bh1 = new BasicProposition("bh1", EVF);
		Proposition ch2 = new BasicProposition("ch2", EVF);
		State expectedRealState = new BasicState(ah0, bh1, ch2);

		assertEquals(realState, expectedRealState);
	}

	@Test
	public void AnnouncementResultingSizesTest() {
		//Announcing bh1 to a with ratio 0.7
		Proposition pre = new BasicProposition("bh1", EVF);
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		String[] socMparents = "a".split("\\.");
		if (socMparents.length == 1) {
			simulationState.removeSociety(a);
			simulationState.insertSociety(newGroup);
		} else {
			Society socMparent = simulationState.getSociety(socMparents[socMparents.length - 2]);
			socMparent.getSocieties().remove(a);
			socMparent.getSocieties().add(newGroup);
		}

		Society a_new = simulationState.getSociety("a.a_n");
		Society a_old = simulationState.getSociety("a.a_o");
		assertEquals(a_old.getSize(), 0.9, DELTA); //Part that received the announcement.
		assertEquals(a_new.getSize(), 2.1, DELTA); //Part that did not receive the announcement.
	}

	@Test
	public void NewNotKnowsArbitraryPropTest() {

		State realState = simulationState.getRealState();

		//Announcing bh1 to a with ratio 0.7
		Proposition pre = new BasicProposition("bh1", EVF);
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		simulationState.removeSociety(a);
		simulationState.insertSociety(newGroup);

		Society a_new = simulationState.getSociety("a.a_n");

		//Checking if the new society in fact does not know an arbitrary proposition.
		BasicKnowledgeOperator bko = new BasicKnowledgeOperator(a_new.getSocietyModel(), new BasicProposition("unknown", EVF));
		assertEquals(bko.eval(a_new, realState), 0.0, DELTA);
	}

	@Test
	public void NewKnowsAnnouncedPropTest() {

		State realState = simulationState.getRealState();

		Proposition bh1 = new BasicProposition("bh1", EVF);

		//Announcing bh1 to a with ratio 0.7
		Proposition pre = bh1;
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		simulationState.removeSociety(a);
		simulationState.insertSociety(newGroup);

		Society a_new = simulationState.getSociety("a.a_n");

		//Checking if the new society knows the announced proposition.
		BasicKnowledgeOperator bko = new BasicKnowledgeOperator(a_new.getSocietyModel(), bh1);
		assertEquals(bko.eval(a_new, realState), 1.0, DELTA);
	}

	@Test
	public void NewInfersPropTest() {

		State realState = simulationState.getRealState();

		Proposition ch2 = new BasicProposition("ch2", EVF);

		//Announcing bh1 to a with ratio 0.7
		Proposition pre = new BasicProposition("bh1", EVF);
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		simulationState.removeSociety(a);
		simulationState.insertSociety(newGroup);

		Society a_new = simulationState.getSociety("a.a_n");

		//Checking if the new society can infer ch2 from the announcement.
		BasicKnowledgeOperator bko = new BasicKnowledgeOperator(a_new.getSocietyModel(), ch2);
		assertEquals(bko.eval(a_new, realState), 1.0, DELTA);
	}

	@Test
	public void OldNotKnowsAnnouncedPropTest() {

		State realState = simulationState.getRealState();

		Proposition bh1 = new BasicProposition("bh1", EVF);

		//Announcing bh1 to a with ratio 0.7
		Proposition pre = bh1;
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		simulationState.removeSociety(a);
		simulationState.insertSociety(newGroup);

		Society a_old = simulationState.getSociety("a.a_o");

		//Checking if the old society does not know the announced proposition.
		BasicKnowledgeOperator bko = new BasicKnowledgeOperator(a_old.getSocietyModel(), bh1);
		assertEquals(bko.eval(a_old, realState), 0.0, DELTA);
	}

	@Test
	public void GroupKnowsAnnouncedRatioTest() {

		State realState = simulationState.getRealState();

		Proposition bh1 = new BasicProposition("bh1", EVF);

		//Announcing bh1 to a with ratio 0.7
		Proposition pre = bh1;
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		simulationState.removeSociety(a);
		simulationState.insertSociety(newGroup);

		//Checking if the group knows the announced proposition with the announced ratio.
		BasicKnowledgeOperator bko = new BasicKnowledgeOperator(a.getSocietyModel(), bh1);
		assertEquals(bko.eval(a, realState), 0.7, DELTA);
	}

	@Test
	public void GroupInferPropTest() {

		State realState = simulationState.getRealState();

		Proposition ch2 = new BasicProposition("ch2", EVF);

		//Announcing bh1 to a with ratio 0.7
		Proposition pre = new BasicProposition("bh1", EVF);
		Population a = (Population) simulationState.getSociety("a");
		Double ratio = 0.7;

		Group newGroup = BasicAnnouncement.announce(a, pre, ratio);

		simulationState.removeSociety(a);
		simulationState.insertSociety(newGroup);

		//Checking if the group can infer ch2 from the announcement with the announced ratio.
		BasicKnowledgeOperator bko = new BasicKnowledgeOperator(a.getSocietyModel(), ch2);
		assertEquals(bko.eval(a, realState), 0.7, DELTA);
	}
}
