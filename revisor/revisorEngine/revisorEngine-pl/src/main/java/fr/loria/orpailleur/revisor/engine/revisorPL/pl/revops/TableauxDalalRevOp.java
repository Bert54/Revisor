package fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops;

import java.util.ArrayList;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLConstant;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

public class TableauxDalalRevOp extends AbstractDalalRevOp {
	
	// Constants :
	
	private static final int options = 0x0FFFFFF;
	
	// Constructors :
	
	public TableauxDalalRevOp(final LI li, final PLFormula psi, final PLFormula mu) {
		super(li, psi, mu);
	}
	
	// Methods :
	
	protected PLFormula toDNF(final PLFormula f) {
		return f.toDNF(options);
	}
	
	@Override
	protected void listerModeles(final ArrayList<LitSet> mod_psi, final ArrayList<LitSet> mod_mu) throws TautologyException {
		this.psi = this.toDNF(this.psi);
		this.mu = this.toDNF(this.mu);
		
		if(this.mu == PLConstant.TRUE || this.psi == PLConstant.TRUE) {
			throw new TautologyException();
		}
		
		PLFormula[] psi_conjs = {this.psi};
		
		if(this.psi instanceof OR) {
			psi_conjs = this.psi.listeFils();
		}
		
		PLFormula[] mu_conjs = {this.mu};
		
		if(this.mu instanceof OR) {
			mu_conjs = this.mu.listeFils();
		}
		
		for(PLFormula conj : psi_conjs) {
			try {
				mod_psi.add(this.enInterpretation(conj));
			}
			catch(Exception e) {
			}
		}
		for(PLFormula conj : mu_conjs) {
			try {
				mod_mu.add(this.enInterpretation(conj));
			}
			catch(Exception e) {
			}
		}
		
		if(mod_psi.isEmpty()) {
			mod_psi.add(new LitSet(this.li));
		}
	}
	
	/**
	 * @param conj - Une n-conjonction de literaux
	 * @return Le modele correspondant a cette conjonction
	 * (ex : pour a & b & !c renvoie a:Vrai, b:Vrai, c:Faux)
	 * @throws Exception
	 */
	private LitSet enInterpretation(final PLFormula conj) throws Exception {
		LitSet interpretation = null;
		
		if(conj instanceof AND) {
			interpretation = ((AND) conj).asLitSet();
		}
		else if(conj instanceof PLLiteral) {
			interpretation = new LitSet(this.li);
			interpretation.add(((PLLiteral) conj).getID());
		}
		
		if(interpretation == null) {
			throw new Exception();
		}
		else if(interpretation.contains(LI.FALSE)) {
			throw new Exception();
		}
		else {
			interpretation.remove(LI.TRUE);
			return interpretation;
		}
	}
	
}
