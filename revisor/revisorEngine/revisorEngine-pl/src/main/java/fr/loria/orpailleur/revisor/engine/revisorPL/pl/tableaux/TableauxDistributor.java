package fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.NOT;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

/**
 * @author Gabin PERSONENI
 * 
 *         Permet d'appliquer des regles de distributivite Par ex : (a,b)
 *         distribue par (c,d) distribue par (e,f) donne (a,c,e), (a,c,f),
 *         (a,d,e), ..., (b,d,f)
 */
public class TableauxDistributor extends TableauxNormalizer {
	
	// Fields :
	
	private final LI li;
	private ArrayList<PLLiteral> pile = new ArrayList<>();
	private HashSet<Integer> clash_index = new HashSet<>();
	private HashMap<Integer, Integer> lit_occurences = new HashMap<>();
	private ArrayList<PLLiteral> partie_lineaire;
	private ArrayList<PLLiteral[][]> partie_branchee;
	private int neutral_elt = 0;
	protected ArrayList<PLFormula[]> generalisations = new ArrayList<>();
	
	// Constructors :
	
	public TableauxDistributor(final LI li, final ArrayList<PLLiteral> partie_lineaire, final ArrayList<PLLiteral[][]> partie_branchee, final int neutral, final int options) {
		super(options);
		this.li = li;
		this.neutral_elt = neutral;
		this.partie_lineaire = partie_lineaire;
		this.partie_branchee = partie_branchee;
	}
	
	// Methods :
	
	@SuppressWarnings("unused")
	public ArrayList<PLFormula[]> distribute() {
		this.message("*****\nDebut distribution");
		ArrayList<PLFormula[]> distributions = new ArrayList<>();
		
		if((this.options & RARITY_SORT) != 0) {
			this.partie_branchee = this.triRarete(this.partie_lineaire, this.partie_branchee);
		}
		
		// profondeur dans l'arbre d'exploration
		int profondeur = 0;
		// branchements de la recherche courante
		int branchements[] = new int[this.partie_branchee.size()];
		// contiendra les futures branches
		
		for(int i = 0; i < this.partie_lineaire.size(); i++) {
			try {
				this.push(this.partie_lineaire.get(i));
				this.afficherPile();
			}
			catch(RedundancyException e) {
				this.message("Fin distribution\n*****");
				return distributions;
			}
			catch(ClashException e) {
				this.message("Fin distribution\n*****");
				return distributions;
			}
		}
		
		for(int i = 0; i < branchements.length; i++) {
			branchements[i] = -1;
		}
		
		while(profondeur > -1) {
			while(profondeur < this.partie_branchee.size() && branchements[profondeur] < this.partie_branchee.get(profondeur).length - 1) {
				branchements[profondeur]++;
				PLLiteral segment[] = this.partie_branchee.get(profondeur)[branchements[profondeur]];
				int i = 0;
				
				try {
					for(PLLiteral f : segment) {
						this.push(f);
						i++;
					}
					
					this.afficherPile();
					profondeur++;
				}
				catch(ClashException e) {
					while(i > 0) {
						this.pop();
						i--;
					}
				}
				catch(RedundancyException e) {
					while(i > 0) {
						this.pop();
						i--;
					}
				}
			}
			
			if(profondeur == this.partie_branchee.size()) {
				this.message("Profondeur max");
				PLFormula jonction[] = this.toArray();
				distributions.add(jonction);
				
				if((this.options & GENERALIZE) != 0 && jonction.length < this.partie_lineaire.size() + this.partie_branchee.size()) {
					this.ajouterGeneralisation(jonction);
				}
			}
			else {
				this.message("Branchage max:" + branchements[profondeur]);
				branchements[profondeur] = -1;
			}
			
			profondeur--;
			
			if(profondeur < 0) {
				break;
			}
			
			this.message("Pop:" + this.partie_branchee.get(profondeur)[branchements[profondeur]].length);
			// backtrack
			
			for(PLFormula f : this.partie_branchee.get(profondeur)[branchements[profondeur]]) {
				this.pop();
			}
			
			this.afficherPile();
			
			while(profondeur > -1 && branchements[profondeur] >= this.partie_branchee.get(profondeur).length - 1) {
				branchements[profondeur] = -1;
				profondeur--;
				
				if(profondeur < 0) {
					break;
				}
				
				this.message("Pop:" + this.partie_branchee.get(profondeur)[branchements[profondeur]].length);
				
				for(PLFormula f : this.partie_branchee.get(profondeur)[branchements[profondeur]]) {
					this.pop();
				}
				
				this.afficherPile();
				// a | (!a&c) | (d&e)
			}
		}
		
		this.message("Fin distribution\n*****");
		return new TableauxSimplifier(distributions, this.options).simplify();
	}
	
