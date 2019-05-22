package fr.loria.orpailleur.revisor.engine.revisorQA;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeAlgebra;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintFormula;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintScenario;

/**
 * @author Valmi Dufout-Lussier
 */
public class RevisorQACLI {
	
	// Constants :
	
	private static final boolean VERBOSE = true;
	
	private static final int EXIT_OK = 0;
	private static final int EXIT_USAGE = 1;
	private static final int EXIT_FAIL = 2;
	
	// Main :
	
	public static void main(String[] args) {
		List<String> argv = Arrays.asList(args);
		
		if(argv.size() < 3 || argv.get(0).equals("PC") && argv.size() < 4) {
			printUsage();
			System.exit(EXIT_USAGE);
		}
		
		String algebra_str, source_str, target_str, dk_str;
		boolean use_dk = false;
		boolean use_pc = false;
		
		if(argv.get(0).equals("PC")) {
			use_pc = true;
			algebra_str = argv.get(1);
			source_str = argv.get(2);
			target_str = argv.get(3);
			
			if(argv.size() > 4) {
				use_dk = true;
				dk_str = argv.get(4);
			}
			else {
				dk_str = null;
			}
		}
		else {
			algebra_str = argv.get(0);
			source_str = argv.get(1);
			target_str = argv.get(2);
			
			if(argv.size() > 3) {
				use_dk = true;
				dk_str = argv.get(3);
			}
			else {
				dk_str = null;
			}
		}
		
		try {
			QualitativeAlgebra algebra = RevisorQA.loadAlgebra(algebra_str);
			QualitativeConstraintFormula source = load(source_str, algebra, use_pc);
			QualitativeConstraintFormula target = load(target_str, algebra, use_pc);
			QualitativeConstraintFormula dk = null;
			
			if(use_dk) {
				dk = load(dk_str, algebra, use_pc);
			}
			
			if(VERBOSE) {
				printInformation(source, target, dk, use_pc);
			}
			
			revise(source, target, dk);
		}
		catch(Exception e) {
			System.err.println("Revision failed");
			System.err.println(e.getMessage());
			System.exit(EXIT_FAIL);
		}
		
		System.exit(EXIT_OK);
	}
	
	// Methods :
	
	private static void printUsage() {
		System.err.println("Usage: java revisor_qa.RevisorQACLI ['PC'] <algebra> <psi> <mu>");
		System.err.println("  or:  java revisor_qa.RevisorQACLI ['PC'] <algebra> <source> <target> <knowledge>");
	}
	
	private static QualitativeConstraintFormula load(String file, QualitativeAlgebra algebra, boolean pc) throws Exception {
		QualitativeConstraintFormula qcf;
		
		if(pc) {
			qcf = RevisorQA.parseFilePC(file, algebra);
		}
		else {
			qcf = RevisorQA.parseFile(file, algebra);
		}
		
		return qcf;
	}
	
	private static void revise(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk) throws Exception {
		List<QualitativeConstraintScenario> solution;
		
		if(dk == null) {
			solution = RevisorQA.reviseExhaustively(source, target);
		}
		else {
			solution = RevisorQA.adaptExhaustively(source, target, dk);
		}
		
		printResult(solution);
	}
	
	private static void printResult(List<QualitativeConstraintScenario> solution) throws Exception {
		Iterator<QualitativeConstraintScenario> itr = solution.iterator();
		
		while(itr.hasNext()) {
			QualitativeConstraintScenario scenario = itr.next();
			System.out.println("Result (d=" + scenario.getDistance() + "): " + scenario);
		}
	}
	
	private static void printInformation(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk, boolean use_pc) throws Exception {
		boolean use_dk = (dk != null);
		
		if(use_pc && use_dk) {
			System.out.println("Adaptation using propositional closure: PSI & DK + MU & DK");
		}
		else if(use_pc && !use_dk) {
			System.out.println("Revision using propositional closure: PSI + MU");
		}
		else if(!use_pc && use_dk) {
			System.out.println("Adaptation without propositional closure: PSI & DK + MU & DK");
		}
		else {
			System.out.println("Revision without propositional closure: PSI + MU");
		}
		
		System.out.println("PSI = " + source);
		System.out.println(" MU = " + target);
		
		if(use_dk) {
			System.out.println(" DK = " + dk);
		}
	}
	
}
