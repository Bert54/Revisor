package fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux;

import java.util.ArrayList;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.NOT;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

public class TableauxSimplifier extends TableauxNormalizer {
	
	// Fields :
	
	private ArrayList<PLFormula[]> jonction;
	private ArrayList<PLFormula[]> formules_generales = new ArrayList<>();
	
	// Constructors :
	
	public TableauxSimplifier(final ArrayList<PLFormula[]> jonction) {
		super(OPTIONS_TRANSFORMATIONS);
		this.jonction = jonction;
	}
	
	public TableauxSimplifier(final ArrayList<PLFormula[]> jonction, final int options) {
		super(options);
		this.jonction = jonction;
	}
	
	// Methods :
	
	public ArrayList<PLFormula[]> simplify() {
		if((this.options & GENERALIZE_POST) == GENERALIZE_POST) {
			this.message("::Simplification::");
			this.jonction = triPlusPetitsDAbord(this.jonction);
			
			// POUR CHAQUE FORMULE [F] A INSERER
			for(int ji = 0; ji < this.jonction.size(); ji++) {
				boolean pas_trop_specifique = true;
				PLFormula[] fp = this.jonction.get(ji);
				
				// POUR CHAQUE FORMULE [G] DEJA INSEREE
				for(int fgi = 0; fgi < this.formules_generales.size() && pas_trop_specifique; fgi++) {
					PLFormula[] fgenerale = this.formules_generales.get(fgi);
					// SI [F] a moins d'operandes que [G],
					// [G] !=> [G] joint a [F]
					// (exemple: a ET b !=> (a ET b) OU (a))
					//
					// vu le tri par taille des formules, on arrete le test des
					// qu'on trouve une formule trop grande
					if(fgenerale.length > fp.length) {
						break;
					}
					
					boolean match_total = true;
					
					// TEST SI IL EXISTE UNE FORMULE [G] TELLE QUE
					// [G] => [G] joint a [F]
					// iteration sur les parties de [G]
					for(int i1 = 0; i1 < fgenerale.length && match_total; i1++) {
						String nomvar = null;
						Boolean var_affirmative = null;
						
						if(fgenerale[i1] instanceof PLLiteral) {
							nomvar = (((PLLiteral) fgenerale[i1]).getName());
							var_affirmative = true;
						}
						else if(fgenerale[i1] instanceof NOT && fgenerale[i1].listeFils()[0] instanceof PLLiteral) {
							nomvar = (((PLLiteral) fgenerale[i1].listeFils()[0]).getName());
							var_affirmative = false;
						}
						
						boolean trouve = false;
						
						// iteration sur les parties de [F]
						if(nomvar != null) {
							for(int i2 = 0; i2 < fp.length && !trouve; i2++) {
								String nomvar2 = null;
								Boolean var2_affirmative = null;
								
								if(fp[i2] instanceof PLLiteral) {
									nomvar2 = (((PLLiteral) fp[i2]).getName());
									var2_affirmative = true;
								}
								else if(fp[i2] instanceof NOT && fp[i2].listeFils()[0] instanceof PLLiteral) {
									nomvar2 = (((PLLiteral) fp[i2].listeFils()[0]).getName());
									var2_affirmative = false;
								}
								
								if(nomvar2 != null) {
									trouve = (nomvar.compareTo(nomvar2) == 0) && (var_affirmative == var2_affirmative);
								}
								else {
									trouve = false;
								}
							}
						}
						
						match_total = match_total & trouve;
					}
					
					pas_trop_specifique = !match_total;
				}
				
				if(pas_trop_specifique) {
					this.formules_generales.add(fp);
				}
				else {
					continue;
				}
			}
			
			return this.formules_generales;
		}
		else {
			return this.jonction;
		}
	}
	
	private static class TableauFormules implements Comparable<TableauFormules> {
		
		public PLFormula[] fp;
		
		public TableauFormules(final PLFormula[] fp) {
			this.fp = fp;
		}
		
		@Override
		public int compareTo(final TableauFormules arg) {
			if(arg.fp.length > this.fp.length) {
				return -1;
			}
			else {
				return 1;
			}
		}
	}
	
	private static ArrayList<PLFormula[]> triPlusPetitsDAbord(final ArrayList<PLFormula[]> liste) {
		TreeSet<TableauFormules> arbreTrie = new TreeSet<>();
		
		for(PLFormula[] fp : liste) {
			arbreTrie.add(new TableauFormules(fp));
		}
		
		ArrayList<PLFormula[]> listeTriee = new ArrayList<>(liste.size());
		
		for(TableauFormules tf : arbreTrie) {
			listeTriee.add(tf.fp);
		}
		
		return listeTriee;
	}
	
}
