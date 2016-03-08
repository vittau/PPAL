package cli;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;
import controller.SimulationState;
import model.evaluable.*;
import model.societies.BasicAnnouncement;
import model.societies.Group;
import model.societies.Population;
import model.societies.Society;
import model.structures.BasicState;
import model.structures.SocietyModel;
import model.structures.State;
import parser.BasicXMLPPALParser;
import parser.PPALParser;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements a basic command-line user interface for the PPAL Model Checker.
 */
public class BasicCLI {

	private static final String CONSOLE_NAME = "ppalmc";
	private static final String HINT_TEXT =
			"  _____  _____        _        __  __           _      _    _____ _               _             \n" +
					" |  __ \\|  __ \\ /\\   | |      |  \\/  |         | |    | |  / ____| |             | |            \n" +
					" | |__) | |__) /  \\  | |      | \\  / | ___   __| | ___| | | |    | |__   ___  ___| | _____ _ __ \n" +
					" |  ___/|  ___/ /\\ \\ | |      | |\\/| |/ _ \\ / _` |/ _ \\ | | |    | '_ \\ / _ \\/ __| |/ / _ \\ '__|\n" +
					" | |    | |  / ____ \\| |____  | |  | | (_) | (_| |  __/ | | |____| | | |  __/ (__|   <  __/ |   \n" +
					" |_|    |_| /_/    \\_\\______| |_|  |_|\\___/ \\__,_|\\___|_|  \\_____|_| |_|\\___|\\___|_|\\_\\___|_|\n" +
			"0.1 (First Flight) - by Vitor Machado (http://www.cos.ufrj.br/~vmachado/)\n" +
			"Enter \"?list\" for a list of available commands. For detailed info enter \"?help COMMAND_NAME\". Enter \"exit\" to quit.";

	private final EvaluationFunction evf = new BasicEvaluationFunction();
	private PPALParser parser = new BasicXMLPPALParser(evf);
	private SimulationState simulationState = null;

	@Command(description = "Current version of the PPAL Model Checker.")
	public String version() {
		return "0.1";
	}

	@Command(description = "Open a model file.")
	public void open(
			@Param(name = "path", description = "Absolute or relative path to the model file.")
			String path
	) {
		File file = new File("./Examples./PPALExample.xml");
		//File file = new File(path);

		System.out.println("Opening file: " + path);

		try {
			simulationState = parser.parseDocument(file);
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

		System.out.println("Model loaded successfully.");
	}

	@Command(description = "Prints the current simulation state.")
	public void print(
			@Param(name = "parts", description = "[a - Complete model, r - Real state, s - Societies]")
			String parts
	) {
		if (simulationState == null) {
			System.out.println("No model loaded. Enter \"?help open\" for more details on how to open a model.");
		} else {
			if (parts.trim().equals("a")) {
				SimulationStatePrinter.print(simulationState);
			} else if (parts.trim().equals("r")) {
				SimulationStatePrinter.printRealState(simulationState);
			} else if (parts.trim().equals("s")) {
				SimulationStatePrinter.printSocieties(simulationState);
			} else {
				System.out.println("Invalid parameter. Enter \"?help print\" for more details.");
			}
		}
	}

	@Command(description = "Knowledge operator.")
	public void knows(
			@Param(name = "model", description = "Name of the society whose model will be used in evaluation.")
			String model,
			@Param(name = "state", description = "State where the evaluation will take place. Use & as separator for propositions.")
			String state,
			@Param(name = "society", description = "Society whose knowledge will be assessed.")
			String society,
			@Param(name = "proposition", description = "Proposition to be assessed.")
			String proposition
	) {
		if (simulationState == null) {
			System.out.println("No model loaded. Enter \"?help open\" for more details on how to open a model.");
		}
		else {
			Society socM = simulationState.getSociety(model);

			if(socM == null) {
				System.out.println("Society \"" + model + "\" not found.");
				return;
			}

			//String[] propsSt = state.replace("{", "").replace("}", "").split(",");
			String[] propsSt = state.split("&");
			Set<Proposition> setPropsSt = new HashSet<Proposition>();
			for (String propSt : propsSt) {
				Proposition p = new BasicProposition(propSt.trim(), evf);
				setPropsSt.add(p);
			}
			State st = new BasicState(setPropsSt.toArray(new Proposition[setPropsSt.size()]));

			Proposition prop = new BasicProposition(proposition, evf);

			Society soc = simulationState.getSociety(society);
			if(soc == null) {
				System.out.println("Society \"" + society + "\" not found.");
				return;
			}


			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.HALF_DOWN);
			Double knowsResult = knowsInside(socM, st, prop, soc);
			if(knowsResult == null) {
				return;
			}
			System.out.println(df.format(knowsResult));

		}
	}

