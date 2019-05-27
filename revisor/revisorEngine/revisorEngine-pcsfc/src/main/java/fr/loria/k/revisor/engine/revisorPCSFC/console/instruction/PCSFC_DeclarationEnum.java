package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.EnumSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;

public class PCSFC_DeclarationEnum<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends PCSFC_Declaration<C> {

	private static final String DETECTED_DUPLICATE_MODALITY = "Duplicated modality";
	private static final String DUPLICATED_MODALITIES = "	WARNING: Duplicated modalities detected. Removing duplicates.";
	
	private ArrayList<String> modalities;
	
	public PCSFC_DeclarationEnum(C console, String inputText, String idf, ArrayList<String> mods) {
		super(console, inputText, new ArrayList<String>());
		this.identifiers.add(idf);
		this.modalities = mods;
	}

	/**
	 * Semantic analysis specific to an enumeration declaration
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
			// the next part is why we are overriding this method: we need to also verify the modalities in
			// order to check for duplicates
			set = new HashSet<String>(this.modalities);
			if (set.size() < this.modalities.size()) {
				this.console.getLogger().logWarning(DETECTED_DUPLICATE_MODALITY);
				this.addWarningMessage(DUPLICATED_MODALITIES);
				this.modalities = new ArrayList<String>(set);
			}
		} catch (DoubleDeclareException doubleDeclareExc) {
			this.console.getLogger().logError(doubleDeclareExc);
			this.addErrorMessage(String.format(VARIABLE_X_ALREADY_DECLARED, latestIdf));
			throw new InstructionValidationException("Invalid Instruction.", null, true, false);
		}
	}
	
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		try {
			for (String idf: this.identifiers) {
				TableOfSymbols.getInstance().addEntry(new Entry(idf), new EnumSymbol(idf, VariableType.ENUMERATION, this.modalities));
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
				str.append("enumeration \\:" + RevisorPCSFC.formatNameToLatex(idf) + " = {");
			}
			else {
				str.append("enumeration " + idf + " = {");
			}
			int i = 0;
			while (i != this.modalities.size() - 1) {
				str.append(this.modalities.get(i) + ", ");
				i++;
			}
			str.append(this.modalities.get(i));
			str.append("}");
		}
		return str.toString();
	}

	@Override
	protected String createFormatedInputText() {
		StringBuilder str = new StringBuilder("enum ");
		for (String idf: this.identifiers) {
			str.append(idf + "(");
			int i = 0;
			while (i != this.modalities.size() - 1) {
				str.append(this.modalities.get(i) + ", ");
				i++;
			}
			str.append(this.modalities.get(i));
		}
		str.append(");");
		return str.toString();
	}

}
