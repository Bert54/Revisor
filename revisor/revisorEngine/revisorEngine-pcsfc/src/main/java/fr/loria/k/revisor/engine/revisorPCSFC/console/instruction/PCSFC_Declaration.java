package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;

public abstract class PCSFC_Declaration<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {

	protected static final String VARIABLE_X_ALREADY_DECLARED = "Variable '%s' has already been declared.";
	
	protected ArrayList<String> identifiers;
	
	protected PCSFC_Declaration(C console, String inputText, final ArrayList<String> idfs) {
		super(console, inputText);
		this.identifiers = idfs;	
	}

	@Override
	protected abstract void doValidate() throws InstructionValidationException;

	@Override
	protected void doExecute() throws InstructionExecutionException {
		for (String idf: this.identifiers) {
			this.console.registerVariable(idf);
		}
	}

	@Override
	protected String createFormatedInputText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String createOutput(boolean latex) {
		// TODO Auto-generated method stub
		return null;
	}

}
