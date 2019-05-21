package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.MissingEnumModalityException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCBoolean;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractFormula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;

public class PCSFC_IdentifierLitteral<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends AbstractFormula<C, PCSFCFormula> {

	private static final String X_IS_MISSING_MODALITY = "'%s' is missing which modality it needs to be";
	private static final String ENUM_VARIABLE_MISSING_MODALITY = "Enumeration litterals must be used with one of their modality.";

	
	protected final PCSFC_Identifier<C> identifier;
	protected VariableType type;
	
	public PCSFC_IdentifierLitteral(final PCSFC_Identifier<C> identifier) {
		this.identifier = identifier;
		this.type = null;
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
		if (this.type == VariableType.FORMULA) {
			return PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.identifier.getName());
		}
		else { // if (this.type == VariableType.BOOLEAN)
			return new PCSFCBoolean(identifier.getName());
		}
	}

	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		this.identifier.validate(console, newVars);
		try {
			Symbol s = TableOfSymbols.getInstance().identify(new Entry(this.identifier.getName()));
			this.type = s.getVariableType();
			// only formula, boolean and enumeration identifiers can be used as litterals
			if (this.type != VariableType.FORMULA && this.type != VariableType.BOOLEAN && this.type != VariableType.ENUMERATION) {
				throw new IncorrectVariableTypeException("Litteral variable is not a formula, a boolean or an enumeration", null, true, false);			}
			// if the identifiers identifies an enumeration in this class, that means no modality has been specified.
			// therefore we throw an exception
			if (this.type == VariableType.ENUMERATION) {
				throw new MissingEnumModalityException(String.format(X_IS_MISSING_MODALITY, s.getSymbolName()), null, true, false);
			}
		} catch (VariableNotDeclaredException | IncorrectVariableTypeException exc) {
			throw new FormulaValidationException(exc);
		} catch (MissingEnumModalityException meme) {
			console.getLogger().logError(meme);
			throw new FormulaValidationException(ENUM_VARIABLE_MISSING_MODALITY, null, true, false);
		}
	}
	
	@Override
	public String toString(boolean latex) {
		if (latex) {
			RevisorPCSFC.formatNameToLatex(this.identifier.getName());
		}
		return this.identifier.getName();
	}

}
