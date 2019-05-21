package fr.loria.orpailleur.revisor.engine.revisorPL;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;

/**
 * @author Gabin PERSONENI
 * @author William PHILBERT
 */
public class TestRevisorPL {
	
	public static void main(final String args[]) throws InterpretationFunctionDomainException {
		example1();
		example2();
	}
	
	/**
	 * Print an adaptation example in the standard output.
	 * @param title - a title for the example.
	 * @param source - a PL formula representing the source.
	 * @param target - a PL formula representing the target.
	 * @param dk - a PL formula representing the domain knowledge.
	 * @param result - a PL formula representing the result.
	 */
	private static void printExample(final String title, final PLFormula source, final PLFormula target, final PLFormula dk, final PLFormula result) {
		String titleLine = "-- " + title + " --";
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < titleLine.length(); i++) {
			builder.append('-');
		}
		
		String separatorLine = builder.toString();
		
		System.out.println();
		System.out.println(separatorLine);
		System.out.println(titleLine);
		System.out.println(separatorLine);
		System.out.println();
		System.out.println("source : " + source);
		System.out.println("target : " + target);
		System.out.println("dk     : " + dk);
		System.out.println("result : " + result);
	}
	
	/**
	 * In this example, we have a recipe for an apple pie, but we want to make a
	 * non-apple pie. We know about 3 fruits : apples, peaches, and pears. We
	 * expect to get as a result a recipe for a pear pie or a peach pie.
	 */
	private static void example1() {
		// Reset the weight of every variable to 1.0
		RevisorPL.resetWeights();
		
		PLFormula source = RevisorPL.parseFormula("pie & apple & pie_shell & sugar");
		PLFormula target = RevisorPL.parseFormula("pie & !apple");
		PLFormula dk = RevisorPL.parseFormula("(pear | peach | apple > fruit) & (fruit > pear | peach | apple)");
		
		PLFormula result = RevisorPL.adapt(source, target, dk);
		printExample("Example 1 : apple pie", source, target, dk, result);
	}
	
	/**
	 * In this example, we have a recipe for a mint and chocolate cake. However
	 * we don't like the association of mint and chocolate
	 */
	private static void example2() {
		// Reset the weight of every variable to 1.0
		RevisorPL.resetWeights();
		
		// Set the weight of chocolate to 2.0
		RevisorPL.setWeight("chocolate", 2.0);
		
		PLFormula source = RevisorPL.parseFormula("mint & chocolate & cake");
		PLFormula target = RevisorPL.parseFormula("!(chocolate & mint)");
		PLFormula dk = PL.TRUE; // Any tautology
		
		PLFormula result = RevisorPL.adapt(source, target, dk);
		printExample("Example 2 : mint and chocolate cake", source, target, dk, result);
	}
	
}
