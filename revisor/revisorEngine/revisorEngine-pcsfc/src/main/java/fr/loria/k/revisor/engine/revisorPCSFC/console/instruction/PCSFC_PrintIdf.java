package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import fr.loria.k.revisor.engine.revisorPCSFC.console.RevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.formula.PCSFC_Identifier;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;

public class PCSFC_PrintIdf<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {

	private static final String IDF_PRINTED = "\nIdentifier '%s' has been printed.";
	private static final String CANT_USE_UNDECLARED_IDF = "Can't use an identifier that has not been declared.";
	
	
	private PCSFC_Identifier<RevisorConsolePCSFC> idf;
	private Symbol symbol;
	
	public PCSFC_PrintIdf(C console, String inputText, String idfName) {
		super(console, inputText);
		this.idf = new PCSFC_Identifier<RevisorConsolePCSFC>(idfName);
		this.symbol = null;
	}

	@Override
	protected void doValidate() throws InstructionValidationException {
		try {
			this.symbol = TableOfSymbols.getInstance().identify(new Entry(this.idf.getName()));
		} catch (VariableNotDeclaredException vnde) {
			this.console.getLogger().logError(vnde);
			this.addErrorMessage(CANT_USE_UNDECLARED_IDF);
			throw new InstructionValidationException("Invalid Instruction.", null, true, false);
		}
		
	}

	@Override
	protected void doExecute() throws InstructionExecutionException {
		if (this.symbol.getVariableType() == VariableType.FORMULA) {
			System.out.println(this.symbol + " -- formula: " + PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.symbol.getSymbolName()).toString(false));
		}
		else {
			System.out.println(this.symbol);
		}
	}

	@Override
	protected String createFormatedInputText() {
		return this.simplifiedString(this.inputText);
	}

	@Override
	protected String createOutput(boolean latex) {
		StringBuilder str = new StringBuilder(); 
		if (this.symbol.getVariableType() == VariableType.FORMULA) {
			str.append(this.symbol.toString(latex) + " -- formula: " + PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.symbol.getSymbolName()).toString(latex));
		}
		else {
			str.append(this.symbol.toString(latex));
		}
		return str.toString();
	}
	
	@Override
	protected String createOutputText() {
		return this.createOutput(false) + String.format(IDF_PRINTED, this.idf.getName());
	}

}
