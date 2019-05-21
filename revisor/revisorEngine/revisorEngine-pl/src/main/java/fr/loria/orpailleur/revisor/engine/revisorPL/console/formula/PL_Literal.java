package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractFormula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

/**
 * @author William Philbert
 */
public class PL_Literal<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends AbstractFormula<C, PLLiteral> implements Comparable<PL_Literal<?>> {
	
	// Fields :
	
	protected final PL_Identifier<C> identifier;
	protected final boolean inverted;
	
	// Constructors :
	
	public PL_Literal(final PL_Identifier<C> identifier, final boolean inverted) {
		this.identifier = identifier;
		this.inverted = inverted;
	}
	
	// Getters :
	
	public PL_Identifier<C> getIdentifier() {
		return this.identifier;
	}
	
	public boolean isInverted() {
		return this.inverted;
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		final Collection<Expression<C, ?>> children = new LinkedList<>();
		children.add(this.identifier);
		return children;
	}
	
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		this.identifier.validateVariableOnly(console, newVars);
	}
	
	@Override
	public PLLiteral getValue(final C console) {
		return console.getPl().LITT(this.toString());
	}
	
	@Override
	public boolean isUnary() {
		return true;
	}
	
	@Override
	public String toString(final boolean latex) {
		return (this.inverted ? PL.symbol(PL.NOT_SYMBOL, latex) : "") + this.identifier.toString(latex);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object instanceof PL_Literal) {
			final PL_Literal<?> other = (PL_Literal<?>) object;
			return this.identifier.getName().equals(other.identifier.getName()) && (this.inverted == other.inverted);
		}
		
		return false;
	}
	
	@Override
	public int compareTo(final PL_Literal<?> other) {
		if(other != null) {
			final int nameComp = this.identifier.getName().compareTo(other.identifier.getName());
			
			if(nameComp == 0) {
				return (this.inverted ? -1 : 1) - (other.inverted ? -1 : 1);
			}
			
			return nameComp;
		}
		
		return -1;
	}
	
}
