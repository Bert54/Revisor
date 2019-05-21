package fr.loria.orpailleur.revisor.engine.revisorPLAK;

import java.util.Collection;
import java.util.LinkedList;

import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.mode.ConsoleMode;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.RevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class RevisorPLAKConsoleMode {
	
	public static void main(final String args[]) {
		try {
			ConsoleMode<RevisorConsolePLAK> console = new ConsoleMode<>(new RevisorConsolePLAK());
			//console.printExample(example1(), "Example 1 : a, b, c");
			//console.printExample(example2(), "Example 2 : pear pie");
			console.start();
		}
		catch(ConsoleInitializationException argh) {
			argh.printStackTrace();
		}
		
		System.exit(0);
	}
	
	/**
	 * A basic example using rules and different flip costs.
	 */
	public static Collection<String> example1() {
		Collection<String> commands = new LinkedList<>();
		
		commands.add("source := a & b");
		commands.add("target := !a & !c");
		commands.add("dk := true");
		
		commands.add("a.flipcost := 2");
		
		commands.add("theRules := {}");
		commands.add("theRules += [0.5] b : a ~> c");
		commands.add("theRules += [0.5] b : c ~> a");
		
		commands.add("result := adapt(dk, source, target, theRules)");
		
		return commands;
	}
	
	/**
	 * In this example, we have a recipe for an pear pie, but we want to make a
	 * non-pear pie. We know about 3 fruits : apples, pears and peaches. We
	 * expect to get as a result another pie recipe.
	 */
	public static Collection<String> example2() {
		Collection<String> commands = new LinkedList<>();
		
		commands.add("source := pie & pie_shell & pear & sugar & egg");
		commands.add("target := pie & !pear & !cinnamon & !egg");
		commands.add("dk := fruit <=> (apple | peach | pear)");
		
		commands.add("theRules := {}");
		commands.add("theRules += [0.3] cake : egg ~> banana");
		commands.add("theRules += [0.3] pie : egg ~> flour & cider_vinegar");
		commands.add("theRules += [0.7] : pear ~> peach");
		commands.add("theRules += [0.3] : pear ~> apple & cinnamon");
		commands.add("theRules += [0.3] : cinnamon ~> vanilla_sugar");
		commands.add("theRules += [0.3] : cinnamon ~> orange_blossom");
		
		commands.add("result := adapt(dk, source, target, theRules)");
		
		return commands;
	}
	
}
