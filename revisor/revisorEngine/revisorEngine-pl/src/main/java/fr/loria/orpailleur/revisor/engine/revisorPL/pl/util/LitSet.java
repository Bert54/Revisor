package fr.loria.orpailleur.revisor.engine.revisorPL.pl.util;

import java.util.HashSet;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

/**
 * @author Gabin PERSONENI
 */
public class LitSet extends HashSet<Integer> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	private final LI li;
	
	// Constructors :
	
	public LitSet(final LI li) {
		this.li = li;
	}
	
	/**
	 * Copy constructor.
	 * @param set - The set to be copied.
	 */
	public LitSet(final LI li, final LitSet set) {
		super(set);
		this.li = li;
	}
	
	public LitSet(final LI li, final int i) {
		super(i);
		this.li = li;
	}
	
	// Methods :
	
	public boolean add(final PLLiteral lit) {
		return this.add(lit.getID());
	}
	
	@Override
	public boolean add(final Integer id) {
		if(id == LI.TRUE) {
			return false;
		}
		else if(id == LI.FALSE) {
			this.clear();
			return super.add(id);
		}
		else {
			return super.add(id);
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		
		for(String s : this.li.sortedNames(this)) {
			str += s + " ";
		}
		
		return str;
	}
	
}
