package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 @author Valmi Dufout-Lussier
 @date 01.02.2013
 */
public class QualitativeConstraintNetwork implements QualitativeConstraintFormula {
	
	// Constants :
	
	private static final String INCONSISTENT = "INCONSISTENT";
	
	/**
	 * Setting this to true will cause toString() to return only the object number.
	 * (all inconsistent QCNs are equivalent and have number "INCONSISTENT",
	 * which is basically the only possible reason for wanting to use this option)
	 */
	private static final boolean DEBUG_STRING = false;
	
	// Fields :
	
	public String objnr;
	public QualitativeAlgebra algebra;
	private QAPBackendRequest qap;
	
	// Constructors :
	
	public QualitativeConstraintNetwork(QAPBackendRequest qap, String input, QualitativeAlgebra algebra, String inputType) throws Exception {
		this.qap = qap;
		this.algebra = algebra;
		
		if(inputType.equals("file")) {
			this.objnr = qap.createQCN(input, algebra.getLabel());
		}
		else if(inputType.equals("formula")) {
			this.objnr = qap.createQCNfromString(input, algebra.getLabel());
		}
	}
	
	public QualitativeConstraintNetwork(QAPBackendRequest qap, String input, QualitativeAlgebra algebra) throws Exception {
		this(qap, input, algebra, "file");
	}
	
	public QualitativeConstraintNetwork(QAPBackendRequest qap, String objnr) throws Exception {
		this.qap = qap;
		this.objnr = objnr;
	}
	
	public QualitativeConstraintNetwork(QAPBackendRequest qap, QualitativeConstraintFormula qcn1, QualitativeConstraintFormula qcn2) throws Exception {
		this.qap = qap;
		this.algebra = qcn1.getAlgebra();
		this.objnr = qap.conjoinQCN(qcn1.getObjnr(), qcn2.getObjnr());
	}
	
	public QualitativeConstraintNetwork(QAPBackendRequest qap, QualitativeConstraintFormula qcn, QualitativeVariableSubstitution subst, String operation, QualitativeConstraintFormula qcn1, QualitativeConstraintFormula qcn2) throws Exception {
		this.qap = qap;
		this.algebra = qcn.getAlgebra();
		String objnr = qcn.getObjnr();
		
		if(operation.equals("abstract")) {
			while(subst.hasNextFrom()) {
				objnr = qap.abstractQCN(objnr, subst.getNextFromIndex(), subst.getNextFrom());
			}
		}
		else if(operation.equals("refine")) {
			String qcn1_ref = (qcn1 == null || !qcn1.isConsistent()) ? "" : qcn1.getObjnr();
			String qcn2_ref = (qcn2 == null || !qcn2.isConsistent()) ? "" : qcn2.getObjnr();
			
			while(subst.hasNextTo()) {
				objnr = qap.refineQCN(objnr, subst.getNextToIndex(), subst.getNextTo(), qcn1_ref, qcn2_ref);
			}
		}
		else {
			throw new Exception("Unsupported operation");
		}
		
		this.objnr = objnr;
	}
	
	// Methods :
	
