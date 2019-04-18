package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import java.io.IOException;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Resources;

/**
 * @author William Philbert
 */
public abstract class Help<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String INSERT_GRAMMAR_HERE = "[INSERT_GRAMMAR_HERE]";
	private static final String CANT_READ_HELP_FILE = "Can't read help file.";
	private static final String CANT_READ_GRAMMAR_FILE = "Can't read grammar file.";
	
	// Class fields :
	
	private static String helpText = null;
	
	// Constructors :
	
	protected Help(final C console, final String inputText) {
		super(console, inputText);
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to check.
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		// Nothing to do.
	}
	
	@Override
	public String createFormatedInputText() {
		return this.simplifiedString(this.inputText);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
	@Override
	protected String createOutputText() {
		if(helpText == null) {
			helpText = this.getHelpText();
		}
		
		return helpText;
	}
	
	protected String getHelpText() {
		String help;
		
		try {
			help = Resources.readResource(this.helpFilePath());
			String grammar;
			
			try {
				grammar = Resources.readResource(this.grammarFilePath());
			}
			catch(IOException argh) {
				this.getLogger().logError(argh);
				grammar = CANT_READ_GRAMMAR_FILE;
			}
			
			help = help.replace(INSERT_GRAMMAR_HERE, grammar);
		}
		catch(IOException argh) {
			this.getLogger().logError(argh);
			help = CANT_READ_HELP_FILE;
		}
		
		return help;
	}
	
	protected abstract String helpFilePath();
	
	protected abstract String grammarFilePath();
	
}
