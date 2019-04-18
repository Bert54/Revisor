package fr.loria.orpailleur.revisor.engine.core.console.formula;

import java.util.Collection;
import java.util.LinkedList;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexStringBuilder;

/**
 * @author William Philbert
 */
public abstract class UnaryOperator<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends AbstractFormula<C, V> {
	
	// Fields :
	
	protected final Formula<C, V> child;
	
	// Constructors :
	
	protected UnaryOperator(final Formula<C, V> child) {
		this.child = child;
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		Collection<Expression<C, ?>> children = new LinkedList<>();
		children.add(this.child);
		return children;
	}
	
	@Override
	public boolean isUnary() {
		return true;
	}
	
	public abstract String operator(final boolean latex);
	
	@Override
	public String toString(final boolean latex) {
		final LatexStringBuilder builder = new LatexStringBuilder();
		final boolean parentheses = !this.child.isUnary();
		
		builder.append(this.operator(latex));
		
		if(parentheses) {
			builder.append("(", latex);
		}
		
		builder.append(this.child.toString(latex));
		
		if(parentheses) {
			builder.append(")", latex);
		}
		
		return builder.toString();
	}
	
}
