package fr.loria.k.revisor.engine.revisorPCSFC.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Identifier;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormulaVariableList;

public class PCSFC_Identifier<C extends AbstractRevisorConsolePCSFC<C, ?, ?, ?>> extends Identifier<C, PCSFCFormula> {

	public PCSFC_Identifier(String name) {
		super(name);
	}

	@Override
	public Collection<Expression<C, ?>> getChildren() {
		return new LinkedList<>();
	}

	@Override
	public PCSFCFormula getValue(C console) {
		if(console.getMacroList().isMacro(this.name)) {
			return console.getMacroList().getValue(this.name);
		}
		else {
			PCSFCFormulaVariableList.getInstance().getFormulaByIdentifier(this.name);
			return null;
		}
	}

	@Override
	public boolean isRightTypeMacro(C console) {
		return console.getMacroList().isMacro(this.name);
	}

	@Override
	public String formatNameToLatex(String name) {
		return RevisorPCSFC.formatNameToLatex(this.name);
	}
	
}
