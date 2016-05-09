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
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class implements a basic command-line user interface for the PPAL Model Checker.
 */
public class BasicCLI {

	private static final String CONSOLE_NAME = "ppalmc";
	private static final String VERSION = "0.2.1";
	private static final String VERSION_CODENAME = "Second Flight";
	private static final String HINT_TEXT =
			"PPAL MODEL CHECKER\n" +
			VERSION + " (" + VERSION_CODENAME + ") - by Vitor Machado (vittau.github.io)\n" +
			"Enter \"?list\" for a list of available commands. For detailed info enter \"?help COMMAND_NAME\". Enter \"exit\" to quit.";

	private final EvaluationFunction evf = new BasicEvaluationFunction();
	private PPALParser parser = new BasicXMLPPALParser(evf);
	private SimulationState simulationState = null;

	@Command(description = "Current version of the PPAL Model Checker.")
	public String version() {
		return VERSION;
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

			Society modelSoc = simulationState.getSociety(model);
			if(modelSoc == null) {
				System.out.println("Model \"" + model + "\" not found.");
				return;
			}
			if(modelSoc.getSocietyModel() == null) {
				System.out.println("Only population models can be used (in the future, group models will be accepted).");
				return;
			}

			Population pop = (Population) modelSoc;

			Proposition pre = new BasicProposition(precondition, evf);

			Group newGroup = BasicAnnouncement.announce(pop, pre, ratioD);

			String parent = model.replaceFirst("_.$", "");
			if(parent.equals(model)) {
				simulationState.removeSociety(pop);
				simulationState.insertSociety(newGroup);
				for(Society s : newGroup.getSocieties())
					simulationState.insertSociety(s);
			}
			else {
				Society socMparent = simulationState.getSociety(parent);
				socMparent.getSocieties().remove(pop);
				socMparent.getSocieties().add(newGroup);
			}
			System.out.println("\"" + precondition + "\" announced to \"" + model + "\" with ratio " + ratio + " successfully.");
		}
	}

	//TODO: Create variations of announce and knows for unary and binary operators.

	@Command(description = "Boolean operators.")
	public void operate(
			@Param(name = "society", description = "Society whose knowledge will be assessed.")
			String society,
			@Param(name = "state", description = "State where the evaluation will take place. Use & as separator for propositions.")
			String state,
			@Param(name = "left operand", description = "Left operand.")
			String leftOperand,
			@Param(name = "operator", description = "Operator (and/&, or/|, imp/->).")
			String operator,
			@Param(name = "right operand", description = "Right operand.")
			String rightOperand
	) {
		if (simulationState == null) {
			System.out.println("No model loaded. Enter \"?help open\" for more details on how to open a model.");
		} else {
			Society soc = simulationState.getSociety(society);
			if(soc == null) {
				System.out.println("Society \"" + society+ "\" not found.");
				return;
			}

			String[] propsSt = state.split("&");
			Set<Proposition> setPropsSt = new HashSet<Proposition>();
			for (String propSt : propsSt) {
				Proposition p = new BasicProposition(propSt.trim(), evf);
				setPropsSt.add(p);
			}
			State st = new BasicState(setPropsSt.toArray(new Proposition[setPropsSt.size()]));

			Evaluable evaluable = recursiveEval(leftOperand, operator, rightOperand);
			System.out.println(evaluable.eval(soc, st));
		}
	}

	@Command(description = "Boolean operators.")
	public void operate(
			@Param(name = "society", description = "Society whose knowledge will be assessed.")
			String society,
			@Param(name = "state", description = "State where the evaluation will take place. Use & as separator for propositions.")
			String state,
			@Param(name = "operator", description = "Operator (and/&, or/|, imp/->).")
			String operator,
			@Param(name = "right operand", description = "Right operand.")
			String rightOperand
	) {
		operate(society, state, null, operator, rightOperand);
	}

	@SuppressWarnings("Duplicates")
	public Evaluable recursiveEval(String leftOperand, String operator, String rightOperand) throws InvalidParameterException {

		Evaluable left = null, right = null;

		Scanner s = new Scanner(System.in);

		if(leftOperand != null) {
			if(leftOperand.startsWith("$")) {
				String varName = leftOperand.substring(1);
				System.out.println(varName + "> ");
				boolean doAgain;
				do {
					doAgain = false;
					String[] split = s.nextLine().split(" ");
					if(split.length == 2) {
						left = recursiveEval(null, split[0], split[1]);
					} else if(split.length == 3) {
						left = recursiveEval(split[0], split[1], split[2]);
					} else {
						doAgain = true;
					}
				}
				while(doAgain);
			} else {
				left = new BasicProposition(leftOperand, evf);
			}
		}
		if(rightOperand.startsWith("$")) {
			String varName = rightOperand.substring(1);
			System.out.println(varName + "> ");
			boolean doAgain;
			do {
				doAgain = false;
				String[] split = s.nextLine().split(" ");
				if(split.length == 2) {
					right = recursiveEval(null, split[0], split[1]);
				} else if(split.length == 3) {
					right = recursiveEval(split[0], split[1], split[2]);
				} else {
					doAgain = true;
				}
			}
			while(doAgain);
		} else {
			right = new BasicProposition(rightOperand, evf);
		}

		String opLower = operator.toLowerCase();
		if(left != null) {
			if(opLower.equals("and") || opLower.equals("&")) {
				return new BasicConjunctionOperator(left, right);
			} else if(opLower.equals("or") || opLower.equals("|")) {
				return new BasicDisjunctionOperator(left, right);
			} else if(opLower.equals("imp") || opLower.equals("->")) {
				return new BasicImplicationOperator(BasicImplicationOperator.DEFAULT_Z_VALUE, left, right);
			}
		} else {
			if(opLower.equals("not") || opLower.equals("!")) {
				return new BasicNegationOperator(right);
			}
		}
		throw new InvalidParameterException("Invalid parameters.");
	}

	//TODO: Implement the rest of the basic command-line user interface.

	public static void main(String[] args) throws IOException {
		try {
			ShellFactory.createConsoleShell(CONSOLE_NAME, HINT_TEXT, new BasicCLI()).commandLoop();
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
