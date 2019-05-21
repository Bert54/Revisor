package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLConstant;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops.TableauxDalalRevOp;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops.TautologyException;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.AdaptationRule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.Flip;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.RuleSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.util.DotIt;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.util.DotIt.DotNode;

/**
 * @author Gabin PERSONENI
 */
public class AKAdaptOp extends TableauxDalalRevOp {
	
	// Fields :
	
	/**
	 * Used for debug
	 */
	public static DotIt dotIt = null;
	public static Double maxSolutionCost = Double.POSITIVE_INFINITY;
	
	private static Double lastAKdistance = null;
	
	public static Double getLastAKDistance() {
		return lastAKdistance;
	}
	
	private RuleSet ruleSet;
	
	// Constructors :
	
	/**
	 * psi and mu must be in the form of a disjunction of their prime
	 * implicants, to ensure syntax independence
	 * 
	 * @param psi
	 * @param mu
	 * @param rules
	 * @param options
	 */
	public AKAdaptOp(final LI li, final PLFormula psi, final PLFormula mu, final RuleSet rules) {
		super(li, psi, mu);
		this.ruleSet = rules;
		this.ruleSet.makeReady();
	}
	
	// Methods :
	
	@Override
	public PLFormula revisePsi() {
		ArrayList<LitSet> psibranches = new ArrayList<>();
		ArrayList<LitSet> mubranches = new ArrayList<>();
		ArrayList<LitSet> solutions = new ArrayList<>();
		
		try {
			this.listerModeles(psibranches, mubranches);
		}
		catch(TautologyException e) {
			return new AND(this.li, this.psi, this.mu);
		}
		
		Comparator<State> comprtr = new Comparator<State>() {
			
			@Override
			public int compare(final State arg0, final State arg1) {
				if(AKAdaptOp.this.F(arg0) > AKAdaptOp.this.F(arg1)) {
					return -1;
				}
				else if(AKAdaptOp.this.F(arg0) < AKAdaptOp.this.F(arg1)) {
					return +1;
				}
				else {
					return (int) (arg0.id - arg1.id);
				}
			}
			
		};
		
		ArrayList<State> explSet = new ArrayList<>();
		double solCost = maxSolutionCost;
		
		for(LitSet sset : psibranches) {
			for(LitSet tset : mubranches) {
				State s = new State(this.li, sset, tset);
				explSet.add(s);
				
				if(dotIt != null) {
					dotIt.addNode(s);
				}
			}
		}
		
		Collections.sort(explSet, comprtr);
		boolean verbose = false;
		
		while(explSet.size() > 0 && this.F(explSet.get(explSet.size() - 1)) <= solCost) {
			if(dotIt != null) {
				dotIt.dotWrite();
			}
			
			State current = explSet.remove(explSet.size() - 1);
			
			if(verbose) {
				current.print();
			}
			
			if(current.isFinal()) {
				solutions.add(current.asSolution());
				solCost = this.F(current);
				lastAKdistance = solCost;
			}
			else {
				ArrayList<AdaptationRule> rules = this.ruleSet.findRules(current.sset, current.origin);
				ArrayList<Integer> flips = this.ruleSet.findFlips(current.clashes, current.origin);
				
				for(AdaptationRule r : rules) {
					State newS = new State(this.li, current, r);
					explSet.add(newS);
					
					if(dotIt != null) {
						dotIt.addEdge(current, r, newS);
					}
				}
				
				for(Integer flip : flips) {
					State newS = new State(this.li, current, flip);
					explSet.add(newS);
					
					if(dotIt != null) {
						dotIt.addEdge(current, "F<sub>" + this.li.getDotName(flip) + "</sub>", newS);
					}
				}
			}
			
			Collections.sort(explSet, comprtr);
			
			if(verbose) {
				System.out.println("end\n");
			}
		}
		
		if(dotIt != null) {
			dotIt.dotWrite();
		}
		
		if(solutions.size() < 1) {
			lastAKdistance = Double.POSITIVE_INFINITY;
			return new PLConstant(this.li, false);
		}
		else if(solutions.size() == 1) {
			return new AND(this.li, solutions.get(0));
		}
		else {
			AND[] models = new AND[solutions.size()];
			
			for(int i = 0; i < solutions.size(); i++) {
				models[i] = new AND(this.li, solutions.get(i));
			}
			
			return new OR(this.li, models);
		}
	}
	
	private double F(final State s) {
		return s.gCost + s.hCost;
	}
	
	private double ERC(final Integer l) {
		Double flipCost = this.flipCosts.get(l);
		
		if(flipCost == null) {
			return Math.min(this.ruleSet.estimateMinRepairCost(l), this.flipCostDefault);
		}
		else {
			return Math.min(this.ruleSet.estimateMinRepairCost(l), flipCost);
		}
	}
	
	private long someNumber = 0;
	
	private class State implements DotNode {
		
		private final LI li;
		private long id = 0;
		private LitSet sset, tset, clashes;
		private double gCost = 0;
		private double hCost = 0;
		private Rule origin = null;
		
