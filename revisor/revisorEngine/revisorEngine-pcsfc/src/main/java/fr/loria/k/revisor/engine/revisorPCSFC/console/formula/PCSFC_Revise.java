package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConcolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
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

public class PCSFC_Revise<C extends AbstractRevisorConcolePCSFC<C, ?, ?, ?>> extends SpecialOperator<C, PCSFCFormula> {

	protected static final String UNDECLARED_VARIABLE = "Can't use a variable that has not been declared.";
	protected static final String X_IS_OF_INCORRECT_VARIABLE_TYPE = "'%s' is not a %s";
	protected static final String INCORRECT_VARIABLE_TYPE = "Wrong variable(s) type(s) used in revise()";
	
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		try {
			Symbol sPsi = TableOfSymbols.getInstance().identify(new Entry(this.psiName));
			Symbol sMu = TableOfSymbols.getInstance().identify(new Entry(this.muName));
			Symbol sEps = TableOfSymbols.getInstance().identify(new Entry(this.epsilonName));
			if (sPsi.getVariableType() != VariableType.FORMULA) {
				throw new IncorrectVariableTypeException(String.format(X_IS_OF_INCORRECT_VARIABLE_TYPE, this.psiName, "formula"));
			}
			if (sMu.getVariableType() != VariableType.FORMULA) {
				throw new IncorrectVariableTypeException(String.format(X_IS_OF_INCORRECT_VARIABLE_TYPE, this.muName, "formula"));
			}
			if (sEps.getVariableType() != VariableType.CONSTANT) {
				throw new IncorrectVariableTypeException(String.format(X_IS_OF_INCORRECT_VARIABLE_TYPE, this.epsilonName, "constant"));
			}
			this.psi = PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.psiName);
			this.mu = PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.muName);
			this.epsilon = ((ConstantSymbol) sEps).getValueOfConstant();
			for(Expression<C, ?> child : this.getChildren()) {
				child.validate(console, newVars);
			}
		} catch (VariableNotDeclaredException vnde) {
			console.getLogger().logError(vnde);
			throw new FormulaValidationException(UNDECLARED_VARIABLE);
		} catch (IncorrectVariableTypeException ivte) {
			console.getLogger().logError(ivte);
			throw new FormulaValidationException(INCORRECT_VARIABLE_TYPE);
		}
	}
	
	@Override
	public PCSFCFormula getValue(C console) {
		final PCSFCFormula result = console.revise(this.psi, this.mu, this.epsilon);
		return result;
	}

	@Override
	public String operator(boolean latex) {
		return "revise";
	}

	

}
