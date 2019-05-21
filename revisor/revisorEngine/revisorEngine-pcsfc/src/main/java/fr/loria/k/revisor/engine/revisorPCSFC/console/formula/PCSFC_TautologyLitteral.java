package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import java.util.Collection;
import java.util.LinkedList;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCTautology;
import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractFormula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;

public class PCSFC_TautologyLitteral<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends AbstractFormula<C, PCSFCFormula> {

	@Override
	public boolean isUnary() {
		return true;
	}

	@Override
	public Collection<Expression<C, ?>> getChildren() {
		return new LinkedList<>();
	}

	@Override
	public PCSFCFormula getValue(C console) {
		return new PCSFCTautology();
	}

	@Override
	public String toString(boolean latex) {
		return "true";
	}
	
}
