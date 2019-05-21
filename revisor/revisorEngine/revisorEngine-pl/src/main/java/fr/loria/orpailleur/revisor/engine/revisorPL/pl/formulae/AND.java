package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import java.util.ArrayList;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux.TableauxDistributor;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux.TableauxNormalizer;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux.TableauxSimplifier;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

public class AND extends PLNaryFormula {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public AND(final LI li, final PLFormula... fils) {
		super(li, fils);
	}
	
	public AND(final LI li, final LitSet literals) {
		super(li, li.litsetToArray(literals));
	}
	
	// Methods :
	
	/**
	 * Will throw a class cast exception if children are not all PLLiterals or PLConstants.
	 * @return the LitSet corresponding to this conjunction.
	 */
	public LitSet asLitSet() {
		LitSet litSet = new LitSet(this.li);
		
		for(PLFormula f : this.getChildren()) {
			if(f instanceof PLLiteral) {
				litSet.add(((PLLiteral) f).getID());
			}
		}
		
		return litSet;
	}
	
	@Override
	public boolean estSatisfaitePar(final Interpretation i) throws InterpretationFunctionDomainException {
		for(PLFormula f : this.getChildren()) {
			if(!f.estSatisfaitePar(i)) {
				return false;
			}
		}
		
		return true;
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
			return PLConstant.TRUE;
		}
		
		ArrayList<PLFormula[]> liste_disj = new ArrayList<>();
		ArrayList<PLFormula> conjonction_simplifiee = new ArrayList<>();
		
		for(PLFormula f : this.getChildren()) {
			f = f.toNNF();
			f = f.toCNF(options_distributeur);
			
			if(f instanceof AND) {
				for(PLFormula f2 : f.listeFils()) {
					if(f2 instanceof OR) {
						liste_disj.add(f2.listeFils());
					}
					else {
						PLFormula[] fp = {f2};
						liste_disj.add(fp);
					}
				}
			}
			else if(f instanceof OR) {
				liste_disj.add(f.listeFils());
			}
			else if(f == PLConstant.TRUE) {
				// on omet VRAI : VRAI & a & b ... == a & b ...
			}
			else if(f == PLConstant.FALSE) {
				return PLConstant.FALSE; // FAUX & a & b ... == FAUX
			}
			else {
				PLFormula[] fp = {f};
				liste_disj.add(fp);
			}
		}
		
		TableauxSimplifier s = new TableauxSimplifier(liste_disj, options_distributeur);
		
		for(PLFormula[] conjonction : s.simplify()) {
			if(conjonction.length > 1) {
				conjonction_simplifiee.add(new OR(this.li, conjonction));
			}
			else if(conjonction.length == 1) {
				conjonction_simplifiee.add(conjonction[0]);
			}
		}
		
		PLFormula fp;
		
		if(conjonction_simplifiee.size() > 1) {
			fp = new AND(this.li, conjonction_simplifiee.toArray(new PLFormula[conjonction_simplifiee.size()]));
		}
		else if(conjonction_simplifiee.size() > 0) {
			fp = conjonction_simplifiee.get(0);
		}
		else {
			fp = PLConstant.TRUE;
		}
		
		if((options_distributeur & TableauxNormalizer.VERBOSE) == TableauxNormalizer.VERBOSE) {
			System.out.println(this + " >>into cd-form>> " + fp);
		}
		
		return fp;
	}
	
	@Override
	public PLFormula toDNF(final int options_distributeur) {
		if(this.nombreFils() == 0) {
			return PLConstant.TRUE;
		}
		
		boolean valeur_0n = true;
		ArrayList<PLFormula> conjonctions_distribuees = new ArrayList<>();
		ArrayList<PLLiteral> conjonction = new ArrayList<>();
		ArrayList<PLLiteral[][]> disjonctions = new ArrayList<>();
		
		for(PLFormula f : this.getChildren()) {
			f = f.toNNF();
			
			if(f instanceof AND) {
				f = f.toDNF(options_distributeur);
			}
			else if(f instanceof OR) {
				f = f.toDNF(options_distributeur);
			}
			
			if(f == PLConstant.TRUE) {
				// on omet VRAI : VRAI & a & b & ... == a & b & ...
				valeur_0n = valeur_0n && true;
			}
			else {
				valeur_0n = valeur_0n && false;
				
				if(f instanceof AND) {
					for(PLFormula f2 : f.listeFils()) {
						if(f2 instanceof OR) {
							PLLiteral[] fils = (PLLiteral[]) f2.listeFils();
							PLLiteral[][] branchement = new PLLiteral[fils.length][];
							
							for(int i = 0; i < fils.length; i++) {
								PLLiteral[] fils_i = {fils[i]};
								branchement[i] = fils_i;
							}
							
							disjonctions.add(branchement);
						}
						else {
							conjonction.add((PLLiteral) f2);
						}
					}
				}
				else if(f instanceof OR) {
					PLFormula[] fils = f.listeFils();
					PLLiteral[][] branchement = new PLLiteral[fils.length][];
					
					for(int i = 0; i < fils.length; i++) {
						PLLiteral[] fils_i = null;
						
						if(fils[i] instanceof AND) {
							fils_i = PLLiteral.asLiteralArray(fils[i].listeFils());
						}
						else {
							fils_i = new PLLiteral[] {(PLLiteral) fils[i]};
						}
						
						branchement[i] = fils_i;
					}
					
					disjonctions.add(branchement);
				}
				else if(f == PLConstant.FALSE) {
					return PLConstant.FALSE; // FAUX & a & b & ... == FAUX
				}
				else {
					conjonction.add((PLLiteral) f);
				}
			}
		}
		
		TableauxDistributor dis = new TableauxDistributor(this.li, conjonction, disjonctions, LI.TRUE, options_distributeur);
		
		for(PLFormula[] fils_conjonction : dis.distribute()) {
			if(fils_conjonction.length > 1) {
				conjonctions_distribuees.add(new AND(this.li, fils_conjonction));
			}
			else if(fils_conjonction.length == 1) {
				conjonctions_distribuees.add(fils_conjonction[0]);
			}
		}
		
		PLFormula fp = null;
		
		if(conjonctions_distribuees.size() > 1) {
			fp = new OR(this.li, conjonctions_distribuees.toArray(new PLFormula[conjonctions_distribuees.size()]));
		}
		else if(conjonctions_distribuees.size() > 0) {
			fp = conjonctions_distribuees.get(0);
		}
		else {
			fp = (valeur_0n) ? PLConstant.TRUE : PLConstant.FALSE;
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
			
			if(f instanceof AND) {
				for(PLFormula f2 : f.listeFils()) {
					nouveaux_fils.add(f2);
				}
			}
			else if(f == PLConstant.FALSE) {
				return PLConstant.FALSE; // FAUX & a & b & ... == FAUX
			}
			else if(f == PLConstant.TRUE) {
				// on omet VRAI : VRAI & a & b & ... == a & b & ...
			}
			else {
				nouveaux_fils.add(f);
			}
		}
		
		return new AND(this.li, nouveaux_fils.toArray(new PLFormula[nouveaux_fils.size()]));
	}
	
	@Override
	public String operator(boolean latex) {
		return latex ? PL.LATEX_AND_SYMBOL : PL.AND_SYMBOL;
	}
	
}
