package fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

/**
 * Une implementation de l'interface Interpretation. Les interpretations des
 * differentes variables prop. sont indexees dans une HashMap.
 * 
 * @author Gabin Personeni
 */
public class HashedInterpretation extends Interpretation {
	
	// Fields :
	
	protected HashMap<Integer, Boolean> interpretation_variables = new HashMap<>();
	public String nom = null;
	public Boolean valeur_defaut = null;
	
	// Constructors :
	
	public HashedInterpretation(final LI li) {
		super(li);
	}
	
	/**
	 * Construit une interpretation a partir d'une Map associant.
	 * @param interpretation
	 */
	public HashedInterpretation(final LI li, final Map<String, Boolean> interpretation) {
		super(li);
		
		for(String var : interpretation.keySet()) {
			this.interpretation_variables.put(Math.abs(li.getID(var)), interpretation.get(var));
		}
	}
	
	/**
	 * Construit une interpretation a partir d'une collection de VariableInterpretee.
	 * @param variables
	 */
	public HashedInterpretation(final LI li, final Iterable<AtomicInterpretation> variables) {
		super(li);
		
		for(AtomicInterpretation var : variables) {
			this.interpretation_variables.put(var.id, var.interpretation);
		}
	}
	
	/**
	 * Construit une interpretation qui associe une valeur par defaut a toute variable.
	 * @param valeur_defaut - Valeur booleene par defaut.
	 */
	public HashedInterpretation(final LI li, final boolean valeur_defaut) {
		super(li);
		this.valeur_defaut = valeur_defaut;
	}
	
	/**
	 * Construit une interpretation nommee.
	 * @param nom - Nom de cette interpretation.
	 */
	public HashedInterpretation(final LI li, final String nom) {
		super(li);
		this.nom = nom;
	}
	
	/**
	 * Construit une interpretation nommee qui associe une valeur par defaut a toute variable.
	 * @param valeur_defaut - Valeur booleene par defaut.
	 * @param nom - Nom de cette interpretation.
	 */
	public HashedInterpretation(final LI li, final boolean valeur_defaut, final String nom) {
		super(li);
		this.nom = nom;
		this.valeur_defaut = valeur_defaut;
	}
	
	// Methods :
	
	@Override
	public void delete(final PLLiteral v) {
		this.interpretation_variables.remove(v.getName());
	}
	
	@Override
	public void set(final String name, final boolean interpretation) {
		this.interpretation_variables.put(Math.abs(this.li.getID(name)), interpretation);
	}
	
	@Override
	public void delete(final String name) {
		this.interpretation_variables.remove(name);
	}
	
	/**
	 * @return le nom de cette interpretation (si elle est nommee), ou les interpretations de chaque variable.
	 */
	@Override
	public String toString() {
		if(this.nom != null) {
			return this.nom;
		}
		else {
			String description = "";
			
			for(Integer v : this.interpretation_variables.keySet()) {
				Boolean b = this.interpretation_variables.get(v);
				
				if(b == null) {
					b = this.valeur_defaut;
				}
				
				description += v + ":" + b + " ";
			}
			
			return description;
		}
	}
	
	@Override
	public int size() {
		return this.interpretation_variables.size();
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return this.interpretation_variables.keySet().iterator();
	}
	
	@Override
	public boolean satisfies(final Integer var) throws InterpretationFunctionDomainException {
		if(var == LI.TRUE) {
			return true;
		}
		else if(var == LI.FALSE) {
			return false;
		}
		
		Boolean result = null;
		
		if(var < 0) {
			result = !this.interpretation_variables.get(Math.abs(var));
		}
		else {
			result = this.interpretation_variables.get(Math.abs(var));
		}
		
		if(result != null) {
			return result;
		}
		else {
			throw new InterpretationFunctionDomainException(this.li, var);
		}
	}
	
	@Override
	public void set(final Integer v, final boolean interpretation) {
		this.interpretation_variables.put(Math.abs(v), interpretation);
	}
	
	@Override
	public boolean canInterpret(final Integer var) {
		return (var == LI.TRUE) || (var == LI.FALSE) || (this.interpretation_variables.get(var) != null);
	}
	
}
