package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.ConstantSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.EnumSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;

public class PCSFC_PrintVars<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {

	private static final String VARS_PRINTED = "\nExisting variables printed.";
	
	public PCSFC_PrintVars(C console, String inputText) {
		super(console, inputText);
	}

	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to do here.
	}

	@Override
	protected void doExecute() throws InstructionExecutionException {
		
	}

	protected Collection<Symbol> sortSymbolCollection(Collection<Symbol> collection) {
		ArrayList<Symbol> sortedList = new ArrayList<Symbol>();
		ArrayList<Symbol> temp = new ArrayList<>();
		for (Symbol s: collection) {
			if (s.getVariableType() == VariableType.BOOLEAN) {
				temp.add(s);
			}
		}
		Collections.sort(temp);
		sortedList.addAll(temp);
		temp.clear();
		for (Symbol s: collection) {
			if (s.getVariableType() == VariableType.CONSTANT) {
				temp.add(s);
			}
		}
		Collections.sort(temp);
		sortedList.addAll(temp);
		temp.clear();
		for (Symbol s: collection) {
			if (s.getVariableType() == VariableType.ENUMERATION) {
				temp.add(s);
			}
		}
		Collections.sort(temp);
		sortedList.addAll(temp);
		temp.clear();
		for (Symbol s: collection) {
			if (s.getVariableType() == VariableType.FORMULA) {
				temp.add(s);
			}
		}
		Collections.sort(temp);
		sortedList.addAll(temp);
		temp.clear();
		for (Symbol s: collection) {
			if (s.getVariableType() == VariableType.INTEGER) {
				temp.add(s);
			}
		}
		Collections.sort(temp);
		sortedList.addAll(temp);
		temp.clear();
		for (Symbol s: collection) {
			if (s.getVariableType() == VariableType.REAL) {
				temp.add(s);
			}
		}
		Collections.sort(temp);
		sortedList.addAll(temp);
		temp.clear();
		return sortedList;
	}
	
	@Override
	protected String createFormatedInputText() {
		return this.simplifiedString(this.inputText);
	}

	@Override
	protected String createOutput(boolean latex) {
		StringBuilder str = new StringBuilder();
		ArrayList<Symbol> variableNames = TableOfSymbols.getInstance().getAllSymbols();
		if (variableNames.isEmpty()) {
			if (latex) {
				str.append("\\texttt{No variables declared.}");
			}
			else {
				str.append("No variables declared.");
			}
		}
		else {
			variableNames = (ArrayList<Symbol>) this.sortSymbolCollection(variableNames);
			for (Symbol s : variableNames) {
				if (s.getVariableType() == VariableType.FORMULA) {
					str.append(s.toString(latex) + " -- formula: " + PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(s.getSymbolName()).toString(latex));
				}
				else {
					str.append(s.toString(latex));
				}
				str.append(System.lineSeparator());
			}	
		}
		return str.toString();
	}
	
	@Override
	protected String createOutputText() {
		return this.createOutput(false) + VARS_PRINTED;
	}

}