	//TODO: This should probably happen inside BasicKnowledgeOperator itself.
	private Double knowsInside(Society socM, State st, Proposition prop, Society soc) {

		Double result;

		SocietyModel sm = socM.getSocietyModel();

		if(sm == null) { //Group
			result = 0.0;
			for(Society s : socM.getSocieties()) {
				Double knowsResult = knowsInside(s, st, prop, soc);
				if(knowsResult == null) {
					return null;
				}
				result += knowsResult*s.getSize();
			}
			return result/socM.getSize();

		}
		else {
			State actualState = null;
			for (State smSt : sm.getStates()) {
				if (st.equals(smSt))
					actualState = smSt;
			}
			if (actualState == null) {
				System.out.println("Error: No such state.");
				return null;
			}
			BasicKnowledgeOperator bko = new BasicKnowledgeOperator(sm, prop);

			try {
				return bko.eval(soc, actualState);
			}
			catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
	}

	@Command(description = "Announcement operator.")
	public void announce(
			@Param(name = "model", description = "Name of the society whose model will be used in the announcement.")
			String model,
			@Param(name="ratio", description = "Ratio of the announcement.")
			String ratio,
			@Param(name = "population", description = "Population who will receive the announcement.")
			String population,
			@Param(name = "precondition", description = "Proposition to be assessed as precondition.")
			String precondition
	) {
		if (simulationState == null) {
			System.out.println("No model loaded. Enter \"?help open\" for more details on how to open a model.");
		} else {
			Double ratioD;
			try {
				ratioD = Double.parseDouble(ratio);
			} catch (NumberFormatException e) {
				System.out.println("Invalid ratio.");
				return;
			}

			if(ratioD < 0D || ratioD > 1D) {
				System.out.println("Ratio must be between 0.0 and 1.0 (both inclusive).");
				return;
			}

			Society soc = simulationState.getSociety(population);
			Population pop;
			if (!(soc instanceof Population)) {
				System.out.println("Only populations can be announced to (in the future, group announcements will be implemented).");
				return;
			}
			else
				pop = (Population) soc;

			Proposition pre = new BasicProposition(precondition, evf);

			Group newGroup = BasicAnnouncement.announce(pop, pre, ratioD);

			//TODO: Check for bugs here.
			String[] socMparents = model.split("\\.");
			if(socMparents.length == 1) {
				simulationState.removeSociety(pop);
				simulationState.insertSociety(newGroup);
				for(Society s : newGroup.getSocieties())
					simulationState.insertSociety(s);
			}
			else {
				Society socMparent = simulationState.getSociety(socMparents[socMparents.length - 2]);
				socMparent.getSocieties().remove(pop);
				socMparent.getSocieties().add(newGroup);
			}
			System.out.println("\"" + precondition + "\" announced to \"" + population + "\" with ratio " + ratio + " successfully.");
		}
	}

	//TODO: Implement the rest of the basic command-line user interface.

	public static void main(String[] args) throws IOException {
		try {
			ShellFactory.createConsoleShell(CONSOLE_NAME, HINT_TEXT, new BasicCLI()).commandLoop();
		}
		catch(NullPointerException npe) { //Why does this happen when I kill the process?
			System.exit(-1);
		}
	}
}
