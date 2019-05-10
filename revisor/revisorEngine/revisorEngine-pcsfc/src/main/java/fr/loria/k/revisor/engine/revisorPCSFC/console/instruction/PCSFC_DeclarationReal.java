package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConcolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;

public class PCSFC_DeclarationReal<C extends AbstractRevisorConcolePCSFC<C, ?, ?, ?>> extends PCSFC_Declaration<C> {

	public PCSFC_DeclarationReal(C console, String inputText, ArrayList<String> idfs) {
		super(console, inputText, idfs);
	}


	@Override
	protected void doExecute() throws InstructionExecutionException {
		try {
			for (String idf: this.identifiers) {
				TableOfSymbols.getInstance().addEntry(new Entry(idf), new Symbol(idf, VariableType.REAL));
			}
		} catch (DoubleDeclareException doubleDeclareExcExec) {
			this.console.getLogger().logError(doubleDeclareExcExec);
			this.addErrorMessage(VARIABLE_ALREADY_DECLARED_EXEC);
			throw new InstructionExecutionException("Exception while execution declaration.");
		}
	}

	@Override
	public String createOutput(boolean latex) {
		StringBuilder str = new StringBuilder();
		for (String idf: this.identifiers) {
			str.append(idf + " : real\n");
		}
		return str.toString();
	}

}
