package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import java.util.Collection;
import java.util.LinkedList;

import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

public abstract class PLNaryFormula extends PLFormula {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	private PLFormula[] children = null;
	
	// Constructors :
	
	public PLNaryFormula(final LI li, final PLFormula... fils) {
		super(li);
		this.setChildren(fils);
	}
	
	// Getters :
	
	public PLFormula[] getChildren() {
		return this.children;
	}
	
	// Setters :
	
	protected void setChildren(final PLFormula[] children) {
		this.children = children;
	}
	
	// Methods :
	
	protected void getDisjunctions() {
		// TODO - Lost method ?
	}
	
	@Override
	public PLFormula[] listeFils() {
		return this.getChildren();
	}
	
	@Override
	public int nombreFils() {
		return this.getChildren().length;
	}
	
	public abstract String operator(boolean latex);
	
	@Override
	protected boolean isUnary() {
		PLFormula[] children = this.getChildren();
		return (children.length == 1) && children[0].isUnary();
	}
	
	@Override
	public String toString(boolean latex) {
		Collection<String> childrenStrings = new LinkedList<>();
		String leftPar = StringUtils.symbol("(", latex);
		String rightPar = StringUtils.symbol(")", latex);
		
		for(PLFormula child : this.getChildren()) {
			String childString = child.toString(latex);
			
			if(!child.isUnary()) {
				childString = leftPar + childString + rightPar;
			}
			
			childrenStrings.add(childString);
		}
		
		String space = StringUtils.symbol(" ", latex);
		return StringUtils.toString(childrenStrings, "", space + this.operator(latex) + space, "");
	}
	
	@Override
	public String toString_Prefixed() {
		PLFormula[] liste_fils = this.listeFils();
		
		if(liste_fils.length != 1) {
			return this.operator(false) + StringUtils.toString(liste_fils, "(", " ", ")");
		}
		else {
			return this.operator(false) + liste_fils[0];
		}
	}
	
}