	private void push(final PLLiteral element) throws ClashException, RedundancyException {
		// TEST DES CLASHS
		if((this.options & TEST_CLASHES) == TEST_CLASHES) {
			Integer litID = null;
			litID = element.getID();
			
			if(litID == this.neutral_elt) {
				return;
			}
			
			if(litID == -this.neutral_elt) {
				throw new ClashException(this.li.getSimpleName(litID));
			}
			
			// TEST DE GENERALISATION
			if((this.options & GENERALIZE) == GENERALIZE && litID != null) {
				for(PLFormula[] jonction_formules : this.generalisations) {
					if(this.pile.size() + 1 >= jonction_formules.length) {
						boolean match = true;
						
						for(int i = 0; i < jonction_formules.length; i++) {
							Integer occ_name = null;
							
							if(jonction_formules[i] instanceof PLLiteral) {
								occ_name = (((PLLiteral) jonction_formules[i]).getID());
							}
							
							if(occ_name == null) {
								match = false;
							}
							else {
								match = (litID == occ_name) || (this.lit_occurences.get(occ_name) != null && this.clash_index.contains(occ_name));
							}
							
							if(match == false) {
								break;
							}
						}
						
						if(match == true) {
							throw new RedundancyException();
						}
					}
				}
			}
			
			if(this.clash_index.contains(-litID) || litID == -this.neutral_elt) {
				throw new ClashException(this.li.getSimpleName(litID));
			}
			
			this.clash_index.add(litID);
			Integer occurrences = this.lit_occurences.get(litID);
			
			if(occurrences == null) {
				occurrences = 0;
			}
			
			this.lit_occurences.put(litID, occurrences + 1);
		}
		
		this.pile.add(element);
	}
	
	private PLFormula pop() {
		if(this.pile.size() <= 0) {
			this.message("Pile vide, POP impossible");
			return null;
		}
		
		PLFormula f = this.pile.remove(this.pile.size() - 1);
		
		if((this.options & TEST_CLASHES) == TEST_CLASHES) {
			Integer littID = null;
			
			if(f instanceof PLLiteral) {
				littID = ((PLLiteral) f).getID();
			}
			
			Integer occurences_de_f = this.lit_occurences.get(littID);
			
			if(occurences_de_f != null) {
				if(occurences_de_f == 1) {
					this.clash_index.remove(littID);
					this.lit_occurences.remove(littID);
				}
				else {
					this.lit_occurences.put(littID, occurences_de_f - 1);
				}
				
				this.message("POP " + littID + ", " + (occurences_de_f - 1) + " restants");
			}
		}
		
		return f;
	}
	
	private PLFormula[] toArray() {
		this.message("::ToArray::");
		
		if((this.options & REMOVE_DUPLICATES) == REMOVE_DUPLICATES) {
			this.message("Suppression doublons");
			PLFormula formules[] = new PLFormula[this.lit_occurences.size()];
			int i = 0;
			
			for(Integer littID : this.lit_occurences.keySet()) {
				PLFormula f = new PLLiteral(this.li, littID);
				formules[i++] = f;
			}
			
			return formules;
		}
		else {
			return this.pile.toArray(new PLFormula[this.pile.size()]);
		}
	}
	
	private void afficherPile() {
		if(this.verbose) {
			String s = "";
			
			if(this.pile.isEmpty()) {
				s = "[VIDE]";
			}
			else {
				for(PLFormula f : this.pile) {
					s += "[" + f + "] ";
				}
			}
			
			System.out.println(s);
		}
	}
	
	protected void ajouterGeneralisation(final PLFormula[] nouvgen) {
		for(int g = 0; g < this.generalisations.size(); g++) {
			if(nouvgen.length < this.generalisations.get(g).length) {
				PLFormula[] generalisation = this.generalisations.get(g);
				boolean match_total = true;
				
				for(int i = 0; i < nouvgen.length; i++) {
					String occ_name_i = null;
					Boolean affirmatif_i = null;
					
					if(nouvgen[i] instanceof PLLiteral) {
						occ_name_i = (((PLLiteral) nouvgen[i]).getName());
						affirmatif_i = true;
					}
					else if(nouvgen[i] instanceof NOT && nouvgen[i].listeFils()[0] instanceof PLLiteral) {
						occ_name_i = (((PLLiteral) nouvgen[i].listeFils()[0]).getName());
						affirmatif_i = false;
					}
					
					boolean match_local = false;
					
					for(int j = 0; j < generalisation.length; j++) {
						String occ_name_j = null;
						Boolean affirmatif_j = null;
						
						if(generalisation[j] instanceof PLLiteral) {
							occ_name_j = (((PLLiteral) generalisation[j]).getName());
							affirmatif_j = true;
						}
						else if(generalisation[j] instanceof NOT && generalisation[j].listeFils()[0] instanceof PLLiteral) {
							occ_name_j = (((PLLiteral) generalisation[j].listeFils()[0]).getName());
							affirmatif_j = false;
						}
						
						if(occ_name_i.compareTo(occ_name_j) == 0 && affirmatif_i == affirmatif_j) {
							match_local = true;
							break;
						}
					}
					if(!match_local) {
						match_total = false;
						break;
					}
				}
				
				if(match_total) {
					this.generalisations.remove(g);
				}
			}
		}
		
		this.message("::Generalisation::");
		this.generalisations.add(nouvgen);
	}
	
