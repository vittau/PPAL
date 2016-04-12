package cli;

import asg.cliche.Shell;
import asg.cliche.ShellDependent;

/**
 * Created by Vitor on 12/04/2016.
 */
public class ExpSubshell implements ShellDependent {

	private Shell theShell;

	@Override
	public void cliSetShell(Shell shell) {
		this.theShell = shell;
	}

	public double eval(String leftOperand, String operator, String rightOperand) {

		//TODO: Implement sub-expressions using subshells. Also, use processLine method from cliche instead of commandLoop.

		return 0.0;
	}
}
