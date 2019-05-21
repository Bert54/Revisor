package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

/**
 * Extends PLLiteral for implementation purposes
 */
public class PLConstant extends PLLiteral {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	private static final LI DEFAULT_LI = new LI();
	public static final PLConstant TRUE = new PLConstant(DEFAULT_LI, true);
	public static final PLConstant FALSE = new PLConstant(DEFAULT_LI, false);
	
	// Constructors :
	
	public PLConstant(final LI li, final boolean val) {
		super(li, val ? LI.TRUE : LI.FALSE);
	}
	
}
