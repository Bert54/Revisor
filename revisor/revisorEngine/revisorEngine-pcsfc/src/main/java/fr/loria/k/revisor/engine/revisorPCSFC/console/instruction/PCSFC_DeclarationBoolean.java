package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;

public class PCSFC_DeclarationBoolean<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends PCSFC_Declaration<C> {

	public PCSFC_DeclarationBoolean(C console, String inputText, ArrayList<String> idfs) {
		super(console, inputText, idfs);
	}

	/**
	 * Execution of a new declaration instruction: declaring a boolean
	 */
	@Override
	protected void doExecute() throws InstructionExecutionException {
		try {
			for (String idf: this.identifiers) {
				TableOfSymbols.getInstance().addEntry(new Entry(idf), new Symbol(idf, VariableType.BOOLEAN));
			}
		} catch (DoubleDeclareException doubleDeclareExcExec) {
			this.console.getLogger().logError(doubleDeclareExcExec);
			this.addErrorMessage(VARIABLE_ALREADY_DECLARED_EXEC);
			throw new InstructionExecutionException("Exception while execution declaration.", null, true, false);
		}
		
	}

	@Override
	public String createOutput(boolean latex) {
		StringBuilder str = new StringBuilder();
		for (String idf: this.identifiers) {
			if (latex) {
				str.append(RevisorPCSFC.formatNameToLatex(idf) + ": boolean" + System.lineSeparator());
			}
			else {
				str.append(idf + ": boolean" + System.lineSeparator());
			}
		}
		return str.toString();
	}

	@Override
	protected String createFormatedInputText() {
		StringBuilder str = new StringBuilder();
		int i = 0;
		while (i < this.identifiers.size() - 1) {
			str.append(this.identifiers.get(i) + ", ");
			i++;
		}
		str.append(this.identifiers.get(i));
		str.append(": boolean;");
		return str.toString();
	}

}
