package fr.loria.orpailleur.revisor.engine.revisorPL.pl.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

/**
 * @author Quentin BRABANT
 */
public class Reduction {
	
	// Fields :
	
	private final LI li;
	private final PL pl;
	private final RevisorLogger logger;
	
	// Constructors :
	
	public Reduction(LI li, PL pl) {
		this.li = li;
		this.pl = pl;
		this.logger = RevisorLogger.instance();
	}
	
	// Methods :
	
	/**
	 * Method returning a substitution which applied to psi produce psi'.</br>
	 * psi' can be any formula such that (psi revised by mu) is equivalent to (psi' and mu).</br>
	 * The three parameters are intended to be psi, mu, and psi revised by mu, from a previous revision operation.
	 * @param psi - revised formula
	 * @param mu - formula revising psi
	 * @param result - result of the revision
	 * @return the substitution that transform psi into psi'
	 */
	public Substitution findSubstitution(PLFormula psi, PLFormula mu, PLFormula result) {
		PLFormula[] tab;
		
		result = result.toCNF();
		mu = mu.toCNF();
		psi = psi.toCNF();
		
		psi = deleteDeductibleDisjunctions(psi, mu).toCNF().flatten();
		
		result = deleteDeductibleDisjunctions(result, mu);
		
		result = result.toDNF().flatten();
		
		if(!(result instanceof OR)) {
			result = this.pl.OR(result);
		}
		
		result = supprimerLitterauxDeductibles(result, mu, psi);
		
		this.logger.logDebug("RES : Après suppression des littéraux déductibles : " + result);
		result = result.toCNF().flatten();
		
		if(!(result instanceof AND)) {
			result = this.pl.AND(result);
		}
		
		this.logger.logDebug("RES : Après suppression des littéraux déductibles CNF : " + result);
		
		tab = deleteEqualDisjunctions(result, psi);
		
		result = tab[0].flatten();
		psi = tab[1].flatten();
		
		return new Substitution(psi, result);
	}
	
	/**
	 * Fonction prenant en paramètre deux formules en CNF et retournant la
	 * formule f dont on a supprimé les disjonctions impliquées par des
	 * disjonctions de mu
	 */
	private PLFormula deleteDeductibleDisjunctions(PLFormula f, PLFormula mu) {
		// On s'assure que les formules d'entrée sont sous la bonne forme
		if(!(f instanceof AND)) {
			f = this.pl.AND(f).flatten();
		}
		else {
			f = f.flatten();
		}
		
		if(!(mu instanceof AND)) {
			mu = this.pl.AND(mu).flatten();
		}
		else {
			mu = mu.flatten();
		}
		
		this.logger.logTrace("---Supprimer les disjonctions déductibles dans f :");
		this.logger.logDebug("f : " + f);
		this.logger.logDebug("mu : " + mu);
		
		PLFormula[] tabResultat = ((AND) f).listeFils();
		PLFormula[] tabMu = mu.listeFils();
		
		// On parcourt les disjonctions de f et mu en CNF, et lorsqu'un
		// disjonction de f contient tous les littéraux d'une disjonction de mu,
		// on la supprime (on la remplace par TRUE)
		for(int i = 0; i < tabResultat.length; i++) {
			if(!(tabResultat[i] instanceof OR)) {
				tabResultat[i] = this.pl.OR(tabResultat[i]);
			}
			
			for(int j = 0; j < tabMu.length; j++) {
				if(!(tabMu[j] instanceof OR)) {
					tabMu[j] = this.pl.OR(tabMu[j]);
				}
				
				if(implicationDisjonction(tabMu[j], tabResultat[i])) {
					tabResultat[i] = PL.TRUE;
				}
			}
		}
		
		this.logger.logDebug("f nettoyé : " + f);
		return this.pl.AND(tabResultat);
	}
	
