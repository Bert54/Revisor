package fr.loria.k.revisor.engine.revisorPCSFC;

import fr.loria.k.revisor.engine.revisorPCSFC.console.RevisorConsolePCSFC;
import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.mode.ConsoleMode;

public class RevisorPCSFCConsoleMode {

	public static void main(final String args[]) {
		try {
			ConsoleMode<RevisorConsolePCSFC> console = new ConsoleMode<>(new RevisorConsolePCSFC());

			console.start();
		}
		catch(ConsoleInitializationException initExc) {
			initExc.printStackTrace();
		}
		
		System.exit(0);
	}

}
