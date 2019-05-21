package fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops;

import java.util.ArrayList;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

public class TruthTableDalalRevOp extends AbstractDalalRevOp {
	
	// Constructors :
	
	public TruthTableDalalRevOp(final LI li, final PLFormula psi, final PLFormula mu) {
		super(li, psi, mu);
	}
	
	// Methods :
	
	@Override
	protected void listerModeles(final ArrayList<LitSet> mod_psi, final ArrayList<LitSet> mod_mu) {
		try {
			for(Interpretation int_psi : this.psi) {
				if(int_psi.satisfies(this.psi)) {
					LitSet mpsi = new LitSet(this.li);
					
					for(Integer lit : int_psi) {
						if(int_psi.satisfies(lit)) {
							mpsi.add(lit);
						}
						else {
							mpsi.add(-lit);
						}
					}
					
					mod_psi.add(mpsi);
				}
			}
			
			for(Interpretation int_mu : this.mu) {
				if(int_mu.satisfies(this.mu)) {
					LitSet mmu = new LitSet(this.li);
					
					for(Integer lit : int_mu) {
						if(int_mu.satisfies(lit)) {
							mmu.add(lit);
						}
						else {
							mmu.add(-lit);
						}
					}
					
					mod_mu.add(mmu);
				}
			}
		}
		catch(InterpretationFunctionDomainException e) {
			// A priori impossible.
			e.printStackTrace();
		}
	}
	
}
