package cli;

import controller.SimulationState;
import model.societies.Society;

import java.util.Set;

/**
 * This class prints a SimulationState to the console.
 */
public class SimulationStatePrinter {

    public static void print(SimulationState simulationState) {
        printRealState(simulationState);
        printSocieties(simulationState);
    }

    public static void printRealState(SimulationState simulationState) {
        System.out.println("Real state: " + simulationState.getRealState().toString());
    }

    public static void printSocieties(SimulationState simulationState) {
        System.out.println("Societies:");
        Set<Society> societies = simulationState.getSocieties();
        for(Society soc : societies) {
            System.out.println(soc);
        }
        //TODO: Implement the rest of this printing method.
    }
}
