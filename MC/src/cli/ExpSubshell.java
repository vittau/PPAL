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

	//TODO: Implement operators using subshell. Use processLine method from cliche.
}
