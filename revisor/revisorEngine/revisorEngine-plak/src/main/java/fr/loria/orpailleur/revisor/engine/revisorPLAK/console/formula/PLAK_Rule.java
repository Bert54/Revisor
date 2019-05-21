package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractExpression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexStringBuilder;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Literal;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_LiteralSet;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_Rule<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractExpression<C, PLAK_Rule<C>> implements Comparable<PLAK_Rule<?>> {
	
	// Constants :
	
	private static final String RULE_COSTS_MUST_BE_STRICTLY_POSITIVE = "Rule costs must be strictly positive.";
	private static final String MISSING_LITERAL_IN_LEFT_OR_RIGHT_SIDE = "There must be at least one literal in the left or right side of the rule.";
	private static final String EQUAL_LEFT_AND_RIGHT_PARTS = "The left part and the right part of a rule must be different.";
	private static final String LITERAL_IN_CONTEXT_AND_LEFT_PART = "The literal '%s' can't be in the context and the left part of the same rule.";
	private static final String LITERAL_IN_CONTEXT_AND_RIGHT_PART = "The literal '%s' can't be in the context and the right part of the same rule.";
	private static final String LITERAL_IN_LEFT_AND_RIGHT_PART = "The literal '%s' is in the left and the right part of the same rule; It should be placed in the context part instead.";
	
	// Fields :
	
	protected final PL_LiteralSet<C> context;
	protected final PL_LiteralSet<C> left;
	protected final PL_LiteralSet<C> right;
	protected double cost;
	
	// Constructors :
	
	public PLAK_Rule(final PL_LiteralSet<C> context, final PL_LiteralSet<C> left, final PL_LiteralSet<C> right, final double cost) {
		this.context = context;
		this.left = left;
		this.right = right;
		this.cost = cost;
	}
	
	// Getters :
	
	public PL_LiteralSet<C> getContext() {
		return this.context;
	}
	
	public PL_LiteralSet<C> getLeft() {
		return this.left;
	}
	
	public PL_LiteralSet<C> getRight() {
		return this.right;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	// Setters :
	
	public void setCost(final double cost) {
		this.cost = cost;
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		final Collection<Expression<C, ?>> children = new LinkedList<>();
		children.add(this.context);
		children.add(this.left);
		children.add(this.right);
		return children;
	}
	
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		if(this.cost <= 0) {
			throw new FormulaValidationException(RULE_COSTS_MUST_BE_STRICTLY_POSITIVE);
		}
		
		if(this.left.size() == 0 && this.right.size() == 0) {
			throw new FormulaValidationException(MISSING_LITERAL_IN_LEFT_OR_RIGHT_SIDE);
		}
		
		if(this.left.equals(this.right)) {
			throw new FormulaValidationException(EQUAL_LEFT_AND_RIGHT_PARTS);
		}
		
		for(PL_Literal<C> literal : this.context.getLiterals()) {
			if(this.left.contains(literal)) {
				throw new FormulaValidationException(String.format(LITERAL_IN_CONTEXT_AND_LEFT_PART, literal));
			}
			if(this.right.contains(literal)) {
				throw new FormulaValidationException(String.format(LITERAL_IN_CONTEXT_AND_RIGHT_PART, literal));
			}
		}
		
		for(PL_Literal<C> literal : this.left.getLiterals()) {
			if(this.right.contains(literal)) {
				this.addWarningMessage(String.format(LITERAL_IN_LEFT_AND_RIGHT_PART, literal));
				break;
			}
		}
		
		super.validate(console, newVars);
	}
	
	@Override
	public PLAK_Rule<C> getValue(final C console) {
		return this;
	}
	
	public PLAK_Rule<C> copy() {
		return new PLAK_Rule<>(this.context, this.left, this.right, this.cost);
	}
	
	public String operator(final boolean latex) {
		return latex ? PL.LATEX_SUBSTITUTION_SYMBOL : PL.SUBSTITUTION_SYMBOL;
	}
	
	@Override
	public String toString(final boolean latex) {
		final LatexStringBuilder builder = new LatexStringBuilder();
		final String space = StringUtils.symbol(" ", latex);
		
		builder.append("[", latex);
		builder.append(GuiConstants.DEFAULT_NUMBER_FORMAT.format(this.cost));
		builder.append("]", latex);
		builder.append(space);
		
		if(!this.context.isEmpty()) {
			builder.append(this.context.toString(latex));
			builder.append(" : ", latex);
		}
		
		builder.append(this.left.toString(latex));
		builder.append(space);
		builder.append(this.operator(latex));
		builder.append(space);
		builder.append(this.right.toString(latex));
		
		return builder.toString();
	}
	
	public Set<PL_Literal<C>> getAllLeftLiterals() {
		Set<PL_Literal<C>> result = new TreeSet<>();
		result.addAll(this.left.getLiterals());
		result.addAll(this.context.getLiterals());
		return result;
	}
	
	public Set<PL_Literal<C>> getAllRightLiterals() {
		Set<PL_Literal<C>> result = new TreeSet<>();
		result.addAll(this.right.getLiterals());
		result.addAll(this.context.getLiterals());
		return result;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object instanceof PLAK_Rule) {
			PLAK_Rule<?> other = (PLAK_Rule<?>) object;
			return this.getAllLeftLiterals().equals(other.getAllLeftLiterals()) && this.getAllRightLiterals().equals(other.getAllRightLiterals());
		}
		
		return false;
	}
	
	@Override
	public int compareTo(final PLAK_Rule<?> other) {
		if(other == null) {
			return -1;
		}
		
		if(this.equals(other)) {
			return 0;
		}
		
		int result = this.context.compareTo(other.context);
		
		if(result == 0) {
			result = this.left.compareTo(other.left);
			
			if(result == 0) {
				result = this.right.compareTo(other.right);
			}
		}
		
		return result;
	}
	
}
