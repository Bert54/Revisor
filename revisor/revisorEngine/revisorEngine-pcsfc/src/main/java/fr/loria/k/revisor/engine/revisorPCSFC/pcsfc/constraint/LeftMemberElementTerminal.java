package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Entry;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.Symbol;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.TableOfSymbols;
import fr.loria.k.revisor.engine.revisorPCSFC.console.tos.VariableType;

public class LeftMemberElementTerminal<T> extends LeftMember {

	private T coefficient;
	private Variable variable;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LeftMemberElementTerminal(fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElementTerminal element) {
		try {
		this.coefficient = (T) element.getCoefficient();
		Symbol s;
			s = TableOfSymbols.getInstance().identify((new Entry(element.getVariableName())));
			if (s.getVariableType() == VariableType.INTEGER) {
				this.variable = new IntegerVariable(element.getVariableName());
			}
			else {
				this.variable = new RealVariable(element.getVariableName());
			}
			} catch (VariableNotDeclaredException e) {
				e.printStackTrace();
			}
	}

	public LeftMemberElementTerminal(T coefficient, String variableName) {
		this.coefficient = coefficient;
		if (((double)coefficient % 1) == 0) {
			this.variable = new IntegerVariable(variableName);
		}
		else {
			this.variable = new RealVariable(variableName);
		}
	}
	
	public LeftMemberElementTerminal(LeftMemberElementTerminal<T> lm) {
		this.coefficient = lm.getCoefficient();
		this.variable = lm.getVariable();
	}
	
	@SuppressWarnings("unchecked")
	public LeftMemberElementTerminal(String termName) {
		this.coefficient = (T)new Double(1);
		this.variable = new RealVariable(termName);
	}

	public T getCoefficient() {
		return this.coefficient;
	}
	
	public Variable getVariable() {
		return this.variable;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if (((double)coefficient == Math.floor((double)coefficient)) && !Double.isInfinite((double)coefficient)) {
			int coeffInt = ((Double) this.coefficient).intValue();
			if (coeffInt == -1) {
				str.append("-");
			}
			else if (coeffInt != 1) {
				str.append(coeffInt);
			}
		}
		else {
			str.append(this.coefficient.toString());
		}
		str.append(this.variable);
		return str.toString();
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

}
