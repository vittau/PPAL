package cli;

import asg.cliche.Shell;
import asg.cliche.ShellDependent;
import model.societies.Society;
import model.structures.State;

import java.security.InvalidParameterException;

/**
 * Created by Vitor on 12/04/2016.
 */
public class ExpSubshell implements ShellDependent {

	private Shell theShell;

	@Override
	public void cliSetShell(Shell shell) {
		this.theShell = shell;
	}

	public double eval(Society soc, State st, String leftOperand, String operator, String rightOperand) throws InvalidParameterException {

		if(leftOperand.startsWith("$")) {
			String varName = leftOperand.substring(1);
			//TODO: Subshell here. Also, use processLine method from cliche instead of commandLoop.
		}
		else {
			//TODO: Evaluate here.
		}
		if(rightOperand.startsWith("$")) {
			String varName = rightOperand.substring(1);
			//TODO: Subshell here. Also, use processLine method from cliche instead of commandLoop.
		}
		else {
			//TODO: Evaluate here.
		}

		//TODO: Evaluate operator here.

		return 0.0;
	}
}
