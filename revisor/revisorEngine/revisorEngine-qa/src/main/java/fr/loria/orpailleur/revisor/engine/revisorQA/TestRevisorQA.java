package fr.loria.orpailleur.revisor.engine.revisorQA;

import java.util.List;

import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeAlgebra;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintNetwork;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintScenario;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeVariableSubstitution;

/**
 * @author Valmi Dufout-Lussier
 * @author William Philbert
 */
public class TestRevisorQA {
	
	public static void main(String[] args) throws Exception {
		ICCBRpaperExample();
		KRpaperExample();
	}
	
	public static void ICCBRpaperExample() {
		System.out.println("ICCBRpaperExample:");
		
		String source_str = "low-water channel { ntpp } flood plain ; plot(maize) { ec } low-water channel";
		String target_str = "low-water channel { ntpp } flood plain";
		String dk_str = "plot(miscanthus) { dc, ec } flood plain";
		
		try {
			QualitativeAlgebra algebra = RevisorQA.loadAlgebra("rcc8");
			QualitativeConstraintNetwork psi = RevisorQA.parseFormula(source_str, algebra);
			QualitativeConstraintNetwork mu = RevisorQA.parseFormula(target_str, algebra);
			QualitativeConstraintNetwork dk = RevisorQA.parseFormula(dk_str, algebra);
			QualitativeVariableSubstitution subst = RevisorQA.prepareSubstitution("maize", "miscanthus");
			List<QualitativeConstraintScenario> solution = RevisorQA.adaptExhaustively(psi, mu, dk, subst);
			
			for(QualitativeConstraintScenario scenario : solution) {
				System.out.println("Result (d=" + scenario.getDistance() + "): " + scenario);
			}
		}
		catch(Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void KRpaperExample() {
		System.out.println("KRpaperExample:");
		
		String source_file = "./qa-example-data/kr-2014/no-pcqa-1/source.qcn";
		String target_file = "./qa-example-data/kr-2014/no-pcqa-1/target.qcn";
		String dk_file = "./qa-example-data/kr-2014/no-pcqa-1/dk.qcn";
		
		try {
			QualitativeAlgebra algebra = RevisorQA.loadAlgebra("allen");
			QualitativeConstraintNetwork psi = RevisorQA.parseFile(source_file, algebra);
			QualitativeConstraintNetwork mu = RevisorQA.parseFile(target_file, algebra);
			QualitativeConstraintNetwork dk = RevisorQA.parseFile(dk_file, algebra);
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
