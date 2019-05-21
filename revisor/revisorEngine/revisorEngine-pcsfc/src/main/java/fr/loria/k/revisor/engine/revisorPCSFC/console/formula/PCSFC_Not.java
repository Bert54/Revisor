package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCNot;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.UnaryOperator;

public class PCSFC_Not<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends UnaryOperator<C, PCSFCFormula> {

	public PCSFC_Not(Formula<C, PCSFCFormula> child) {
		super(child);
	}

	@Override
	public PCSFCFormula getValue(C console) {
		return new PCSFCNot(this.child.getValue(console));
	}

	@Override
	public String operator(boolean latex) {
		return "!";
	}

}
