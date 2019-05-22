package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.usingLP_SOLVE;

import java.util.HashMap;
import java.util.Vector;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LPproblem;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LPsolution;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Objective;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Simplex;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.AttributeValueSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.IntegerVariable;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearFunction;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealVariable;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Value;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

/**
 * Used to interact with the LPsolve library. In particular build a linear
 * problem and solve it.
 * 
 * @author Cynthia Florentin
 * @author Sophie PIERRAT
 */
public class LPSproblem extends LPproblem {
	
	// On lui donne un nom qui ne pourra pas changer
	// protected static final RealVariable obj = new RealVariable("fct obj");
	
	// Fields :
	
	private AttributeValueSpace space;
	private Objective objective;
	private Simplex simplex;
	private int[] colno;
	private double[] rows, tabar;
	private int nbCaseTab;
	private int tailleEspace;
	private int nbContrainte;
	private LpSolve lp;
	
	// Constructors :
	
	/**
	 * Defines the linear problem in LP solve, it is then ready to be run.
	 * @param space - Provides the variables and their type.
	 * @param restrictions - Provides the linear constraints.
	 * @param objective - Provides the objective function together with the oj=bjective type (ie. minimization or maximization)
	 */
	public LPSproblem(AttributeValueSpace space, Simplex restrictions, Objective objective) {
		this.space = space;
		this.simplex = restrictions;
		this.objective = objective;
		declarationVariable();
		
		try {
			this.lp = LpSolve.makeLp(0, getTailleEspace());
			
			if(this.lp.getLp() == 0) {
				throw new ModelLpException();
			}
			
			this.lp.setAddRowmode(true);
			creationMatrice();
			majTypeVariable();
			creationFctObjective(objective);
		}
		catch(ModelLpException mle) {
			// TODO Auto-generated catch block
			mle.printStackTrace();
		}
		catch(LpSolveException lse) {
			// TODO Auto-generated catch block
			lse.printStackTrace();
		}
	}
	
	/**
	 * Par défaut, la matrice contruit avec des type Real On teste si le type
	 * est IntegerVariable.class, on change le type de la colonne.
	 * @throws LpSolveException
	 */
	private void majTypeVariable() throws LpSolveException {
		for(int l = 1; l <= this.tailleEspace; l++) {
			Variable val = getSpace().getVariables().elementAt(l - 1);
			
			if(val.getClass().equals(IntegerVariable.class)) {
				this.lp.setInt(l, true);
			}
		}
	}
	
	//	/**
	//	 * Methode qui redonne le probleme lp.
	//	 * @return
	//	 */
	//	private LpSolve getLp() {
	//		return this.lp;
	//	}
	
	/**
	 * Creation de la matrice contenant toutes les variables de l'espace et les contraintes linéaire.
	 * @throws LpSolveException
	 */
	private void creationMatrice() throws LpSolveException {
		for(int l = 1; l <= this.tailleEspace; l++) {
			Variable val = getSpace().getVariables().elementAt(l - 1);
			this.lp.setColName(l, val.toString());
		}
		
		constructionMatrice();
	}
	
	private void constructionMatrice() throws LpSolveException {
		int i = 0;
		
		for(int k = 0; k < this.nbContrainte; k++) {
			LinearConstraint lc = getSimplex().getConstraints().elementAt(k);
			HashMap<Variable, Value> h = lc.getPounderedVariables();
			
			//			int j = 0;
			//			
			//			for(int l = 1; l <= this.tailleEspace; l++) {
			//				Variable val = getSpace().getVariables().elementAt(l - 1);
			//				this.colno[j] = l;
			//				
			//				if(h.containsKey(val)) {
			//					this.rows[j++] = h.get(val).getValueDouble();
			//					this.tabar[i] = h.get(val).getValueDouble();
			//				}
			//				else {
			//					this.rows[j++] = 0;
			//				}
			//				
			//				i++;
			//			}
			//			
			//			/* add the rows to lpsolve */
			//			this.lp.addConstraintex(j, getRowsdouble(), this.colno, lc.getType().getVal(), lc.getOffset().getValueDouble());
			
			for(int l = 0; l < this.tailleEspace; l++) {
				Variable val = getSpace().getVariables().elementAt(l);
				this.colno[l] = l + 1;
				
				if(h.containsKey(val)) {
					this.rows[l] = h.get(val).getValueDouble();
					this.tabar[i] = h.get(val).getValueDouble();
				}
				else {
					this.rows[l] = 0;
				}
				
				i++;
			}
			
			/* add the rows to lpsolve */
			this.lp.addConstraintex(this.tailleEspace, getRowsdouble(), this.colno, LPSConstraintType.getLPSconstrTypeCode(lc.getType()), lc.getOffset().getValueDouble());
		}
	}
	
	private void declarationVariable() {
		this.tailleEspace = getSpace().getVariables().size();
		this.nbContrainte = getSimplex().getConstraints().size();
		this.nbCaseTab = this.tailleEspace * this.nbContrainte;
		// creation des tableaux
		this.colno = new int[this.tailleEspace];
		this.rows = new double[this.tailleEspace]; // [nbContrainte];
		this.tabar = new double[this.nbCaseTab];
	}
	
