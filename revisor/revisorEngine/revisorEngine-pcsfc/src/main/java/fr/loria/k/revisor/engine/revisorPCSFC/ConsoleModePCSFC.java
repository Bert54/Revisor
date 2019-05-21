package fr.loria.k.revisor.engine.revisorPCSFC;

import java.util.Scanner;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.mode.ConsoleMode;
import fr.loria.orpailleur.revisor.engine.core.console.mode.SpecialCommand;
import fr.loria.orpailleur.revisor.engine.core.utils.config.Configuration;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

public class ConsoleModePCSFC<C extends RevisorConsole<C, ?, ?, Instruction<C>>> extends ConsoleMode<C> {

	public ConsoleModePCSFC(C console) {
		super(console);
	}

	public void start() {
		if(!this.running) {
			this.running = true;
			this.consoleMode = true;
			this.clear();
			
			String s1 = "          Console mode          ";
			String s2 = "type \"exit\" to terminate";
			String s3 = "type \"clear\" to delete all variables";
			String s4 = "type \"printvars\" to print every variables with their value if they have one";
			String s5 = "type \"load <File>\" to load multiple commands from a file";
			String s6 = "type \"<Identifier>\" to print the properties of a declared identifier";
			this.print(this.getSeparator(s1, s2, s3, s4, s5, s6));
			
			try(Scanner scanner = new Scanner(System.in)) {
				while(this.consoleMode) {
					prompt();
					String command = StringUtils.simplifiedString(scanner.nextLine());
					
					if(!command.isEmpty()) {
						SpecialCommand specialCommand = this.specialCommands.get(command);
						
						if(specialCommand != null) {
							specialCommand.run();
							this.println(specialCommand.getOutput());
							this.println();
						}
						else {
							this.console.executeCommand(command);
						}
						
						try {
							this.config.save();
						}
						catch(Exception argh) {
							this.console.getLogger().logError(argh, Configuration.CANT_SAVE_CONFIGURATION);
							this.println(Configuration.CANT_SAVE_CONFIGURATION);
							this.println(argh.getMessage());
							this.println();
						}
					}
				}
			}
			
			this.consoleMode = false;
			this.running = false;
		}
	}


}
