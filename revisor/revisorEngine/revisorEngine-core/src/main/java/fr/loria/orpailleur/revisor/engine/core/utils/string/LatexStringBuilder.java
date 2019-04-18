package fr.loria.orpailleur.revisor.engine.core.utils.string;

import java.text.NumberFormat;
import java.util.Map;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;

/**
 * @author William Philbert
 */
public class LatexStringBuilder {
	
	// Constants :
	
	public static final NumberFormat NUMBER_FORMAT = GuiConstants.DEFAULT_NUMBER_FORMAT;
	
	// Fields :
	
	protected final StringBuilder builder = new StringBuilder();
	protected final SymbolMap symbolMap = new SymbolMap();
	
	// Constructors :
	
	@SafeVarargs
	public LatexStringBuilder(final Map<String, String>... symbolMaps) {
		this.symbolMap.putAll(StringUtils.symbolMap());
		
		for(Map<String, String> map : symbolMaps) {
			this.symbolMap.putAll(map);
		}
	}
	
	// Methods :
	
	/**
	 * If latex is true, use a SymbolMap to get the latex version of the given String and append it to this sequence.
	 * Else the given string itself is appended.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final String str, final boolean latex) {
		this.builder.append(this.symbolMap.symbol(str, latex));
		return this;
	}
	
	/**
	 * Gets the string representation of the given boolean.
	 * Then, if latex is true, use a SymbolMap to get the latex version of the String and append it to this sequence.
	 * Else the string itself is appended.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final boolean b, final boolean latex) {
		this.builder.append(this.symbolMap.symbol(String.valueOf(b), latex));
		return this;
	}
	
	/**
	 * Gets the string representation of the given char.
	 * Then, if latex is true, use a SymbolMap to get the latex version of the String and append it to this sequence.
	 * Else the string itself is appended.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final char c, final boolean latex) {
		this.builder.append(this.symbolMap.symbol(String.valueOf(c), latex));
		return this;
	}
	
	/**
	 * Appends the string representation of the Object argument to this sequence.
	 * @see StringBuilder#append(Object)
	 */
	public LatexStringBuilder append(final Object obj) {
		this.builder.append(obj);
		return this;
	}
	
	/**
	 * Appends the specified string to this sequence.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final String str) {
		this.builder.append(str);
		return this;
	}
	
	/**
	 * Appends the string representation of the boolean argument to this sequence.
	 * @see StringBuilder#append(boolean)
	 */
	public LatexStringBuilder append(final boolean b) {
		this.builder.append(b);
		return this;
	}
	
	/**
	 * Appends the string representation of the char argument to this sequence.
	 * @see StringBuilder#append(char)
	 */
	public LatexStringBuilder append(final char c) {
		this.builder.append(c);
		return this;
	}
	
	/**
	 * Uses a NumberFormat to convert the int to a String and appends it to this sequence.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final int i) {
		this.builder.append(NUMBER_FORMAT.format(i));
		return this;
	}
	
	/**
	 * Uses a NumberFormat to convert the long to a String and appends it to this sequence.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final long lng) {
		this.builder.append(NUMBER_FORMAT.format(lng));
		return this;
	}
	
	/**
	 * Uses a NumberFormat to convert the float to a String and appends it to this sequence.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final float f) {
		this.builder.append(NUMBER_FORMAT.format(f));
		return this;
	}
	
	/**
	 * Uses a NumberFormat to convert the double to a String and appends it to this sequence.
	 * @see StringBuilder#append(String)
	 */
	public LatexStringBuilder append(final double d) {
		this.builder.append(NUMBER_FORMAT.format(d));
		return this;
	}
	
	/**
	 * @see StringBuilder#toString()
	 */
	@Override
	public String toString() {
		return this.builder.toString();
	}
	
}
