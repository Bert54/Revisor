package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser.PLFormulaParser;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser.PLFormulaSyntaxError;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

/**
 * @author Gabin PERSONENI
 */
public class RuleSet {
	
	// Fields :
	
	private final LI li;
	private int last_identifier = 0;
	private boolean ready = false;
	private boolean commut_ready = false;
	private HashMap<Integer, AdaptationRule> allTheRules = new HashMap<>();
	private HashMap<Integer, Double> estRepairsCosts = new HashMap<>();
	// literal in rule left-part -> matching rules
	private HashMap<Integer, ArrayList<AdaptationRule>> rules_by_repairs = new HashMap<>();
	private HashMap<AdaptationRule, HashSet<AdaptationRule>> forbidden_sequences = new HashMap<>();
	
	// Constructors :
	
	public RuleSet(final LI li) {
		this.li = li;
	}
	
	public RuleSet(final LI li, final String filepath) {
		this.li = li;
		File f = new File(filepath);
		
		try(FileReader freader = new FileReader(f); BufferedReader reader = new BufferedReader(freader)) {
			String s = reader.readLine();
			String s2[] = s.split("[ ]*\\$[ ]*");
			this.addRule(s2[0], new Double(s2[1]));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Methods :
	
	public double estimateMinRepairCost(final Integer i) {
		Double erc = this.estRepairsCosts.get(i);
		
		if(erc == null) {
			return 1d;
		}
		else {
			return erc;
		}
	}
	
	public ArrayList<Integer> findFlips(final LitSet clashes, final Rule lastUsedRule) {
		if(this.commut_ready) {
			if(lastUsedRule instanceof AdaptationRule) {
				ArrayList<Integer> flips = new ArrayList<>();
				AdaptationRule ar = (AdaptationRule) lastUsedRule;
				
				for(Integer l : clashes) {
					if(ar.contains(l)) {
						flips.add(l);
					}
				}
				
				return flips;
			}
			else if(lastUsedRule instanceof Flip) {
				ArrayList<Integer> flips = new ArrayList<>();
				Flip f = (Flip) lastUsedRule;
				Integer flip = f.getLiteral();
				
				for(Integer l : clashes) {
					if(flip < l) {
						flips.add(l);
					}
				}
				
				return flips;
			}
		}
		
		return new ArrayList<>(clashes);
	}
	
	public ArrayList<AdaptationRule> findRules(final LitSet context, final Rule lastUsedRule) {
		ArrayList<AdaptationRule> matching_rules = new ArrayList<>();
		HashSet<AdaptationRule> forbidden_rules = null;
		
		if(lastUsedRule instanceof AdaptationRule) {
			forbidden_rules = this.forbidden_sequences.get(lastUsedRule);
		}
		
		if(forbidden_rules != null) {
			for(AdaptationRule r : this.allTheRules.values()) {
				if((!forbidden_rules.contains(r)) && r.match(context)) {
					matching_rules.add(r);
				}
			}
		}
		else {
			for(AdaptationRule r : this.allTheRules.values()) {
				if(r.match(context)) {
					matching_rules.add(r);
				}
			}
		}
		
		return matching_rules;
	}
	
	public AdaptationRule getRuleById(final int id) {
		return this.allTheRules.get(id);
	}
	
	private void unready() {
		this.ready = false;
		this.commut_ready = false;
	}
	
	public void addRule(final String rule, final double cost) {
		this.unready();
		PLFormulaParser parser = new PLFormulaParser(this.li);
		
		try {
			String rule_splitted[] = rule.split(":");
			
			if(rule_splitted.length != 2) {
				System.err.println("Bad input");
				return;
			}
			else {
				PLFormula context = parser.parse(rule_splitted[0]);
				rule_splitted = rule_splitted[1].split("\\*=");
				
				if(rule_splitted.length != 2) {
					System.err.println("Bad input");
					return;
				}
				else {
					PLFormula left = parser.parse(rule_splitted[0]);
					PLFormula right = parser.parse(rule_splitted[1]);
					
					if(context instanceof PLLiteral) {
						context = new AND(this.li, context);
					}
					
					if(left instanceof PLLiteral) {
						left = new AND(this.li, left);
					}
					
					if(right instanceof PLLiteral) {
						right = new AND(this.li, right);
					}
					
					this.addRule((AND) context, (AND) left, (AND) right, cost);
				}
			}
		}
		catch(PLFormulaSyntaxError e) {
			System.err.println("Bad input");
			e.printStackTrace();
			return;
		}
	}
	
	public void addRule(final AND context, final AND left, final AND right, final double cost) {
		this.unready();
		
		try {
			AdaptationRule r = new AdaptationRule(this.li, context.asLitSet(), left.asLitSet(), right.asLitSet(), cost, this.last_identifier++);
			HashSet<Integer> r_left = r.getRepairs();
			
			if(r_left.size() == 0) {
				// that is forbidden !
				System.err.println("Rule rejected : no left part");
				return;
			}
			else {
				for(Integer l : r_left) {
					ArrayList<AdaptationRule> rules = this.rules_by_repairs.get(l);
					
					if(rules == null) {
						rules = new ArrayList<>();
						this.rules_by_repairs.put(l, rules);
					}
					
					rules.add(r);
					// minimal repair cost estimation
					Double erc = this.estRepairsCosts.get(l);
					
					if(erc == null) {
						erc = Double.POSITIVE_INFINITY;
					}
					
					erc = Math.min(erc, r.getRuleCost() / r_left.size());
					this.estRepairsCosts.put(l, erc);
				}
			}
			
			this.allTheRules.put(r.getIdentifier(), r);
		}
		catch(ClassCastException e) {
			System.err.println("context, left-part, and right-part of the rule must be conjunctions of PLLiterals or PLConstants");
			e.printStackTrace();
		}
	}
	
	private void addForbiddenSequence(final AdaptationRule r1, final AdaptationRule r2) {
		HashSet<AdaptationRule> forbidden_rules = this.forbidden_sequences.get(r1);
		
		if(forbidden_rules == null) {
			forbidden_rules = new HashSet<>();
			this.forbidden_sequences.put(r1, forbidden_rules);
		}
		
		forbidden_rules.add(r2);
	}
	
	private void computeForbiddenSequences() {
		final ArrayList<ComSet> comSets = this.findComSets();
		
		for(ComSet cs : comSets) {
			ArrayList<AdaptationRule> rules = cs.getRules();
			
			for(int i = rules.size() - 1; i >= 0; i--) {
				AdaptationRule r1 = rules.get(i);
				
				for(int j = i - 1; j >= 0; j--) {
					AdaptationRule r2 = rules.get(j);
					this.addForbiddenSequence(r1, r2);
				}
			}
		}
	}
	
	private ArrayList<ComSet> findComSets() {
		HashSet<ComSet> notMaximumSets = new HashSet<>();
		ArrayList<ComSet> maximumComSets = new ArrayList<>();
		ArrayList<ComSet> currentLevelComSets = null;
		ArrayList<ComSet> nextLevelComSets = new ArrayList<>();
		
		for(int i = 0; i < this.allTheRules.size(); i++) {
			AdaptationRule r1 = this.allTheRules.get(i);
			
			if(r1 != null) {
				for(int j = i + 1; j < this.allTheRules.size(); j++) {
					AdaptationRule r2 = this.allTheRules.get(j);
					ComSet comSet = null;
					
					if(r2 != null && r1 != r2 && (comSet = ComSet.test(this.li, r1, r2)) != null) {
						nextLevelComSets.add(comSet);
					}
				}
			}
		}
		
		while(nextLevelComSets.size() != 0) {
			currentLevelComSets = nextLevelComSets;
			nextLevelComSets = new ArrayList<>();
			
			for(int i = 0; i < currentLevelComSets.size(); i++) {
				for(int j = i + 1; j < currentLevelComSets.size(); j++) {
					ComSet comSet = ComSet.test(this.li, currentLevelComSets.get(i), currentLevelComSets.get(j));
					
					if(comSet != null) {
						notMaximumSets.add(currentLevelComSets.get(i));
						notMaximumSets.add(currentLevelComSets.get(j));
						nextLevelComSets.add(comSet);
					}
				}
			}
			
			for(ComSet cs : currentLevelComSets) {
				if(!notMaximumSets.contains(cs)) {
					maximumComSets.add(cs);
				}
			}
		}
		
		return maximumComSets;
	}
	
	public void computeCommutables() {
		if(!this.commut_ready) {
			this.computeForbiddenSequences();
			this.commut_ready = true;
		}
	}
	
	public void makeReady() {
		if(!this.ready) {
			for(Integer i1 : this.rules_by_repairs.keySet()) {
				Collections.sort(this.rules_by_repairs.get(i1));
			}
			
			this.ready = true;
		}
	}
	
	public void printAll() {
		System.out.println("AK Set:");
		
		for(AdaptationRule r : this.allTheRules.values()) {
			System.out.println(r);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		
		if(this.allTheRules.size() > 0) {
			for(AdaptationRule r : this.allTheRules.values()) {
				builder.append(r);
				builder.append(", ");
			}
			
			int end = builder.length();
			builder.delete(end - 2, end);
		}
		
		builder.append("}");
		
		return builder.toString();
	}
	
}
