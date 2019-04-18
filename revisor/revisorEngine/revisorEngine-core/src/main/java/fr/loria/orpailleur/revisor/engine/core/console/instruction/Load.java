package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Environment;

/**
 * @author William Philbert
 */
public class Load<C extends RevisorConsole<C, ?, ?, Instruction<C>>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String COMMENT_START = "#";
	
	private static final String LOADING_INSTRUCTIONS_FROM_FILE_X = "Loading instructions from file '%s' ...";
	private static final String DONE_LOADING_INSTRUCTIONS_FROM_FILE_X = "Done loading instructions from file '%s'.";
	private static final String ERROR_IN_FILE_X_AT_LINE_Y = "Error in file '%s' at line %d.";
	
	private static final String FILE_X_DOES_NOT_EXIST = "The file '%s' doesn't seem to exist.";
	private static final String CANT_FIND_FILE_X = "Can't find file '%s'.";
	
	// Fields :
	
	protected final String file;
	
	// Constructors :
	
	public Load(final C console, final String inputText, final String file) {
		super(console, inputText);
		this.file = file.trim();
	}
	
	// Methods :
	
	@Override
	public List<Instruction<C>> getInstructionsToSave(boolean recursiveSave) {
		LinkedList<Instruction<C>> result = new LinkedList<>();
		
		if(this.isVisible()) {
			if(recursiveSave) {
				result.add(this);
			}
			else {
				for(Instruction<C> child : this.children) {
					result.addAll(child.getInstructionsToSave(recursiveSave));
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean hasMultipleTasks() {
		return true;
	}
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		try {
			File file = this.console.getEnvironment().getCanonicalFile(new File(this.file));
			
			if(!file.isFile()) {
				this.addWarningMessage(String.format(FILE_X_DOES_NOT_EXIST, this.file));
			}
		}
		catch(SecurityException | IOException argh) {
			this.console.getLogger().logError(argh);
			this.addWarningMessage(String.format(CANT_FIND_FILE_X, this.file));
		}
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		boolean back = false;
		
		try {
			Environment env = this.console.getEnvironment();
			File file = env.getCanonicalFile(new File(this.file));
			back = env.goToParent(file);
			
			List<String> commands = this.readFile(file);
			this.executeCommands(commands);
		}
		catch(IllegalArgumentException | SecurityException | IOException argh) {
			throw new InstructionExecutionException(argh);
		}
		finally {
			if(back) {
				this.console.getEnvironment().back();
			}
		}
		
	}
	
	protected List<String> readFile(File file) throws InstructionExecutionException {
		List<String> commands = new LinkedList<>();
		
		try(BufferedReader input = new BufferedReader(new FileReader(file))) {
			for(String line = input.readLine(); line != null; line = input.readLine()) {
				String command = line.split(COMMENT_START)[0].trim();
				
				if(!command.isEmpty()) {
					commands.add(command);
				}
			}
		}
		catch(IOException argh) {
			throw new InstructionExecutionException(argh.getMessage(), argh);
		}
		
		return commands;
	}
	
	protected void executeCommands(final List<String> commands) throws InstructionExecutionException {
		for(int i = 0; i < commands.size(); i++) {
			Instruction<C> instruction = this.console.parseCommand(commands.get(i));
			this.addChild(instruction);
			instruction.validate();
			instruction.execute();
			
			if(!instruction.isSuccessful()) {
				throw new InstructionExecutionException(String.format(ERROR_IN_FILE_X_AT_LINE_Y, this.file, i + 1));
			}
		}
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("load %s", this.file);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
	@Override
	protected String createOutputText() {
		return this.hasErrorMessages() ? "" : String.format(DONE_LOADING_INSTRUCTIONS_FROM_FILE_X, this.file);
	}
	
	@Override
	protected String createPreExecutionMessage() {
		return String.format(LOADING_INSTRUCTIONS_FROM_FILE_X, this.file);
	}
	
}
