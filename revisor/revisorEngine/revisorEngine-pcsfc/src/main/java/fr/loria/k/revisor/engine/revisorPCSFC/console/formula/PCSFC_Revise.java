package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.InvalidClassUsedInRevisionException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.ConstantSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.SpecialOperator;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

public class PCSFC_Revise<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends SpecialOperator<C, PCSFCFormula> {

	protected static final String UNDECLARED_VARIABLE = "Can't use a variable that has not been declared.";
	protected static final String X_IS_OF_INCORRECT_VARIABLE_TYPE = "'%s' is not a %s";
	protected static final String INCORRECT_VARIABLE_TYPE = "Wrong variable(s) type(s) used in revise()";
	protected static final String CANT_USE_X_IN_REVISION = "'%s' is not a valid formula for the revision algorithm.";
	protected static final String INCORRECT_FORMULA_IN_REVISION = "At least one of the formulas can't be used by the revision algorithm.";
	
	protected final String psiName;
	protected PCSFCFormula psi;
	protected final String muName;
	protected PCSFCFormula mu;
	protected final String epsilonName;
	protected double epsilon;
	
	public PCSFC_Revise(final String psi, final String mu, final String epsilon) {
		super();
		this.psiName = psi;
		this.psi = null;
		this.muName = mu;
		this.mu = null;
		this.epsilonName = epsilon;
	}
	
	public PCSFC_Revise(final String psi, final String mu, double eps) {
		super();
		this.psiName = psi;
		this.psi = null;
		this.muName = mu;
		this.mu = null;
		this.epsilonName = "";
		this.epsilon = eps;
	}
	
	/**
	 * Semantic analysis of a revision as a formula
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		try {
			// checks if the identifiers used as psi and mu have been declared
			Symbol sPsi = TableOfSymbols.getInstance().identify(new Entry(this.psiName));
			Symbol sMu = TableOfSymbols.getInstance().identify(new Entry(this.muName));
			// checks if the identifiers used as psi and mu are formulas
			if (sPsi.getVariableType() != VariableType.FORMULA) {
				throw new IncorrectVariableTypeException(String.format(X_IS_OF_INCORRECT_VARIABLE_TYPE, this.psiName, "formula"));
			}
			if (sMu.getVariableType() != VariableType.FORMULA) {
				throw new IncorrectVariableTypeException(String.format(X_IS_OF_INCORRECT_VARIABLE_TYPE, this.muName, "formula"));
			}
			this.psi = PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.psiName).toPCLC();
			this.mu = PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.muName).toPCLC();
			// if epsilon has been provided as an identifier, checks if the identifier exists and if it
			// is a constant.
			if (!this.epsilonName.equals("")) {
				Symbol sEps = TableOfSymbols.getInstance().identify(new Entry(this.epsilonName));
				if (sEps.getVariableType() != VariableType.CONSTANT) {
					throw new IncorrectVariableTypeException(String.format(X_IS_OF_INCORRECT_VARIABLE_TYPE, this.epsilonName, "constant"));
				}
				this.epsilon = ((ConstantSymbol) sEps).getValueOfConstant();
			}
			// the two next conditions are failsafes in order to make sure that psi and mu can be used by
			// the revision algorithm.
			if (!this.psi.canRevise()) {
				throw new InvalidClassUsedInRevisionException(String.format(CANT_USE_X_IN_REVISION, this.psi));
			}
			if (!this.mu.canRevise()) {
				throw new InvalidClassUsedInRevisionException(String.format(CANT_USE_X_IN_REVISION, this.mu));
			}
			for(Expression<C, ?> child : this.getChildren()) {
				child.validate(console, newVars);
			}
		} catch (VariableNotDeclaredException vnde) {
			console.getLogger().logError(vnde);
			throw new FormulaValidationException(UNDECLARED_VARIABLE);
		} catch (IncorrectVariableTypeException ivte) {
			console.getLogger().logError(ivte);
			throw new FormulaValidationException(INCORRECT_VARIABLE_TYPE);
		} catch (InvalidClassUsedInRevisionException icuire) {
			console.getLogger().logError(icuire);
			throw new FormulaValidationException(INCORRECT_FORMULA_IN_REVISION);
		}
	}
	
	public PCSFCFormula getPsi() {
		return this.psi;
	}
	
	public PCSFCFormula getMu() {
		return this.mu;
	}
	
	/**
	 * launches the revision
	 */
	@Override
	public PCSFCFormula getValue(C console) {	
		final PCSFCFormula result = console.revise(this.psi, this.mu, this.epsilon);
		if (console.mustConvertPCLCFormulaAfterRevision()) {
			return result.toPCSFC();
		}
		return result;
	}

	@Override
	public String operator(boolean latex) {
		return "revise";
	}

	@Override
	public String toString(final boolean latex) {
		if (!this.epsilonName.equals("")) {
			return this.operator(latex) + "(" + this.psiName + ", " + this.muName + ", " + this.epsilon + ")";
		}
		else {
			return this.operator(latex) + "(" + this.psiName + ", " + this.muName + ", " + this.epsilonName + ")";
		}
	}

}
