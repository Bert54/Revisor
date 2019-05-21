package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.instruction.Help;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_Help<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends Help<C> {
	
	// Constants :
	
	private static final String HELP_FILE = "resources/RevisorPLAKHelp.txt";
	private static final String GRAMMAR_FILE = "resources/RevisorPLAKGrammar.txt";
	
	// Constructors :
	
	public PLAK_Help(final C console, final String inputText) {
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
