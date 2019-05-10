package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;

import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.ConstantSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;

public abstract class PCSFC_Declaration<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {

	protected static final String VARIABLE_X_ALREADY_DECLARED = "Variable '%s' has already been declared.";
	protected static final String VARIABLE_ALREADY_DECLARED = "Can't declare a variable more than once";
	protected static final String VARIABLE_ALREADY_DECLARED_EXEC = "Double declaration detected during execution.";
	
	protected ArrayList<String> identifiers;
	
	protected PCSFC_Declaration(C console, String inputText, final ArrayList<String> idfs) {
		super(console, inputText);
		this.identifiers = idfs;	
	}

	@Override
	protected void doValidate() throws InstructionValidationException {
		String latestIdf = "";
		try {
			for (String idf: this.identifiers) {
				latestIdf = idf;
				if (TableOfSymbols.getInstance().hasEntryByName(new Entry(idf))) {
					throw new DoubleDeclareException(VARIABLE_ALREADY_DECLARED);
				}
			}
		} catch (DoubleDeclareException doubleDeclareExc) {
			this.console.getLogger().logError(doubleDeclareExc);
			this.addErrorMessage(String.format(VARIABLE_X_ALREADY_DECLARED, latestIdf));
			throw new InstructionValidationException("Invalid Instruction.");
		}
	}

	@Override
	protected abstract void doExecute() throws InstructionExecutionException;

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
