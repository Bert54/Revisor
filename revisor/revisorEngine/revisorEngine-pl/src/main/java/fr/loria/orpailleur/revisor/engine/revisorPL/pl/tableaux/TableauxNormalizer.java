package fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.NOT;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

public abstract class TableauxNormalizer {
	
	// Constants :
	
	public static int OPTIONS_TRANSFORMATIONS = TableauxNormalizer.TEST_CLASHES | TableauxNormalizer.REMOVE_DUPLICATES | TableauxNormalizer.RARITY_SORT;
	
	public static final int VERBOSE = 0x80000000;
	
	/**
	 * Tester les clashs dans les tableaux semantiques (obligatoire)
	 */
	public static final int TEST_CLASHES = 0x01;
	
	/**
	 * Supprimer les doublons d'une branche dans les tableaux semantiques (recommande)
	 */
	public static final int REMOVE_DUPLICATES = 0x03;
	
	/**
	 * Tri rarete (voir rapport)
	 */
	public static final int RARITY_SORT = 0x04;
	
	/**
	 * Generaliser (a & (a | b)) devient (a), etc... (voir rapport)
	 */
	public static final int GENERALIZE = 0x0B;
	
	/**
	 * Generaliser apres l'execution de la methode des tableaux
	 */
	public static final int GENERALIZE_POST = 0x10;
	
	// Fields :
	
	protected int options = 0x0;
	protected boolean verbose = false;
	
	// Constructors :
	
	public TableauxNormalizer(final int options) {
		this.options = options;
		this.verbose = (this.options & VERBOSE) == VERBOSE;
	}
	
	// Methods :
	
	protected void message(final Object message) {
		if(this.verbose)
			System.out.println(message);
	}
	
	protected static final String nomVariable(final PLFormula f) {
		String occ_name = null;
		if(f instanceof PLLiteral) {
			occ_name = (((PLLiteral) f).getName());
		}
		else if(f instanceof NOT && f.listeFils()[0] instanceof PLLiteral) {
			occ_name = (((PLLiteral) f.listeFils()[0]).getName());
		}
		return occ_name;
	}
	
}