	/**
	 * Fonction retournant dans un tableau de taille 2 les formules result et
	 * psi, après avoir supprimé dans chacune d'entre elles (passées en CNF),
	 * les disjonctions communes aux deux formules.
	 */
	private PLFormula[] deleteEqualDisjunctions(PLFormula result, PLFormula psi) {
		// On s'assure que les formules d'entrée sont sont la bonne forme
		if(!(result instanceof AND)) {
			result = this.pl.AND(result).flatten();
		}
		else {
			result = result.flatten();
		}
		
		if(!(psi instanceof AND)) {
			psi = this.pl.AND(psi).flatten();
		}
		else {
			psi = psi.flatten();
		}
		
		this.logger.logDebug("---Supprimer contraintes communes :");
		this.logger.logDebug("psi : " + psi);
		this.logger.logDebug("result : " + result);
		
		PLFormula[] tabResultat = listerSupprimables((AND) result);
		PLFormula[] tabPsi = psi.listeFils();
		
		// On parcourt les disjonctions de f et mu en CNF, et lorsque deux
		// disjonctions appartenant à result et psi sont égales, on les supprime
		// des deux formules
		for(int i = 0; i < tabResultat.length; i++) {
			if(!(tabResultat[i] instanceof OR)) {
				tabResultat[i] = this.pl.OR(tabResultat[i]);
			}
			
			for(int j = 0; j < tabPsi.length; j++) {
				if(!(tabPsi[j] instanceof OR)) {
					tabPsi[j] = this.pl.OR(tabPsi[j]);
				}
				
				if(disjunctionEquality(tabPsi[j], tabResultat[i])) {
					tabResultat[i] = PL.TRUE;
					tabPsi[j] = PL.TRUE;
				}
			}
		}
		
		result = this.pl.AND(tabResultat);
		psi = this.pl.AND(tabPsi);
		PLFormula[] retour = {result, psi};
		this.logger.logDebug("psi nettoyé : " + psi);
		this.logger.logDebug("result nettoyé : " + result);
		return retour;
	}
	
	/**
	 * Function deleting every literal that can be deleted from each sub-formula
	 * from result in DNF, without changing it's value.<\br> mu is used as a
	 * conjunction of implications that are used to determine which literal can
	 * be deduced using which other ones in result.<\br> The literals that are
	 * also contained in psi in CNF are not deleted.
	 * @param result - in DNF
	 * @param mu - in CNF
	 * @param psi - in CNF
	 * @return the "cleaned" result formula
	 */
	private PLFormula supprimerLitterauxDeductibles(PLFormula result, PLFormula mu, PLFormula psi) {
		this.logger.logDebug("---Supprimer littéraux déductibles :");
		this.logger.logDebug("1 : " + result);
		this.logger.logDebug("2 : " + mu);
		this.logger.logDebug("3 : " + psi);
		
		PLFormula[] tabResultat = result.listeFils();
		PLFormula[] tabPsi = psi.listeFils();
		HashSet<Integer> litterauxPsi = new HashSet<>();
		
		// On créé une liste contenant tous les littéraux isolé (cad appartenant
		// à une disjonction d'un seul littéral...) dans la formule de psi en
		// CNF
		for(PLFormula lit : tabPsi) {
			if(lit instanceof PLLiteral) {
				litterauxPsi.add(((PLLiteral) lit).getID());
			}
		}
		
		// Pour chaque sous formule (conjonction de littéraux) du résultat en
		// DNF, on appelle la fonction "apply rules" qui supprime les littéraux
		// déductibles
		for(int i = 0; i < tabResultat.length; i++) {
			tabResultat[i] = applyRules(tabResultat[i], mu, litterauxPsi);
			this.logger.logDebug("Or : " + tabResultat[i]);
		}
		
		// Chaque sous formule nettoyée est utilisée afin de recréer la formule
		// de résultat
		return this.pl.OR(tabResultat);
	}
	
