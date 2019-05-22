package fr.loria.orpailleur.revisor.engine.revisorCLC;

import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.LinearizableDistanceBasedConservativeAdaptation;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexCase;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexDomainKnowledge;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint.ConstraintType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Mediator;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Simplex;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableAttributeValueMetricSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableDistance;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealVariable;

/**
 * @author Julien Cojan
 */
public class TestCBRadaptation {
	
	// Load the library :
	//	static {
	//		try {
	//			System.loadLibrary("glpkjava");
	//		}
	//		catch(UnsatisfiedLinkError e) {
	//			System.err.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
	//			System.exit(1);
	//		}
	//	}
	
	// Parameters :
	
	private static float appleSugarDensity = (float) 0.1;
	private static float pearSugarDensity = (float) 0.15;
	
	// Source case data :
	
	private static float sourceAppleMass = 300;
	private static float sourcePearMass = 0;
	private static float sourceDoughMass = 100;
	// static float sourceSugarMass = 70;
	private static float sourceSaccharoseMass = 40;
	
	// Distance coefficients :
	
	private static RealValue fruitMassVarCost = new RealValue(5);
	private static RealValue appleMassVarCost = new RealValue(1);
	private static RealValue pearMassVarCost = new RealValue(1);
	private static RealValue sugarMassVarCost = new RealValue(5);
	private static RealValue Saccharose = new RealValue(1);
	private static RealValue doughMassVarCost = new RealValue(1);
	
	// Problem variables :
	
	private static RealVariable fruitMass;
	private static RealVariable pearMass;
	private static RealVariable appleMass;
	private static RealVariable sugarMass;
	private static RealVariable saccharoseMass;
	private static RealVariable doughMass;
	
	// Objects used :
	
	private static LinearizableAttributeValueMetricSpace representationSpace;
	private static SimplexCase srce, tgt, ctxtSrce, ctxtTgt, tgtCompleted;
	private static SimplexDomainKnowledge dk;
	private static LinearizableDistanceBasedConservativeAdaptation adaptKnowledge;
	
	// Defines the representation Space and its variables.
	private static void buildSpace() {
		LinearizableDistance d = new LinearizableDistance();
		representationSpace = new LinearizableAttributeValueMetricSpace(d);
		
		fruitMass = new RealVariable("fruit mass");
		representationSpace.addDimention(fruitMass);
		d.addTerm(fruitMass, fruitMassVarCost);
		
		pearMass = new RealVariable("pear mass");
		representationSpace.addDimention(pearMass);
		d.addTerm(pearMass, pearMassVarCost);
		
		appleMass = new RealVariable("apple mass");
		representationSpace.addDimention(appleMass);
		d.addTerm(appleMass, appleMassVarCost);
		
		sugarMass = new RealVariable("sugar mass");
		representationSpace.addDimention(sugarMass);
		d.addTerm(sugarMass, sugarMassVarCost);
		
		saccharoseMass = new RealVariable("saccharose mass");
		representationSpace.addDimention(saccharoseMass);
		d.addTerm(saccharoseMass, Saccharose);
		
		doughMass = new RealVariable("dough mass");
		representationSpace.addDimention(doughMass);
		d.addTerm(doughMass, doughMassVarCost);
	}
	
