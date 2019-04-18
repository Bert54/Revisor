package fr.loria.orpailleur.revisor.engine.core.utils.string;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author William Philbert
 */
public class StringUtils {
	
	// Constants :
	
	public static final String LEFT_PAR = "(";
	public static final String RIGHT_PAR = ")";
	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";
	public static final String LEFT_ANGLE = "<";
	public static final String RIGHT_ANGLE = ">";
	
	public static final String LATEX_LEFT_PAR = "(";
	public static final String LATEX_RIGHT_PAR = ")";
	public static final String LATEX_LEFT_BRACE = "{\\lbrace}";
	public static final String LATEX_RIGHT_BRACE = "{\\rbrace}";
	public static final String LATEX_LEFT_ANGLE = "{\\langle}";
	public static final String LATEX_RIGHT_ANGLE = "{\\rangle}";
	
	public static final String COMMA_SPACE = ", ";
	public static final String SPACE_COLON_SPACE = " : ";
	
	public static final String DEFAULT_COLLECTION_OPEN = LEFT_PAR;
	public static final String DEFAULT_COLLECTION_SEPARATOR = COMMA_SPACE;
	public static final String DEFAULT_COLLECTION_CLOSE = RIGHT_PAR;
	
	public static final String DEFAULT_MAP_OPEN = LEFT_BRACE;
	public static final String DEFAULT_MAP_LINK = SPACE_COLON_SPACE;
	public static final String DEFAULT_MAP_SEPARATOR = COMMA_SPACE;
	public static final String DEFAULT_MAP_CLOSE = RIGHT_BRACE;
	
	public static final String NULL = "null";
	
	protected static final SymbolMap symbolMap = new SymbolMap();
	
	static {
		symbolMap.put(LEFT_PAR, LATEX_LEFT_PAR);
		symbolMap.put(RIGHT_PAR, LATEX_RIGHT_PAR);
		symbolMap.put(LEFT_BRACE, LATEX_LEFT_BRACE);
		symbolMap.put(RIGHT_BRACE, LATEX_RIGHT_BRACE);
		symbolMap.put(LEFT_ANGLE, LATEX_LEFT_ANGLE);
		symbolMap.put(RIGHT_ANGLE, LATEX_RIGHT_ANGLE);
	}
	
	// Methods for Latex :
	
	public static String symbol(String textSymbol) {
		return symbol(textSymbol, true);
	}
	
	public static String symbol(String textSymbol, boolean latex) {
		return symbolMap.symbol(textSymbol, latex);
	}
	
	public static Map<String, String> symbolMap() {
		return Collections.unmodifiableMap(symbolMap);
	}
	
	// Methods for Strings :
	
	/**
	 * Returns a simplified version of the given String.
	 * Replaces all long spaces by one space character. Also removes leading and trailing whitespace.
	 * If the given String is null, an empty String is returned.
	 * @param text - a String.
	 * @return a simplified version of the given String.
	 */
	public static String simplifiedString(String text) {
		if(text == null) {
			return "";
		}
		
		return text.trim().replaceAll("\\s+", " ");
	}
	
	// Methods for Colors :
	
	public static String toHexString(Color value) {
		if(value != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(Integer.toHexString(value.getRGB()).toUpperCase());
			
			while(builder.length() < 8) {
				builder.insert(0, "0");
			}
			
			return builder.toString();
		}
		
		return NULL;
	}
	
	public static Color toColor(String str) throws IllegalArgumentException {
		if(str == null || str.equals(NULL)) {
			return null;
		}
		
		try {
			int argb = (int) Long.parseLong(str, 16);
			return new Color(argb, true);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal color value: " + str, argh);
		}
	}
	
	// Methods for Collections :
	
	public static <T> String toString(Collection<T> collection) {
		return toString(collection, DEFAULT_COLLECTION_OPEN, DEFAULT_COLLECTION_SEPARATOR, DEFAULT_COLLECTION_CLOSE);
	}
	
	public static <T> String toString(Collection<T> collection, String open, String separator, String close) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(open);
		
		if(collection.size() > 0) {
			for(T element : collection) {
				builder.append(element);
				builder.append(separator);
			}
			
			int end = builder.length();
			builder.delete(end - separator.length(), end);
		}
		
		builder.append(close);
		