	/**
	 * Method removing as many literal as possible from conjunction so that the
	 * formula "conjunction and mu" still have the same value.
	 * @param conjunction - conjunction of literals.
	 * @param mu - in CNF.
	 * @param psiLiterals - literals not to be removed from conjunction.
	 * @return cleaned conjunction of literals.
	 */
	private AND applyRules(PLFormula conjunction, PLFormula mu, HashSet<Integer> psiLiterals) {
		// On s'assure que les formules sont bien du bon type afin d'éviter les
		// erreurs par la suite
		if(!(conjunction instanceof AND)) {
			conjunction = this.pl.AND(conjunction);
		}
		
		if(!(mu instanceof AND)) {
			mu = this.pl.AND(mu);
		}
		
		this.logger.logDebug("---Appliquer règles sur : " + conjunction);
		
		PLFormula[] tabMu = mu.listeFils();
		PLFormula[] litterauxRegle;
		int litR;
		LitSet litterauxConjonction = ((AND) conjunction).asLitSet();
		
		// Set contenant les disjonctions à un seul littéral de mu
		LitSet litteraux_mu = new LitSet(this.li);
		
		for(PLFormula disjonction : tabMu) {
			if(!(disjonction instanceof OR)) {
				litteraux_mu.add(((PLLiteral) disjonction).getID());
				litterauxConjonction.add(((PLLiteral) disjonction));
				this.logger.logDebug("Ajouter litteral : " + disjonction);
			}
		}
		
		// On liste les "règles" applicables sur la conjonction. On appelle
		// règle toute implication dans mu pouvant être utilisée pour déduire un
		// littéral à partir d'un ensemble d'autres littéraux (voir classe
		// ApplicationRegle).
		
		ArrayList<ApplicationRegle> listeAR = new ArrayList<>();
		
		boolean fin_regle;
		int litteral_cible;
		
		// parcours des disjonctions de mu
		for(PLFormula disjonction : tabMu) {
			this.logger.logDebug("\t Règle : " + disjonction);
			
			if(!(disjonction instanceof OR)) {
				disjonction = this.pl.OR(disjonction);
			}
			
			litterauxRegle = disjonction.listeFils();
			
			ApplicationRegle regles = new ApplicationRegle(this.li);
			fin_regle = false;
			litteral_cible = 0;
			
			// Pour chaque disjonction dans mu, on regarde dans conjonction si
			// on se retrouve dans le cas de figure où :
			// La négation de chaque littéral de la disjonctionest présente dans
			// la conjonction, à l'exception d'un seul, qui est présent sans
			// être précédé de négation.
			// Si ce cas de figure se présente, on ajoute une règle à la liste
			// "regles", avec en partie droite les littéraux précédés de
			// négation, et en partie gauche le littéral sans négation
			for(int i = 0; i < litterauxRegle.length && !fin_regle; i++) {
				litR = ((PLLiteral) litterauxRegle[i]).getID();
				fin_regle = true;
				
				for(int litF : litterauxConjonction) {
					if(litF == -1 * litR) {
						regles.addGauche(litF);
						fin_regle = false;
						break;
					}
					else if(litF == litR) {
						if(litteral_cible == 0 && !psiLiterals.contains(litF)) {
							regles.setDroite(litF);
							litteral_cible = litF;
							fin_regle = false;
							break;
						}
						else {
							fin_regle = true;
							break;
						}
					}
				}
			}
			
			if(!fin_regle && litteral_cible != 0) {
				listeAR.add(regles);
			}
		}
		
		this.logger.logDebug("Litteraux de mu ajoutés : " + litteraux_mu);
		
		// On réduit la conjonction à l'aide des règles séléctionnées
		LitSet simplification = simplificationParMarquage(litterauxConjonction, listeAR);
		
		// On réduit la conjonction à l'aide des littéraux isolés de mu.
		for(int l : litteraux_mu) {
			simplification.remove(l);
		}
		
		String[] sortie = new String[simplification.size()];
		int j = 0;
		
		for(int i : simplification) {
			sortie[j] = (this.li.getName(i));
			j++;
		}
		
		return this.pl.AND((Object[]) sortie);
	}
	
