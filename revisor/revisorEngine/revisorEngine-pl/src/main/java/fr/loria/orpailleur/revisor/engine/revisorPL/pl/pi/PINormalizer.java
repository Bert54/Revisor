package fr.loria.orpailleur.revisor.engine.revisorPL.pl.pi;

import java.util.ArrayList;
import java.util.HashSet;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

public class PINormalizer {
	
	private class Implicant {
		
		// Fields :
		
		LitSet literals;
		boolean used_for_merge = false;
		
		// int _1count = 0;
		
		// Constructors :
		
		private Implicant(final LitSet litset) {
			this.literals = litset;
			
			this.init();
		}
		
		public Implicant(final PLLiteral lit) {
			this.literals = new LitSet(lit.getLi(), 1);
			this.literals.add(lit);
			
			this.init();
		}
		
		public Implicant(final AND conj) {
			try {
				this.literals = conj.asLitSet();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			this.init();
		}
		
		// Methods :
		
		@Override
		public String toString() {
			return this.literals.toString();
		}
		
		public int size() {
			return this.literals.size();
		}
		
		public void enumVars() {
			for(Integer l : this.literals) {
				PINormalizer.this.variables.add(Math.abs(l));
			}
		}
		
		private void init() {
			/*
			 * for (Integer l : this.literals) { if (l > 0) { this._1count++; }
			 * }
			 */
		}
		
	}
	
	// Fields :
	
	private final LI li;
	private ArrayList<Implicant> final_implicants = new ArrayList<>();
	private ArrayList<Implicant> implicants = new ArrayList<>();
	private HashSet<Integer> variables = new HashSet<>();
	
	private int step = 0;
	
	// Constructors :
	
	public PINormalizer(final PLFormula f) {
		this.li = f.getLi();
		
		PLFormula dnf = f.toDNF(0x0FFFFFFF);
		
		if(dnf instanceof AND) {
			this.addInitialImplicant((AND) dnf);
		}
		else if(dnf instanceof OR) {
			for(int i = 0; i < dnf.nombreFils(); i++) {
				PLFormula clause = ((OR) dnf).getChildren()[i];
				if(clause instanceof AND) {
					this.addInitialImplicant((AND) clause);
				}
				else if(clause instanceof PLLiteral) {
					this.addInitialImplicant((PLLiteral) clause);
				}
				else {
					try {
						throw new Exception("Not a proper DNF");
					}
					catch(Exception e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}
		else if(dnf instanceof PLLiteral) {
			this.addInitialImplicant((PLLiteral) dnf);
		}
		else {
			try {
				throw new Exception("Not a proper DNF");
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	// Methods :
	
	private void addInitialImplicant(final PLLiteral lit) {
		Implicant i = new Implicant(lit);
		i.enumVars();
		this.implicants.add(i);
	}
	
	private void addInitialImplicant(final AND conj) {
		Implicant i = new Implicant(conj);
		i.enumVars();
		this.implicants.add(i);
	}
	
	private PLFormula toDNF() {
		ArrayList<PLFormula> clauses = new ArrayList<>();
		
		for(Implicant i : this.final_implicants) {
			clauses.add(new AND(this.li, i.literals));
		}
		
		OR dnf = new OR(this.li, clauses.toArray(new PLFormula[clauses.size()]));
		
		return dnf;
	}
	
	private Implicant merge(final Implicant i1, final Implicant i2) {
		LitSet newImp = new LitSet(this.li);
		HashSet<Integer> differences = new HashSet<>();
		HashSet<Integer> unmatched1 = new HashSet<>();
		HashSet<Integer> unmatched2 = new HashSet<>();
		
		{
			for(Integer l : i1.literals) {
				if(i2.literals.contains(l)) {
					newImp.add(l);
				}
				else {
					if(i2.literals.contains(-l)) {
						differences.add(Math.abs(l));
						
						if(differences.size() > 1) {
							return null;
						}
					}
					else {
						unmatched1.add(l);
					}
				}
			}
			
			for(Integer l : i2.literals) {
				if(i1.literals.contains(l)) {
					newImp.add(l);
				}
				else {
					if(i1.literals.contains(-l)) {
						differences.add(Math.abs(l));
						
						if(differences.size() > 1) {
							return null;
						}
					}
					else {
						unmatched2.add(l);
					}
				}
			}
		}
		
		if(differences.size() == 0) {
			// SUBSOMPTION CASES
			if(unmatched1.size() > 0 && unmatched2.size() == 0) {
				i1.used_for_merge = true;
			}
			else if(unmatched2.size() > 0 && unmatched1.size() == 0) {
				i2.used_for_merge = true;
			}
			
			return null;
		}
		else {
			if(unmatched1.size() > 0 && unmatched2.size() > 0) {
				
			}
			else if(unmatched1.size() > 0) {
				i1.used_for_merge = true;
			}
			else if(unmatched2.size() > 0) {
				i2.used_for_merge = true;
			}
		}
		
		newImp.addAll(unmatched1);
		newImp.addAll(unmatched2);
		return new Implicant(newImp);
	}
	
	public PLFormula normalize() {
		ArrayList<Implicant> newImplicants = new ArrayList<>();
		
		int newCount = 0;
		
		do {
			// this.print();
			newCount = 0;
			this.step++;
			
			newImplicants = new ArrayList<>();
			
			for(int i = 0; i < this.implicants.size(); i++) {
				for(int j = i + 1; j < this.implicants.size(); j++) {
					Implicant imp = this.merge(this.implicants.get(i), this.implicants.get(j));
					
					if(imp != null) {
						newCount++;
						newImplicants.add(imp);
					}
				}
			}
			for(Implicant i : this.implicants) {
				if(!i.used_for_merge) {
					// this.final_implicants.add(i);
					if(newCount != 0 && i.size() <= (this.li.getVarCount() - this.step)) {
						newImplicants.add(i);
					}
					else {
						this.final_implicants.add(i);
					}
				}
			}
			this.implicants = newImplicants;
		}
		while(newCount != 0);
		
		return this.toDNF().toDNF(0x0FFFFFFF);
	}
	
	public void print() {
		for(Implicant i : this.implicants) {
			System.out.println(i);
		}
	}
	
}
