package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.comparator;

import java.util.Comparator;

/**
 * @author William Philbert
 */
public class LiteralStringComparator implements Comparator<String> {
	
	// Methods :
	
	@Override
	public int compare(String a, String b) {
		boolean aInv = a.startsWith("!");
		boolean bInv = b.startsWith("!");
		String aName = aInv ? a.substring(1) : a;
		String bName = bInv ? b.substring(1) : b;
		
		int result = aName.compareTo(bName);
		
		if(result == 0) {
			result = (aInv ? 1 : -1) - (bInv ? 1 : -1);
		}
		
		return result;
	}
	
}
