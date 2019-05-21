package fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

public class InterpretationIterator implements Iterator<Interpretation> {
	
	// Fields :
	
	private final LI li;
	private ArrayList<AtomicInterpretation> variables_interpretees = new ArrayList<>();
	private long num_interpretation = 0;
	
	// Constructors :
	
	public InterpretationIterator(final PLFormula formule) {
		this.li = formule.getLi();
		
		ArrayList<PLFormula> noeuds_explorables = new ArrayList<>();
		noeuds_explorables.add(formule);
		
		while(noeuds_explorables.isEmpty() == false) {
			PLFormula form = noeuds_explorables.remove(noeuds_explorables.size() - 1);
			PLFormula[] liste_fils = form.listeFils();
			
			for(PLFormula fils : liste_fils) {
				noeuds_explorables.add(fils);
			}
			
			if(form instanceof PLLiteral) {
				AtomicInterpretation var = new AtomicInterpretation(((PLLiteral) form).getID(), true);
				
				for(int i = 0; i <= this.variables_interpretees.size(); i++) {
					if(i == this.variables_interpretees.size()) {
						// si on atteint la fin de la liste on insere la
						// nouvelle variable a la fin
						this.variables_interpretees.add(var);
					}
					
					AtomicInterpretation ieme_var = this.variables_interpretees.get(i);
					
					if(ieme_var.id == var.id) {
						// si la variable existe deja on ne l'ajoute pas
						break;
					}
					else if(ieme_var.id < var.id) {
						// on insere la variable de maniere a respecter l'ordre
						// alphabetique inverse
						this.variables_interpretees.add(i, var);
						break;
					}
				}
			}
		}
	}
	
	// Methods :
	
	@Override
	public boolean hasNext() {
		return this.num_interpretation < Math.pow(2L, this.variables_interpretees.size());
	}
	
	@Override
	public Interpretation next() throws NoSuchElementException {
		if(!this.hasNext()) {
			throw new NoSuchElementException();
		}
		
		this.num_interpretation++;
		
		for(AtomicInterpretation vi : this.variables_interpretees) {
			if(vi.interpretation == false) {
				vi.interpretation = true;
				break;
			}
			else {
				vi.interpretation = false;
			}
		}
		
		return new HashedInterpretation(this.li, this.variables_interpretees);
	}
	
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
}
