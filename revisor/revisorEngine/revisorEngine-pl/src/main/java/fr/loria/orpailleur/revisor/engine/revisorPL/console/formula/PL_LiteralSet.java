package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractFormula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

/**
 * @author William Philbert
 */
public class PL_LiteralSet<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends AbstractFormula<C, AND> implements Comparable<PL_LiteralSet<?>> {
	
	// Fields :
	
	protected final SortedSet<PL_Literal<C>> literals;
	
	// Constructors :
	
	public PL_LiteralSet() {
		this.literals = this.newLiteralSet();
	}
	
	public PL_LiteralSet(final Set<PL_Literal<C>> literals) {
		this.literals = this.newLiteralSet(literals);
	}
	
	// Methods :
	
	/**
	 * Creates an empty SortedSet to be used to store the literals of this literal set.
	 * @return an empty SortedSet.
	 */
	public SortedSet<PL_Literal<C>> newLiteralSet() {
		return new TreeSet<>();
	}
	
	/**
	 * Creates a SortedSet containing the given literals.
	 * If the given literals are in a set of the proper form, this set is returned, else a set of the proper form is created and returned.
	 * @param literals - the literals to include in the set.
	 * @return a SortedSet containing the given literals.
	 */
	public SortedSet<PL_Literal<C>> newLiteralSet(final Set<PL_Literal<C>> literals) {
		if(literals instanceof TreeSet) {
			return (TreeSet<PL_Literal<C>>) literals;
		}
		else {
			return new TreeSet<>(literals);
		}
	}
	
	/**
	 * Returns the number of literals in this set.
	 * @return the number of literals in this set.
	 */
	public int size() {
		return this.literals.size();
	}
	
	/**
	 * Indicates if this set contains no literal.
	 * @return true if the set contains no literal, else false.
	 */
	public boolean isEmpty() {
		return this.literals.isEmpty();
	}
	
	/**
	 * Indicates whether this literal set contains the given literal.
	 * @param literal - the literal we are looking for.
	 * @return true if this literal set contains the given literal, else false.
	 */
	public boolean contains(final PL_Literal<C> literal) {
		return this.literals.contains(literal);
	}
	
	/**
	 * Returns an unmodifiable view of the literals of this set.
	 * @return an unmodifiable view of the literals of this set.
	 */
	public SortedSet<PL_Literal<C>> getLiterals() {
		return Collections.unmodifiableSortedSet(this.literals);
	}
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		final Collection<Expression<C, ?>> children = new LinkedList<>();
		children.addAll(this.literals);
		return children;
	}
	
	@Override
	public AND getValue(final C console) {
		PLLiteral[] literals = new PLLiteral[this.literals.size()];
		int i = 0;
		
		for(PL_Literal<C> literal : this.literals) {
			literals[i] = literal.getValue(console);
			i++;
		}
		
		return console.getPl().AND(literals);
	}
	
	@Override
	public boolean isUnary() {
		return this.literals.size() == 1;
	}
	
	protected String operator(final boolean latex) {
		return latex ? PL.LATEX_AND_SYMBOL : PL.AND_SYMBOL;
	}
	
	@Override
	public String toString(final boolean latex) {
		final String space = StringUtils.symbol(" ", latex);
		return StringUtils.toString(this.literals, latex, "", space + this.operator(latex) + space, "");
	}
	
	@Override
	public boolean equals(final Object object) {
		return (object instanceof PL_LiteralSet) && this.literals.equals(((PL_LiteralSet<?>) object).literals);
	}
	
	@Override
	public int compareTo(final PL_LiteralSet<?> other) {
		if(other == null) {
			return -1;
		}
		
		if(this.equals(other)) {
			return 0;
		}
		
		PL_Literal<?>[] thisSet = new PL_Literal<?>[this.literals.size()];
		PL_Literal<?>[] otherSet = new PL_Literal<?>[this.literals.size()];
		thisSet = this.literals.toArray(thisSet);
		otherSet = other.literals.toArray(otherSet);
		int i = 0;
		
		while(true) {
			final boolean exceedThisSize = i >= thisSet.length;
			final boolean exceedOtherSize = i >= otherSet.length;
			
			if(exceedThisSize && exceedOtherSize) {
				return 0;
			}
			
			if(exceedThisSize) {
				return -1;
			}
			
			if(exceedOtherSize) {
				return 1;
			}
			
			int result = thisSet[i].compareTo(otherSet[i]);
			
			if(result != 0) {
				return result;
			}
			
			i++;
		}
	}
	
}