	/**
	 * Fonction retournant true si pour les disjonctions de littéraux a et b :
	 * a |= b, false sinon.
	 */
	private boolean implicationDisjonction(PLFormula a, PLFormula b) {
		boolean implique = true;
		
		if(!(a instanceof OR)) {
			a = this.pl.OR(a);
		}
		
		if(!(b instanceof OR)) {
			b = this.pl.OR(b);
		}
		
		PLFormula[] taba = a.listeFils();
		PLFormula[] tabb = b.listeFils();
		
		// Parcours des disjonctions pour vérifier que tout littéral de a
		// appartient à b
		for(int i = 0; i < taba.length && implique; i++) {
			implique = false;
			
			for(int j = 0; j < tabb.length && !implique; j++) {
				if(((PLLiteral) taba[i]).getID() == ((PLLiteral) tabb[j]).getID()) {
					implique = true;
				}
			}
		}
		
		return implique;
	}
	
	/**
	 * Fonction retournant true si les disjonctions de littéraux a et b sont
	 * équivalentes, false sinon.
	 */
	private boolean disjunctionEquality(PLFormula a, PLFormula b) {
		if(!(a instanceof OR)) {
			a = this.pl.OR(a);
		}
		
		if(!(b instanceof OR)) {
			b = this.pl.OR(b);
		}
		
		ArrayList<PLFormula> litterauxA = new ArrayList<>(Arrays.asList(a.listeFils()));
		ArrayList<PLFormula> litterauxB = new ArrayList<>(Arrays.asList(b.listeFils()));
		
		if(litterauxA.size() != litterauxB.size()) {
			return false;
		}
		
		// Si a et b contiennent le même nombre de littéraux, on parcourt ces
		// littéraux ; si on ne trouve aucun littéraux de a qui est absent de b,
		// alors a et b sont équivalentes
		
		boolean equal = true;
		
		for(PLFormula da : litterauxA) {
			equal = false;
			
			for(PLFormula db : litterauxB) {
				if(((PLLiteral) da).getID() == ((PLLiteral) db).getID()) {
					equal = true;
					litterauxB.remove(db);
					break;
				}
			}
			
			if(!equal) {
				break;
			}
		}
		
		return equal;
	}
	
	/**
	 * Fonction qui prend en entrée une formule en CNF et retourne un tableau contenant toutes les
	 * disjonctions qui ne partagent aucun littéral avec d'autres disjonctions de la formule.
	 */
	private PLFormula[] listerSupprimables(AND f) {
		PLFormula[] tabF = f.listeFils();
		ArrayList<Integer> listIndices = new ArrayList<>();
		
		//Parcours des disjonctions de la formule
		for(int i = 0; i < tabF.length; i++) {
			boolean add = true;
			OR fi;
			
			if(tabF[i] instanceof OR) {
				fi = (OR) tabF[i];
			}
			else {
				fi = this.pl.OR(tabF[i]);
			}
			
			PLFormula[] littsi = fi.listeFils();
			
			//On vérifie, pour chaque autre disjonction, qu'elle ne contient pas de littéral commun
			for(int j = 0; j < tabF.length && add; j++) {
				if(i != j) {
					OR fj;
					
					if(tabF[j] instanceof OR) {
						fj = (OR) tabF[j];
					}
					else {
						fj = this.pl.OR(tabF[j]);
					}
					
					PLFormula[] littsj = fj.listeFils();
					
					for(int k = 0; k < littsi.length && add; k++) {
						for(int l = 0; l < littsj.length && add; l++) {
							if(((PLLiteral) littsi[k]).getID() == ((PLLiteral) littsj[l]).getID()) {
								add = false;
							}
						}
					}
				}
			}
			
			if(add) {
				listIndices.add(i);
			}
		}
		
		PLFormula[] retour = new PLFormula[listIndices.size()];
		
		for(int i = 0; i < listIndices.size(); i++) {
			retour[i] = tabF[listIndices.get(i)];
		}
		
		return retour;
	}
	