	private static class SegmentEvalue implements Comparable<SegmentEvalue> {
		
		PLLiteral[] f = null;
		Integer valeur = 0;
		
		public SegmentEvalue(final PLLiteral[] f, final int valeur) {
			this.valeur = valeur;
			this.f = f;
		}
		
		@Override
		public String toString() {
			return "(OCC:" + this.valeur + ")[" + this.f + "]";
		}
		
		@Override
		public int compareTo(final SegmentEvalue arg) {
			if(this.valeur < arg.valeur) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}
	
	private static class TableauFormulePropEvaluee implements Comparable<TableauFormulePropEvaluee> {
		
		PLLiteral[][] formules = null;
		Float valeur = 0f;
		
		@Override
		public String toString() {
			String str = "(R:" + this.valeur + ")";
			
			for(PLLiteral ft[] : this.formules) {
				for(PLLiteral f : ft) {
					str += "[" + f + "]";
				}
				
				str += " ; ";
			}
			
			return str;
		}
		
		@Override
		public int compareTo(final TableauFormulePropEvaluee arg) {
			if(this.valeur <= arg.valeur) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}
	
	private ArrayList<PLLiteral[][]> triRarete(final ArrayList<PLLiteral> partie_lineaire, final ArrayList<PLLiteral[][]> partie_branchee) {
		this.message("Tri preliminaire");
		HashMap<Integer, Float> occVars = new HashMap<>();
		TreeSet<TableauFormulePropEvaluee> arbreFormulesTrie = new TreeSet<>();
		
		for(PLLiteral f : partie_lineaire) {
			Integer litID = f.getID();
			Float occ = occVars.get(litID);
			occ = (occ == null) ? 0 : occ;
			occVars.put(litID, occ + 1);
		}
		
		for(PLLiteral branchange[][] : partie_branchee) {
			for(PLLiteral segment[] : branchange)
				for(PLLiteral f : segment) {
					Integer litID = f.getID();
					Float occ = occVars.get(litID);
					occ = (occ == null) ? 0 : occ;
					occVars.put(litID, occ + 1);
				}
		}
		
		for(PLLiteral branchement[][] : partie_branchee) {
			// Indice de rarete pour le tri en profondeur (= quel branchement
			// faire en premier)
			float rarete_branchement = 0;
			TreeSet<SegmentEvalue> segmentsTries = new TreeSet<>();
			
			for(PLLiteral segment[] : branchement) {
				// Indice de rarete pour le tri en largeur (= quel branche du
				// branchement explorer en premier)
				float rarete_segment = Float.MAX_VALUE;
				
				for(PLLiteral f : segment) {
					if(f instanceof PLLiteral) {
						Integer litID = ((PLLiteral) f).getID();
						Float occurences = occVars.get(litID);
						Float occurences_opposite = occVars.get(-litID);
						occurences = (occurences == null) ? 0f : occurences;
						occurences_opposite = (occurences_opposite == null) ? 0f : occurences_opposite;
						rarete_branchement = Math.max(occurences_opposite / (occurences + occurences_opposite), rarete_branchement);
						rarete_segment = Math.min(occurences, rarete_segment);
						this.message("R(" + litID + ")=" + occurences_opposite / (occurences + occurences_opposite));
					}
				}
				
				segmentsTries.add(new SegmentEvalue(segment, (int) rarete_segment));
			}
			
			PLLiteral[][] flist_triee = new PLLiteral[segmentsTries.size()][];
			int i = 0;
			
			for(SegmentEvalue vpe : segmentsTries) {
				flist_triee[i++] = vpe.f;
				this.message(vpe);
			}
			
			TableauFormulePropEvaluee tfpe = new TableauFormulePropEvaluee();
			tfpe.formules = flist_triee;
			tfpe.valeur = rarete_branchement;
			arbreFormulesTrie.add(tfpe);
		}
		
		ArrayList<PLLiteral[][]> listeTriee = new ArrayList<>(partie_branchee.size());
		
		for(TableauFormulePropEvaluee tfpe : arbreFormulesTrie) {
			listeTriee.add(tfpe.formules);
			this.message(tfpe);
		}
		
		return listeTriee;
	}
	
}
