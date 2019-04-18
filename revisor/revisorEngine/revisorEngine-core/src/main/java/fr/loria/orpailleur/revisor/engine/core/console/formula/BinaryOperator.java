package fr.loria.orpailleur.revisor.engine.core.console.formula;

import java.util.Collection;
import java.util.LinkedList;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

/**
 * @author William Philbert
 */
public abstract class BinaryOperator<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends AbstractFormula<C, V> {
	
	// Fields :
	
	protected final Formula<C, V> left;
	protected final Formula<C, V> right;
	
	// Constructors :
	
	protected BinaryOperator(final Formula<C, V> left, final Formula<C, V> right) {
		this.left = left;
		this.right = right;
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		Collection<Expression<C, ?>> children = new LinkedList<>();
		children.add(this.left);
		children.add(this.right);
		return children;
	}
	
	@Override
	public boolean isUnary() {
		return false;
	}
	
	public abstract boolean canExtend();
	
	public abstract String operator(final boolean latex);
	
	@Override
	public String toString(final boolean latex) {
		final StringBuilder builder = new StringBuilder();
		
		final boolean extendToLeft = this.left.isUnary() || (this.canExtend() && (this.left.getClass() == this.getClass()));
		final boolean extendToRight = this.right.isUnary() || (this.canExtend() && (this.right.getClass() == this.getClass()));
		
		final String leftPar = StringUtils.symbol("(", latex);
		final String rightPar = StringUtils.symbol(")", latex);
		final String space = StringUtils.symbol(" ", latex);
		
		if(!extendToLeft) {
			builder.append(leftPar);
		}
		
		builder.append(this.left.toString(latex));
		
		if(!extendToLeft) {
			builder.append(rightPar);
		}
		
		builder.append(space);
		builder.append(this.operator(latex));
		builder.append(space);
		
		if(!extendToRight) {
			builder.append(leftPar);
		}
		
		builder.append(this.right.toString(latex));
		
		if(!extendToRight) {
			builder.append(rightPar);
		}
		
		return builder.toString();
	}
	
}
