package fr.loria.orpailleur.revisor.engine.revisorPLAK;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.RuleSet;

/**
 * @author Gabin PERSONENI
 * @author Alice HERMANN
 * @author William PHILBERT
 */
public class TestRevisorPLAK {
	
	public static void main(final String args[]) {
		example1();
		example2();
		//testAllen();
	}
	
	/**
	 * Print an adaptation example in the standard output.
	 * @param title - a title for the example.
	 * @param source - a PL formula representing the source.
	 * @param target - a PL formula representing the target.
	 * @param dk - a PL formula representing the domain knowledge.
	 * @param result - a PL formula representing the result.
	 */
	private static void printExample(final String title, final PLFormula source, final PLFormula target, final PLFormula dk, final RuleSet rules, final PLFormula result) {
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
		System.out.println("rules  : " + rules);
		System.out.println("result : " + result);
	}
	
	/**
	 * A basic example using rules and different flip costs
	 */
	public static void example1() {
		RevisorPLAK.clearFlipCosts();
		
		PLFormula source = RevisorPLAK.parseFormula("a & b");
		PLFormula target = RevisorPLAK.parseFormula("!a & !c");
		PLFormula dk = RevisorPLAK.parseFormula("true");
		
		RevisorPLAK.setFlipCost("a", 2.0);
		
		RuleSet rules = new RuleSet(RevisorPLAK.li);
		rules.addRule("b : a *= c", 0.5);
		rules.addRule("b : c *= a", 0.5);
		
		PLFormula result = RevisorPLAK.adaptAK(source, target, dk, rules);
		printExample("Example 1 : a, b, c", source, target, dk, rules, result);
	}
	
	/**
	 * In this example, we have a recipe for an pear pie, but we want to make a
	 * non-pear pie. We know about 3 fruits : apples, pears and peaches. We
	 * expect to get as a result another pie recipe.
	 */
	public static void example2() {
		RevisorPLAK.clearFlipCosts();
		
		// source = pie & pie_shell & pear & sugar & egg
		// target = pie & !pear & !cinnamon & !egg
		// dk = (apple | peach | pear) <=> fruit
		PLFormula source = RevisorPLAK.parseFormula("pie & pie_shell & pear & sugar & egg");
		PLFormula target = RevisorPLAK.parseFormula("pie & !pear & !cinnamon & !egg");
		PLFormula dk = RevisorPLAK.pl.EQ(RevisorPLAK.parseFormula("fruit"), RevisorPLAK.parseFormula("(apple | peach | pear)"));
		
		RuleSet rules = new RuleSet(RevisorPLAK.li);
		
		// cake & egg ~> cake & banana
		rules.addRule("cake : egg *= banana", 0.3);
		
		// pie & egg ~> pie & flour & cider_vinegar
		rules.addRule("pie : egg *= flour & cider_vinegar", 0.3);
		
		// pear ~> peach
		rules.addRule(": pear *= peach", 0.7);
		
		// pear ~> apple & cinnamon
		rules.addRule(": pear *= apple & cinnamon", 0.3);
		
		// cinnamon ~> vanilla_sugar
		rules.addRule(": cinnamon *= vanilla_sugar", 0.3);
		
		// cinnamon ~> orange_blossom
		rules.addRule(": cinnamon *= orange_blossom", 0.3);
		
		PLFormula result = (RevisorPLAK.adaptAK(source, target, dk, rules));
		result = RevisorPLAK.simplifiedDNF(result);
		printExample("Example 2 : pear pie", source, target, dk, rules, result);
	}
	
