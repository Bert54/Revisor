package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.ConstantSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
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

	/**
	 * Semantic analysis on a declaration
	 */
	@Override
	protected void doValidate() throws InstructionValidationException {
		String latestIdf = "";
		try {
			// the purpose of this first section is to make sure that the user is not trying to declare two or
			// more variables that all have the same name at the same time
			Set<String> set = new HashSet<String>(this.identifiers);
			if (set.size() < this.identifiers.size()){
				throw new DoubleDeclareException(VARIABLE_ALREADY_DECLARED, null, true, false);
			}
			// this part is the classical part of the analysis that checks if some of the variables already
			// exist in the table of symbols
			for (String idf: this.identifiers) {
				latestIdf = idf;
				if (TableOfSymbols.getInstance().hasEntryByName(new Entry(idf))) {
					throw new DoubleDeclareException(VARIABLE_ALREADY_DECLARED, null, true, false);
				}
			}
		} catch (DoubleDeclareException doubleDeclareExc) {
			this.console.getLogger().logError(doubleDeclareExc);
			if (latestIdf.contentEquals("")) {
				this.addErrorMessage(VARIABLE_ALREADY_DECLARED);
			}
			else {
				this.addErrorMessage(String.format(VARIABLE_X_ALREADY_DECLARED, latestIdf));
			}
			throw new InstructionValidationException("Invalid Instruction.", null, true, false);
		}
	}

	@Override
	protected abstract void doExecute() throws InstructionExecutionException;

}
