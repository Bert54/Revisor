package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint.LeftMemberElementTerminal;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint.OperatorMoreEquals;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint.RightMember;

public class PCSFCBoolean extends PCSFCFormula {

	public static final int DELIMITER = 1;
	
	private final String name;
	
	public PCSFCBoolean(final String n) {
		this.name = n;
	}

	@Override
	public String toString(boolean latex) {
		if (latex) {
			return RevisorPCSFC.formatNameToLatex(this.name);
		}
		return this.name;
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCConstraint(new LeftMemberElementTerminal<Double>("integer_encoding_" + this.name), new OperatorMoreEquals(), new RightMember<Double>((double)DELIMITER));
	}

	@Override
	public PCSFCFormula toPCSFC() {
		return new PCSFCBoolean(this.name);
	}
	
	@Override
	public boolean canRevise() {
		return false;
	}

}