	/**
	 * Valmi pour QA->PLAK
	 * Does not terminate, for now.
	 */
	public static void testAllen() {
		RevisorPLAK.clearFlipCosts();
		RuleSet rules = new RuleSet(RevisorPLAK.li);
		
		PLFormula dk = RevisorPLAK.parseFormula("( !Xk,j,b | !Xj,i,b | Xk,i,b ) & ( !Xk,i,b | !Xi,j,b | Xk,j,b ) & ( !Xj,k,b | !Xk,i,b | Xj,i,b ) & ( !Xj,i,b | !Xi,k,b | Xj,k,b ) & ( !Xi,k,b | !Xk,j,b | Xi,j,b ) & ( !Xi,j,b | !Xj,k,b | Xi,k,b ) & ( !Xk,j,b | !Xj,i,m | Xk,i,b ) & ( !Xk,i,b | !Xi,j,m | Xk,j,b ) & ( !Xj,k,b | !Xk,i,m | Xj,i,b ) & ( !Xj,i,b | !Xi,k,m | Xj,k,b ) & ( !Xi,k,b | !Xk,j,m | Xi,j,b ) & ( !Xi,j,b | !Xj,k,m | Xi,k,b ) & ( !Xk,j,b | !Xj,i,o | Xk,i,b ) & ( !Xk,i,b | !Xi,j,o | Xk,j,b ) & ( !Xj,k,b | !Xk,i,o | Xj,i,b ) & ( !Xj,i,b | !Xi,k,o | Xj,k,b ) & ( !Xi,k,b | !Xk,j,o | Xi,j,b ) & ( !Xi,j,b | !Xj,k,o | Xi,k,b ) & ( !Xk,j,b | !Xj,i,s | Xk,i,b ) & ( !Xk,i,b | !Xi,j,s | Xk,j,b ) & ( !Xj,k,b | !Xk,i,s | Xj,i,b ) & ( !Xj,i,b | !Xi,k,s | Xj,k,b ) & ( !Xi,k,b | !Xk,j,s | Xi,j,b ) & ( !Xi,j,b | !Xj,k,s | Xi,k,b ) & ( !Xk,j,b | !Xj,i,d | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,d | Xk,i,s ) & ( !Xk,i,b | !Xi,j,d | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,d | Xk,j,s ) & ( !Xj,k,b | !Xk,i,d | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,d | Xj,i,s ) & ( !Xj,i,b | !Xi,k,d | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,d | Xj,k,s ) & ( !Xi,k,b | !Xk,j,d | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,d | Xi,j,s ) & ( !Xi,j,b | !Xj,k,d | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,d | Xi,k,s ) & ( !Xk,j,b | !Xj,i,f | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,d | Xk,i,s ) & ( !Xk,i,b | !Xi,j,f | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,d | Xk,j,s ) & ( !Xj,k,b | !Xk,i,f | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,d | Xj,i,s ) & ( !Xj,i,b | !Xi,k,f | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,d | Xj,k,s ) & ( !Xi,k,b | !Xk,j,f | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,d | Xi,j,s ) & ( !Xi,j,b | !Xj,k,f | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,d | Xi,k,s ) & ( !Xk,j,b | !Xj,i,eq | Xk,i,b ) & ( !Xk,i,b | !Xi,j,eq | Xk,j,b ) & ( !Xj,k,b | !Xk,i,eq | Xj,i,b ) & ( !Xj,i,b | !Xi,k,eq | Xj,k,b ) & ( !Xi,k,b | !Xk,j,eq | Xi,j,b ) & ( !Xi,j,b | !Xj,k,eq | Xi,k,b ) & ( !Xk,j,b | !Xj,i,bi | Xk,i,b | Xk,i,bi | Xk,i,d | Xk,i,di | Xk,i,o | Xk,i,oi | Xk,i,m | Xk,i,mi | Xk,i,s | Xk,i,si | Xk,i,f | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,b | !Xi,j,bi | Xk,j,b | Xk,j,bi | Xk,j,d | Xk,j,di | Xk,j,o | Xk,j,oi | Xk,j,m | Xk,j,mi | Xk,j,s | Xk,j,si | Xk,j,f | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,b | !Xk,i,bi | Xj,i,b | Xj,i,bi | Xj,i,d | Xj,i,di | Xj,i,o | Xj,i,oi | Xj,i,m | Xj,i,mi | Xj,i,s | Xj,i,si | Xj,i,f | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,b | !Xi,k,bi | Xj,k,b | Xj,k,bi | Xj,k,d | Xj,k,di | Xj,k,o | Xj,k,oi | Xj,k,m | Xj,k,mi | Xj,k,s | Xj,k,si | Xj,k,f | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,b | !Xk,j,bi | Xi,j,b | Xi,j,bi | Xi,j,d | Xi,j,di | Xi,j,o | Xi,j,oi | Xi,j,m | Xi,j,mi | Xi,j,s | Xi,j,si | Xi,j,f | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,b | !Xj,k,bi | Xi,k,b | Xi,k,bi | Xi,k,d | Xi,k,di | Xi,k,o | Xi,k,oi | Xi,k,m | Xi,k,mi | Xi,k,s | Xi,k,si | Xi,k,f | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,b | !Xj,i,mi | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,d | Xk,i,s ) & ( !Xk,i,b | !Xi,j,mi | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,d | Xk,j,s ) & ( !Xj,k,b | !Xk,i,mi | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,d | Xj,i,s ) & ( !Xj,i,b | !Xi,k,mi | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,d | Xj,k,s ) & ( !Xi,k,b | !Xk,j,mi | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,d | Xi,j,s ) & ( !Xi,j,b | !Xj,k,mi | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,d | Xi,k,s ) & ( !Xk,j,b | !Xj,i,oi | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,d | Xk,i,s ) & ( !Xk,i,b | !Xi,j,oi | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,d | Xk,j,s ) & ( !Xj,k,b | !Xk,i,oi | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,d | Xj,i,s ) & ( !Xj,i,b | !Xi,k,oi | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,d | Xj,k,s ) & ( !Xi,k,b | !Xk,j,oi | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,d | Xi,j,s ) & ( !Xi,j,b | !Xj,k,oi | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,d | Xi,k,s ) & ( !Xk,j,b | !Xj,i,si | Xk,i,b ) & ( !Xk,i,b | !Xi,j,si | Xk,j,b ) & ( !Xj,k,b | !Xk,i,si | Xj,i,b ) & ( !Xj,i,b | !Xi,k,si | Xj,k,b ) & ( !Xi,k,b | !Xk,j,si | Xi,j,b ) & ( !Xi,j,b | !Xj,k,si | Xi,k,b ) & ( !Xk,j,b | !Xj,i,di | Xk,i,b ) & ( !Xk,i,b | !Xi,j,di | Xk,j,b ) & ( !Xj,k,b | !Xk,i,di | Xj,i,b ) & ( !Xj,i,b | !Xi,k,di | Xj,k,b ) & ( !Xi,k,b | !Xk,j,di | Xi,j,b ) & ( !Xi,j,b | !Xj,k,di | Xi,k,b ) & ( !Xk,j,b | !Xj,i,fi | Xk,i,b ) & ( !Xk,i,b | !Xi,j,fi | Xk,j,b ) & ( !Xj,k,b | !Xk,i,fi | Xj,i,b ) & ( !Xj,i,b | !Xi,k,fi | Xj,k,b ) & ( !Xi,k,b | !Xk,j,fi | Xi,j,b ) & ( !Xi,j,b | !Xj,k,fi | Xi,k,b ) & ( !Xk,j,m | !Xj,i,b | Xk,i,b ) & ( !Xk,i,m | !Xi,j,b | Xk,j,b ) & ( !Xj,k,m | !Xk,i,b | Xj,i,b ) & ( !Xj,i,m | !Xi,k,b | Xj,k,b ) & ( !Xi,k,m | !Xk,j,b | Xi,j,b ) & ( !Xi,j,m | !Xj,k,b | Xi,k,b ) & ( !Xk,j,m | !Xj,i,m | Xk,i,b ) & ( !Xk,i,m | !Xi,j,m | Xk,j,b ) & ( !Xj,k,m | !Xk,i,m | Xj,i,b ) & ( !Xj,i,m | !Xi,k,m | Xj,k,b ) & ( !Xi,k,m | !Xk,j,m | Xi,j,b ) & ( !Xi,j,m | !Xj,k,m | Xi,k,b ) & ( !Xk,j,m | !Xj,i,o | Xk,i,b ) & ( !Xk,i,m | !Xi,j,o | Xk,j,b ) & ( !Xj,k,m | !Xk,i,o | Xj,i,b ) & ( !Xj,i,m | !Xi,k,o | Xj,k,b ) & ( !Xi,k,m | !Xk,j,o | Xi,j,b ) & ( !Xi,j,m | !Xj,k,o | Xi,k,b ) & ( !Xk,j,m | !Xj,i,s | Xk,i,m ) & ( !Xk,i,m | !Xi,j,s | Xk,j,m ) & ( !Xj,k,m | !Xk,i,s | Xj,i,m ) & ( !Xj,i,m | !Xi,k,s | Xj,k,m ) & ( !Xi,k,m | !Xk,j,s | Xi,j,m ) & ( !Xi,j,m | !Xj,k,s | Xi,k,m ) & ( !Xk,j,m | !Xj,i,d | Xk,i,o | Xk,i,d | Xk,i,s ) & ( !Xk,i,m | !Xi,j,d | Xk,j,o | Xk,j,d | Xk,j,s ) & ( !Xj,k,m | !Xk,i,d | Xj,i,o | Xj,i,d | Xj,i,s ) & ( !Xj,i,m | !Xi,k,d | Xj,k,o | Xj,k,d | Xj,k,s ) & ( !Xi,k,m | !Xk,j,d | Xi,j,o | Xi,j,d | Xi,j,s ) & ( !Xi,j,m | !Xj,k,d | Xi,k,o | Xi,k,d | Xi,k,s ) & ( !Xk,j,m | !Xj,i,f | Xk,i,d | Xk,i,s | Xk,i,o ) & ( !Xk,i,m | !Xi,j,f | Xk,j,d | Xk,j,s | Xk,j,o ) & ( !Xj,k,m | !Xk,i,f | Xj,i,d | Xj,i,s | Xj,i,o ) & ( !Xj,i,m | !Xi,k,f | Xj,k,d | Xj,k,s | Xj,k,o ) & ( !Xi,k,m | !Xk,j,f | Xi,j,d | Xi,j,s | Xi,j,o ) & ( !Xi,j,m | !Xj,k,f | Xi,k,d | Xi,k,s | Xi,k,o ) & ( !Xk,j,m | !Xj,i,eq | Xk,i,m ) & ( !Xk,i,m | !Xi,j,eq | Xk,j,m ) & ( !Xj,k,m | !Xk,i,eq | Xj,i,m ) & ( !Xj,i,m | !Xi,k,eq | Xj,k,m ) & ( !Xi,k,m | !Xk,j,eq | Xi,j,m ) & ( !Xi,j,m | !Xj,k,eq | Xi,k,m ) & ( !Xk,j,m | !Xj,i,bi | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,di | Xk,i,si ) & ( !Xk,i,m | !Xi,j,bi | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,di | Xk,j,si ) & ( !Xj,k,m | !Xk,i,bi | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,di | Xj,i,si ) & ( !Xj,i,m | !Xi,k,bi | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,di | Xj,k,si ) & ( !Xi,k,m | !Xk,j,bi | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,di | Xi,j,si ) & ( !Xi,j,m | !Xj,k,bi | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,di | Xi,k,si ) & ( !Xk,j,m | !Xj,i,mi | Xk,i,f | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,m | !Xi,j,mi | Xk,j,f | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,m | !Xk,i,mi | Xj,i,f | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,m | !Xi,k,mi | Xj,k,f | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,m | !Xk,j,mi | Xi,j,f | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,m | !Xj,k,mi | Xi,k,f | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,m | !Xj,i,oi | Xk,i,o | Xk,i,d | Xk,i,s ) & ( !Xk,i,m | !Xi,j,oi | Xk,j,o | Xk,j,d | Xk,j,s ) & ( !Xj,k,m | !Xk,i,oi | Xj,i,o | Xj,i,d | Xj,i,s ) & ( !Xj,i,m | !Xi,k,oi | Xj,k,o | Xj,k,d | Xj,k,s ) & ( !Xi,k,m | !Xk,j,oi | Xi,j,o | Xi,j,d | Xi,j,s ) & ( !Xi,j,m | !Xj,k,oi | Xi,k,o | Xi,k,d | Xi,k,s ) & ( !Xk,j,m | !Xj,i,si | Xk,i,m ) & ( !Xk,i,m | !Xi,j,si | Xk,j,m ) & ( !Xj,k,m | !Xk,i,si | Xj,i,m ) & ( !Xj,i,m | !Xi,k,si | Xj,k,m ) & ( !Xi,k,m | !Xk,j,si | Xi,j,m ) & ( !Xi,j,m | !Xj,k,si | Xi,k,m ) & ( !Xk,j,m | !Xj,i,di | Xk,i,b ) & ( !Xk,i,m | !Xi,j,di | Xk,j,b ) & ( !Xj,k,m | !Xk,i,di | Xj,i,b ) & ( !Xj,i,m | !Xi,k,di | Xj,k,b ) & ( !Xi,k,m | !Xk,j,di | Xi,j,b ) & ( !Xi,j,m | !Xj,k,di | Xi,k,b ) & ( !Xk,j,m | !Xj,i,fi | Xk,i,b ) & ( !Xk,i,m | !Xi,j,fi | Xk,j,b ) & ( !Xj,k,m | !Xk,i,fi | Xj,i,b ) & ( !Xj,i,m | !Xi,k,fi | Xj,k,b ) & ( !Xi,k,m | !Xk,j,fi | Xi,j,b ) & ( !Xi,j,m | !Xj,k,fi | Xi,k,b ) & ( !Xk,j,o | !Xj,i,b | Xk,i,b ) & ( !Xk,i,o | !Xi,j,b | Xk,j,b ) & ( !Xj,k,o | !Xk,i,b | Xj,i,b ) & ( !Xj,i,o | !Xi,k,b | Xj,k,b ) & ( !Xi,k,o | !Xk,j,b | Xi,j,b ) & ( !Xi,j,o | !Xj,k,b | Xi,k,b ) & ( !Xk,j,o | !Xj,i,m | Xk,i,b ) & ( !Xk,i,o | !Xi,j,m | Xk,j,b ) & ( !Xj,k,o | !Xk,i,m | Xj,i,b ) & ( !Xj,i,o | !Xi,k,m | Xj,k,b ) & ( !Xi,k,o | !Xk,j,m | Xi,j,b ) & ( !Xi,j,o | !Xj,k,m | Xi,k,b ) & ( !Xk,j,o | !Xj,i,o | Xk,i,b | Xk,i,o | Xk,i,m ) & ( !Xk,i,o | !Xi,j,o | Xk,j,b | Xk,j,o | Xk,j,m ) & ( !Xj,k,o | !Xk,i,o | Xj,i,b | Xj,i,o | Xj,i,m ) & ( !Xj,i,o | !Xi,k,o | Xj,k,b | Xj,k,o | Xj,k,m ) & ( !Xi,k,o | !Xk,j,o | Xi,j,b | Xi,j,o | Xi,j,m ) & ( !Xi,j,o | !Xj,k,o | Xi,k,b | Xi,k,o | Xi,k,m ) & ( !Xk,j,o | !Xj,i,s | Xk,i,o ) & ( !Xk,i,o | !Xi,j,s | Xk,j,o ) & ( !Xj,k,o | !Xk,i,s | Xj,i,o ) & ( !Xj,i,o | !Xi,k,s | Xj,k,o ) & ( !Xi,k,o | !Xk,j,s | Xi,j,o ) & ( !Xi,j,o | !Xj,k,s | Xi,k,o ) & ( !Xk,j,o | !Xj,i,d | Xk,i,o | Xk,i,d | Xk,i,s ) & ( !Xk,i,o | !Xi,j,d | Xk,j,o | Xk,j,d | Xk,j,s ) & ( !Xj,k,o | !Xk,i,d | Xj,i,o | Xj,i,d | Xj,i,s ) & ( !Xj,i,o | !Xi,k,d | Xj,k,o | Xj,k,d | Xj,k,s ) & ( !Xi,k,o | !Xk,j,d | Xi,j,o | Xi,j,d | Xi,j,s ) & ( !Xi,j,o | !Xj,k,d | Xi,k,o | Xi,k,d | Xi,k,s ) & ( !Xk,j,o | !Xj,i,f | Xk,i,d | Xk,i,s | Xk,i,o ) & ( !Xk,i,o | !Xi,j,f | Xk,j,d | Xk,j,s | Xk,j,o ) & ( !Xj,k,o | !Xk,i,f | Xj,i,d | Xj,i,s | Xj,i,o ) & ( !Xj,i,o | !Xi,k,f | Xj,k,d | Xj,k,s | Xj,k,o ) & ( !Xi,k,o | !Xk,j,f | Xi,j,d | Xi,j,s | Xi,j,o ) & ( !Xi,j,o | !Xj,k,f | Xi,k,d | Xi,k,s | Xi,k,o ) & ( !Xk,j,o | !Xj,i,eq | Xk,i,o ) & ( !Xk,i,o | !Xi,j,eq | Xk,j,o ) & ( !Xj,k,o | !Xk,i,eq | Xj,i,o ) & ( !Xj,i,o | !Xi,k,eq | Xj,k,o ) & ( !Xi,k,o | !Xk,j,eq | Xi,j,o ) & ( !Xi,j,o | !Xj,k,eq | Xi,k,o ) & ( !Xk,j,o | !Xj,i,bi | Xk,i,bi | Xk,i,oi | Xk,i,di | Xk,i,mi | Xk,i,si ) & ( !Xk,i,o | !Xi,j,bi | Xk,j,bi | Xk,j,oi | Xk,j,di | Xk,j,mi | Xk,j,si ) & ( !Xj,k,o | !Xk,i,bi | Xj,i,bi | Xj,i,oi | Xj,i,di | Xj,i,mi | Xj,i,si ) & ( !Xj,i,o | !Xi,k,bi | Xj,k,bi | Xj,k,oi | Xj,k,di | Xj,k,mi | Xj,k,si ) & ( !Xi,k,o | !Xk,j,bi | Xi,j,bi | Xi,j,oi | Xi,j,di | Xi,j,mi | Xi,j,si ) & ( !Xi,j,o | !Xj,k,bi | Xi,k,bi | Xi,k,oi | Xi,k,di | Xi,k,mi | Xi,k,si ) & ( !Xk,j,o | !Xj,i,mi | Xk,i,oi | Xk,i,di | Xk,i,si ) & ( !Xk,i,o | !Xi,j,mi | Xk,j,oi | Xk,j,di | Xk,j,si ) & ( !Xj,k,o | !Xk,i,mi | Xj,i,oi | Xj,i,di | Xj,i,si ) & ( !Xj,i,o | !Xi,k,mi | Xj,k,oi | Xj,k,di | Xj,k,si ) & ( !Xi,k,o | !Xk,j,mi | Xi,j,oi | Xi,j,di | Xi,j,si ) & ( !Xi,j,o | !Xj,k,mi | Xi,k,oi | Xi,k,di | Xi,k,si ) & ( !Xk,j,o | !Xj,i,oi | Xk,i,o | Xk,i,oi | Xk,i,d | Xk,i,s | Xk,i,f | Xk,i,di | Xk,i,si | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,o | !Xi,j,oi | Xk,j,o | Xk,j,oi | Xk,j,d | Xk,j,s | Xk,j,f | Xk,j,di | Xk,j,si | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,o | !Xk,i,oi | Xj,i,o | Xj,i,oi | Xj,i,d | Xj,i,s | Xj,i,f | Xj,i,di | Xj,i,si | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,o | !Xi,k,oi | Xj,k,o | Xj,k,oi | Xj,k,d | Xj,k,s | Xj,k,f | Xj,k,di | Xj,k,si | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,o | !Xk,j,oi | Xi,j,o | Xi,j,oi | Xi,j,d | Xi,j,s | Xi,j,f | Xi,j,di | Xi,j,si | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,o | !Xj,k,oi | Xi,k,o | Xi,k,oi | Xi,k,d | Xi,k,s | Xi,k,f | Xi,k,di | Xi,k,si | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,o | !Xj,i,si | Xk,i,di | Xk,i,fi | Xk,i,o ) & ( !Xk,i,o | !Xi,j,si | Xk,j,di | Xk,j,fi | Xk,j,o ) & ( !Xj,k,o | !Xk,i,si | Xj,i,di | Xj,i,fi | Xj,i,o ) & ( !Xj,i,o | !Xi,k,si | Xj,k,di | Xj,k,fi | Xj,k,o ) & ( !Xi,k,o | !Xk,j,si | Xi,j,di | Xi,j,fi | Xi,j,o ) & ( !Xi,j,o | !Xj,k,si | Xi,k,di | Xi,k,fi | Xi,k,o ) & ( !Xk,j,o | !Xj,i,di | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,di | Xk,i,fi ) & ( !Xk,i,o | !Xi,j,di | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,di | Xk,j,fi ) & ( !Xj,k,o | !Xk,i,di | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,di | Xj,i,fi ) & ( !Xj,i,o | !Xi,k,di | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,di | Xj,k,fi ) & ( !Xi,k,o | !Xk,j,di | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,di | Xi,j,fi ) & ( !Xi,j,o | !Xj,k,di | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,di | Xi,k,fi ) & ( !Xk,j,o | !Xj,i,fi | Xk,i,b | Xk,i,o | Xk,i,m ) & ( !Xk,i,o | !Xi,j,fi | Xk,j,b | Xk,j,o | Xk,j,m ) & ( !Xj,k,o | !Xk,i,fi | Xj,i,b | Xj,i,o | Xj,i,m ) & ( !Xj,i,o | !Xi,k,fi | Xj,k,b | Xj,k,o | Xj,k,m ) & ( !Xi,k,o | !Xk,j,fi | Xi,j,b | Xi,j,o | Xi,j,m ) & ( !Xi,j,o | !Xj,k,fi | Xi,k,b | Xi,k,o | Xi,k,m ) & ( !Xk,j,s | !Xj,i,b | Xk,i,b ) & ( !Xk,i,s | !Xi,j,b | Xk,j,b ) & ( !Xj,k,s | !Xk,i,b | Xj,i,b ) & ( !Xj,i,s | !Xi,k,b | Xj,k,b ) & ( !Xi,k,s | !Xk,j,b | Xi,j,b ) & ( !Xi,j,s | !Xj,k,b | Xi,k,b ) & ( !Xk,j,s | !Xj,i,m | Xk,i,b ) & ( !Xk,i,s | !Xi,j,m | Xk,j,b ) & ( !Xj,k,s | !Xk,i,m | Xj,i,b ) & ( !Xj,i,s | !Xi,k,m | Xj,k,b ) & ( !Xi,k,s | !Xk,j,m | Xi,j,b ) & ( !Xi,j,s | !Xj,k,m | Xi,k,b ) & ( !Xk,j,s | !Xj,i,o | Xk,i,b | Xk,i,o | Xk,i,m ) & ( !Xk,i,s | !Xi,j,o | Xk,j,b | Xk,j,o | Xk,j,m ) & ( !Xj,k,s | !Xk,i,o | Xj,i,b | Xj,i,o | Xj,i,m ) & ( !Xj,i,s | !Xi,k,o | Xj,k,b | Xj,k,o | Xj,k,m ) & ( !Xi,k,s | !Xk,j,o | Xi,j,b | Xi,j,o | Xi,j,m ) & ( !Xi,j,s | !Xj,k,o | Xi,k,b | Xi,k,o | Xi,k,m ) & ( !Xk,j,s | !Xj,i,s | Xk,i,s ) & ( !Xk,i,s | !Xi,j,s | Xk,j,s ) & ( !Xj,k,s | !Xk,i,s | Xj,i,s ) & ( !Xj,i,s | !Xi,k,s | Xj,k,s ) & ( !Xi,k,s | !Xk,j,s | Xi,j,s ) & ( !Xi,j,s | !Xj,k,s | Xi,k,s ) & ( !Xk,j,s | !Xj,i,d | Xk,i,d ) & ( !Xk,i,s | !Xi,j,d | Xk,j,d ) & ( !Xj,k,s | !Xk,i,d | Xj,i,d ) & ( !Xj,i,s | !Xi,k,d | Xj,k,d ) & ( !Xi,k,s | !Xk,j,d | Xi,j,d ) & ( !Xi,j,s | !Xj,k,d | Xi,k,d ) & ( !Xk,j,s | !Xj,i,f | Xk,i,d ) & ( !Xk,i,s | !Xi,j,f | Xk,j,d ) & ( !Xj,k,s | !Xk,i,f | Xj,i,d ) & ( !Xj,i,s | !Xi,k,f | Xj,k,d ) & ( !Xi,k,s | !Xk,j,f | Xi,j,d ) & ( !Xi,j,s | !Xj,k,f | Xi,k,d ) & ( !Xk,j,s | !Xj,i,eq | Xk,i,s ) & ( !Xk,i,s | !Xi,j,eq | Xk,j,s ) & ( !Xj,k,s | !Xk,i,eq | Xj,i,s ) & ( !Xj,i,s | !Xi,k,eq | Xj,k,s ) & ( !Xi,k,s | !Xk,j,eq | Xi,j,s ) & ( !Xi,j,s | !Xj,k,eq | Xi,k,s ) & ( !Xk,j,s | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,s | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,s | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,s | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,s | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,s | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,s | !Xj,i,mi | Xk,i,mi ) & ( !Xk,i,s | !Xi,j,mi | Xk,j,mi ) & ( !Xj,k,s | !Xk,i,mi | Xj,i,mi ) & ( !Xj,i,s | !Xi,k,mi | Xj,k,mi ) & ( !Xi,k,s | !Xk,j,mi | Xi,j,mi ) & ( !Xi,j,s | !Xj,k,mi | Xi,k,mi ) & ( !Xk,j,s | !Xj,i,oi | Xk,i,oi | Xk,i,d | Xk,i,f ) & ( !Xk,i,s | !Xi,j,oi | Xk,j,oi | Xk,j,d | Xk,j,f ) & ( !Xj,k,s | !Xk,i,oi | Xj,i,oi | Xj,i,d | Xj,i,f ) & ( !Xj,i,s | !Xi,k,oi | Xj,k,oi | Xj,k,d | Xj,k,f ) & ( !Xi,k,s | !Xk,j,oi | Xi,j,oi | Xi,j,d | Xi,j,f ) & ( !Xi,j,s | !Xj,k,oi | Xi,k,oi | Xi,k,d | Xi,k,f ) & ( !Xk,j,s | !Xj,i,si | Xk,i,s | Xk,i,si | Xk,i,eq ) & ( !Xk,i,s | !Xi,j,si | Xk,j,s | Xk,j,si | Xk,j,eq ) & ( !Xj,k,s | !Xk,i,si | Xj,i,s | Xj,i,si | Xj,i,eq ) & ( !Xj,i,s | !Xi,k,si | Xj,k,s | Xj,k,si | Xj,k,eq ) & ( !Xi,k,s | !Xk,j,si | Xi,j,s | Xi,j,si | Xi,j,eq ) & ( !Xi,j,s | !Xj,k,si | Xi,k,s | Xi,k,si | Xi,k,eq ) & ( !Xk,j,s | !Xj,i,di | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,di | Xk,i,fi ) & ( !Xk,i,s | !Xi,j,di | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,di | Xk,j,fi ) & ( !Xj,k,s | !Xk,i,di | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,di | Xj,i,fi ) & ( !Xj,i,s | !Xi,k,di | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,di | Xj,k,fi ) & ( !Xi,k,s | !Xk,j,di | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,di | Xi,j,fi ) & ( !Xi,j,s | !Xj,k,di | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,di | Xi,k,fi ) & ( !Xk,j,s | !Xj,i,fi | Xk,i,b | Xk,i,m | Xk,i,o ) & ( !Xk,i,s | !Xi,j,fi | Xk,j,b | Xk,j,m | Xk,j,o ) & ( !Xj,k,s | !Xk,i,fi | Xj,i,b | Xj,i,m | Xj,i,o ) & ( !Xj,i,s | !Xi,k,fi | Xj,k,b | Xj,k,m | Xj,k,o ) & ( !Xi,k,s | !Xk,j,fi | Xi,j,b | Xi,j,m | Xi,j,o ) & ( !Xi,j,s | !Xj,k,fi | Xi,k,b | Xi,k,m | Xi,k,o ) & ( !Xk,j,d | !Xj,i,b | Xk,i,b ) & ( !Xk,i,d | !Xi,j,b | Xk,j,b ) & ( !Xj,k,d | !Xk,i,b | Xj,i,b ) & ( !Xj,i,d | !Xi,k,b | Xj,k,b ) & ( !Xi,k,d | !Xk,j,b | Xi,j,b ) & ( !Xi,j,d | !Xj,k,b | Xi,k,b ) & ( !Xk,j,d | !Xj,i,m | Xk,i,b ) & ( !Xk,i,d | !Xi,j,m | Xk,j,b ) & ( !Xj,k,d | !Xk,i,m | Xj,i,b ) & ( !Xj,i,d | !Xi,k,m | Xj,k,b ) & ( !Xi,k,d | !Xk,j,m | Xi,j,b ) & ( !Xi,j,d | !Xj,k,m | Xi,k,b ) & ( !Xk,j,d | !Xj,i,o | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,d | Xk,i,s ) & ( !Xk,i,d | !Xi,j,o | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,d | Xk,j,s ) & ( !Xj,k,d | !Xk,i,o | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,d | Xj,i,s ) & ( !Xj,i,d | !Xi,k,o | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,d | Xj,k,s ) & ( !Xi,k,d | !Xk,j,o | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,d | Xi,j,s ) & ( !Xi,j,d | !Xj,k,o | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,d | Xi,k,s ) & ( !Xk,j,d | !Xj,i,s | Xk,i,d ) & ( !Xk,i,d | !Xi,j,s | Xk,j,d ) & ( !Xj,k,d | !Xk,i,s | Xj,i,d ) & ( !Xj,i,d | !Xi,k,s | Xj,k,d ) & ( !Xi,k,d | !Xk,j,s | Xi,j,d ) & ( !Xi,j,d | !Xj,k,s | Xi,k,d ) & ( !Xk,j,d | !Xj,i,d | Xk,i,d ) & ( !Xk,i,d | !Xi,j,d | Xk,j,d ) & ( !Xj,k,d | !Xk,i,d | Xj,i,d ) & ( !Xj,i,d | !Xi,k,d | Xj,k,d ) & ( !Xi,k,d | !Xk,j,d | Xi,j,d ) & ( !Xi,j,d | !Xj,k,d | Xi,k,d ) & ( !Xk,j,d | !Xj,i,f | Xk,i,d ) & ( !Xk,i,d | !Xi,j,f | Xk,j,d ) & ( !Xj,k,d | !Xk,i,f | Xj,i,d ) & ( !Xj,i,d | !Xi,k,f | Xj,k,d ) & ( !Xi,k,d | !Xk,j,f | Xi,j,d ) & ( !Xi,j,d | !Xj,k,f | Xi,k,d ) & ( !Xk,j,d | !Xj,i,eq | Xk,i,d ) & ( !Xk,i,d | !Xi,j,eq | Xk,j,d ) & ( !Xj,k,d | !Xk,i,eq | Xj,i,d ) & ( !Xj,i,d | !Xi,k,eq | Xj,k,d ) & ( !Xi,k,d | !Xk,j,eq | Xi,j,d ) & ( !Xi,j,d | !Xj,k,eq | Xi,k,d ) & ( !Xk,j,d | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,d | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,d | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,d | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,d | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,d | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,d | !Xj,i,mi | Xk,i,bi ) & ( !Xk,i,d | !Xi,j,mi | Xk,j,bi ) & ( !Xj,k,d | !Xk,i,mi | Xj,i,bi ) & ( !Xj,i,d | !Xi,k,mi | Xj,k,bi ) & ( !Xi,k,d | !Xk,j,mi | Xi,j,bi ) & ( !Xi,j,d | !Xj,k,mi | Xi,k,bi ) & ( !Xk,j,d | !Xj,i,oi | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,d | Xk,i,f ) & ( !Xk,i,d | !Xi,j,oi | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,d | Xk,j,f ) & ( !Xj,k,d | !Xk,i,oi | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,d | Xj,i,f ) & ( !Xj,i,d | !Xi,k,oi | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,d | Xj,k,f ) & ( !Xi,k,d | !Xk,j,oi | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,d | Xi,j,f ) & ( !Xi,j,d | !Xj,k,oi | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,d | Xi,k,f ) & ( !Xk,j,d | !Xj,i,si | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,d | Xk,i,f ) & ( !Xk,i,d | !Xi,j,si | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,d | Xk,j,f ) & ( !Xj,k,d | !Xk,i,si | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,d | Xj,i,f ) & ( !Xj,i,d | !Xi,k,si | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,d | Xj,k,f ) & ( !Xi,k,d | !Xk,j,si | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,d | Xi,j,f ) & ( !Xi,j,d | !Xj,k,si | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,d | Xi,k,f ) & ( !Xk,j,d | !Xj,i,di | Xk,i,b | Xk,i,bi | Xk,i,d | Xk,i,di | Xk,i,o | Xk,i,oi | Xk,i,m | Xk,i,mi | Xk,i,s | Xk,i,si | Xk,i,f | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,d | !Xi,j,di | Xk,j,b | Xk,j,bi | Xk,j,d | Xk,j,di | Xk,j,o | Xk,j,oi | Xk,j,m | Xk,j,mi | Xk,j,s | Xk,j,si | Xk,j,f | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,d | !Xk,i,di | Xj,i,b | Xj,i,bi | Xj,i,d | Xj,i,di | Xj,i,o | Xj,i,oi | Xj,i,m | Xj,i,mi | Xj,i,s | Xj,i,si | Xj,i,f | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,d | !Xi,k,di | Xj,k,b | Xj,k,bi | Xj,k,d | Xj,k,di | Xj,k,o | Xj,k,oi | Xj,k,m | Xj,k,mi | Xj,k,s | Xj,k,si | Xj,k,f | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,d | !Xk,j,di | Xi,j,b | Xi,j,bi | Xi,j,d | Xi,j,di | Xi,j,o | Xi,j,oi | Xi,j,m | Xi,j,mi | Xi,j,s | Xi,j,si | Xi,j,f | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,d | !Xj,k,di | Xi,k,b | Xi,k,bi | Xi,k,d | Xi,k,di | Xi,k,o | Xi,k,oi | Xi,k,m | Xi,k,mi | Xi,k,s | Xi,k,si | Xi,k,f | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,d | !Xj,i,fi | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,d | Xk,i,s ) & ( !Xk,i,d | !Xi,j,fi | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,d | Xk,j,s ) & ( !Xj,k,d | !Xk,i,fi | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,d | Xj,i,s ) & ( !Xj,i,d | !Xi,k,fi | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,d | Xj,k,s ) & ( !Xi,k,d | !Xk,j,fi | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,d | Xi,j,s ) & ( !Xi,j,d | !Xj,k,fi | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,d | Xi,k,s ) & ( !Xk,j,f | !Xj,i,b | Xk,i,b ) & ( !Xk,i,f | !Xi,j,b | Xk,j,b ) & ( !Xj,k,f | !Xk,i,b | Xj,i,b ) & ( !Xj,i,f | !Xi,k,b | Xj,k,b ) & ( !Xi,k,f | !Xk,j,b | Xi,j,b ) & ( !Xi,j,f | !Xj,k,b | Xi,k,b ) & ( !Xk,j,f | !Xj,i,m | Xk,i,m ) & ( !Xk,i,f | !Xi,j,m | Xk,j,m ) & ( !Xj,k,f | !Xk,i,m | Xj,i,m ) & ( !Xj,i,f | !Xi,k,m | Xj,k,m ) & ( !Xi,k,f | !Xk,j,m | Xi,j,m ) & ( !Xi,j,f | !Xj,k,m | Xi,k,m ) & ( !Xk,j,f | !Xj,i,o | Xk,i,o | Xk,i,d | Xk,i,s ) & ( !Xk,i,f | !Xi,j,o | Xk,j,o | Xk,j,d | Xk,j,s ) & ( !Xj,k,f | !Xk,i,o | Xj,i,o | Xj,i,d | Xj,i,s ) & ( !Xj,i,f | !Xi,k,o | Xj,k,o | Xj,k,d | Xj,k,s ) & ( !Xi,k,f | !Xk,j,o | Xi,j,o | Xi,j,d | Xi,j,s ) & ( !Xi,j,f | !Xj,k,o | Xi,k,o | Xi,k,d | Xi,k,s ) & ( !Xk,j,f | !Xj,i,s | Xk,i,d ) & ( !Xk,i,f | !Xi,j,s | Xk,j,d ) & ( !Xj,k,f | !Xk,i,s | Xj,i,d ) & ( !Xj,i,f | !Xi,k,s | Xj,k,d ) & ( !Xi,k,f | !Xk,j,s | Xi,j,d ) & ( !Xi,j,f | !Xj,k,s | Xi,k,d ) & ( !Xk,j,f | !Xj,i,d | Xk,i,d ) & ( !Xk,i,f | !Xi,j,d | Xk,j,d ) & ( !Xj,k,f | !Xk,i,d | Xj,i,d ) & ( !Xj,i,f | !Xi,k,d | Xj,k,d ) & ( !Xi,k,f | !Xk,j,d | Xi,j,d ) & ( !Xi,j,f | !Xj,k,d | Xi,k,d ) & ( !Xk,j,f | !Xj,i,f | Xk,i,f ) & ( !Xk,i,f | !Xi,j,f | Xk,j,f ) & ( !Xj,k,f | !Xk,i,f | Xj,i,f ) & ( !Xj,i,f | !Xi,k,f | Xj,k,f ) & ( !Xi,k,f | !Xk,j,f | Xi,j,f ) & ( !Xi,j,f | !Xj,k,f | Xi,k,f ) & ( !Xk,j,f | !Xj,i,eq | Xk,i,f ) & ( !Xk,i,f | !Xi,j,eq | Xk,j,f ) & ( !Xj,k,f | !Xk,i,eq | Xj,i,f ) & ( !Xj,i,f | !Xi,k,eq | Xj,k,f ) & ( !Xi,k,f | !Xk,j,eq | Xi,j,f ) & ( !Xi,j,f | !Xj,k,eq | Xi,k,f ) & ( !Xk,j,f | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,f | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,f | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,f | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,f | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,f | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,f | !Xj,i,mi | Xk,i,bi ) & ( !Xk,i,f | !Xi,j,mi | Xk,j,bi ) & ( !Xj,k,f | !Xk,i,mi | Xj,i,bi ) & ( !Xj,i,f | !Xi,k,mi | Xj,k,bi ) & ( !Xi,k,f | !Xk,j,mi | Xi,j,bi ) & ( !Xi,j,f | !Xj,k,mi | Xi,k,bi ) & ( !Xk,j,f | !Xj,i,oi | Xk,i,bi | Xk,i,oi | Xk,i,mi ) & ( !Xk,i,f | !Xi,j,oi | Xk,j,bi | Xk,j,oi | Xk,j,mi ) & ( !Xj,k,f | !Xk,i,oi | Xj,i,bi | Xj,i,oi | Xj,i,mi ) & ( !Xj,i,f | !Xi,k,oi | Xj,k,bi | Xj,k,oi | Xj,k,mi ) & ( !Xi,k,f | !Xk,j,oi | Xi,j,bi | Xi,j,oi | Xi,j,mi ) & ( !Xi,j,f | !Xj,k,oi | Xi,k,bi | Xi,k,oi | Xi,k,mi ) & ( !Xk,j,f | !Xj,i,si | Xk,i,bi | Xk,i,oi | Xk,i,mi ) & ( !Xk,i,f | !Xi,j,si | Xk,j,bi | Xk,j,oi | Xk,j,mi ) & ( !Xj,k,f | !Xk,i,si | Xj,i,bi | Xj,i,oi | Xj,i,mi ) & ( !Xj,i,f | !Xi,k,si | Xj,k,bi | Xj,k,oi | Xj,k,mi ) & ( !Xi,k,f | !Xk,j,si | Xi,j,bi | Xi,j,oi | Xi,j,mi ) & ( !Xi,j,f | !Xj,k,si | Xi,k,bi | Xi,k,oi | Xi,k,mi ) & ( !Xk,j,f | !Xj,i,di | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,di | Xk,i,si ) & ( !Xk,i,f | !Xi,j,di | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,di | Xk,j,si ) & ( !Xj,k,f | !Xk,i,di | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,di | Xj,i,si ) & ( !Xj,i,f | !Xi,k,di | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,di | Xj,k,si ) & ( !Xi,k,f | !Xk,j,di | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,di | Xi,j,si ) & ( !Xi,j,f | !Xj,k,di | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,di | Xi,k,si ) & ( !Xk,j,f | !Xj,i,fi | Xk,i,f | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,f | !Xi,j,fi | Xk,j,f | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,f | !Xk,i,fi | Xj,i,f | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,f | !Xi,k,fi | Xj,k,f | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,f | !Xk,j,fi | Xi,j,f | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,f | !Xj,k,fi | Xi,k,f | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,eq | !Xj,i,b | Xk,i,b ) & ( !Xk,i,eq | !Xi,j,b | Xk,j,b ) & ( !Xj,k,eq | !Xk,i,b | Xj,i,b ) & ( !Xj,i,eq | !Xi,k,b | Xj,k,b ) & ( !Xi,k,eq | !Xk,j,b | Xi,j,b ) & ( !Xi,j,eq | !Xj,k,b | Xi,k,b ) & ( !Xk,j,eq | !Xj,i,m | Xk,i,m ) & ( !Xk,i,eq | !Xi,j,m | Xk,j,m ) & ( !Xj,k,eq | !Xk,i,m | Xj,i,m ) & ( !Xj,i,eq | !Xi,k,m | Xj,k,m ) & ( !Xi,k,eq | !Xk,j,m | Xi,j,m ) & ( !Xi,j,eq | !Xj,k,m | Xi,k,m ) & ( !Xk,j,eq | !Xj,i,o | Xk,i,o ) & ( !Xk,i,eq | !Xi,j,o | Xk,j,o ) & ( !Xj,k,eq | !Xk,i,o | Xj,i,o ) & ( !Xj,i,eq | !Xi,k,o | Xj,k,o ) & ( !Xi,k,eq | !Xk,j,o | Xi,j,o ) & ( !Xi,j,eq | !Xj,k,o | Xi,k,o ) & ( !Xk,j,eq | !Xj,i,s | Xk,i,s ) & ( !Xk,i,eq | !Xi,j,s | Xk,j,s ) & ( !Xj,k,eq | !Xk,i,s | Xj,i,s ) & ( !Xj,i,eq | !Xi,k,s | Xj,k,s ) & ( !Xi,k,eq | !Xk,j,s | Xi,j,s ) & ( !Xi,j,eq | !Xj,k,s | Xi,k,s ) & ( !Xk,j,eq | !Xj,i,d | Xk,i,d ) & ( !Xk,i,eq | !Xi,j,d | Xk,j,d ) & ( !Xj,k,eq | !Xk,i,d | Xj,i,d ) & ( !Xj,i,eq | !Xi,k,d | Xj,k,d ) & ( !Xi,k,eq | !Xk,j,d | Xi,j,d ) & ( !Xi,j,eq | !Xj,k,d | Xi,k,d ) & ( !Xk,j,eq | !Xj,i,f | Xk,i,f ) & ( !Xk,i,eq | !Xi,j,f | Xk,j,f ) & ( !Xj,k,eq | !Xk,i,f | Xj,i,f ) & ( !Xj,i,eq | !Xi,k,f | Xj,k,f ) & ( !Xi,k,eq | !Xk,j,f | Xi,j,f ) & ( !Xi,j,eq | !Xj,k,f | Xi,k,f ) & ( !Xk,j,eq | !Xj,i,eq | Xk,i,eq ) & ( !Xk,i,eq | !Xi,j,eq | Xk,j,eq ) & ( !Xj,k,eq | !Xk,i,eq | Xj,i,eq ) & ( !Xj,i,eq | !Xi,k,eq | Xj,k,eq ) & ( !Xi,k,eq | !Xk,j,eq | Xi,j,eq ) & ( !Xi,j,eq | !Xj,k,eq | Xi,k,eq ) & ( !Xk,j,eq | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,eq | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,eq | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,eq | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,eq | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,eq | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,eq | !Xj,i,mi | Xk,i,mi ) & ( !Xk,i,eq | !Xi,j,mi | Xk,j,mi ) & ( !Xj,k,eq | !Xk,i,mi | Xj,i,mi ) & ( !Xj,i,eq | !Xi,k,mi | Xj,k,mi ) & ( !Xi,k,eq | !Xk,j,mi | Xi,j,mi ) & ( !Xi,j,eq | !Xj,k,mi | Xi,k,mi ) & ( !Xk,j,eq | !Xj,i,oi | Xk,i,oi ) & ( !Xk,i,eq | !Xi,j,oi | Xk,j,oi ) & ( !Xj,k,eq | !Xk,i,oi | Xj,i,oi ) & ( !Xj,i,eq | !Xi,k,oi | Xj,k,oi ) & ( !Xi,k,eq | !Xk,j,oi | Xi,j,oi ) & ( !Xi,j,eq | !Xj,k,oi | Xi,k,oi ) & ( !Xk,j,eq | !Xj,i,si | Xk,i,si ) & ( !Xk,i,eq | !Xi,j,si | Xk,j,si ) & ( !Xj,k,eq | !Xk,i,si | Xj,i,si ) & ( !Xj,i,eq | !Xi,k,si | Xj,k,si ) & ( !Xi,k,eq | !Xk,j,si | Xi,j,si ) & ( !Xi,j,eq | !Xj,k,si | Xi,k,si ) & ( !Xk,j,eq | !Xj,i,di | Xk,i,di ) & ( !Xk,i,eq | !Xi,j,di | Xk,j,di ) & ( !Xj,k,eq | !Xk,i,di | Xj,i,di ) & ( !Xj,i,eq | !Xi,k,di | Xj,k,di ) & ( !Xi,k,eq | !Xk,j,di | Xi,j,di ) & ( !Xi,j,eq | !Xj,k,di | Xi,k,di ) & ( !Xk,j,eq | !Xj,i,fi | Xk,i,fi ) & ( !Xk,i,eq | !Xi,j,fi | Xk,j,fi ) & ( !Xj,k,eq | !Xk,i,fi | Xj,i,fi ) & ( !Xj,i,eq | !Xi,k,fi | Xj,k,fi ) & ( !Xi,k,eq | !Xk,j,fi | Xi,j,fi ) & ( !Xi,j,eq | !Xj,k,fi | Xi,k,fi ) & ( !Xk,j,bi | !Xj,i,b | Xk,i,b | Xk,i,bi | Xk,i,d | Xk,i,di | Xk,i,o | Xk,i,oi | Xk,i,m | Xk,i,mi | Xk,i,s | Xk,i,si | Xk,i,f | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,bi | !Xi,j,b | Xk,j,b | Xk,j,bi | Xk,j,d | Xk,j,di | Xk,j,o | Xk,j,oi | Xk,j,m | Xk,j,mi | Xk,j,s | Xk,j,si | Xk,j,f | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,bi | !Xk,i,b | Xj,i,b | Xj,i,bi | Xj,i,d | Xj,i,di | Xj,i,o | Xj,i,oi | Xj,i,m | Xj,i,mi | Xj,i,s | Xj,i,si | Xj,i,f | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,bi | !Xi,k,b | Xj,k,b | Xj,k,bi | Xj,k,d | Xj,k,di | Xj,k,o | Xj,k,oi | Xj,k,m | Xj,k,mi | Xj,k,s | Xj,k,si | Xj,k,f | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,bi | !Xk,j,b | Xi,j,b | Xi,j,bi | Xi,j,d | Xi,j,di | Xi,j,o | Xi,j,oi | Xi,j,m | Xi,j,mi | Xi,j,s | Xi,j,si | Xi,j,f | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,bi | !Xj,k,b | Xi,k,b | Xi,k,bi | Xi,k,d | Xi,k,di | Xi,k,o | Xi,k,oi | Xi,k,m | Xi,k,mi | Xi,k,s | Xi,k,si | Xi,k,f | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,bi | !Xj,i,m | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,d | Xk,i,f ) & ( !Xk,i,bi | !Xi,j,m | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,d | Xk,j,f ) & ( !Xj,k,bi | !Xk,i,m | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,d | Xj,i,f ) & ( !Xj,i,bi | !Xi,k,m | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,d | Xj,k,f ) & ( !Xi,k,bi | !Xk,j,m | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,d | Xi,j,f ) & ( !Xi,j,bi | !Xj,k,m | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,d | Xi,k,f ) & ( !Xk,j,bi | !Xj,i,o | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,d | Xk,i,f ) & ( !Xk,i,bi | !Xi,j,o | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,d | Xk,j,f ) & ( !Xj,k,bi | !Xk,i,o | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,d | Xj,i,f ) & ( !Xj,i,bi | !Xi,k,o | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,d | Xj,k,f ) & ( !Xi,k,bi | !Xk,j,o | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,d | Xi,j,f ) & ( !Xi,j,bi | !Xj,k,o | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,d | Xi,k,f ) & ( !Xk,j,bi | !Xj,i,s | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,d | Xk,i,f ) & ( !Xk,i,bi | !Xi,j,s | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,d | Xk,j,f ) & ( !Xj,k,bi | !Xk,i,s | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,d | Xj,i,f ) & ( !Xj,i,bi | !Xi,k,s | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,d | Xj,k,f ) & ( !Xi,k,bi | !Xk,j,s | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,d | Xi,j,f ) & ( !Xi,j,bi | !Xj,k,s | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,d | Xi,k,f ) & ( !Xk,j,bi | !Xj,i,d | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,d | Xk,i,f ) & ( !Xk,i,bi | !Xi,j,d | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,d | Xk,j,f ) & ( !Xj,k,bi | !Xk,i,d | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,d | Xj,i,f ) & ( !Xj,i,bi | !Xi,k,d | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,d | Xj,k,f ) & ( !Xi,k,bi | !Xk,j,d | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,d | Xi,j,f ) & ( !Xi,j,bi | !Xj,k,d | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,d | Xi,k,f ) & ( !Xk,j,bi | !Xj,i,f | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,f | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,f | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,f | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,f | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,f | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,eq | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,eq | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,eq | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,eq | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,eq | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,eq | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,mi | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,mi | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,mi | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,mi | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,mi | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,mi | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,oi | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,oi | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,oi | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,oi | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,oi | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,oi | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,si | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,si | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,si | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,si | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,si | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,si | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,di | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,di | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,di | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,di | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,di | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,di | Xi,k,bi ) & ( !Xk,j,bi | !Xj,i,fi | Xk,i,bi ) & ( !Xk,i,bi | !Xi,j,fi | Xk,j,bi ) & ( !Xj,k,bi | !Xk,i,fi | Xj,i,bi ) & ( !Xj,i,bi | !Xi,k,fi | Xj,k,bi ) & ( !Xi,k,bi | !Xk,j,fi | Xi,j,bi ) & ( !Xi,j,bi | !Xj,k,fi | Xi,k,bi ) & ( !Xk,j,mi | !Xj,i,b | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,di | Xk,i,fi ) & ( !Xk,i,mi | !Xi,j,b | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,di | Xk,j,fi ) & ( !Xj,k,mi | !Xk,i,b | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,di | Xj,i,fi ) & ( !Xj,i,mi | !Xi,k,b | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,di | Xj,k,fi ) & ( !Xi,k,mi | !Xk,j,b | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,di | Xi,j,fi ) & ( !Xi,j,mi | !Xj,k,b | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,di | Xi,k,fi ) & ( !Xk,j,mi | !Xj,i,m | Xk,i,s | Xk,i,si | Xk,i,eq ) & ( !Xk,i,mi | !Xi,j,m | Xk,j,s | Xk,j,si | Xk,j,eq ) & ( !Xj,k,mi | !Xk,i,m | Xj,i,s | Xj,i,si | Xj,i,eq ) & ( !Xj,i,mi | !Xi,k,m | Xj,k,s | Xj,k,si | Xj,k,eq ) & ( !Xi,k,mi | !Xk,j,m | Xi,j,s | Xi,j,si | Xi,j,eq ) & ( !Xi,j,mi | !Xj,k,m | Xi,k,s | Xi,k,si | Xi,k,eq ) & ( !Xk,j,mi | !Xj,i,o | Xk,i,oi | Xk,i,d | Xk,i,f ) & ( !Xk,i,mi | !Xi,j,o | Xk,j,oi | Xk,j,d | Xk,j,f ) & ( !Xj,k,mi | !Xk,i,o | Xj,i,oi | Xj,i,d | Xj,i,f ) & ( !Xj,i,mi | !Xi,k,o | Xj,k,oi | Xj,k,d | Xj,k,f ) & ( !Xi,k,mi | !Xk,j,o | Xi,j,oi | Xi,j,d | Xi,j,f ) & ( !Xi,j,mi | !Xj,k,o | Xi,k,oi | Xi,k,d | Xi,k,f ) & ( !Xk,j,mi | !Xj,i,s | Xk,i,d | Xk,i,f | Xk,i,oi ) & ( !Xk,i,mi | !Xi,j,s | Xk,j,d | Xk,j,f | Xk,j,oi ) & ( !Xj,k,mi | !Xk,i,s | Xj,i,d | Xj,i,f | Xj,i,oi ) & ( !Xj,i,mi | !Xi,k,s | Xj,k,d | Xj,k,f | Xj,k,oi ) & ( !Xi,k,mi | !Xk,j,s | Xi,j,d | Xi,j,f | Xi,j,oi ) & ( !Xi,j,mi | !Xj,k,s | Xi,k,d | Xi,k,f | Xi,k,oi ) & ( !Xk,j,mi | !Xj,i,d | Xk,i,oi | Xk,i,d | Xk,i,f ) & ( !Xk,i,mi | !Xi,j,d | Xk,j,oi | Xk,j,d | Xk,j,f ) & ( !Xj,k,mi | !Xk,i,d | Xj,i,oi | Xj,i,d | Xj,i,f ) & ( !Xj,i,mi | !Xi,k,d | Xj,k,oi | Xj,k,d | Xj,k,f ) & ( !Xi,k,mi | !Xk,j,d | Xi,j,oi | Xi,j,d | Xi,j,f ) & ( !Xi,j,mi | !Xj,k,d | Xi,k,oi | Xi,k,d | Xi,k,f ) & ( !Xk,j,mi | !Xj,i,f | Xk,i,mi ) & ( !Xk,i,mi | !Xi,j,f | Xk,j,mi ) & ( !Xj,k,mi | !Xk,i,f | Xj,i,mi ) & ( !Xj,i,mi | !Xi,k,f | Xj,k,mi ) & ( !Xi,k,mi | !Xk,j,f | Xi,j,mi ) & ( !Xi,j,mi | !Xj,k,f | Xi,k,mi ) & ( !Xk,j,mi | !Xj,i,eq | Xk,i,mi ) & ( !Xk,i,mi | !Xi,j,eq | Xk,j,mi ) & ( !Xj,k,mi | !Xk,i,eq | Xj,i,mi ) & ( !Xj,i,mi | !Xi,k,eq | Xj,k,mi ) & ( !Xi,k,mi | !Xk,j,eq | Xi,j,mi ) & ( !Xi,j,mi | !Xj,k,eq | Xi,k,mi ) & ( !Xk,j,mi | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,mi | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,mi | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,mi | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,mi | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,mi | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,mi | !Xj,i,mi | Xk,i,bi ) & ( !Xk,i,mi | !Xi,j,mi | Xk,j,bi ) & ( !Xj,k,mi | !Xk,i,mi | Xj,i,bi ) & ( !Xj,i,mi | !Xi,k,mi | Xj,k,bi ) & ( !Xi,k,mi | !Xk,j,mi | Xi,j,bi ) & ( !Xi,j,mi | !Xj,k,mi | Xi,k,bi ) & ( !Xk,j,mi | !Xj,i,oi | Xk,i,bi ) & ( !Xk,i,mi | !Xi,j,oi | Xk,j,bi ) & ( !Xj,k,mi | !Xk,i,oi | Xj,i,bi ) & ( !Xj,i,mi | !Xi,k,oi | Xj,k,bi ) & ( !Xi,k,mi | !Xk,j,oi | Xi,j,bi ) & ( !Xi,j,mi | !Xj,k,oi | Xi,k,bi ) & ( !Xk,j,mi | !Xj,i,si | Xk,i,bi ) & ( !Xk,i,mi | !Xi,j,si | Xk,j,bi ) & ( !Xj,k,mi | !Xk,i,si | Xj,i,bi ) & ( !Xj,i,mi | !Xi,k,si | Xj,k,bi ) & ( !Xi,k,mi | !Xk,j,si | Xi,j,bi ) & ( !Xi,j,mi | !Xj,k,si | Xi,k,bi ) & ( !Xk,j,mi | !Xj,i,di | Xk,i,bi ) & ( !Xk,i,mi | !Xi,j,di | Xk,j,bi ) & ( !Xj,k,mi | !Xk,i,di | Xj,i,bi ) & ( !Xj,i,mi | !Xi,k,di | Xj,k,bi ) & ( !Xi,k,mi | !Xk,j,di | Xi,j,bi ) & ( !Xi,j,mi | !Xj,k,di | Xi,k,bi ) & ( !Xk,j,mi | !Xj,i,fi | Xk,i,mi ) & ( !Xk,i,mi | !Xi,j,fi | Xk,j,mi ) & ( !Xj,k,mi | !Xk,i,fi | Xj,i,mi ) & ( !Xj,i,mi | !Xi,k,fi | Xj,k,mi ) & ( !Xi,k,mi | !Xk,j,fi | Xi,j,mi ) & ( !Xi,j,mi | !Xj,k,fi | Xi,k,mi ) & ( !Xk,j,oi | !Xj,i,b | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,di | Xk,i,fi ) & ( !Xk,i,oi | !Xi,j,b | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,di | Xk,j,fi ) & ( !Xj,k,oi | !Xk,i,b | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,di | Xj,i,fi ) & ( !Xj,i,oi | !Xi,k,b | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,di | Xj,k,fi ) & ( !Xi,k,oi | !Xk,j,b | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,di | Xi,j,fi ) & ( !Xi,j,oi | !Xj,k,b | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,di | Xi,k,fi ) & ( !Xk,j,oi | !Xj,i,m | Xk,i,o | Xk,i,di | Xk,i,fi ) & ( !Xk,i,oi | !Xi,j,m | Xk,j,o | Xk,j,di | Xk,j,fi ) & ( !Xj,k,oi | !Xk,i,m | Xj,i,o | Xj,i,di | Xj,i,fi ) & ( !Xj,i,oi | !Xi,k,m | Xj,k,o | Xj,k,di | Xj,k,fi ) & ( !Xi,k,oi | !Xk,j,m | Xi,j,o | Xi,j,di | Xi,j,fi ) & ( !Xi,j,oi | !Xj,k,m | Xi,k,o | Xi,k,di | Xi,k,fi ) & ( !Xk,j,oi | !Xj,i,o | Xk,i,o | Xk,i,oi | Xk,i,d | Xk,i,s | Xk,i,f | Xk,i,di | Xk,i,si | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,oi | !Xi,j,o | Xk,j,o | Xk,j,oi | Xk,j,d | Xk,j,s | Xk,j,f | Xk,j,di | Xk,j,si | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,oi | !Xk,i,o | Xj,i,o | Xj,i,oi | Xj,i,d | Xj,i,s | Xj,i,f | Xj,i,di | Xj,i,si | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,oi | !Xi,k,o | Xj,k,o | Xj,k,oi | Xj,k,d | Xj,k,s | Xj,k,f | Xj,k,di | Xj,k,si | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,oi | !Xk,j,o | Xi,j,o | Xi,j,oi | Xi,j,d | Xi,j,s | Xi,j,f | Xi,j,di | Xi,j,si | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,oi | !Xj,k,o | Xi,k,o | Xi,k,oi | Xi,k,d | Xi,k,s | Xi,k,f | Xi,k,di | Xi,k,si | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,oi | !Xj,i,s | Xk,i,oi | Xk,i,d | Xk,i,f ) & ( !Xk,i,oi | !Xi,j,s | Xk,j,oi | Xk,j,d | Xk,j,f ) & ( !Xj,k,oi | !Xk,i,s | Xj,i,oi | Xj,i,d | Xj,i,f ) & ( !Xj,i,oi | !Xi,k,s | Xj,k,oi | Xj,k,d | Xj,k,f ) & ( !Xi,k,oi | !Xk,j,s | Xi,j,oi | Xi,j,d | Xi,j,f ) & ( !Xi,j,oi | !Xj,k,s | Xi,k,oi | Xi,k,d | Xi,k,f ) & ( !Xk,j,oi | !Xj,i,d | Xk,i,oi | Xk,i,d | Xk,i,f ) & ( !Xk,i,oi | !Xi,j,d | Xk,j,oi | Xk,j,d | Xk,j,f ) & ( !Xj,k,oi | !Xk,i,d | Xj,i,oi | Xj,i,d | Xj,i,f ) & ( !Xj,i,oi | !Xi,k,d | Xj,k,oi | Xj,k,d | Xj,k,f ) & ( !Xi,k,oi | !Xk,j,d | Xi,j,oi | Xi,j,d | Xi,j,f ) & ( !Xi,j,oi | !Xj,k,d | Xi,k,oi | Xi,k,d | Xi,k,f ) & ( !Xk,j,oi | !Xj,i,f | Xk,i,oi ) & ( !Xk,i,oi | !Xi,j,f | Xk,j,oi ) & ( !Xj,k,oi | !Xk,i,f | Xj,i,oi ) & ( !Xj,i,oi | !Xi,k,f | Xj,k,oi ) & ( !Xi,k,oi | !Xk,j,f | Xi,j,oi ) & ( !Xi,j,oi | !Xj,k,f | Xi,k,oi ) & ( !Xk,j,oi | !Xj,i,eq | Xk,i,oi ) & ( !Xk,i,oi | !Xi,j,eq | Xk,j,oi ) & ( !Xj,k,oi | !Xk,i,eq | Xj,i,oi ) & ( !Xj,i,oi | !Xi,k,eq | Xj,k,oi ) & ( !Xi,k,oi | !Xk,j,eq | Xi,j,oi ) & ( !Xi,j,oi | !Xj,k,eq | Xi,k,oi ) & ( !Xk,j,oi | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,oi | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,oi | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,oi | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,oi | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,oi | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,oi | !Xj,i,mi | Xk,i,bi ) & ( !Xk,i,oi | !Xi,j,mi | Xk,j,bi ) & ( !Xj,k,oi | !Xk,i,mi | Xj,i,bi ) & ( !Xj,i,oi | !Xi,k,mi | Xj,k,bi ) & ( !Xi,k,oi | !Xk,j,mi | Xi,j,bi ) & ( !Xi,j,oi | !Xj,k,mi | Xi,k,bi ) & ( !Xk,j,oi | !Xj,i,oi | Xk,i,bi | Xk,i,oi | Xk,i,mi ) & ( !Xk,i,oi | !Xi,j,oi | Xk,j,bi | Xk,j,oi | Xk,j,mi ) & ( !Xj,k,oi | !Xk,i,oi | Xj,i,bi | Xj,i,oi | Xj,i,mi ) & ( !Xj,i,oi | !Xi,k,oi | Xj,k,bi | Xj,k,oi | Xj,k,mi ) & ( !Xi,k,oi | !Xk,j,oi | Xi,j,bi | Xi,j,oi | Xi,j,mi ) & ( !Xi,j,oi | !Xj,k,oi | Xi,k,bi | Xi,k,oi | Xi,k,mi ) & ( !Xk,j,oi | !Xj,i,si | Xk,i,oi | Xk,i,bi | Xk,i,mi ) & ( !Xk,i,oi | !Xi,j,si | Xk,j,oi | Xk,j,bi | Xk,j,mi ) & ( !Xj,k,oi | !Xk,i,si | Xj,i,oi | Xj,i,bi | Xj,i,mi ) & ( !Xj,i,oi | !Xi,k,si | Xj,k,oi | Xj,k,bi | Xj,k,mi ) & ( !Xi,k,oi | !Xk,j,si | Xi,j,oi | Xi,j,bi | Xi,j,mi ) & ( !Xi,j,oi | !Xj,k,si | Xi,k,oi | Xi,k,bi | Xi,k,mi ) & ( !Xk,j,oi | !Xj,i,di | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,di | Xk,i,si ) & ( !Xk,i,oi | !Xi,j,di | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,di | Xk,j,si ) & ( !Xj,k,oi | !Xk,i,di | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,di | Xj,i,si ) & ( !Xj,i,oi | !Xi,k,di | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,di | Xj,k,si ) & ( !Xi,k,oi | !Xk,j,di | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,di | Xi,j,si ) & ( !Xi,j,oi | !Xj,k,di | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,di | Xi,k,si ) & ( !Xk,j,oi | !Xj,i,fi | Xk,i,oi | Xk,i,di | Xk,i,si ) & ( !Xk,i,oi | !Xi,j,fi | Xk,j,oi | Xk,j,di | Xk,j,si ) & ( !Xj,k,oi | !Xk,i,fi | Xj,i,oi | Xj,i,di | Xj,i,si ) & ( !Xj,i,oi | !Xi,k,fi | Xj,k,oi | Xj,k,di | Xj,k,si ) & ( !Xi,k,oi | !Xk,j,fi | Xi,j,oi | Xi,j,di | Xi,j,si ) & ( !Xi,j,oi | !Xj,k,fi | Xi,k,oi | Xi,k,di | Xi,k,si ) & ( !Xk,j,si | !Xj,i,b | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,di | Xk,i,fi ) & ( !Xk,i,si | !Xi,j,b | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,di | Xk,j,fi ) & ( !Xj,k,si | !Xk,i,b | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,di | Xj,i,fi ) & ( !Xj,i,si | !Xi,k,b | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,di | Xj,k,fi ) & ( !Xi,k,si | !Xk,j,b | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,di | Xi,j,fi ) & ( !Xi,j,si | !Xj,k,b | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,di | Xi,k,fi ) & ( !Xk,j,si | !Xj,i,m | Xk,i,o | Xk,i,di | Xk,i,fi ) & ( !Xk,i,si | !Xi,j,m | Xk,j,o | Xk,j,di | Xk,j,fi ) & ( !Xj,k,si | !Xk,i,m | Xj,i,o | Xj,i,di | Xj,i,fi ) & ( !Xj,i,si | !Xi,k,m | Xj,k,o | Xj,k,di | Xj,k,fi ) & ( !Xi,k,si | !Xk,j,m | Xi,j,o | Xi,j,di | Xi,j,fi ) & ( !Xi,j,si | !Xj,k,m | Xi,k,o | Xi,k,di | Xi,k,fi ) & ( !Xk,j,si | !Xj,i,o | Xk,i,o | Xk,i,di | Xk,i,fi ) & ( !Xk,i,si | !Xi,j,o | Xk,j,o | Xk,j,di | Xk,j,fi ) & ( !Xj,k,si | !Xk,i,o | Xj,i,o | Xj,i,di | Xj,i,fi ) & ( !Xj,i,si | !Xi,k,o | Xj,k,o | Xj,k,di | Xj,k,fi ) & ( !Xi,k,si | !Xk,j,o | Xi,j,o | Xi,j,di | Xi,j,fi ) & ( !Xi,j,si | !Xj,k,o | Xi,k,o | Xi,k,di | Xi,k,fi ) & ( !Xk,j,si | !Xj,i,s | Xk,i,s | Xk,i,si | Xk,i,eq ) & ( !Xk,i,si | !Xi,j,s | Xk,j,s | Xk,j,si | Xk,j,eq ) & ( !Xj,k,si | !Xk,i,s | Xj,i,s | Xj,i,si | Xj,i,eq ) & ( !Xj,i,si | !Xi,k,s | Xj,k,s | Xj,k,si | Xj,k,eq ) & ( !Xi,k,si | !Xk,j,s | Xi,j,s | Xi,j,si | Xi,j,eq ) & ( !Xi,j,si | !Xj,k,s | Xi,k,s | Xi,k,si | Xi,k,eq ) & ( !Xk,j,si | !Xj,i,d | Xk,i,oi | Xk,i,d | Xk,i,f ) & ( !Xk,i,si | !Xi,j,d | Xk,j,oi | Xk,j,d | Xk,j,f ) & ( !Xj,k,si | !Xk,i,d | Xj,i,oi | Xj,i,d | Xj,i,f ) & ( !Xj,i,si | !Xi,k,d | Xj,k,oi | Xj,k,d | Xj,k,f ) & ( !Xi,k,si | !Xk,j,d | Xi,j,oi | Xi,j,d | Xi,j,f ) & ( !Xi,j,si | !Xj,k,d | Xi,k,oi | Xi,k,d | Xi,k,f ) & ( !Xk,j,si | !Xj,i,f | Xk,i,oi ) & ( !Xk,i,si | !Xi,j,f | Xk,j,oi ) & ( !Xj,k,si | !Xk,i,f | Xj,i,oi ) & ( !Xj,i,si | !Xi,k,f | Xj,k,oi ) & ( !Xi,k,si | !Xk,j,f | Xi,j,oi ) & ( !Xi,j,si | !Xj,k,f | Xi,k,oi ) & ( !Xk,j,si | !Xj,i,eq | Xk,i,si ) & ( !Xk,i,si | !Xi,j,eq | Xk,j,si ) & ( !Xj,k,si | !Xk,i,eq | Xj,i,si ) & ( !Xj,i,si | !Xi,k,eq | Xj,k,si ) & ( !Xi,k,si | !Xk,j,eq | Xi,j,si ) & ( !Xi,j,si | !Xj,k,eq | Xi,k,si ) & ( !Xk,j,si | !Xj,i,bi | Xk,i,bi ) & ( !Xk,i,si | !Xi,j,bi | Xk,j,bi ) & ( !Xj,k,si | !Xk,i,bi | Xj,i,bi ) & ( !Xj,i,si | !Xi,k,bi | Xj,k,bi ) & ( !Xi,k,si | !Xk,j,bi | Xi,j,bi ) & ( !Xi,j,si | !Xj,k,bi | Xi,k,bi ) & ( !Xk,j,si | !Xj,i,mi | Xk,i,mi ) & ( !Xk,i,si | !Xi,j,mi | Xk,j,mi ) & ( !Xj,k,si | !Xk,i,mi | Xj,i,mi ) & ( !Xj,i,si | !Xi,k,mi | Xj,k,mi ) & ( !Xi,k,si | !Xk,j,mi | Xi,j,mi ) & ( !Xi,j,si | !Xj,k,mi | Xi,k,mi ) & ( !Xk,j,si | !Xj,i,oi | Xk,i,oi ) & ( !Xk,i,si | !Xi,j,oi | Xk,j,oi ) & ( !Xj,k,si | !Xk,i,oi | Xj,i,oi ) & ( !Xj,i,si | !Xi,k,oi | Xj,k,oi ) & ( !Xi,k,si | !Xk,j,oi | Xi,j,oi ) & ( !Xi,j,si | !Xj,k,oi | Xi,k,oi ) & ( !Xk,j,si | !Xj,i,si | Xk,i,si ) & ( !Xk,i,si | !Xi,j,si | Xk,j,si ) & ( !Xj,k,si | !Xk,i,si | Xj,i,si ) & ( !Xj,i,si | !Xi,k,si | Xj,k,si ) & ( !Xi,k,si | !Xk,j,si | Xi,j,si ) & ( !Xi,j,si | !Xj,k,si | Xi,k,si ) & ( !Xk,j,si | !Xj,i,di | Xk,i,di ) & ( !Xk,i,si | !Xi,j,di | Xk,j,di ) & ( !Xj,k,si | !Xk,i,di | Xj,i,di ) & ( !Xj,i,si | !Xi,k,di | Xj,k,di ) & ( !Xi,k,si | !Xk,j,di | Xi,j,di ) & ( !Xi,j,si | !Xj,k,di | Xi,k,di ) & ( !Xk,j,si | !Xj,i,fi | Xk,i,di ) & ( !Xk,i,si | !Xi,j,fi | Xk,j,di ) & ( !Xj,k,si | !Xk,i,fi | Xj,i,di ) & ( !Xj,i,si | !Xi,k,fi | Xj,k,di ) & ( !Xi,k,si | !Xk,j,fi | Xi,j,di ) & ( !Xi,j,si | !Xj,k,fi | Xi,k,di ) & ( !Xk,j,di | !Xj,i,b | Xk,i,b | Xk,i,o | Xk,i,m | Xk,i,di | Xk,i,fi ) & ( !Xk,i,di | !Xi,j,b | Xk,j,b | Xk,j,o | Xk,j,m | Xk,j,di | Xk,j,fi ) & ( !Xj,k,di | !Xk,i,b | Xj,i,b | Xj,i,o | Xj,i,m | Xj,i,di | Xj,i,fi ) & ( !Xj,i,di | !Xi,k,b | Xj,k,b | Xj,k,o | Xj,k,m | Xj,k,di | Xj,k,fi ) & ( !Xi,k,di | !Xk,j,b | Xi,j,b | Xi,j,o | Xi,j,m | Xi,j,di | Xi,j,fi ) & ( !Xi,j,di | !Xj,k,b | Xi,k,b | Xi,k,o | Xi,k,m | Xi,k,di | Xi,k,fi ) & ( !Xk,j,di | !Xj,i,m | Xk,i,o | Xk,i,di | Xk,i,fi ) & ( !Xk,i,di | !Xi,j,m | Xk,j,o | Xk,j,di | Xk,j,fi ) & ( !Xj,k,di | !Xk,i,m | Xj,i,o | Xj,i,di | Xj,i,fi ) & ( !Xj,i,di | !Xi,k,m | Xj,k,o | Xj,k,di | Xj,k,fi ) & ( !Xi,k,di | !Xk,j,m | Xi,j,o | Xi,j,di | Xi,j,fi ) & ( !Xi,j,di | !Xj,k,m | Xi,k,o | Xi,k,di | Xi,k,fi ) & ( !Xk,j,di | !Xj,i,o | Xk,i,o | Xk,i,di | Xk,i,fi ) & ( !Xk,i,di | !Xi,j,o | Xk,j,o | Xk,j,di | Xk,j,fi ) & ( !Xj,k,di | !Xk,i,o | Xj,i,o | Xj,i,di | Xj,i,fi ) & ( !Xj,i,di | !Xi,k,o | Xj,k,o | Xj,k,di | Xj,k,fi ) & ( !Xi,k,di | !Xk,j,o | Xi,j,o | Xi,j,di | Xi,j,fi ) & ( !Xi,j,di | !Xj,k,o | Xi,k,o | Xi,k,di | Xi,k,fi ) & ( !Xk,j,di | !Xj,i,s | Xk,i,di | Xk,i,fi | Xk,i,o ) & ( !Xk,i,di | !Xi,j,s | Xk,j,di | Xk,j,fi | Xk,j,o ) & ( !Xj,k,di | !Xk,i,s | Xj,i,di | Xj,i,fi | Xj,i,o ) & ( !Xj,i,di | !Xi,k,s | Xj,k,di | Xj,k,fi | Xj,k,o ) & ( !Xi,k,di | !Xk,j,s | Xi,j,di | Xi,j,fi | Xi,j,o ) & ( !Xi,j,di | !Xj,k,s | Xi,k,di | Xi,k,fi | Xi,k,o ) & ( !Xk,j,di | !Xj,i,d | Xk,i,o | Xk,i,oi | Xk,i,d | Xk,i,s | Xk,i,f | Xk,i,di | Xk,i,si | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,di | !Xi,j,d | Xk,j,o | Xk,j,oi | Xk,j,d | Xk,j,s | Xk,j,f | Xk,j,di | Xk,j,si | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,di | !Xk,i,d | Xj,i,o | Xj,i,oi | Xj,i,d | Xj,i,s | Xj,i,f | Xj,i,di | Xj,i,si | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,di | !Xi,k,d | Xj,k,o | Xj,k,oi | Xj,k,d | Xj,k,s | Xj,k,f | Xj,k,di | Xj,k,si | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,di | !Xk,j,d | Xi,j,o | Xi,j,oi | Xi,j,d | Xi,j,s | Xi,j,f | Xi,j,di | Xi,j,si | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,di | !Xj,k,d | Xi,k,o | Xi,k,oi | Xi,k,d | Xi,k,s | Xi,k,f | Xi,k,di | Xi,k,si | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,di | !Xj,i,f | Xk,i,di | Xk,i,si | Xk,i,oi ) & ( !Xk,i,di | !Xi,j,f | Xk,j,di | Xk,j,si | Xk,j,oi ) & ( !Xj,k,di | !Xk,i,f | Xj,i,di | Xj,i,si | Xj,i,oi ) & ( !Xj,i,di | !Xi,k,f | Xj,k,di | Xj,k,si | Xj,k,oi ) & ( !Xi,k,di | !Xk,j,f | Xi,j,di | Xi,j,si | Xi,j,oi ) & ( !Xi,j,di | !Xj,k,f | Xi,k,di | Xi,k,si | Xi,k,oi ) & ( !Xk,j,di | !Xj,i,eq | Xk,i,di ) & ( !Xk,i,di | !Xi,j,eq | Xk,j,di ) & ( !Xj,k,di | !Xk,i,eq | Xj,i,di ) & ( !Xj,i,di | !Xi,k,eq | Xj,k,di ) & ( !Xi,k,di | !Xk,j,eq | Xi,j,di ) & ( !Xi,j,di | !Xj,k,eq | Xi,k,di ) & ( !Xk,j,di | !Xj,i,bi | Xk,i,bi | Xk,i,oi | Xk,i,di | Xk,i,mi | Xk,i,si ) & ( !Xk,i,di | !Xi,j,bi | Xk,j,bi | Xk,j,oi | Xk,j,di | Xk,j,mi | Xk,j,si ) & ( !Xj,k,di | !Xk,i,bi | Xj,i,bi | Xj,i,oi | Xj,i,di | Xj,i,mi | Xj,i,si ) & ( !Xj,i,di | !Xi,k,bi | Xj,k,bi | Xj,k,oi | Xj,k,di | Xj,k,mi | Xj,k,si ) & ( !Xi,k,di | !Xk,j,bi | Xi,j,bi | Xi,j,oi | Xi,j,di | Xi,j,mi | Xi,j,si ) & ( !Xi,j,di | !Xj,k,bi | Xi,k,bi | Xi,k,oi | Xi,k,di | Xi,k,mi | Xi,k,si ) & ( !Xk,j,di | !Xj,i,mi | Xk,i,o | Xk,i,di | Xk,i,si ) & ( !Xk,i,di | !Xi,j,mi | Xk,j,o | Xk,j,di | Xk,j,si ) & ( !Xj,k,di | !Xk,i,mi | Xj,i,o | Xj,i,di | Xj,i,si ) & ( !Xj,i,di | !Xi,k,mi | Xj,k,o | Xj,k,di | Xj,k,si ) & ( !Xi,k,di | !Xk,j,mi | Xi,j,o | Xi,j,di | Xi,j,si ) & ( !Xi,j,di | !Xj,k,mi | Xi,k,o | Xi,k,di | Xi,k,si ) & ( !Xk,j,di | !Xj,i,oi | Xk,i,oi | Xk,i,di | Xk,i,si ) & ( !Xk,i,di | !Xi,j,oi | Xk,j,oi | Xk,j,di | Xk,j,si ) & ( !Xj,k,di | !Xk,i,oi | Xj,i,oi | Xj,i,di | Xj,i,si ) & ( !Xj,i,di | !Xi,k,oi | Xj,k,oi | Xj,k,di | Xj,k,si ) & ( !Xi,k,di | !Xk,j,oi | Xi,j,oi | Xi,j,di | Xi,j,si ) & ( !Xi,j,di | !Xj,k,oi | Xi,k,oi | Xi,k,di | Xi,k,si ) & ( !Xk,j,di | !Xj,i,si | Xk,i,di ) & ( !Xk,i,di | !Xi,j,si | Xk,j,di ) & ( !Xj,k,di | !Xk,i,si | Xj,i,di ) & ( !Xj,i,di | !Xi,k,si | Xj,k,di ) & ( !Xi,k,di | !Xk,j,si | Xi,j,di ) & ( !Xi,j,di | !Xj,k,si | Xi,k,di ) & ( !Xk,j,di | !Xj,i,di | Xk,i,di ) & ( !Xk,i,di | !Xi,j,di | Xk,j,di ) & ( !Xj,k,di | !Xk,i,di | Xj,i,di ) & ( !Xj,i,di | !Xi,k,di | Xj,k,di ) & ( !Xi,k,di | !Xk,j,di | Xi,j,di ) & ( !Xi,j,di | !Xj,k,di | Xi,k,di ) & ( !Xk,j,di | !Xj,i,fi | Xk,i,di ) & ( !Xk,i,di | !Xi,j,fi | Xk,j,di ) & ( !Xj,k,di | !Xk,i,fi | Xj,i,di ) & ( !Xj,i,di | !Xi,k,fi | Xj,k,di ) & ( !Xi,k,di | !Xk,j,fi | Xi,j,di ) & ( !Xi,j,di | !Xj,k,fi | Xi,k,di ) & ( !Xk,j,fi | !Xj,i,b | Xk,i,b ) & ( !Xk,i,fi | !Xi,j,b | Xk,j,b ) & ( !Xj,k,fi | !Xk,i,b | Xj,i,b ) & ( !Xj,i,fi | !Xi,k,b | Xj,k,b ) & ( !Xi,k,fi | !Xk,j,b | Xi,j,b ) & ( !Xi,j,fi | !Xj,k,b | Xi,k,b ) & ( !Xk,j,fi | !Xj,i,m | Xk,i,m ) & ( !Xk,i,fi | !Xi,j,m | Xk,j,m ) & ( !Xj,k,fi | !Xk,i,m | Xj,i,m ) & ( !Xj,i,fi | !Xi,k,m | Xj,k,m ) & ( !Xi,k,fi | !Xk,j,m | Xi,j,m ) & ( !Xi,j,fi | !Xj,k,m | Xi,k,m ) & ( !Xk,j,fi | !Xj,i,o | Xk,i,o ) & ( !Xk,i,fi | !Xi,j,o | Xk,j,o ) & ( !Xj,k,fi | !Xk,i,o | Xj,i,o ) & ( !Xj,i,fi | !Xi,k,o | Xj,k,o ) & ( !Xi,k,fi | !Xk,j,o | Xi,j,o ) & ( !Xi,j,fi | !Xj,k,o | Xi,k,o ) & ( !Xk,j,fi | !Xj,i,s | Xk,i,o ) & ( !Xk,i,fi | !Xi,j,s | Xk,j,o ) & ( !Xj,k,fi | !Xk,i,s | Xj,i,o ) & ( !Xj,i,fi | !Xi,k,s | Xj,k,o ) & ( !Xi,k,fi | !Xk,j,s | Xi,j,o ) & ( !Xi,j,fi | !Xj,k,s | Xi,k,o ) & ( !Xk,j,fi | !Xj,i,d | Xk,i,o | Xk,i,d | Xk,i,s ) & ( !Xk,i,fi | !Xi,j,d | Xk,j,o | Xk,j,d | Xk,j,s ) & ( !Xj,k,fi | !Xk,i,d | Xj,i,o | Xj,i,d | Xj,i,s ) & ( !Xj,i,fi | !Xi,k,d | Xj,k,o | Xj,k,d | Xj,k,s ) & ( !Xi,k,fi | !Xk,j,d | Xi,j,o | Xi,j,d | Xi,j,s ) & ( !Xi,j,fi | !Xj,k,d | Xi,k,o | Xi,k,d | Xi,k,s ) & ( !Xk,j,fi | !Xj,i,f | Xk,i,f | Xk,i,fi | Xk,i,eq ) & ( !Xk,i,fi | !Xi,j,f | Xk,j,f | Xk,j,fi | Xk,j,eq ) & ( !Xj,k,fi | !Xk,i,f | Xj,i,f | Xj,i,fi | Xj,i,eq ) & ( !Xj,i,fi | !Xi,k,f | Xj,k,f | Xj,k,fi | Xj,k,eq ) & ( !Xi,k,fi | !Xk,j,f | Xi,j,f | Xi,j,fi | Xi,j,eq ) & ( !Xi,j,fi | !Xj,k,f | Xi,k,f | Xi,k,fi | Xi,k,eq ) & ( !Xk,j,fi | !Xj,i,eq | Xk,i,fi ) & ( !Xk,i,fi | !Xi,j,eq | Xk,j,fi ) & ( !Xj,k,fi | !Xk,i,eq | Xj,i,fi ) & ( !Xj,i,fi | !Xi,k,eq | Xj,k,fi ) & ( !Xi,k,fi | !Xk,j,eq | Xi,j,fi ) & ( !Xi,j,fi | !Xj,k,eq | Xi,k,fi ) & ( !Xk,j,fi | !Xj,i,bi | Xk,i,bi | Xk,i,oi | Xk,i,mi | Xk,i,di | Xk,i,si ) & ( !Xk,i,fi | !Xi,j,bi | Xk,j,bi | Xk,j,oi | Xk,j,mi | Xk,j,di | Xk,j,si ) & ( !Xj,k,fi | !Xk,i,bi | Xj,i,bi | Xj,i,oi | Xj,i,mi | Xj,i,di | Xj,i,si ) & ( !Xj,i,fi | !Xi,k,bi | Xj,k,bi | Xj,k,oi | Xj,k,mi | Xj,k,di | Xj,k,si ) & ( !Xi,k,fi | !Xk,j,bi | Xi,j,bi | Xi,j,oi | Xi,j,mi | Xi,j,di | Xi,j,si ) & ( !Xi,j,fi | !Xj,k,bi | Xi,k,bi | Xi,k,oi | Xi,k,mi | Xi,k,di | Xi,k,si ) & ( !Xk,j,fi | !Xj,i,mi | Xk,i,si | Xk,i,oi | Xk,i,di ) & ( !Xk,i,fi | !Xi,j,mi | Xk,j,si | Xk,j,oi | Xk,j,di ) & ( !Xj,k,fi | !Xk,i,mi | Xj,i,si | Xj,i,oi | Xj,i,di ) & ( !Xj,i,fi | !Xi,k,mi | Xj,k,si | Xj,k,oi | Xj,k,di ) & ( !Xi,k,fi | !Xk,j,mi | Xi,j,si | Xi,j,oi | Xi,j,di ) & ( !Xi,j,fi | !Xj,k,mi | Xi,k,si | Xi,k,oi | Xi,k,di ) & ( !Xk,j,fi | !Xj,i,oi | Xk,i,oi | Xk,i,di | Xk,i,si ) & ( !Xk,i,fi | !Xi,j,oi | Xk,j,oi | Xk,j,di | Xk,j,si ) & ( !Xj,k,fi | !Xk,i,oi | Xj,i,oi | Xj,i,di | Xj,i,si ) & ( !Xj,i,fi | !Xi,k,oi | Xj,k,oi | Xj,k,di | Xj,k,si ) & ( !Xi,k,fi | !Xk,j,oi | Xi,j,oi | Xi,j,di | Xi,j,si ) & ( !Xi,j,fi | !Xj,k,oi | Xi,k,oi | Xi,k,di | Xi,k,si ) & ( !Xk,j,fi | !Xj,i,si | Xk,i,di ) & ( !Xk,i,fi | !Xi,j,si | Xk,j,di ) & ( !Xj,k,fi | !Xk,i,si | Xj,i,di ) & ( !Xj,i,fi | !Xi,k,si | Xj,k,di ) & ( !Xi,k,fi | !Xk,j,si | Xi,j,di ) & ( !Xi,j,fi | !Xj,k,si | Xi,k,di ) & ( !Xk,j,fi | !Xj,i,di | Xk,i,di ) & ( !Xk,i,fi | !Xi,j,di | Xk,j,di ) & ( !Xj,k,fi | !Xk,i,di | Xj,i,di ) & ( !Xj,i,fi | !Xi,k,di | Xj,k,di ) & ( !Xi,k,fi | !Xk,j,di | Xi,j,di ) & ( !Xi,j,fi | !Xj,k,di | Xi,k,di ) & ( !Xk,j,fi | !Xj,i,fi | Xk,i,fi ) & ( !Xk,i,fi | !Xi,j,fi | Xk,j,fi ) & ( !Xj,k,fi | !Xk,i,fi | Xj,i,fi ) & ( !Xj,i,fi | !Xi,k,fi | Xj,k,fi ) & ( !Xi,k,fi | !Xk,j,fi | Xi,j,fi ) & ( !Xi,j,fi | !Xj,k,fi | Xi,k,fi )");
		PLFormula source = RevisorPLAK.parseFormula("(Xi,j,b) & (!Xi,j,m & !Xi,j,o & !Xi,j,s & !Xi,j,d & !Xi,j,f & !Xi,j,eq & !Xi,j,bi & !Xi,j,mi & !Xi,j,oi & !Xi,j,si & !Xi,j,di & !Xi,j,fi) & (Xi,k,b) & (!Xi,k,m & !Xi,k,o & !Xi,k,s & !Xi,k,d & !Xi,k,f & !Xi,k,eq & !Xi,k,bi & !Xi,k,mi & !Xi,k,oi & !Xi,k,si & !Xi,k,di & !Xi,k,fi) & (Xj,k,b) & (!Xj,k,m & !Xj,k,o & !Xj,k,s & !Xj,k,d & !Xj,k,f & !Xj,k,eq & !Xj,k,bi & !Xj,k,mi & !Xj,k,oi & !Xj,k,si & !Xj,k,di & !Xj,k,fi)");
		PLFormula target = RevisorPLAK.parseFormula("(Xi,j,b) & (!Xi,j,m & !Xi,j,o & !Xi,j,s & !Xi,j,d & !Xi,j,f & !Xi,j,eq & !Xi,j,bi & !Xi,j,mi & !Xi,j,oi & !Xi,j,si & !Xi,j,di & !Xi,j,fi) & (Xi,k,b) & (!Xi,k,m & !Xi,k,o & !Xi,k,s & !Xi,k,d & !Xi,k,f & !Xi,k,eq & !Xi,k,bi & !Xi,k,mi & !Xi,k,oi & !Xi,k,si & !Xi,k,di & !Xi,k,fi) & (Xj,k,b) & (!Xj,k,m & !Xj,k,o & !Xj,k,s & !Xj,k,d & !Xj,k,f & !Xj,k,eq & !Xj,k,bi & !Xj,k,mi & !Xj,k,oi & !Xj,k,si & !Xj,k,di & !Xj,k,fi)");
		
		rules.addRule(": Xi,j,si *= Xi,j,di", 0.00001);
		rules.addRule(": Xj,i,si *= Xj,i,di", 0.00001);
		rules.addRule(": Xi,j,si *= Xi,j,eq", 0.00001);
		rules.addRule(": Xj,i,si *= Xj,i,eq", 0.00001);
		rules.addRule(": Xi,j,si *= Xi,j,oi", 0.00001);
		rules.addRule(": Xj,i,si *= Xj,i,oi", 0.00001);
		rules.addRule(": Xi,j,di *= Xi,j,si", 0.00001);
		rules.addRule(": Xj,i,di *= Xj,i,si", 0.00001);
		rules.addRule(": Xi,j,di *= Xi,j,fi", 0.00001);
		rules.addRule(": Xj,i,di *= Xj,i,fi", 0.00001);
		rules.addRule(": Xi,j,d *= Xi,j,s", 0.00001);
		rules.addRule(": Xj,i,d *= Xj,i,s", 0.00001);
		rules.addRule(": Xi,j,d *= Xi,j,f", 0.00001);
		rules.addRule(": Xj,i,d *= Xj,i,f", 0.00001);
		rules.addRule(": Xi,j,m *= Xi,j,b", 0.00001);
		rules.addRule(": Xj,i,m *= Xj,i,b", 0.00001);
		rules.addRule(": Xi,j,m *= Xi,j,o", 0.00001);
		rules.addRule(": Xj,i,m *= Xj,i,o", 0.00001);
		rules.addRule(": Xi,j,s *= Xi,j,d", 0.00001);
		rules.addRule(": Xj,i,s *= Xj,i,d", 0.00001);
		rules.addRule(": Xi,j,s *= Xi,j,eq", 0.00001);
		rules.addRule(": Xj,i,s *= Xj,i,eq", 0.00001);
		rules.addRule(": Xi,j,s *= Xi,j,o", 0.00001);
		rules.addRule(": Xj,i,s *= Xj,i,o", 0.00001);
		rules.addRule(": Xi,j,fi *= Xi,j,di", 0.00001);
		rules.addRule(": Xj,i,fi *= Xj,i,di", 0.00001);
		rules.addRule(": Xi,j,fi *= Xi,j,eq", 0.00001);
		rules.addRule(": Xj,i,fi *= Xj,i,eq", 0.00001);
		rules.addRule(": Xi,j,fi *= Xi,j,o", 0.00001);
		rules.addRule(": Xj,i,fi *= Xj,i,o", 0.00001);
		rules.addRule(": Xi,j,b *= Xi,j,m", 0.00001);
		rules.addRule(": Xj,i,b *= Xj,i,m", 0.00001);
		rules.addRule(": Xi,j,f *= Xi,j,d", 0.00001);
		rules.addRule(": Xj,i,f *= Xj,i,d", 0.00001);
		rules.addRule(": Xi,j,f *= Xi,j,eq", 0.00001);
		rules.addRule(": Xj,i,f *= Xj,i,eq", 0.00001);
		rules.addRule(": Xi,j,f *= Xi,j,oi", 0.00001);
		rules.addRule(": Xj,i,f *= Xj,i,oi", 0.00001);
		rules.addRule(": Xi,j,eq *= Xi,j,si", 0.00001);
		rules.addRule(": Xj,i,eq *= Xj,i,si", 0.00001);
		rules.addRule(": Xi,j,eq *= Xi,j,s", 0.00001);
		rules.addRule(": Xj,i,eq *= Xj,i,s", 0.00001);
		rules.addRule(": Xi,j,eq *= Xi,j,fi", 0.00001);
		rules.addRule(": Xj,i,eq *= Xj,i,fi", 0.00001);
		rules.addRule(": Xi,j,eq *= Xi,j,f", 0.00001);
		rules.addRule(": Xj,i,eq *= Xj,i,f", 0.00001);
		rules.addRule(": Xi,j,bi *= Xi,j,mi", 0.00001);
		rules.addRule(": Xj,i,bi *= Xj,i,mi", 0.00001);
		rules.addRule(": Xi,j,oi *= Xi,j,si", 0.00001);
		rules.addRule(": Xj,i,oi *= Xj,i,si", 0.00001);
		rules.addRule(": Xi,j,oi *= Xi,j,f", 0.00001);
		rules.addRule(": Xj,i,oi *= Xj,i,f", 0.00001);
		rules.addRule(": Xi,j,oi *= Xi,j,mi", 0.00001);
		rules.addRule(": Xj,i,oi *= Xj,i,mi", 0.00001);
		rules.addRule(": Xi,j,mi *= Xi,j,bi", 0.00001);
		rules.addRule(": Xj,i,mi *= Xj,i,bi", 0.00001);
		rules.addRule(": Xi,j,mi *= Xi,j,oi", 0.00001);
		rules.addRule(": Xj,i,mi *= Xj,i,oi", 0.00001);
		rules.addRule(": Xi,j,o *= Xi,j,m", 0.00001);
		rules.addRule(": Xj,i,o *= Xj,i,m", 0.00001);
		rules.addRule(": Xi,j,o *= Xi,j,s", 0.00001);
		rules.addRule(": Xj,i,o *= Xj,i,s", 0.00001);
		rules.addRule(": Xi,j,o *= Xi,j,fi", 0.00001);
		rules.addRule(": Xj,i,o *= Xj,i,fi", 0.00001);
		rules.addRule(": Xi,k,si *= Xi,k,di", 0.00001);
		rules.addRule(": Xk,i,si *= Xk,i,di", 0.00001);
		rules.addRule(": Xi,k,si *= Xi,k,eq", 0.00001);
		rules.addRule(": Xk,i,si *= Xk,i,eq", 0.00001);
		rules.addRule(": Xi,k,si *= Xi,k,oi", 0.00001);
		rules.addRule(": Xk,i,si *= Xk,i,oi", 0.00001);
		rules.addRule(": Xi,k,di *= Xi,k,si", 0.00001);
		rules.addRule(": Xk,i,di *= Xk,i,si", 0.00001);
		rules.addRule(": Xi,k,di *= Xi,k,fi", 0.00001);
		rules.addRule(": Xk,i,di *= Xk,i,fi", 0.00001);
		rules.addRule(": Xi,k,d *= Xi,k,s", 0.00001);
		rules.addRule(": Xk,i,d *= Xk,i,s", 0.00001);
		rules.addRule(": Xi,k,d *= Xi,k,f", 0.00001);
		rules.addRule(": Xk,i,d *= Xk,i,f", 0.00001);
		rules.addRule(": Xi,k,m *= Xi,k,b", 0.00001);
		rules.addRule(": Xk,i,m *= Xk,i,b", 0.00001);
		rules.addRule(": Xi,k,m *= Xi,k,o", 0.00001);
		rules.addRule(": Xk,i,m *= Xk,i,o", 0.00001);
		rules.addRule(": Xi,k,s *= Xi,k,d", 0.00001);
		rules.addRule(": Xk,i,s *= Xk,i,d", 0.00001);
		rules.addRule(": Xi,k,s *= Xi,k,eq", 0.00001);
		rules.addRule(": Xk,i,s *= Xk,i,eq", 0.00001);
		rules.addRule(": Xi,k,s *= Xi,k,o", 0.00001);
		rules.addRule(": Xk,i,s *= Xk,i,o", 0.00001);
		rules.addRule(": Xi,k,fi *= Xi,k,di", 0.00001);
		rules.addRule(": Xk,i,fi *= Xk,i,di", 0.00001);
		rules.addRule(": Xi,k,fi *= Xi,k,eq", 0.00001);
		rules.addRule(": Xk,i,fi *= Xk,i,eq", 0.00001);
		rules.addRule(": Xi,k,fi *= Xi,k,o", 0.00001);
		rules.addRule(": Xk,i,fi *= Xk,i,o", 0.00001);
		rules.addRule(": Xi,k,b *= Xi,k,m", 0.00001);
		rules.addRule(": Xk,i,b *= Xk,i,m", 0.00001);
		rules.addRule(": Xi,k,f *= Xi,k,d", 0.00001);
		rules.addRule(": Xk,i,f *= Xk,i,d", 0.00001);
		rules.addRule(": Xi,k,f *= Xi,k,eq", 0.00001);
		rules.addRule(": Xk,i,f *= Xk,i,eq", 0.00001);
		rules.addRule(": Xi,k,f *= Xi,k,oi", 0.00001);
		rules.addRule(": Xk,i,f *= Xk,i,oi", 0.00001);
		rules.addRule(": Xi,k,eq *= Xi,k,si", 0.00001);
		rules.addRule(": Xk,i,eq *= Xk,i,si", 0.00001);
		rules.addRule(": Xi,k,eq *= Xi,k,s", 0.00001);
		rules.addRule(": Xk,i,eq *= Xk,i,s", 0.00001);
		rules.addRule(": Xi,k,eq *= Xi,k,fi", 0.00001);
		rules.addRule(": Xk,i,eq *= Xk,i,fi", 0.00001);
		rules.addRule(": Xi,k,eq *= Xi,k,f", 0.00001);
		rules.addRule(": Xk,i,eq *= Xk,i,f", 0.00001);
		rules.addRule(": Xi,k,bi *= Xi,k,mi", 0.00001);
		rules.addRule(": Xk,i,bi *= Xk,i,mi", 0.00001);
		rules.addRule(": Xi,k,oi *= Xi,k,si", 0.00001);
		rules.addRule(": Xk,i,oi *= Xk,i,si", 0.00001);
		rules.addRule(": Xi,k,oi *= Xi,k,f", 0.00001);
		rules.addRule(": Xk,i,oi *= Xk,i,f", 0.00001);
		rules.addRule(": Xi,k,oi *= Xi,k,mi", 0.00001);
		rules.addRule(": Xk,i,oi *= Xk,i,mi", 0.00001);
		rules.addRule(": Xi,k,mi *= Xi,k,bi", 0.00001);
		rules.addRule(": Xk,i,mi *= Xk,i,bi", 0.00001);
		rules.addRule(": Xi,k,mi *= Xi,k,oi", 0.00001);
		rules.addRule(": Xk,i,mi *= Xk,i,oi", 0.00001);
		rules.addRule(": Xi,k,o *= Xi,k,m", 0.00001);
		rules.addRule(": Xk,i,o *= Xk,i,m", 0.00001);
		rules.addRule(": Xi,k,o *= Xi,k,s", 0.00001);
		rules.addRule(": Xk,i,o *= Xk,i,s", 0.00001);
		rules.addRule(": Xi,k,o *= Xi,k,fi", 0.00001);
		rules.addRule(": Xk,i,o *= Xk,i,fi", 0.00001);
		rules.addRule(": Xj,k,si *= Xj,k,di", 0.00001);
		rules.addRule(": Xk,j,si *= Xk,j,di", 0.00001);
		rules.addRule(": Xj,k,si *= Xj,k,eq", 0.00001);
		rules.addRule(": Xk,j,si *= Xk,j,eq", 0.00001);
		rules.addRule(": Xj,k,si *= Xj,k,oi", 0.00001);
		rules.addRule(": Xk,j,si *= Xk,j,oi", 0.00001);
		rules.addRule(": Xj,k,di *= Xj,k,si", 0.00001);
		rules.addRule(": Xk,j,di *= Xk,j,si", 0.00001);
		rules.addRule(": Xj,k,di *= Xj,k,fi", 0.00001);
		rules.addRule(": Xk,j,di *= Xk,j,fi", 0.00001);
		rules.addRule(": Xj,k,d *= Xj,k,s", 0.00001);
		rules.addRule(": Xk,j,d *= Xk,j,s", 0.00001);
		rules.addRule(": Xj,k,d *= Xj,k,f", 0.00001);
		rules.addRule(": Xk,j,d *= Xk,j,f", 0.00001);
		rules.addRule(": Xj,k,m *= Xj,k,b", 0.00001);
		rules.addRule(": Xk,j,m *= Xk,j,b", 0.00001);
		rules.addRule(": Xj,k,m *= Xj,k,o", 0.00001);
		rules.addRule(": Xk,j,m *= Xk,j,o", 0.00001);
		rules.addRule(": Xj,k,s *= Xj,k,d", 0.00001);
		rules.addRule(": Xk,j,s *= Xk,j,d", 0.00001);
		rules.addRule(": Xj,k,s *= Xj,k,eq", 0.00001);
		rules.addRule(": Xk,j,s *= Xk,j,eq", 0.00001);
		rules.addRule(": Xj,k,s *= Xj,k,o", 0.00001);
		rules.addRule(": Xk,j,s *= Xk,j,o", 0.00001);
		rules.addRule(": Xj,k,fi *= Xj,k,di", 0.00001);
		rules.addRule(": Xk,j,fi *= Xk,j,di", 0.00001);
		rules.addRule(": Xj,k,fi *= Xj,k,eq", 0.00001);
		rules.addRule(": Xk,j,fi *= Xk,j,eq", 0.00001);
		rules.addRule(": Xj,k,fi *= Xj,k,o", 0.00001);
		rules.addRule(": Xk,j,fi *= Xk,j,o", 0.00001);
		rules.addRule(": Xj,k,b *= Xj,k,m", 0.00001);
		rules.addRule(": Xk,j,b *= Xk,j,m", 0.00001);
		rules.addRule(": Xj,k,f *= Xj,k,d", 0.00001);
		rules.addRule(": Xk,j,f *= Xk,j,d", 0.00001);
		rules.addRule(": Xj,k,f *= Xj,k,eq", 0.00001);
		rules.addRule(": Xk,j,f *= Xk,j,eq", 0.00001);
		rules.addRule(": Xj,k,f *= Xj,k,oi", 0.00001);
		rules.addRule(": Xk,j,f *= Xk,j,oi", 0.00001);
		rules.addRule(": Xj,k,eq *= Xj,k,si", 0.00001);
		rules.addRule(": Xk,j,eq *= Xk,j,si", 0.00001);
		rules.addRule(": Xj,k,eq *= Xj,k,s", 0.00001);
		rules.addRule(": Xk,j,eq *= Xk,j,s", 0.00001);
		rules.addRule(": Xj,k,eq *= Xj,k,fi", 0.00001);
		rules.addRule(": Xk,j,eq *= Xk,j,fi", 0.00001);
		rules.addRule(": Xj,k,eq *= Xj,k,f", 0.00001);
		rules.addRule(": Xk,j,eq *= Xk,j,f", 0.00001);
		rules.addRule(": Xj,k,bi *= Xj,k,mi", 0.00001);
		rules.addRule(": Xk,j,bi *= Xk,j,mi", 0.00001);
		rules.addRule(": Xj,k,oi *= Xj,k,si", 0.00001);
		rules.addRule(": Xk,j,oi *= Xk,j,si", 0.00001);
		rules.addRule(": Xj,k,oi *= Xj,k,f", 0.00001);
		rules.addRule(": Xk,j,oi *= Xk,j,f", 0.00001);
		rules.addRule(": Xj,k,oi *= Xj,k,mi", 0.00001);
		rules.addRule(": Xk,j,oi *= Xk,j,mi", 0.00001);
		rules.addRule(": Xj,k,mi *= Xj,k,bi", 0.00001);
		rules.addRule(": Xk,j,mi *= Xk,j,bi", 0.00001);
		rules.addRule(": Xj,k,mi *= Xj,k,oi", 0.00001);
		rules.addRule(": Xk,j,mi *= Xk,j,oi", 0.00001);
		rules.addRule(": Xj,k,o *= Xj,k,m", 0.00001);
		rules.addRule(": Xk,j,o *= Xk,j,m", 0.00001);
		rules.addRule(": Xj,k,o *= Xj,k,s", 0.00001);
		rules.addRule(": Xk,j,o *= Xk,j,s", 0.00001);
		rules.addRule(": Xj,k,o *= Xj,k,fi", 0.00001);
		rules.addRule(": Xk,j,o *= Xk,j,fi", 0.00001);
		
		PLFormula result = (RevisorPLAK.adaptAK(source, target, dk, rules));
		result = RevisorPLAK.simplifiedDNF(result);
		RevisorPLAK.print(result);
	}
	
}
