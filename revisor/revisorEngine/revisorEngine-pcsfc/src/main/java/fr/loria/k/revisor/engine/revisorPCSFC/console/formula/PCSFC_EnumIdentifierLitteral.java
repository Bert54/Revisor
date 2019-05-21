package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.IncorrectVariableTypeException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.InvalidEnumModalityException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.EnumSymbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCEnumeration;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCTautology;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

public class PCSFC_EnumIdentifierLitteral<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>>  extends PCSFC_IdentifierLitteral<C> {

	private static final String X_IS_NOT_MODALITY = "'%s' is not a modality of '%s'.";
	private static final String CANT_USE_INEXISTENT_MODALITY = "Can't use a modality that doesn't exist.";
	
	private final String modality;
	
	public PCSFC_EnumIdentifierLitteral(PCSFC_Identifier<C> identifier, String mod) {
		super(identifier);
		this.modality = mod;
	}
	
	@Override
	public PCSFCFormula getValue(C console) {
		Symbol s;
		try {
			s = TableOfSymbols.getInstance().identify(new Entry(this.identifier.getName()));
			return new PCSFCEnumeration(this.identifier.getName(), this.modality, ((EnumSymbol) s).getModalities());
		} catch (VariableNotDeclaredException e) {
			e.printStackTrace();
		}
		return new PCSFCTautology(); // failsafe
	}

	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		this.identifier.validate(console, newVars);
		try {
			Symbol s = TableOfSymbols.getInstance().identify(new Entry(this.identifier.getName()));
			if (s.getVariableType() != VariableType.ENUMERATION) { // failsafe
				throw new IncorrectVariableTypeException("Litteral variable is not a formula, a boolean or an enumeration", null, true, false);
			}
			this.type = s.getVariableType();
			// we check that the modality the modality this variable must be actually exists
			if (!((EnumSymbol) s).getModalities().contains(this.modality)) {
				throw new InvalidEnumModalityException(String.format(X_IS_NOT_MODALITY, this.modality, s.getSymbolName()), null, true, false);
			}
		} catch (VariableNotDeclaredException | IncorrectVariableTypeException exc) {
			throw new FormulaValidationException(exc);
		} catch (InvalidEnumModalityException ieme) {
			console.getLogger().logError(ieme);
			throw new FormulaValidationException(CANT_USE_INEXISTENT_MODALITY, null, true, false);
		}
	}
	
	@Override
	public String toString(boolean latex) {
		StringBuilder str = new StringBuilder();
		if (latex) {
			str.append(RevisorPCSFC.formatNameToLatex(this.identifier.getName()));
		}
		else {
			str.append(this.identifier.getName());
		}
		str.append(" = ");
		str.append(this.modality);
		return str.toString();
	}

}
