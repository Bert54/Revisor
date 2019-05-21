package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.util;

import java.util.ArrayList;

public enum Merge {
	
	// Enum :
	
	;
	
	// Methods :
	
	/**
	 * Merge two sorted array lists. An element must not appear twice in one
	 * list.
	 */
	@SuppressWarnings("javadoc")
	public static <T extends Comparable<T>> ArrayList<T> mergeSortedLists(final ArrayList<T> l1, final ArrayList<T> l2) {
		ArrayList<T> l = new ArrayList<>(l1.size() + l2.size());
		
		for(int i = 0, j = 0; i < l1.size() && j < l2.size();) {
			T r1 = l1.get(i);
			T r2 = l2.get(i);
			
			if(r1.compareTo(r2) == 0) {
				l.add(r1);
				i++;
				j++;
			}
			else if(r1.compareTo(r2) < 0) {
				l.add(r1);
				i++;
			}
			else if(r1.compareTo(r2) > 0) {
				l.add(r2);
				j++;
			}
		}
		
		return l;
	}
	
}
