package fr.loria.orpailleur.revisor.engine.revisorCLC;

import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexCase;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexDomainKnowledge;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint.ConstraintType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Simplex;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.IntegerVariable;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealVariable;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

/**
 * @author Julien Cojan
 */
public class TestRevisorCLC {
	
	public static void main(String[] args) {
		long tini = System.currentTimeMillis();
		example1();
		// System.out.println(Value.roundToDecimal(-0.10000000149011612, 2));
		long tend = System.currentTimeMillis();
		System.out.println("computation time (ms) : " + (tend - tini));
	}
	
	private static void example1() {
		// Parameters :
		
		float appleSugarDensity = (float) 0.1;
		float pearSugarDensity = (float) 0.15;
		
		// Distance coefficients :
		
		RealValue fruitMassVarCost = new RealValue(5);
		RealValue appleMassVarCost = new RealValue(1);
		RealValue pearMassVarCost = new RealValue(1);
		RealValue sugarMassVarCost = new RealValue(5);
		RealValue Saccharose = new RealValue(1);
		RealValue doughMassVarCost = new RealValue(1);
		
		// Source case data :
		
		float sourceDoughMass = 100;
		float sourceSaccharoseMass = 40;
		
		RevisorCLC.initSpace();
		
		// Building space :
		
		// RevisorCLC.setWeight("fruit mass", 5, variableType.Real);
		RealVariable fruitMass = new RealVariable("fruit mass");
		RevisorCLC.setWeight(fruitMass, fruitMassVarCost);
		IntegerVariable pearNb = new IntegerVariable("pear nb");
		RevisorCLC.setWeight(pearNb, new RealValue(50));
		RealVariable pearMass = new RealVariable("pear mass");
		RevisorCLC.setWeight(pearMass, pearMassVarCost);
		IntegerVariable appleNb = new IntegerVariable("apple nb");
		RevisorCLC.setWeight(appleNb, new RealValue(50));
		RealVariable appleMass = new RealVariable("apple mass");
		RevisorCLC.setWeight(appleMass, appleMassVarCost);
		RealVariable sugarMass = new RealVariable("sugar mass");
		RevisorCLC.setWeight(sugarMass, sugarMassVarCost);
		RealVariable saccharoseMass = new RealVariable("saccharose mass");
		RevisorCLC.setWeight(saccharoseMass, Saccharose);
		RealVariable doughMass = new RealVariable("dough mass");
		RevisorCLC.setWeight(doughMass, doughMassVarCost);
		
		// Building Domain knowledge :
		
		Simplex s = new Simplex();
		
		// variables range (all positive)
		constraintDomainVariables(s, fruitMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, pearMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, appleMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, sugarMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, saccharoseMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, doughMass, ConstraintType.GEQ, 0);
		
		LinearConstraint constraint;
		
		// appleMass = 100*appleNb
		constraint = new LinearConstraint();
		constraint.setType(ConstraintType.EQUAL);
		constraint.setOffset(new RealValue(0));
		constraint.addTerm(appleMass, new RealValue(1));
		constraint.addTerm(appleNb, new RealValue(-100));
		s.addConstraint(constraint);
		
		// pearMass = 80*pearNb
		constraint = new LinearConstraint();
		constraint.setType(ConstraintType.EQUAL);
		constraint.setOffset(new RealValue(0));
		constraint.addTerm(pearMass, new RealValue(1));
		constraint.addTerm(pearNb, new RealValue(-80));
		s.addConstraint(constraint);
		
		// fruitMass = appleMass + pearMass
		constraint = new LinearConstraint();
		constraint.setType(ConstraintType.EQUAL);
		constraint.setOffset(new RealValue(0));
		constraint.addTerm(fruitMass, new RealValue(1));
		constraint.addTerm(appleMass, new RealValue(-1));
		constraint.addTerm(pearMass, new RealValue(-1));
		s.addConstraint(constraint);
		
		// sugarMass = saccharoseMass + appleSugarDensity*appleMass + appleSugarDensity*pearMass
		constraint = new LinearConstraint();
		constraint.setType(ConstraintType.EQUAL);
		constraint.setOffset(new RealValue(0));
		constraint.addTerm(sugarMass, new RealValue(1));
		constraint.addTerm(saccharoseMass, new RealValue(-1));
		constraint.addTerm(appleMass, new RealValue(-appleSugarDensity));
		constraint.addTerm(pearMass, new RealValue(-pearSugarDensity));
		s.addConstraint(constraint);
		
		SimplexDomainKnowledge dk = new SimplexDomainKnowledge(s);
		System.out.println("domain knowledge:");
		System.out.println(dk);
		System.out.println();
		
		// Building source case :
		
		Simplex srceSimplex = new Simplex();
		constraintDomainVariables(srceSimplex, pearNb, ConstraintType.EQUAL, 0);
		constraintDomainVariables(srceSimplex, appleNb, ConstraintType.EQUAL, 3);
		constraintDomainVariables(srceSimplex, doughMass, ConstraintType.EQUAL, sourceDoughMass);
		constraintDomainVariables(srceSimplex, saccharoseMass, ConstraintType.EQUAL, sourceSaccharoseMass);
		
		SimplexCase srce = new SimplexCase(srceSimplex);
		System.out.println("source case:");
		System.out.println(srce);
		System.out.println();
		
		// Builing target case :
		
		Simplex tgtSimplex = new Simplex();
		constraintDomainVariables(tgtSimplex, appleMass, ConstraintType.EQUAL, 0);
		
		SimplexCase tgt = new SimplexCase(tgtSimplex);
		System.out.println("target case:");
		System.out.println(tgt);
		System.out.println();
		
		// Run the adaptation :
		
		SimplexCase tgtAdapted = RevisorCLC.adapt(srce, tgt, dk);
		System.out.println("target adapted:");
		System.out.println(tgtAdapted);
	}
	
	private static void constraintDomainVariables(Simplex s, Variable var, ConstraintType constraintType, double value) {
		LinearConstraint contr = new LinearConstraint();
		contr.setType(constraintType);
		contr.addTerm(var, new RealValue(1));
		contr.setOffset(new RealValue(value));
		s.addConstraint(contr);
	}
	
}
