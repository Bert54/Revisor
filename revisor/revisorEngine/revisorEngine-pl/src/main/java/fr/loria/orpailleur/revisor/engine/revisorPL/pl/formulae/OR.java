package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import java.util.ArrayList;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux.TableauxDistributor;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux.TableauxNormalizer;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux.TableauxSimplifier;

public class OR extends PLNaryFormula {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public OR(final LI li, final PLFormula... fils) {
		super(li, fils);
	}
	
	// Methods :
	
	@Override
	public boolean estSatisfaitePar(final Interpretation i) throws InterpretationFunctionDomainException {
		for(PLFormula f : this.getChildren()) {
			if(f.estSatisfaitePar(i)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public PLFormula toNNF() {
		for(int i = 0; i < this.getChildren().length; i++) {
			this.getChildren()[i] = this.getChildren()[i].toNNF();
		}
		
		return this;
	}
	
	@Override
	public PLFormula toCNF(final int options_distributeur) {
		if(this.nombreFils() == 0) {
			return PLConstant.FALSE;
		}
		
		boolean valeur_0n = false;
		ArrayList<PLFormula> disjonctions_distribuees = new ArrayList<>();
		ArrayList<PLLiteral> disjonction = new ArrayList<>();
		ArrayList<PLLiteral[][]> conjonctions = new ArrayList<>();
		
		for(PLFormula f : this.getChildren()) {
			f = f.toNNF();
			
			if(f instanceof AND) {
				f = f.toCNF(options_distributeur);
			}
			else if(f instanceof OR) {
				f = f.toCNF(options_distributeur);
			}
			
			if(f == PLConstant.FALSE) {
				// on omet FAUX : FAUX | a | b | ... == a | b | ...
				valeur_0n = valeur_0n || false;
			}
			else {
				valeur_0n = valeur_0n || true;
				
				if(f instanceof OR) {
					for(PLFormula f2 : f.listeFils()) {
						if(f2 instanceof AND) {
							PLFormula[] fils = f2.listeFils();
							PLLiteral[][] branchement = new PLLiteral[fils.length][];
							
							for(int i = 0; i < fils.length; i++) {
								PLLiteral[] fils_i = {(PLLiteral) fils[i]};
								branchement[i] = fils_i;
							}
							
							conjonctions.add(branchement);
						}
						else {
							disjonction.add((PLLiteral) f2);
						}
					}
				}
				else if(f instanceof AND) {
					PLFormula[] fils = f.listeFils();
					PLLiteral[][] branchement = new PLLiteral[fils.length][];
					
					for(int i = 0; i < fils.length; i++) {
						PLLiteral[] fils_i = null;
						
						if(fils[i] instanceof OR) {
							fils_i = PLLiteral.asLiteralArray(fils[i].listeFils());
						}
						else {
							fils_i = new PLLiteral[] {(PLLiteral) fils[i]};
						}
						
						branchement[i] = fils_i;
					}
					
					conjonctions.add(branchement);
				}
				else if(f == PLConstant.TRUE) {
					return PLConstant.TRUE; // VRAI | a | b | ... == VRAI
				}
				else {
					disjonction.add((PLLiteral) f);
				}
			}
		}
		
		TableauxDistributor dis = new TableauxDistributor(this.li, disjonction, conjonctions, LI.FALSE, options_distributeur);
		
		for(PLFormula[] fils_disjonction : dis.distribute()) {
			if(fils_disjonction.length > 1) {
				disjonctions_distribuees.add(new OR(this.li, fils_disjonction));
			}
			else if(fils_disjonction.length == 1) {
				disjonctions_distribuees.add(fils_disjonction[0]);
			}
		}
		
		PLFormula fp = null;
		
		if(disjonctions_distribuees.size() > 1) {
			fp = new AND(this.li, disjonctions_distribuees.toArray(new PLFormula[disjonctions_distribuees.size()]));
		}
		else if(disjonctions_distribuees.size() > 0) {
			fp = disjonctions_distribuees.get(0);
		}
		else {
			fp = (valeur_0n) ? PLConstant.TRUE : PLConstant.FALSE;
		}
		
		if((options_distributeur & TableauxNormalizer.VERBOSE) == TableauxNormalizer.VERBOSE) {
			System.out.println(this + " >>into cd-form>> " + fp);
		}
		
		return fp;
	}
	
	@Override
	public PLFormula toDNF(final int options_distributeur) {
		if(this.nombreFils() == 0) {
			return PLConstant.FALSE;
		}
		
		ArrayList<PLFormula[]> liste_conj = new ArrayList<>();
		ArrayList<PLFormula> disjonction_simplifiee = new ArrayList<>();
		
		for(PLFormula f : this.getChildren()) {
			f = f.toNNF();
			f = f.toDNF(options_distributeur);
			
			if(f instanceof OR) {
				for(PLFormula f2 : f.listeFils()) {
					if(f2 instanceof AND) {
						liste_conj.add(f2.listeFils());
					}
					else {
						PLFormula[] fp = {f2};
						liste_conj.add(fp);
					}
				}
			}
			else if(f instanceof AND) {
				liste_conj.add(f.listeFils());
			}
			else if(f == PLConstant.FALSE) {
				// on omet FAUX : FAUX | a | b | ... == a | b | ...
			}
			else if(f == PLConstant.TRUE) {
				return PLConstant.TRUE; // VRAI | a | b | ... == VRAI
			}
			else {
				PLFormula[] fp = {f};
				liste_conj.add(fp);
			}
		}
		
		TableauxSimplifier s = new TableauxSimplifier(liste_conj, options_distributeur);
		
		for(PLFormula[] conjonction : s.simplify()) {
			if(conjonction.length > 1) {
				disjonction_simplifiee.add(new AND(this.li, conjonction));
			}
			else if(conjonction.length == 1) {
				disjonction_simplifiee.add(conjonction[0]);
			}
		}
		
		PLFormula fp = null;
		
		if(disjonction_simplifiee.size() > 1) {
			fp = new OR(this.li, disjonction_simplifiee.toArray(new PLFormula[disjonction_simplifiee.size()]));
		}
		else if(disjonction_simplifiee.size() > 0) {
			fp = disjonction_simplifiee.get(0);
		}
		else {
			fp = PLConstant.FALSE;
		}
		
		if((options_distributeur & TableauxNormalizer.VERBOSE) == TableauxNormalizer.VERBOSE) {
			System.out.println(this + " >>into dc-form>> " + fp);
		}
		
		return fp;
	}
	
	@Override
	public PLFormula flatten() {
		ArrayList<PLFormula> nouveaux_fils = new ArrayList<>();
		
		for(PLFormula f : this.getChildren()) {
			f = f.flatten();
			
			if(f instanceof OR) {
				for(PLFormula f2 : f.listeFils()) {
					nouveaux_fils.add(f2);
				}
			}
			else if(f == PLConstant.FALSE) {
				// on omet FAUX : FAUX | a | b | ... == a | b | ...
			}
			else if(f == PLConstant.TRUE) {
				return PLConstant.TRUE; // VRAI | a | b | ... == VRAI
			}
			else {
				nouveaux_fils.add(f);
			}
		}
		
		return new OR(this.li, nouveaux_fils.toArray(new PLFormula[nouveaux_fils.size()]));
	}
	
	@Override
	public String operator(boolean latex) {
		return latex ? PL.LATEX_OR_SYMBOL : PL.OR_SYMBOL;
	}
	
}
