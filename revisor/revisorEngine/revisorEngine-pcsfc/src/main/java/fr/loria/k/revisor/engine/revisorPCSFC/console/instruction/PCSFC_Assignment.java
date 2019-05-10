package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConcolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.formula.PCSFC_Identifier;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Assignment;

public class PCSFC_Assignment<C extends AbstractRevisorConcolePCSFC<C, ?, ?, ?>> extends Assignment<C, PCSFC_Identifier<C>, Formula<C, PCSFCFormula>, PCSFCFormula> {

	protected static final String CANT_ASSIGN_UNDECLARED_VARIABLE = "Can't assign variables that have not been declared.";
	protected static final String CAN_ONLY_ASSIGN_FORMULA_VARIABLES = "Only formula variables can be assigned.";
	protected static final String ASSIGN_WRONG_VARIABLE_TYPE = "Assigned variable is not of formula type.";

	
	public PCSFC_Assignment(C console, String inputText, PCSFC_Identifier<C> left, Formula<C, PCSFCFormula> right) {
		super(console, inputText, left, right);
	}

	/**
	 * Semantic analysis of an assignment
	 */
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.right.validate(this.console, this.newVars); // Basic semantic analysis on right operand of assignment
		this.left.validateLeft(this.console, this.newVars); // Basic semantic analysis on left operand of assignment
		try { // checks if identifier exists and if it is a formula
			Symbol s = TableOfSymbols.getInstance().identify(new Entry(this.left.getName()));
			if (s.getVariableType() != VariableType.FORMULA) {
				throw new IncorrectVariableTypeException(ASSIGN_WRONG_VARIABLE_TYPE);
			}
		} catch (VariableNotDeclaredException vnde) {
			this.console.getLogger().logError(vnde);
			this.addErrorMessage(CANT_ASSIGN_UNDECLARED_VARIABLE);
			throw new InstructionValidationException("Invalid Instruction.");
		} catch (IncorrectVariableTypeException ivte) {
			this.console.getLogger().logError(ivte);
			this.addErrorMessage(CAN_ONLY_ASSIGN_FORMULA_VARIABLES);
			throw new InstructionValidationException("Invalid Instruction.");
		}
		this.addWarningMessages(this.left);
		this.addWarningMessages(this.right);
	}
	
	/*
	 * Execution of the instruction: assigning a PCSFC FOrmula to a variable that is a formula
	 */
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.registerVariables(this.newVars);
		this.result = this.right.getOptimizedValue(this.console);
		PCSFCFormulaVariableList.getInstance().updateFormulaVariable(left.getName(), this.result);
		this.sideNotes.addAll(this.right.getSideNotes());
		this.registerMacro(this.left.getName(), this.result);
	}
	
	@Override
	protected void registerMacro(String name, PCSFCFormula value) throws InstructionExecutionException {
		this.console.getMacroList().addMacro(name, value);
	}

}
