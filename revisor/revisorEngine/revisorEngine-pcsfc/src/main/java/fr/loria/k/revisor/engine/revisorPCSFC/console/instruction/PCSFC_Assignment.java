package fr.loria.k.revisor.engine.revisorPCSFC.console.instruction;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.formula.PCSFC_Identifier;
import fr.loria.k.revisor.engine.revisorPCSFC.console.formula.PCSFC_Revise;
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
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;

public class PCSFC_Assignment<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends Assignment<C, PCSFC_Identifier<C>, Formula<C, PCSFCFormula>, PCSFCFormula> {

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
				throw new IncorrectVariableTypeException(ASSIGN_WRONG_VARIABLE_TYPE, null, true, false);
			}
		} catch (VariableNotDeclaredException vnde) {
			this.console.getLogger().logError(vnde);
			this.addErrorMessage(CANT_ASSIGN_UNDECLARED_VARIABLE);
			throw new InstructionValidationException("Invalid Instruction.", null, true, false);
		} catch (IncorrectVariableTypeException ivte) {
			this.console.getLogger().logError(ivte);
			this.addErrorMessage(CAN_ONLY_ASSIGN_FORMULA_VARIABLES);
			throw new InstructionValidationException("Invalid Instruction.", null, true, false);
		}
		this.addWarningMessages(this.left);
		this.addWarningMessages(this.right);
	}
	
	/*
	 * Execution of the instruction: assigning a PCSFC Formula to a variable that is a formula
	 */
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.registerVariables(this.newVars);
		this.result = this.right.getOptimizedValue(this.console);
		PCSFCFormulaVariableList.getInstance().updateFormulaVariable(left.getName(), this.result);
		this.sideNotes.addAll(this.right.getSideNotes());
		this.registerMacro(this.left.getName(), this.result);
	}
	
	protected String createFormatedInputText() {
		return String.format("%s %s %s;", this.left, this.operator(), this.right);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		StringBuilder str = new StringBuilder();
		// The next bloc is used when we are trying to assign a revised formula to a formula identifier.
		// If the boolean parameter in the engine's settings that asks if the two formulas we need to
		// revise need to be displayed in their PCLC form, then this next bloc generate an output that
		// contains these two formulas in said form.
		if (this.right instanceof PCSFC_Revise) {
			
			// TODO this next warning message is temporary. It states that the algorithm revision hasn't been
			// implemented yet. Remove it once it is done.
			
			if (!this.hasWarningMessages()) {	
				this.addWarningMessage("WARNING: the revision algorithm hasn't been implemented yet. The returned formula is a hardcoded place-holder formula that needs to be removed (alongside this warning) once the algorithm has been implemented.");
			}
			
			if (this.console.displayPCLCFormulas()) {
			String psiSymb;
			String muSymb;
			if (latex) {
				psiSymb = "{\\psi}\\:"; 
				muSymb = "{\\mu}\\:";
			}
			else {
				psiSymb = "psi"; 
				muSymb = "mu";
			}
			str.append("PCLC(" + psiSymb + ") = " + ((PCSFC_Revise<C>) this.right).getPsi().toString(latex) + System.lineSeparator());
			str.append("PCLC(" + muSymb + ") = " +  ((PCSFC_Revise<C>) this.right).getMu().toString(latex) + System.lineSeparator());
			}
		}
		String left = this.left.toString(latex);
		String right = (this.result instanceof LatexFormatable) ? ((LatexFormatable) this.result).toString(latex) : this.result.toString();
		str.append(String.format("%s = %s", left, right));
		return str.toString();
	}
	
	@Override
	protected void registerMacro(String name, PCSFCFormula value) throws InstructionExecutionException {
		this.console.getMacroList().addMacro(name, value);
	}

}
