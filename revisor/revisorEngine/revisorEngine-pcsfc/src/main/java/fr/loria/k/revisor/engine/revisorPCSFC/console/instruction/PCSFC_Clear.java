package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Clear;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;

public class PCSFC_Clear<C extends RevisorConsole<C, ?, ?, ?>> extends Clear<C> {

	public PCSFC_Clear(C console, String inputText) {
		super(console, inputText);
	}

	/**
	 * Executes the instruction: removes every variables
	 */
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.clear();
		Instruction<C> instruction = this;
		
		while (instruction != null) {
			instruction.setVisible(true);
			instruction = instruction.getParent();
		}
		
		TableOfSymbols.getInstance().resetTable();
		PCSFCFormulaVariableList.getInstance().clearVariables();
	}

}
