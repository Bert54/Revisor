package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A QualitativeConstraintFormula object is necessarily in disjunctive normal form,
 * i.e. it is a set of disjoint QCNs.
 * The normalisation is handled in Perl through QAPBackendRequest.createQCN_PC()
 * @author Valmi Dufout-Lussier
 */
public class QualitativeConstraintDisjunction implements QualitativeConstraintFormula {
	
	// Fields :
	
	public String objnr; // unused
	public QualitativeAlgebra algebra;
	private QAPBackendRequest qap;
	
	private Set<QualitativeConstraintNetwork> disjuncts;
	
	// Constructors :
	
	public QualitativeConstraintDisjunction(QAPBackendRequest qap, Set<QualitativeConstraintNetwork> disjuncts, QualitativeAlgebra algebra) {
		this.qap = qap;
		this.algebra = algebra;
		
		for(QualitativeConstraintNetwork disjunct : disjuncts) {
			if(!disjunct.isConsistent()) {
				disjuncts.remove(disjunct);
			}
		}
		
		this.disjuncts = disjuncts;
	}
	
	/**
	 * FOR TESTING: disable after !
	 */
	public QualitativeConstraintDisjunction(Set<QualitativeConstraintNetwork> disjuncts) throws Exception {
		if(disjuncts.isEmpty()) {
			throw new Exception("Cannot construct disjunction from an empty QCN set without specified QAP and algebra.");
		}
		else {
			QualitativeConstraintNetwork qcn = disjuncts.iterator().next();
			this.qap = qcn.getQAP();
			this.algebra = qcn.getAlgebra();
			this.disjuncts = disjuncts;
		}
	}
	
	public QualitativeConstraintDisjunction(QAPBackendRequest qap, String input, QualitativeAlgebra algebra, String inputType) throws Exception {
		this.qap = qap;
		this.algebra = algebra;
		
		if(inputType.equals("file")) {
			List<String> objnrs = qap.createQCN_PC(input, algebra.getLabel());
			Set<QualitativeConstraintNetwork> disjuncts = new HashSet<>();
			
			for(String objnr : objnrs) {
				QualitativeConstraintNetwork qcn = new QualitativeConstraintNetwork(qap, objnr);
				disjuncts.add(qcn);
			}
			
			this.disjuncts = disjuncts;
		}
		else if(inputType.equals("formula")) {
			throw new UnsupportedOperationException("Creating a formula from string is not supported at this time.");
			// this.objnr = qap.createQCNfromString(input, algebra.getLabel());
		}
	}
	
	public QualitativeConstraintDisjunction(QAPBackendRequest qap, String input, QualitativeAlgebra algebra) throws Exception {
		this(qap, input, algebra, "file");
	}
	
	// Methods :
	
	@Override
	public QualitativeConstraintFormula abstractWith(QualitativeVariableSubstitution subst) throws Exception {
		Set<QualitativeConstraintNetwork> new_disjuncts = new HashSet<>();
		
		for(QualitativeConstraintNetwork qcn : this.disjuncts) {
			QualitativeConstraintNetwork new_qcn = new QualitativeConstraintNetwork(qcn.getQAP(), qcn, subst, "abstract", null, null);
			new_disjuncts.add(new_qcn);
		}
		
		return new QualitativeConstraintDisjunction(this.getQAP(), new_disjuncts, this.algebra);
	}
	
	@Override
	public String toString() {
		String output = "";
		
		if(this.disjuncts.isEmpty()) {
			output = "∅";
		}
		else {
			Iterator<QualitativeConstraintNetwork> iter = this.disjuncts.iterator();
			
			while(iter.hasNext()) {
				output += "(" + iter.next().toString() + ")";
				
				if(iter.hasNext()) {
					output += " | ";
				}
			}
		}
		
		return output;
	}
	
