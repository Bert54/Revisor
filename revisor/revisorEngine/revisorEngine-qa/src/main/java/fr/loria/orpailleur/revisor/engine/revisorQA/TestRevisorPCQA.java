package fr.loria.orpailleur.revisor.engine.revisorQA;

import java.util.List;

import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeAlgebra;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintFormula;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintScenario;

/**
 * @author Valmi Dufout-Lussier
 * @author William Philbert
 */
public class TestRevisorPCQA {
	
	public static void main(String[] args) throws Exception {
		example1();
	}
	
	public static void example1() {
		System.out.println("Example 1:");
		
		String source_file = "./qa-example-data/kr-2014/pcqa-1/source.qcn";
		String target_file = "./qa-example-data/kr-2014/pcqa-1/target.qcn";
		String dk_file = "./qa-example-data/kr-2014/pcqa-1/dk.qcn";
		
		try {
			QualitativeAlgebra algebra = RevisorQA.loadAlgebra("allen");
			QualitativeConstraintFormula psi = RevisorQA.parseFilePC(source_file, algebra);
			QualitativeConstraintFormula mu = RevisorQA.parseFilePC(target_file, algebra);
			QualitativeConstraintFormula dk = RevisorQA.parseFilePC(dk_file, algebra);
			List<QualitativeConstraintScenario> solution = RevisorQA.adaptExhaustively(psi, mu, dk);
			
			for(QualitativeConstraintScenario scenario : solution) {
				System.out.println("Result (d=" + scenario.getDistance() + "): " + scenario);
			}
		}
		catch(Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
