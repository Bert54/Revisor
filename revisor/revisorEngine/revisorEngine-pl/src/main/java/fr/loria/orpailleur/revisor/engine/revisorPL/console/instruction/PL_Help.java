package fr.loria.orpailleur.revisor.engine.revisorPL.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.instruction.Help;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;

/**
 * @author William Philbert
 */
public class PL_Help<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends Help<C> {
	
	// Constants :
	
	private static final String HELP_FILE = "resources/RevisorPLHelp.txt";
	private static final String GRAMMAR_FILE = "resources/RevisorPLGrammar.txt";
	
	// Constructors :
	
	public PL_Help(final C console, final String inputText) {
		super(console, inputText);
	}
	
	// Methods :
	
	@Override
	protected String helpFilePath() {
		return HELP_FILE;
	}
	
	@Override
	protected String grammarFilePath() {
		return GRAMMAR_FILE;
	}
	
}