	/**
	 * Limitations: qcn1 mustn't be null, and qcn2 is disregarded.
	 * (I keep all this polymorphism in case it's needed, but I'm not sure anymore why I added it in the first place)
	 */
	@Override
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst, QualitativeConstraintFormula qcn1, QualitativeConstraintFormula qcn2) throws Exception {
		Set<QualitativeConstraintNetwork> qcn1s = qcn1.getDisjuncts();
		Set<QualitativeConstraintNetwork> new_disjuncts = new HashSet<>();
		
		for(QualitativeConstraintNetwork qcn : this.disjuncts) {
			for(QualitativeConstraintNetwork qcn1qcn : qcn1s) {
				QualitativeConstraintNetwork new_qcn = new QualitativeConstraintNetwork(qcn.getQAP(), qcn, subst, "refine", qcn1qcn, null);
				new_disjuncts.add(new_qcn);
			}
		}
		
		return new QualitativeConstraintDisjunction(this.getQAP(), new_disjuncts, this.algebra);
	}
	
	@Override
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst, QualitativeConstraintFormula qcn1) throws Exception {
		return this.refineWith(subst, qcn1, null);
	}
	
	@Override
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst) throws Exception {
		return this.refineWith(subst, null);
	}
	
	@Override
	public QualitativeConstraintFormula conjoinWith(QualitativeConstraintFormula qcn2) throws Exception {
		Set<QualitativeConstraintNetwork> qcn2s = qcn2.getDisjuncts();
		Set<QualitativeConstraintNetwork> new_disjuncts = new HashSet<>();
		
		for(QualitativeConstraintNetwork qcn : this.disjuncts) {
			for(QualitativeConstraintNetwork qcn2qcn : qcn2s) {
				QualitativeConstraintNetwork new_qcn = new QualitativeConstraintNetwork(this.qap, qcn, qcn2qcn);
				
				if(new_qcn.isConsistent()) {
					new_disjuncts.add(new QualitativeConstraintNetwork(this.qap, qcn, qcn2qcn));
				}
			}
		}
		
		return new QualitativeConstraintDisjunction(this.getQAP(), new_disjuncts, this.getAlgebra());
	}
	
	/**
	 * Unimplemented
	 */
	@Override
	public List<QualitativeConstraintScenario> reviseWith(QualitativeConstraintFormula mu) throws Exception {
		//List<String> result_ref = this.qap.reviseQCN(this.getObjnr(), mu.getObjnr());
		List<QualitativeConstraintScenario> result = new ArrayList<>();
		//Iterator<String> itr = result_ref.iterator();
		
		//while(itr.hasNext()) {
		//	QualitativeConstraintScenario qcn = new QualitativeConstraintScenario(this.qap, itr.next());
		//	result.add(qcn);
		//}
		
		return result;
	}
	
	@Override
	public List<QualitativeConstraintScenario> reviseExhaustivelyWith(QualitativeConstraintFormula mu) throws Exception {
		List<QualitativeConstraintScenario> result = new ArrayList<>();
		int bestdistance = Integer.MAX_VALUE;
		Set<QualitativeConstraintNetwork> psi_set = this.getDisjuncts();
		Set<QualitativeConstraintNetwork> mu_set = mu.getDisjuncts();
		
		// it's not entirely clear to me, but inconsistent QCNs do manage to get
		// inside some QCDs, so we'll check just in case
		// (sending an inconsistent QCN to the backend will produce a fatal error)
		for(QualitativeConstraintNetwork psi_qcn : psi_set) {
			if(psi_qcn.isConsistent()) {
				for(QualitativeConstraintNetwork mu_qcn : mu_set) {
					if(mu_qcn.isConsistent()) {
						List<QualitativeConstraintScenario> local_result = psi_qcn.reviseExhaustivelyWith(mu_qcn, bestdistance);
						
						if(psi_qcn.getQAP().getLastRevisionDistance() == bestdistance) {
							result.addAll(local_result);
						}
						else if(psi_qcn.getQAP().getLastRevisionDistance() < bestdistance) {
							bestdistance = psi_qcn.getQAP().getLastRevisionDistance();
							result = local_result;
						}
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public QualitativeAlgebra getAlgebra() {
		return this.algebra;
	}
	
	/**
	 * Field objnr is irrelevant for disjunction.
	 */
	@Override
	public String getObjnr() {
		return null;
	}
	
	@Override
	public QAPBackendRequest getQAP() {
		return this.qap;
	}
	
	@Override
	public Set<QualitativeConstraintNetwork> getDisjuncts() {
		return this.disjuncts;
	}
	
	@Override
	public boolean isConsistent() {
		if(this.disjuncts.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
