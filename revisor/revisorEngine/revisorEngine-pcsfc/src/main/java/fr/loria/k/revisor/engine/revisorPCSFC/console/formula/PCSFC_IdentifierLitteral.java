package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConcolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractFormula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;

public class PCSFC_IdentifierLitteral<C extends AbstractRevisorConcolePCSFC<C, ?, ?, ?>> extends AbstractFormula<C, PCSFCFormula> {

	protected final PCSFC_Identifier<C> identifier;
	
	public PCSFC_IdentifierLitteral(final PCSFC_Identifier<C> identifier) {
		this.identifier = identifier;
	}

	public PCSFC_Identifier<C> getIdentifier() {
		return this.identifier;
	}
	
	@Override
	public boolean isUnary() {
		return true;
	}

	@Override
	public Collection<Expression<C, ?>> getChildren() {
		final Collection<Expression<C, ?>> children = new LinkedList<>();
		children.add(this.identifier);
		return children;
	}

	@Override
	public PCSFCFormula getValue(C console) {
		return PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.identifier.getName());
	}

	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		this.identifier.validate(console, newVars);
		try {
			Symbol s = TableOfSymbols.getInstance().identify(new Entry(this.identifier.getName()));
			if (s.getVariableType() != VariableType.FORMULA) {
				throw new IncorrectVariableTypeException("Litteral variable is not a formula");
			}
		} catch (VariableNotDeclaredException | IncorrectVariableTypeException exc) {
			throw new FormulaValidationException(exc);
		}
	}
	
	@Override
	public String toString(boolean latex) {
		// TODO Auto-generated method stub
		return null;
	}

}