	/**
	 * Fonction ayant pour but de supprimer un maximum de littéraux dans conj,
	 * en utilisant les règles contenues dans rules.
	 */
	private LitSet simplificationParMarquage(LitSet conj, ArrayList<ApplicationRegle> rules) {
		this.logger.logDebug("Conjonction à marquer : " + conj);
		
		// Initialisation d'un maquage associant à chaque littéral x un ensemble
		// de d'ensemble de littéraux. Chaque ensemble de littéraux représente
		// des littéraux dont la présence conjointe dans la conjonction permet
		// de déduire (et donc de supprimer) x.
		HashMap<Integer, HashSet<LitSet>> marquages = new HashMap<>();
		LitSet marqueVraie = this.pl.AND(PL.TRUE).asLitSet();
		marqueVraie.add(1);
		
		// On marque chaque littéral par lui-même
		for(int lit : conj) {
			marquages.put(lit, new HashSet<LitSet>());
			LitSet init = new LitSet(this.li);
			init.add(lit);
			marquages.get(lit).add(init);
		}
		
		boolean encore_un_tour = true;
		
		// Tant qu'on produit de nouveaux marquages...
		while(encore_un_tour) {
			encore_un_tour = false;
			
			//...on itère sur les règles...
			for(ApplicationRegle regle : rules) {
				ArrayList<Integer> litterauxGauche = new ArrayList<>(regle.getGauche());
				
				//... si la partie gauche de la règle ne contient aucun littéral, on ajoute au littéral de la partie droite une marque contenant "vrai"
				if(litterauxGauche.size() == 0) {
					if(marquages.get(regle.getDroite()).add(marqueVraie)) {
						encore_un_tour = true;
					}
				}
				else {
					//...sinon, on met à jour les marques du littéral de la partie droite par la fonction "produitMarquages"
					if(marquages.get(regle.getDroite()).addAll(produitMarquages(litterauxGauche, marquages.get(litterauxGauche.get(0)), marquages))) {
						encore_un_tour = true;
					}
				}
			}
		}
		
		this.logger.logDebug("Marquage des littéraux : " + marquages);
		
		//Suppression des marques redondantes
		nettoyer(marquages);
		
		for(int i : marquages.keySet()) {
			this.logger.logDebug(i + " : " + this.li.getName(i));
		}
		
		this.logger.logDebug("Marquage des littéraux après nettoyage: " + marquages);
		
		//On retire à chaque littéral la marque de lui-même
		ArrayList<LitSet> marquesToRemove = new ArrayList<>();
		
		for(int i : marquages.keySet()) {
			marquesToRemove = new ArrayList<>();
			
			for(LitSet hs : marquages.get(i)) {
				if(hs.size() == 1 && hs.contains(i)) {
					marquesToRemove.add(hs);
					
				}
			}
			
			for(LitSet hs : marquesToRemove) {
				marquages.get(i).remove(hs);
			}
		}
		
		LitSet aSupprimer = new LitSet(this.li);
		
		for(Integer i : aSupprimer) {
			marquages.remove(i);
		}
		
		//Appel de l'heuristique de suppression
		return heuristiqueDeSuppression(marquages);
	}
	
