package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.List;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Load;

public class PCSFC_Load<C extends RevisorConsole<C, ?, ?, Instruction<C>>> extends Load<C> {

	private static final String ERROR_IN_FILE_X_AT_LINE_Y = "Error in file '%s' at line %d.";
	
	public PCSFC_Load(C console, String inputText, String file) {
		super(console, inputText, file);
	}

	protected void executeCommands(final List<String> commands) throws InstructionExecutionException {
		for (int i = 0; i < commands.size(); i++) {
			Instruction<C> instruction = this.console.parseCommand(commands.get(i));
			this.addChild(instruction);
			instruction.validate();
			instruction.execute();
			
			if(!instruction.isSuccessful()) {
				throw new InstructionExecutionException(String.format(ERROR_IN_FILE_X_AT_LINE_Y, this.file, i + 1), null, true, false);
			}
		}
	}
	
}