		private State(final LI li, final State parent, final Integer flip) {
			this.li = li;
			this.sset = new LitSet(li);
			this.sset.addAll(parent.sset);
			this.tset = parent.tset;
			this.clashes = new LitSet(li);
			this.clashes.addAll(parent.clashes);
			this.hCost = parent.hCost;
			
			if(this.clashes.contains(flip)) {
				this.sset.remove(flip);
				this.clashes.remove(flip);
				this.hCost -= AKAdaptOp.this.ERC(flip);
			}
			
			Double flipCost = AKAdaptOp.this.flipCosts.get(flip);
			
			if(flipCost == null) {
				flipCost = AKAdaptOp.this.flipCostDefault;
			}
			
			this.gCost = parent.gCost + flipCost;
			this.id = AKAdaptOp.this.someNumber++;
			// origin = flip
			this.origin = new Flip(flip);
		}
		
		private State(final LI li, final State parent, final AdaptationRule rule) {
			this.li = li;
			this.sset = new LitSet(li);
			this.sset.addAll(parent.sset);
			this.tset = parent.tset;
			this.clashes = new LitSet(li);
			this.clashes.addAll(parent.clashes);
			this.hCost = parent.hCost;
			
			for(Integer flip : rule.getRepairs()) {
				this.sset.remove(flip);
				
				if(this.clashes.contains(flip)) {
					this.clashes.remove(flip);
					this.hCost -= AKAdaptOp.this.ERC(flip);
				}
			}
			
			for(Integer newl : rule.getIntroductions()) {
				this.sset.add(newl);
				
				if(!this.clashes.contains(newl) && this.tset.contains(-newl)) {
					this.clashes.add(newl);
					this.hCost += AKAdaptOp.this.ERC(newl);
				}
			}
			
			this.gCost = parent.gCost + rule.getRuleCost();
			this.id = AKAdaptOp.this.someNumber++;
			// origin = rule
			this.origin = rule;
		}
		
		private State(final LI li, final LitSet sset, final LitSet tset) {
			this.li = li;
			this.sset = new LitSet(li);
			this.sset.addAll(sset);
			this.tset = tset;
			this.clashes = new LitSet(li);
			
			for(Integer l : sset) {
				if(tset.contains(-l)) {
					this.clashes.add(l);
					this.hCost += AKAdaptOp.this.ERC(l);
				}
			}
			
			this.id = AKAdaptOp.this.someNumber++;
			// origin = initial state
			this.origin = null;
		}
		
		public boolean isFinal() {
			return this.clashes.size() == 0;
		}
		
		public LitSet asSolution() {
			this.sset.addAll(this.tset);
			this.tset = null;
			return this.sset;
		}
		
		public void print() {
			System.out.println("SOURCE:");
			this.li.printVars(this.sset);
			System.out.println("TARGET:");
			this.li.printVars(this.tset);
			System.out.println("F = " + this.gCost + " + " + this.hCost);
		}
		
		@Override
		public String dotNodeLabel() {
			if(this.tset == null) {
				String label = "";
				label += "<font color='#008000'>{";
				boolean first = true;
				int c = 0;
				
				for(Integer l : this.sset) {
					if(first) {
						first = false;
					}
					else {
						label += ", ";
					}
					
					label += this.li.getDotName(l);
					
					if(c == 6) {
						label += "<br/>";
						c = 0;
					}
					
					c++;
				}
				
				label += "}</font>";
				DecimalFormat df = new DecimalFormat("####0.00");
				label += "<font color='#000080'>" + "<br/>" + df.format(this.gCost) + "</font>";
				return label;
			}
			else if(this.origin == null || this.clashes.size() == 0) {
				String label = "<font color='#ff0000'>{";
				boolean first = true;
				int c = 0;
				
				for(Integer l : this.sset) {
					if(first) {
						first = false;
					}
					else {
						label += ", ";
					}
					
					if(c == 6) {
						label += "<br/>";
						c = 0;
					}
					
					if(this.clashes.contains(l)) {
						label += "<b><font color='#ff9000'>" + this.li.getDotName(l) + "</font></b>";
					}
					else {
						label += this.li.getDotName(l);
					}
					
					c++;
				}
				
				label += "}</font><br/><font color='#008000'>{";
				first = true;
				
				for(Integer l : this.tset) {
					if(first) {
						first = false;
					}
					else {
						label += ", ";
					}
					
					label += this.li.getDotName(l);
				}
				
				label += "}</font>";
				DecimalFormat df = new DecimalFormat("####0.00");
				label += "<font color='#000080'>" + "<br/>" + df.format(this.gCost) + " + " + df.format(this.hCost) + "</font>";
				return label;
			}
			else {
				DecimalFormat df = new DecimalFormat("####0.00");
				return "S<sub>" + this.id + "</sub><font color='#000080'>" + "<br/>" + df.format(this.gCost) + " + " + df.format(this.hCost) + "</font>";
			}
		}
	}
	
}
