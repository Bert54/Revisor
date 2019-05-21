package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexStringBuilder;

public abstract class PLUnaryFormula extends PLFormula {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected PLFormula child;
	
	// Constructors :
	
	public PLUnaryFormula(final LI li, final PLFormula f) {
		super(li);
		this.child = f;
	}
	
	// Methods :
	
	@Override
	public PLFormula[] listeFils() {
		PLFormula[] fps = {this.child};
		return fps;
	}
	
	public abstract String operator(final boolean latex);
	
	@Override
	protected boolean isUnary() {
		return true;
	}
	
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