	/**
	 * Fonction prenant en entrée le paramètre "marquages", qui représente un ensemble de littéraux auxquels
	 * sont associés un ensemble de marques. Chaque marque est un ensemble de littéraux, dont la présence
	 * permet de supprimer le littéral marquer. Cette heuristique vise à supprimer un maximum des littéraux,
	 * prenant en compte qu'une suppression de littéral aura un impact sur les suppression futures possibles.
	 */
	private LitSet heuristiqueDeSuppression(HashMap<Integer, HashSet<LitSet>> marquages) {
		HashMap<Integer, Double> apparitions = new HashMap<>();
		LitSet result = new LitSet(this.li);
		ArrayList<Integer> toRemove;
		ArrayList<LitSet> marquesToRemove;
		
		//Tant qu'il reste des littéraux potentiellement supprimables...
		while(marquages.size() != 0) {
			//... on compte le nombre d'apparitions de chaque littéral dans les marquages des autres littéraux...
			apparitions = new HashMap<>();
			double nb;
			
			for(int i : marquages.keySet()) {
				nb = 0;
				
				for(int j : marquages.keySet()) {
					for(LitSet marque : marquages.get(j)) {
						if(marque.contains(i)) {
							nb = nb + 1.0 / marque.size();
							break;
						}
					}
				}
				
				apparitions.put(i, nb);
			}
			
			this.logger.logDebug("Apparitions : " + apparitions);
			this.logger.logDebug("Marquages : " + marquages);
			this.logger.logDebug("Result : " + result);
			
			//...on itère ensuite sur les littéraux potentiellement supprimables pour trouver le littéral ayant le nombre minimal d'apparitions dans les marques des autres littéraux. Ce littéral sera "séléctionné" pour la suppression.
			double apparitions_minimales = Double.MAX_VALUE;
			int key_min = 0;
			toRemove = new ArrayList<>();
			
			for(int i : apparitions.keySet()) {
				//Si le littéral n'a pas/plus de marques, il ne peut être supprimé. Il est retiré de la liste des littéraux supprimables, et ajouté aux littéraux du résultat.
				if(marquages.get(i).size() == 0) {
					result.add(i);
					toRemove.add(i);
					
				}
				else {
					if(apparitions.get(i) < apparitions_minimales) {
						key_min = i;
						apparitions_minimales = apparitions.get(i);
					}
				}
			}
			
			for(int i : toRemove) {
				marquages.remove(i);
			}
			
			//Ici, on supprime les marques qui contiennent le littéral séléctionné pour la suppression :
			marquesToRemove = new ArrayList<>();
			this.logger.logDebug("Key min : " + key_min);
			
			if(key_min != 0) {
				apparitions.remove(key_min);
				
				for(HashSet<LitSet> marques : marquages.values()) {
					for(LitSet l : marques) {
						for(int i : l) {
							if(key_min == i) {
								marquesToRemove.add(l);
								break;
							}
						}
					}
					
					for(LitSet l : marquesToRemove) {
						marques.remove(l);
					}
				}
				
				marquages.remove(key_min);
			}
			
		}
		
		return result;
	}
	
	/**
	 * Fonction récursive qui prend en entrée un list d'Integer, représentant une marque (litteraux),
	 * le résultat en construction (inputSet), et l'état global des marquages (marquages).
	 */
	private HashSet<LitSet> produitMarquages(List<Integer> litteraux, HashSet<LitSet> inputSet, HashMap<Integer, HashSet<LitSet>> marquages) {
		//Si la marque ne contient plus qu'un littéral, le résultat a terminé d'être construit ; sinon, on retire le premier littéral.
		if(litteraux.size() == 1) {
			return inputSet;
		}
		
		litteraux.remove(0);
		
		int lit = litteraux.get(0);
		
		HashSet<LitSet> marquesLit = marquages.get(lit);
		HashSet<LitSet> result = new HashSet<>();
		
		for(LitSet marque : marquesLit) {
			for(LitSet marqueinput : inputSet) {
				LitSet boutderesultat = new LitSet(this.li, marque);
				boutderesultat.addAll(marqueinput);
				
				result.add(boutderesultat);
			}
		}
		
		return produitMarquages(litteraux, result, marquages);
	}
	
	/**
	 * Fonction supprimant les marquages redondants dans la HashMap passée en paramètre
	 */
	private static void nettoyer(HashMap<Integer, HashSet<LitSet>> marquages) {
		HashSet<LitSet> toRemove;
		boolean delete = false;
		
		//Pour chaque littéral...
		for(int k : marquages.keySet()) {
			toRemove = new HashSet<>();
			
			//...on itère doublement sur les marquages de ce littéral
			for(LitSet hs1 : marquages.get(k)) {
				delete = false;
				
				//On vérifie que la marque ne contient pas le littéral sur laquelle elle est posée
				for(int i : hs1) {
					if(i == k) {
						toRemove.add(hs1);
					}
				}
				
				if(!delete) {
					for(LitSet hs2 : marquages.get(k)) {
						//Si une marque contient tous les littéraux d'une autre marque, elle est supprimée
						if(hs1 != hs2 && hs2.containsAll(hs1)) {
							delete = true;
						}
						
						if(delete) {
							toRemove.add(hs2);
							break;
						}
					}
				}
			}
			
			marquages.get(k).removeAll(toRemove);
		}
	}
	
}