	@Override
	public List<QualitativeConstraintScenario> reviseWith(QualitativeConstraintFormula mu) throws Exception {
		List<String> result_ref = this.qap.reviseQCN(this.getObjnr(), mu.getObjnr());
		List<QualitativeConstraintScenario> result = new ArrayList<>();
		Iterator<String> itr = result_ref.iterator();
		
		while(itr.hasNext()) {
			QualitativeConstraintScenario qcn = new QualitativeConstraintScenario(this.qap, itr.next());
			result.add(qcn);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		String response = "";
		
		if(DEBUG_STRING) {
			response = this.getObjnr();
		}
		else {
			try {
				response = this.qap.QCNtoString(this.getObjnr());
			}
			catch(Exception e) {
				System.err.println("Warning: " + e.getMessage());
			}
		}
		
		return response;
	}
	
	@Override
	public QualitativeConstraintFormula conjoinWith(QualitativeConstraintFormula qcn2) throws Exception {
		// If the other cojunct is disjunctive, we'll pass handling over to the appropriate class.
		if(qcn2 instanceof QualitativeConstraintDisjunction) {
			return qcn2.conjoinWith(this);
		}
		else {
			return new QualitativeConstraintNetwork(this.qap, this, qcn2);
		}
	}
	
	@Override
	public QualitativeConstraintFormula abstractWith(QualitativeVariableSubstitution subst) throws Exception {
		return new QualitativeConstraintNetwork(this.qap, this, subst, "abstract", null, null);
	}
	
	@Override
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst, QualitativeConstraintFormula qcn1, QualitativeConstraintFormula qcn2) throws Exception {
		QualitativeConstraintFormula result;
		
		// Special handling if qcn1 is disjunctive.
		if(qcn1 instanceof QualitativeConstraintDisjunction) {
			Set<QualitativeConstraintNetwork> qcn1s = qcn1.getDisjuncts();
			Set<QualitativeConstraintNetwork> new_disjuncts = new HashSet<>();
			
			for(QualitativeConstraintNetwork qcn1qcn : qcn1s) {
				QualitativeConstraintNetwork new_qcn = new QualitativeConstraintNetwork(this.getQAP(), this, subst, "refine", qcn1qcn, null);
				new_disjuncts.add(new_qcn);
			}
			
			result = new QualitativeConstraintDisjunction(this.getQAP(), new_disjuncts, this.getAlgebra());
		}
		else {
			result = new QualitativeConstraintNetwork(this.qap, this, subst, "refine", qcn1, qcn2);
		}
		
		return result;
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
	public List<QualitativeConstraintScenario> reviseExhaustivelyWith(QualitativeConstraintFormula mu) throws Exception {
		List<QualitativeConstraintScenario> result = new ArrayList<>();
		//boolean needsorting = true;
		
		if(mu instanceof QualitativeConstraintDisjunction) {
			throw new UnsupportedOperationException("Revision by disjunctions not yet supported.");
		}
		else {
			//needsorting = false;
			List<String> result_ref = this.qap.reviseExhaustivelyQCN(this.getObjnr(), mu.getObjnr());
			Iterator<String> itr = result_ref.iterator();
			
			while(itr.hasNext()) {
				QualitativeConstraintScenario qcn = new QualitativeConstraintScenario(this.qap, itr.next(), this.qap.getLastRevisionDistance());
				result.add(qcn);
			}
			
		}
		
		return result;
	}
	
	public List<QualitativeConstraintScenario> reviseExhaustivelyWith(QualitativeConstraintNetwork mu, int max_distance) throws Exception {
		List<QualitativeConstraintScenario> result = new ArrayList<>();
		
		List<String> result_ref = this.qap.reviseExhaustivelyQCNd(this.getObjnr(), mu.getObjnr(), max_distance);
		Iterator<String> itr = result_ref.iterator();
		
		while(itr.hasNext()) {
			QualitativeConstraintScenario qcn = new QualitativeConstraintScenario(this.qap, itr.next(), this.qap.getLastRevisionDistance());
			result.add(qcn);
		}
		
		return result;
	}
	
	@Override
	public QualitativeAlgebra getAlgebra() {
		return this.algebra;
	}
	
	@Override
	public String getObjnr() {
		return this.objnr;
	}
	
	@Override
	public QAPBackendRequest getQAP() {
		return this.qap;
	}
	
	/**
	 * This weird method is provided for QCDisjunction's convenience.
	 */
	@Override
	public Set<QualitativeConstraintNetwork> getDisjuncts() {
		HashSet<QualitativeConstraintNetwork> disjuncts = new HashSet<>();
		disjuncts.add(this);
		return disjuncts;
	}
	
	@Override
	public boolean isConsistent() {
		return !this.objnr.equals(INCONSISTENT);
	}
	
}
