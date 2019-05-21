package fr.loria.orpailleur.revisor.engine.revisorPL.pl;

import java.util.Collections;
import java.util.Map;

import fr.loria.orpailleur.revisor.engine.core.utils.string.SymbolMap;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.NOT;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLConstant;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser.PLFormulaParser;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser.PLFormulaSyntaxError;

/**
 * @author Gabin PERSONENI
 * @author William PHILBERT
 */
public class PL {
	
	// Constants :
	
	public static final String NOT_SYMBOL = "!";
	public static final String AND_SYMBOL = "&";
	public static final String OR_SYMBOL = "|";
	public static final String XOR_SYMBOL = "^";
	public static final String IMPLICATION_SYMBOL = "=>";
	public static final String EQUIVALENCE_SYMBOL = "<=>";
	public static final String SUBSTITUTION_SYMBOL = "~>";
	public static final String TRUE_SYMBOL = "true";
	public static final String FALSE_SYMBOL = "false";
	
	public static final String LATEX_NOT_SYMBOL = "{\\neg}";
	public static final String LATEX_AND_SYMBOL = "{\\land}";
	public static final String LATEX_OR_SYMBOL = "{\\lor}";
	public static final String LATEX_XOR_SYMBOL = "{\\loplus}";
	public static final String LATEX_IMPLICATION_SYMBOL = "{\\Rightarrow}";
	public static final String LATEX_EQUIVALENCE_SYMBOL = "{\\Leftrightarrow}";
	public static final String LATEX_SUBSTITUTION_SYMBOL = "{\\rightsquigarrow}";
	public static final String LATEX_TRUE_SYMBOL = "{\\top}";
	public static final String LATEX_FALSE_SYMBOL = "{\\bot}";
	
	public static final PLConstant TRUE = PLConstant.TRUE;
	public static final PLConstant FALSE = PLConstant.FALSE;
	
	protected static final SymbolMap symbolMap = new SymbolMap();
	
	static {
		symbolMap.put(NOT_SYMBOL, LATEX_NOT_SYMBOL);
		symbolMap.put(AND_SYMBOL, LATEX_AND_SYMBOL);
		symbolMap.put(OR_SYMBOL, LATEX_OR_SYMBOL);
		symbolMap.put(XOR_SYMBOL, LATEX_XOR_SYMBOL);
		symbolMap.put(IMPLICATION_SYMBOL, LATEX_IMPLICATION_SYMBOL);
		symbolMap.put(EQUIVALENCE_SYMBOL, LATEX_EQUIVALENCE_SYMBOL);
		symbolMap.put(SUBSTITUTION_SYMBOL, LATEX_SUBSTITUTION_SYMBOL);
		symbolMap.put(TRUE_SYMBOL, LATEX_TRUE_SYMBOL);
		symbolMap.put(FALSE_SYMBOL, LATEX_FALSE_SYMBOL);
	}
	
	// Fields :
	
	private final LI li;
	
	// Constructors :
	
	public PL(final LI li) {
		this.li = li;
	}
	
	// Static methods on Strings :
	
	public static String symbol(String textSymbol) {
		return symbol(textSymbol, true);
	}
	
	public static String symbol(String textSymbol, boolean latex) {
		return symbolMap.symbol(textSymbol, latex);
	}
	
	public static Map<String, String> symbolMap() {
		return Collections.unmodifiableMap(symbolMap);
	}
	
	// Methods on PLFormulae :
	
	public final NOT NOT(final PLFormula formuleProp) {
		return new NOT(this.li, formuleProp);
	}
	
	public final AND AND(final PLFormula... formuleProps) {
		return new AND(this.li, formuleProps);
	}
	
	public final OR OR(final PLFormula... formuleProps) {
		return new OR(this.li, formuleProps);
	}
	
	public final OR IMPL(final PLFormula a, final PLFormula b) {
		return this.OR(this.NOT(a), b);
	}
	
	public final AND EQ(final PLFormula a, final PLFormula b) {
		return this.AND(this.IMPL(a, b), this.IMPL(b, a));
	}
	
	public final OR XOR(final PLFormula a, final PLFormula b) {
		return this.OR(this.OR(this.NOT(a), b), this.OR(a, this.NOT(b)));
	}
	
	public final NOT NAND(final PLFormula a, final PLFormula b) {
		return this.NOT(this.AND(a, b));
	}
	
	public final NOT NOR(final PLFormula a, final PLFormula b) {
		return this.NOT(this.OR(a, b));
	}
	
	// Methods on Objects :
	
	public final NOT NOT(final Object operandes) {
		if(operandes instanceof PLFormula) {
			return new NOT(this.li, (PLFormula) operandes);
		}
		else {
			return new NOT(this.li, parseFormula(operandes.toString()));
		}
	}
	
	public final AND AND(final Object... operandes) {
		PLFormula[] formulae = new PLFormula[operandes.length];
		for(int i = 0; i < operandes.length; i++) {
			if(operandes[i] instanceof PLFormula) {
				formulae[i] = (PLFormula) operandes[i];
			}
			else {
				formulae[i] = parseFormula(operandes[i].toString());
			}
		}
		return new AND(this.li, formulae);
	}
	
	public final OR OR(final Object... operandes) {
		PLFormula[] formulae = new PLFormula[operandes.length];
		for(int i = 0; i < operandes.length; i++) {
			if(operandes[i] instanceof PLFormula) {
				formulae[i] = (PLFormula) operandes[i];
			}
			else {
				formulae[i] = parseFormula(operandes[i].toString());
			}
		}
		return new OR(this.li, formulae);
	}
	
	public final OR IMPL(final Object a, final Object b) {
		return this.OR(this.NOT(a), b);
	}
	
	public final AND EQ(final Object a, final Object b) {
		return this.AND(this.IMPL(a, b), this.IMPL(b, a));
	}
	
	public final OR XOR(final Object a, final Object b) {
		return this.OR(this.OR(this.NOT(a), b), this.OR(a, this.NOT(b)));
	}
	
	public final NOT NAND(final Object a, final Object b) {
		return this.NOT(this.AND(a, b));
	}
	
	public final NOT NOR(final Object a, final Object b) {
		return this.NOT(this.OR(a, b));
	}
	
	// Other methods :
	
	public final PLLiteral LITT(final String var) {
		return new PLLiteral(this.li, var);
	}
	
	public final PLFormula parseFormula(final String input) {
		try {
			PLFormula f = new PLFormulaParser(this.li).parse(input);
			return f;
		}
		catch(PLFormulaSyntaxError e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