	private static void buildSourceCase() {
		Simplex s = new Simplex();
		LinearConstraint srceConstraint;
		
		// pearMass = sourcePearMass
		srceConstraint = new LinearConstraint();
		srceConstraint.setType(ConstraintType.EQUAL);
		srceConstraint.setOffset(new RealValue(sourcePearMass));
		srceConstraint.addTerm(pearMass, new RealValue(1));
		s.addConstraint(srceConstraint);
		
		// appleMass = sourceAppleMass
		srceConstraint = new LinearConstraint();
		srceConstraint.setType(ConstraintType.EQUAL);
		srceConstraint.setOffset(new RealValue(sourceAppleMass));
		srceConstraint.addTerm(appleMass, new RealValue(1));
		s.addConstraint(srceConstraint);
		
		// doughMass = sourceDoughMass
		srceConstraint = new LinearConstraint();
		srceConstraint.setType(ConstraintType.EQUAL);
		srceConstraint.setOffset(new RealValue(sourceDoughMass));
		srceConstraint.addTerm(doughMass, new RealValue(1));
		s.addConstraint(srceConstraint);
		
		// saccharoseMass = sourceSaccharoseMass
		srceConstraint = new LinearConstraint();
		srceConstraint.setType(ConstraintType.EQUAL);
		srceConstraint.setOffset(new RealValue(sourceSaccharoseMass));
		srceConstraint.addTerm(saccharoseMass, new RealValue(1));
		s.addConstraint(srceConstraint);
		
		srce = new SimplexCase(s);
		System.out.println("source case:");
		System.out.println(srce);
		System.out.println();
	}
	
	private static void buildTargetCase() {
		Simplex s = new Simplex();
		LinearConstraint tgtConstraint;
		
		// appleMass = sourceAppleMass
		tgtConstraint = new LinearConstraint();
		tgtConstraint.setType(ConstraintType.EQUAL);
		tgtConstraint.setOffset(new RealValue(0));
		tgtConstraint.addTerm(appleMass, new RealValue(1));
		s.addConstraint(tgtConstraint);
		
		tgt = new SimplexCase(s);
		System.out.println("target case:");
		System.out.println(tgt);
		System.out.println();
	}
	
	private static void buildDomainKnowledge() {
		Simplex s = new Simplex();
		
		// variables range (all positive)
		constraintDomainVariables(s, fruitMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, pearMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, appleMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, sugarMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, saccharoseMass, ConstraintType.GEQ, 0);
		constraintDomainVariables(s, doughMass, ConstraintType.GEQ, 0);
		
		LinearConstraint constraint;
		
		// fruitMass = aplleMass + pearMass
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
		
		dk = new SimplexDomainKnowledge(s);
	}
	
	private static void constraintDomainVariables(Simplex s, RealVariable var, ConstraintType constraintType, float value) {
		LinearConstraint contr = new LinearConstraint();
		contr.setType(constraintType);
		contr.addTerm(var, new RealValue(1));
		contr.setOffset(new RealValue(value));
		s.addConstraint(contr);
	}
	
	private static void testContextualization() {
		ctxtSrce = dk.contextualize(srce);
		System.out.println("contextualized source case:");
		System.out.println(ctxtSrce);
		System.out.println();
		
		ctxtTgt = dk.contextualize(tgt);
		System.out.println("contextualized target case:");
		System.out.println(ctxtTgt);
		System.out.println();
	}
	
	private static void testAdaptation() {
		adaptKnowledge = new LinearizableDistanceBasedConservativeAdaptation(representationSpace);
		System.out.println("Adaptation begins NOW");
		tgtCompleted = adaptKnowledge.adapt(ctxtTgt, ctxtSrce);
		System.out.println("Adaptation completed with the following result: \n");
		System.out.println(tgtCompleted);
	}
	
	public static void main(String[] args) {
		//String s = System.getProperty("java.library.path");
		//System.setProperty("java.library.path", "/home/cojanjul/EclipseWorkspace/CBR2GLPK/lib/Unix32:" + s);
		//String s = System.getProperty("java.library.path");
		//String[] vs = s.split(";");
		//System.out.println("java.library.path:");
		//for(int i = 0; i < vs.length; i++) {
		//	System.out.println('\t' + vs[i]);
		//}
		
		buildSpace();
		buildSourceCase();
		buildTargetCase();
		buildDomainKnowledge();
		
		System.out.println("écriture de dk");
		System.out.println(dk);
		System.out.println("fin écriture de dk");
		
		testContextualization();
		Mediator.setSolver(Mediator.Solver.LPSOLVE);
		testAdaptation();
	}
	
}
