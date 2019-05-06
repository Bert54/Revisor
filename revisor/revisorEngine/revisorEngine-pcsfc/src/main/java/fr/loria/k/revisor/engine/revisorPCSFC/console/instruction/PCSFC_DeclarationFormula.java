package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConcolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;

public class PCSFC_DeclarationFormula<C extends AbstractRevisorConcolePCSFC<C, ?, ?, ?>> extends PCSFC_Declaration<C> {

	public PCSFC_DeclarationFormula(C console, String inputText, ArrayList<String> idfs) {
		super(console, inputText, idfs);
	}


	@Override
	protected void doValidate() throws InstructionValidationException {
		String latestIdf = "";
		try {
			for (String idf: this.identifiers) {
				latestIdf = idf;
				TableOfSymbols.getInstance().addEntry(new Entry(idf), new Symbol(idf, VariableType.FORMULA));
			}
		} catch (DoubleDeclareException doubleDeclareExc) {
			int i = 0;
			while (this.identifiers.get(i) != latestIdf) {
				String idf = this.identifiers.get(i);
				TableOfSymbols.getInstance().removeEntry(new Entry(idf));
				i++;
			}
			this.console.getLogger().logError(doubleDeclareExc);
			this.addErrorMessage(String.format(VARIABLE_X_ALREADY_DECLARED, latestIdf));
			throw new InstructionValidationException("Invalid Instruction.");
		}
	}

	@Override
	public String createOutput(boolean latex) {
		StringBuilder str = new StringBuilder();
		for (String idf: this.identifiers) {
			str.append(idf + " : formula\n");
		}
		return str.toString();
	}

}