		return builder.toString();
	}
	
	// Methods for Arrays :
	
	public static <T> String toString(T[] tab) {
		return toString(Arrays.asList(tab));
	}
	
	public static <T> String toString(T[] tab, String open, String separator, String close) {
		return toString(Arrays.asList(tab), open, separator, close);
	}
	
	// Methods for Maps :
	
	public static <K, V> String toString(Map<K, V> map) {
		return toString(map, DEFAULT_MAP_OPEN, DEFAULT_MAP_LINK, DEFAULT_MAP_SEPARATOR, DEFAULT_MAP_CLOSE);
	}
	
	public static <K, V> String toString(Map<K, V> map, String open, String link, String separator, String close) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(open);
		
		if(map.size() > 0) {
			for(Entry<K, V> entry : map.entrySet()) {
				builder.append(entry.getKey());
				builder.append(link);
				builder.append(entry.getValue());
				builder.append(separator);
			}
			
			int end = builder.length();
			builder.delete(end - separator.length(), end);
		}
		
		builder.append(close);
		
		return builder.toString();
	}
	
	// Methods for Collections + Latex :
	
	public static <T extends LatexFormatable> String toString(Collection<T> collection, boolean latex) {
		return toString(collection, latex, symbol(DEFAULT_COLLECTION_OPEN, latex), symbol(DEFAULT_COLLECTION_SEPARATOR, latex), symbol(DEFAULT_COLLECTION_CLOSE, latex));
	}
	
	public static <T extends LatexFormatable> String toString(Collection<T> collection, boolean latex, String open, String separator, String close) {
		if(latex) {
			return toLatex(collection, open, separator, close);
		}
		else {
			return toString(collection, open, separator, close);
		}
	}
	
	public static <T extends LatexFormatable> String toLatex(Collection<T> collection) {
		return toLatex(collection, symbol(DEFAULT_COLLECTION_OPEN), symbol(DEFAULT_COLLECTION_SEPARATOR), symbol(DEFAULT_COLLECTION_CLOSE));
	}
	
	public static <T extends LatexFormatable> String toLatex(Collection<T> collection, String open, String separator, String close) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(open);
		
		if(collection.size() > 0) {
			for(T element : collection) {
				builder.append(element.toLatex());
				builder.append(separator);
			}
			
			int end = builder.length();
			builder.delete(end - separator.length(), end);
		}
		
		builder.append(close);
		
		return builder.toString();
	}
	
	// Methods for Arrays + Latex :
	
	public static <T extends LatexFormatable> String toString(T[] tab, boolean latex) {
		return toString(Arrays.asList(tab), latex);
	}
	
	public static <T extends LatexFormatable> String toString(T[] tab, boolean latex, String open, String separator, String close) {
		return toString(Arrays.asList(tab), latex, open, separator, close);
	}
	
	public static <T extends LatexFormatable> String toLatex(T[] tab) {
		return toLatex(Arrays.asList(tab));
	}
	
	public static <T extends LatexFormatable> String toLatex(T[] tab, String open, String separator, String close) {
		return toLatex(Arrays.asList(tab), open, separator, close);
	}
	
	// Methods for Maps + Latex :
	
	public static <K, V> String toString(Map<K, V> map, boolean latex) {
		return toString(map, latex, symbol(DEFAULT_MAP_OPEN, latex), symbol(DEFAULT_MAP_LINK, latex), symbol(DEFAULT_MAP_SEPARATOR, latex), symbol(DEFAULT_MAP_CLOSE, latex));
	}
	
	public static <K, V> String toString(Map<K, V> map, boolean latex, String open, String link, String separator, String close) {
		if(latex) {
			return toLatex(map, open, link, separator, close);
		}
		else {
			return toString(map, open, link, separator, close);
		}
	}
	
	public static <K, V> String toLatex(Map<K, V> map) {
		return toLatex(map, symbol(DEFAULT_MAP_OPEN), symbol(DEFAULT_MAP_LINK), symbol(DEFAULT_MAP_SEPARATOR), symbol(DEFAULT_MAP_CLOSE));
	}
	
	public static <K, V> String toLatex(Map<K, V> map, String open, String link, String separator, String close) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(open);
		
		if(map.size() > 0) {
			for(Entry<K, V> entry : map.entrySet()) {
				builder.append((entry.getKey() instanceof LatexFormatable) ? ((LatexFormatable) entry.getKey()).toLatex() : entry.getKey());
				builder.append(link);
				builder.append((entry.getValue() instanceof LatexFormatable) ? ((LatexFormatable) entry.getValue()).toLatex() : entry.getValue());
				builder.append(separator);
			}
			
			int end = builder.length();
			builder.delete(end - separator.length(), end);
		}
		
		builder.append(close);
		
		return builder.toString();
	}
	
}
