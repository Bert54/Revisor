package fr.loria.orpailleur.revisor.engine.core.console.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

/**
 * @author William Philbert
 */
public abstract class SpecialOperator<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends AbstractFormula<C, V> {
	
	// Fields :
	
	protected final List<Expression<C, ?>> children;
	
	// Constructors :
	
	@SafeVarargs
	protected SpecialOperator(final Expression<C, ?>... childs) {
		this.children = new ArrayList<>(childs.length);
		
		for(Expression<C, ?> child : childs) {
			this.children.add(child);
		}
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		return Collections.unmodifiableList(this.children);
	}
	
	@Override
	public boolean isUnary() {
		return true;
	}
	
	public abstract String operator(final boolean latex);
	
	@Override
	public String toString(final boolean latex) {
		return this.operator(latex) + StringUtils.toString(this.children, latex);
	}
	
}
