package fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops;

import java.util.ArrayList;
import java.util.HashMap;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLConstant;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

public abstract class AbstractDalalRevOp extends RevOp {
	
	// Fields :
	
	protected final LI li;
	protected HashMap<Integer, Double> flipCosts = new HashMap<>();
	protected double flipCostDefault = 1d;
	
	// Constructors :
	
	public AbstractDalalRevOp(final LI li, final PLFormula psi, final PLFormula mu) {
		super(psi, mu);
		this.li = li;
	}
	
	// Methods :
	
	protected abstract void listerModeles(ArrayList<LitSet> mod_psi, ArrayList<LitSet> mod_mu) throws TautologyException;
	
	@Override
	public PLFormula revisePsi() {
		ArrayList<LitSet> modeles_psi = new ArrayList<>();
		ArrayList<LitSet> modeles_mu = new ArrayList<>();
		ArrayList<LitSet> modeles_psi_retenus = new ArrayList<>();
		ArrayList<LitSet> modeles_mu_retenus = new ArrayList<>();
		
		try {
			this.listerModeles(modeles_psi, modeles_mu);
		}
		catch(TautologyException e) {
			return new AND(this.li, this.psi, this.mu);
		}
		
		double dH_minimale = Double.MAX_VALUE;
		
		for(LitSet m_psi : modeles_psi) {
			for(LitSet m_mu : modeles_mu) {
				double dH = this.dH(m_psi, m_mu, dH_minimale);
				
				if(dH < dH_minimale) {
					modeles_psi_retenus.clear();
					modeles_mu_retenus.clear();
					dH_minimale = dH;
				}
				
				if(dH == dH_minimale) {
					modeles_psi_retenus.add(m_psi);
					modeles_mu_retenus.add(m_mu);
				}
			}
		}
		
		PLFormula[] operandes = new PLFormula[modeles_psi_retenus.size()];
		
		for(int i = 0; i < modeles_psi_retenus.size(); i++) {
			operandes[i] = this.combiner(modeles_psi_retenus.get(i), modeles_mu_retenus.get(i));
		}
		
		if(operandes.length == 0) {
			return PLConstant.FALSE;
		}
		
		return new OR(this.li, operandes);
	}
	
	protected AND combiner(final LitSet mpsi, final LitSet mmu) {
		LitSet mpsiREVmu = new LitSet(this.li);
		mpsiREVmu.addAll(mpsi);
		
		for(Integer lit : mmu) {
			mpsiREVmu.remove(-lit);
			mpsiREVmu.add(lit);
		}
		
		return new AND(this.li, mpsiREVmu);
	}
	
	protected double dH(final LitSet mpsi, final LitSet mmu, final double seuil_intterupt) {
		double dH = 0;
		
		if(mpsi.contains(LI.FALSE) || mmu.contains(LI.FALSE)) {
			return Double.POSITIVE_INFINITY;
		}
		
		for(Integer lit : mpsi) {
			if(mmu.contains(-lit)) {
				Double cost = 0d;
				cost = this.flipCosts.get(-lit);
				cost = (cost == null) ? this.flipCostDefault : cost;
				dH += cost;
			}
			
			if(dH > seuil_intterupt) {
				return Double.POSITIVE_INFINITY;
			}
		}
		
		return dH;
	}
	
	@Override
	public void setLiteralWeight(final Integer lit, final double poids) {
		this.flipCosts.put(lit, poids);
	}
	
}
