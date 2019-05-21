package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.ConstantSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;

public class PCSFC_DeclarationConstant<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends PCSFC_Declaration<C> {

	private double constantValue;
	
	public PCSFC_DeclarationConstant(C console, String inputText, String idf, final String value) {
		super(console, inputText, new ArrayList<String>());
		this.identifiers.add(idf);
		this.constantValue = Double.parseDouble(value);
	}

	/**
	 * Execution of a new declaration instruction: declaring a constant
	 */
	@Override
	protected void doExecute() throws InstructionExecutionException {
		try {
			for (String idf: this.identifiers) {
				TableOfSymbols.getInstance().addEntry(new Entry(idf), new ConstantSymbol(idf, VariableType.CONSTANT, this.constantValue));
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
				str.append("constant \\:" + RevisorPCSFC.formatNameToLatex(idf) + " = " + this.constantValue);
			}
			else {
				str.append("constant " + idf + " = " + this.constantValue);
			}
		}
		return str.toString();
	}

	@Override
	protected String createFormatedInputText() {
		StringBuilder str = new StringBuilder("const ");
		for (String idf: this.identifiers) {
			str.append(idf + " = " + this.constantValue);
		}
		str.append(";");
		return str.toString();
	}

	
}