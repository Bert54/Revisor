package fr.loria.orpailleur.revisor.engine.revisorPL;

import java.util.Collection;
import java.util.LinkedList;

import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.mode.ConsoleMode;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.RevisorConsolePL;

/**
 * @author William Philbert
 */
public class RevisorPLConsoleMode {
	
	public static void main(final String args[]) {
		try {
			ConsoleMode<RevisorConsolePL> console = new ConsoleMode<>(new RevisorConsolePL());
			//console.printExample(example1(), "Example 1 : apple pie");
			//console.printExample(example2(), "Example 2 : mint and chocolate cake");
			console.start();
		}
		catch(ConsoleInitializationException argh) {
			argh.printStackTrace();
		}
		
		System.exit(0);
	}
	
	/**
	 * In this example, we have a recipe for an apple pie, but we want to make a
	 * non-apple pie. We know about 3 fruits : apples, peaches, and pears. We
	 * expect to get as a result a recipe for a pear pie or a peach pie.
	 */
	public static Collection<String> example1() {
		Collection<String> commands = new LinkedList<>();
		
		commands.add("source := pie & apple & pie_shell & sugar");
		commands.add("target := pie & !apple");
		commands.add("dk := fruit <=> (pear | peach | apple)");
		commands.add("result := adapt(dk, source, target)");
		
		return commands;
	}
	
	/**
	 * In this example, we have a recipe for a mint and chocolate cake. However
	 * we don't like the association of mint and chocolate
	 */
	public static Collection<String> example2() {
		Collection<String> commands = new LinkedList<>();
		
		commands.add("source := mint & chocolate & cake");
		commands.add("target := !(chocolate & mint)");
		commands.add("dk := true");
		commands.add("chocolate.weight := 2.0");
		commands.add("result := adapt(dk, source, target)");
		
		return commands;
	}
	
}
