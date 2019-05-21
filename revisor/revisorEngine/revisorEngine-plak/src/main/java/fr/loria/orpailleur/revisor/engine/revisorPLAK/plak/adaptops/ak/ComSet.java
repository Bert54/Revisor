package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak;

import java.util.ArrayList;
import java.util.Collections;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.util.Merge;

public class ComSet {
	
	// Fields :
	
	private ArrayList<AdaptationRule> rules = null;
	private LitSet repairs = null;
	private LitSet rightpart = null;
	
	// Constructors :
	
	private ComSet() {
	}
	
	// Methods :
	
	/**
	 * Test if the union of set1 and set2 is a set of commutative rules.
	 * @param li - The literal register.
	 * @param set1 - One set of commutative rules.
	 * @param set2 - Another set of commutative rules.
	 * @return The set of commutative rules (set1 U set2) if it is actually a set of commutative rules, null otherwise.
	 */
	public static ComSet test(final LI li, ComSet set1, ComSet set2) {
		if(set1.size() + set2.size() > 2) {
			return null;
		}
		
		if(set1.size() > set2.size()) {
			ComSet tmp = set1;
			set1 = set2;
			set2 = tmp;
		}
		
		LitSet ruleOverlapRepairs = new LitSet(li);
		int rulesOverlap = 0;
		
		for(int i = 0, j = 0; i < set1.size() && j < set2.size();) {
			AdaptationRule r1 = set1.rules.get(i);
			AdaptationRule r2 = set2.rules.get(i);
			
			if(r1.compareTo(r2) == 0) {
				ruleOverlapRepairs.addAll(r1.getRepairs());
				rulesOverlap++;
				i++;
				j++;
			}
			else if(r1.compareTo(r2) < 0) {
				i++;
			}
			else if(r1.compareTo(r2) > 0) {
				j++;
			}
		}
		
		if(rulesOverlap >= set1.size()) {
			return null;
		}
		
		for(Integer l : set1.repairs) {
			if(set2.repairs.contains(l) && !ruleOverlapRepairs.contains(l)) {
				return null;
			}
		}
		
		LitSet repairs = new LitSet(li);
		repairs.addAll(set1.repairs);
		repairs.addAll(set2.repairs);
		LitSet rights = new LitSet(li);
		rights.addAll(set1.rightpart);
		rights.addAll(set2.rightpart);
		
		for(Integer l : repairs) {
			if(rights.contains(l)) {
				return null;
			}
		}
		
		ComSet set = new ComSet();
		set.rules = Merge.mergeSortedLists(set1.rules, set2.rules);
		set.repairs = repairs;
		set.rightpart = rights;
		Collections.sort(set.rules);
		return set;
	}
	
	/**
	 * Test if r1 and r2 are commutative.
	 * @param li - The literal register.
	 * @param r1 - One adaptation rule
	 * @param r2 - Another adaptation rule
	 * @return The set of commutative rules {r1, r2} if r1 and r2 are commutative, null otherwise.
	 */
	public static ComSet test(final LI li, final AdaptationRule r1, final AdaptationRule r2) {
		if(r1 == r2) {
			return null;
		}
		
		LitSet repairs = r1.getRepairs();
		LitSet repairs2 = r2.getRepairs();
		
		// test that the repairs sets are disjoint
		// (i.e. the 2 rules remove different literals)
		for(Integer l : repairs) {
			if(repairs2.contains(l)) {
				return null;
			}
		}
		
		repairs = new LitSet(li, repairs);
		repairs.addAll(repairs2); // merge repair sets
		// merge rule right parts
		LitSet rights = r1.getRIGHT();
		rights.addAll(r2.getRIGHT());
		
		// test that repairs and right parts are disjoint
		// (i.e. one rule doesn't remove literals introduced by the other)
		for(Integer l : repairs) {
			if(rights.contains(l)) {
				return null;
			}
		}
		
		// all tests are done, the 2 rules are commutative
		ComSet set = new ComSet();
		set.rules = new ArrayList<>(2);
		set.rules.add(r1);
		set.rules.add(r2);
		set.repairs = repairs;
		set.rightpart = rights;
		Collections.sort(set.rules);
		return set;
	}
	
	public int size() {
		return this.rules.size();
	}
	
	public ArrayList<AdaptationRule> getRules() {
		return this.rules;
	}
	
	@Override
	public String toString() {
		String str = "{";
		
		for(AdaptationRule r : this.rules) {
			if(str.length() > 1) {
				str += ", ";
			}
			
			str += "R" + r.getIdentifier();
		}
		
		return str + "}";
	}
	
}
