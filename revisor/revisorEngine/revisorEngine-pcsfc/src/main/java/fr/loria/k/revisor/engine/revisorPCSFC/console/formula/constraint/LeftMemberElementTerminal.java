package fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.formula.PCSFC_Identifier;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

public class LeftMemberElementTerminal<T, C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends LeftMember {

	protected static final String MALFORMED_COEFFICIENT = "Coefficient in left member of constraint not an integer or real.";
	protected static final String VARIABLE_X_OF_WRONG_TYPE = "Variable '%s' not an integer or real.";
	protected static final String UNDECLARED_VARIABLE = "Can't use a variable that has not been declared.";
	protected static final String WRONG_TYPE_OF_CONSTRAINT_VARIABLE = "Can't use other types of variable than integer or real as constraint variables.";

	
	private T coefficient;
	private final PCSFC_Identifier<C> identifier;
	
	public LeftMemberElementTerminal(T coeff, PCSFC_Identifier<C> identifier) {
		this.coefficient = coeff;
		this.identifier = identifier;
	}

	public T getCoefficient() {
		return this.coefficient;
	}
	
	public String getVariableName() {
		return this.identifier.getName();
	}
	
	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public void validate() throws FormulaValidationException {
		if (!(coefficient instanceof Integer || coefficient instanceof Double)) {
			throw new FormulaValidationException(MALFORMED_COEFFICIENT);
		}
		try {
			Symbol s = TableOfSymbols.getInstance().identify(new Entry(this.identifier.getName()));
			if (!(s.getVariableType() == VariableType.INTEGER || s.getVariableType() == VariableType.REAL)) {
				throw new IncorrectVariableTypeException(String.format(VARIABLE_X_OF_WRONG_TYPE, this.identifier.getName()));
			}
		} catch (VariableNotDeclaredException vnde) {
			throw new FormulaValidationException(UNDECLARED_VARIABLE);
		} catch (IncorrectVariableTypeException ivte) {
			throw new FormulaValidationException(WRONG_TYPE_OF_CONSTRAINT_VARIABLE);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if (((double)coefficient == Math.floor((double)coefficient)) && !Double.isInfinite((double)coefficient)) {
			int coeffInt = ((Double) this.coefficient).intValue();
			if (coeffInt != 1) {
				str.append(coeffInt);
			}
		}
		else {
			str.append(this.coefficient.toString());
		}
		str.append(this.identifier.getName());
		return str.toString();
	}

}