	private void creationFctObjective(Objective o) throws LpSolveException {
		/* rowmode should be turned off again when done building the model */
		this.lp.setAddRowmode(false);
		
		//ObjectiveType type = o.getType();
		LinearFunction fct = o.getObjectiveFunction();
		HashMap<Variable, Value> h = fct.getCoefficients();
		Vector<Variable> var = getSpace().getVariables();
		int j = 0;
		
		for(int m = 0; m < var.size(); m++) {
			Variable v = var.elementAt(m);
			double offset = 0;
			
			if(h.containsKey(v)) {
				if(h.get(v) == null) {
					System.out.println("coef variable null pour variable" + v);
				}
				
				offset = (h.containsKey(v) ? h.get(v).getValueDouble() : 0);
			}
			
			this.colno[j] = m + 1;
			this.rows[j++] = offset;
		}
		
		/* set the objective in lpsolve */
		this.lp.setObjFnex(j, getRowsdouble(), this.colno);
		
		switch(o.getType()) {
			case MAXIMIZE:
				this.lp.setMaxim();
				break;
			case MINIMIZE:
				this.lp.setMinim();
				break;
			default:
				throw new UnsupportedOperationException("La contrainte d'objective n'existe pas");
		}
		
		//		if(type.equals(ObjectiveType.minimize)) {
		//			this.lp.setMinim();
		//		}
		//		else {
		//			if(type.equals(ObjectiveType.maximize)) {
		//				this.lp.setMaxim();
		//			}
		//			else {
		//				
		//			}
		//		}
	}
	
	/**
	 * Runs the resolution of the linear problem.
	 */
	@Override
	public LPsolution solve() {
		LpSolveException errorMsg;
		
		try {
			LPsolution lpsol = new LPsolution(getValObjective());
			this.lp.setVerbose(LpSolve.IMPORTANT);
			int ret = this.lp.solve();
			// System.out.println("Pour test dans classe LPSproblem, ret = " + ret);
			switch(ret) {
				case LpSolve.NOMEMORY:
					throw new Error("LPsolve could not solve the problem : Out of memory");
				case LpSolve.NOFEASFOUND:
				case 2: // value actualy returned when infeasible problem.
					throw new Error("LPsolve could not solve the problem : Non feasible problem");
				case LpSolve.UNBOUNDED:
					throw new Error("LPsolve could not solve the problem : Non bounded solution");
				case 5:
					System.out.println("CBR2LP : mysterious return code 5, seens to be a accuracy issue. Let's ignore it !");
				case LpSolve.OPTIMAL:
					// On ajoute la valeur de l'objective en premier
					//RealValue vobj = new RealValue(getValObjective());
					//lpsol.addHashMap(obj,vobj);
					
					// Ensuite on ajoute chaque variable de l'espace
					double[] resultatOptimal = new double[getTailleEspace()];
					this.lp.getVariables(resultatOptimal);
					
					for(int j = 0; j < getTailleEspace(); j++) {
						lpsol.addHashMap(new RealVariable(this.lp.getColName(j + 1)), new RealValue(resultatOptimal[j]));
					}
					
					// lp.getObjective()
					return lpsol;
				default:
					throw new Error("LPsolve could not solve the problem, return code = " + ret);
			}
		}
		catch(LpSolveException lse) {
			lse.printStackTrace();
			errorMsg = lse;
		}
		
		throw new Error("LP_SOLVE encountered an error:" + errorMsg);
	}
	
	private void affichSolution() throws LpSolveException {
		/* variable values */
		this.lp.getVariables(getRowsdouble());
		
		for(int j = 0; j < getTailleEspace(); j++) {
			System.out.println(this.lp.getColName(j + 1) + ": " + getRowsDouble()[j]);
		}
	}
	
	private void delete() {
		if(this.lp.getLp() != 0) {
			this.lp.deleteLp();
		}
	}
	
	private double getValObjective() throws LpSolveException {
		return this.lp.getObjective();
	}
	
	private AttributeValueSpace getSpace() {
		return this.space;
	}
	
	private void setSpace(AttributeValueSpace space) {
		this.space = space;
	}
	
	private Objective getObjective() {
		return this.objective;
	}
	
	private void setObjective(Objective objective) {
		this.objective = objective;
	}
	
	private Simplex getSimplex() {
		return this.simplex;
	}
	
	private void setSimplex(Simplex simplex) {
		this.simplex = simplex;
	}
	
	private int[] getNocol() {
		return this.colno;
	}
	
	private double[] getRowsDouble() {
		return this.rows;
	}
	
	private double[] getRowsdouble() {
		double[] row = new double[getRowsDouble().length];
		for(int i = 0; i < getRowsDouble().length; i++) {
			row[i] = getRowsDouble()[i];
		}
		return row;
	}
	
	private double[] getTabar() {
		return this.tabar;
	}
	
	private int getNbCaseTab() {
		return this.nbCaseTab;
	}
	
	private int getTailleEspace() {
		return this.tailleEspace;
	}
	
	private int getNbContrainte() {
		return this.nbContrainte;
	}
	
	private String afficheMatrix() {
		StringBuffer sb = new StringBuffer();
		int l = 0;
		
		for(int i = 0; i < getNbContrainte(); i++) {
			for(int j = 0; j < getTailleEspace(); j++) {
				sb.append(getTabar()[l]);
				sb.append("\t");
				l++;
			}
			
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Utilisation de LP_SOLVE\n");
		sb.append("Dans l'espace, on a :\n");
		sb.append(getSpace().toString() + "\n");
		sb.append("Le simplex est : \n");
		sb.append(getSimplex().toString());
		sb.append("La fonction objective vaut :\n");
		sb.append(getObjective().toString() + "\n");
		sb.append("\n");
		sb.append("affichage du probleme");
		this.lp.printLp();
		return sb.toString();
	}
	
}
