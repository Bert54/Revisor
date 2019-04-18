package fr.loria.orpailleur.revisor.engine.core.console.mode;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.UpdateType;
import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;
import fr.loria.orpailleur.revisor.engine.core.utils.config.Configuration;
import fr.loria.orpailleur.revisor.engine.core.utils.config.ConsoleConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigFileStorage;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

/**
 * @author William Philbert
 */
public class ConsoleMode<C extends RevisorConsole<C, ?, ?, Instruction<C>>> implements Observer {
	
	// Constants :
	
	private static final String PROMPT = "> ";
	
	// Fields :
	
	protected final C console;
	protected final ConsoleConfig config;
	protected final Map<String, SpecialCommand> specialCommands = new HashMap<>();
	
	protected boolean running = false;
	protected boolean consoleMode = false;
	
	// Constructors :
	
	public ConsoleMode(C console) {
		this.console = console;
		this.console.addObserver(this);
		this.config = new ConsoleConfig(new ConfigFileStorage("console.properties"));
		
		try {
			this.config.init();
		}
		catch(Exception argh) {
			console.getLogger().logError(argh, Configuration.CANT_INIT_CONFIGURATION);
			this.println(Configuration.CANT_INIT_CONFIGURATION);
			this.println(argh.getMessage());
			this.println();
		}
		
		this.specialCommands.put("exit", new SpecialCommand() {
			
			@Override
			public String getOutput() {
				return ConsoleMode.this.getSeparator("      End of console mode       ");
			}
			
			@Override
			public void run() {
				ConsoleMode.this.consoleMode = false;
			}
			
		});
		
		this.specialCommands.put("format on", new SpecialCommand() {
			
			@Override
			public String getOutput() {
				return "A formated version of commands will now be dispayed.";
			}
			
			@Override
			public void run() {
				ConsoleMode.this.config.formatInput.setValue(true);
			}
			
		});
		
		this.specialCommands.put("format off", new SpecialCommand() {
			
			@Override
			public String getOutput() {
				return "The formated version of commands will no longer be displayed.";
			}
			
			@Override
			public void run() {
				ConsoleMode.this.config.formatInput.setValue(false);
			}
			
		});
		
		this.specialCommands.put("sidenotes on", new SpecialCommand() {
			
			@Override
			public String getOutput() {
				return "Side notes will now be dispayed.";
			}
			
			@Override
			public void run() {
				ConsoleMode.this.config.displaySideNotes.setValue(true);
			}
			
		});
		
		this.specialCommands.put("sidenotes off", new SpecialCommand() {
			
			@Override
			public String getOutput() {
				return "Side notes will no longer be displayed.";
			}
			
			@Override
			public void run() {
				ConsoleMode.this.config.displaySideNotes.setValue(false);
			}
			
		});
	}
	
	// Methods :
	
	protected void clear() {
		this.console.clear();
	}
	
	/**
	 * Starts console mode. Commands can be entered to test the console.
	 */
	public void start() {
		if(!this.running) {
			this.running = true;
			this.consoleMode = true;
			this.clear();
			
			String s1 = "          Console mode          ";
			String s2 = "type \"exit\" to terminate";
			String s3 = "type \"format on/off\" to toggle display of formated input";
			String s4 = "type \"sidenotes on/off\" to toggle display of side notes (ex: substitutions)";
			String s5 = "type \"load <File>\" to load multiple commands from a file";
			String s6 = "type \"help\" for more information";
			this.print(this.getSeparator(s1, s2, s3, s4, s5, s6));
			
			try(Scanner scanner = new Scanner(System.in)) {
				while(this.consoleMode) {
					prompt();
					String command = StringUtils.simplifiedString(scanner.nextLine());
					
					if(!command.isEmpty()) {
						SpecialCommand specialCommand = this.specialCommands.get(command);
						
						if(specialCommand != null) {
							specialCommand.run();
							this.println(specialCommand.getOutput());
							this.println();
						}
						else {
							this.console.executeCommand(command);
						}
						
						try {
							this.config.save();
						}
						catch(Exception argh) {
							this.console.getLogger().logError(argh, Configuration.CANT_SAVE_CONFIGURATION);
							this.println(Configuration.CANT_SAVE_CONFIGURATION);
							this.println(argh.getMessage());
							this.println();
						}
					}
				}
			}
			
			this.consoleMode = false;
			this.running = false;
		}
	}
	
	/**
	 * Print an adaptation example in the standard output.
	 * @param commands - a list of commands to execute.
	 * @param title - a title for the example.
	 * @param options - an optional list of lines to diplay under the title.
	 */
	public void printExample(final Collection<String> commands, final String title, final String... options) {
		if(!this.running) {
			this.running = true;
			this.clear();
			this.println(this.getSeparator(title, options));
			
			for(String command : commands) {
				this.console.executeCommand(command);
			}
			
			this.running = false;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable obs, Object obj) {
		if(obj instanceof UpdateType) {
			UpdateType type = (UpdateType) obj;
			
			if(obs == this.console) {
				if(type == UpdateType.ADD) {
					Instruction<C> newInstruction = this.console.getLastInstruction();
					
					if(newInstruction != null) {
						newInstruction.addObserver(this);
					}
				}
			}
			else if(obs instanceof Instruction) {
				Instruction<C> instruction = (Instruction<C>) obs;
				
				if(type == UpdateType.ADD) {
					Instruction<C> newInstruction = instruction.getLastChild();
					
					if(newInstruction != null) {
						newInstruction.addObserver(this);
					}
				}
				else if(type == UpdateType.VALIDATE) {
					if(instruction.isValid()) {
						this.print(this.getInstructionFirstPart(instruction));
					}
					else {
						this.print(this.toString(instruction));
						instruction.deleteObserver(this);
					}
				}
				else if(type == UpdateType.EXECUTE) {
					this.print(this.getInstructionSecondPart(instruction));
					instruction.deleteObserver(this);
				}
			}
		}
	}
	
	/**
	 * Creates the first part of the String representation of an instruction.
	 * It contains the input and the pre-execution message.
	 * @param instruction - the instruction to represent with a String.
	 * @return the first part of the String representation of an instruction.
	 */
	public String getInstructionFirstPart(Instruction<C> instruction) {
		StringBuilder builder = new StringBuilder();
		boolean formatInput = !this.consoleMode || instruction.hasParent() || this.config.formatInput.getValue();
		
		if(formatInput) {
			builder.append("COMMAND : ");
			builder.append(instruction);
			builder.append('\n');
			builder.append('\n');
		}
		
		if(instruction.hasPreExecutionMessage()) {
			builder.append(instruction.getPreExecutionMessage());
			builder.append('\n');
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	/**
	 * Creates the second part of the String representation of an instruction.
	 * It contains the warnings, the errors and the ouput (with the side notes).
	 * @param instruction - the instruction to represent with a String.
	 * @return the first part of the String representation of an instruction.
	 */
	public String getInstructionSecondPart(Instruction<C> instruction) {
		StringBuilder builder = new StringBuilder();
		boolean displaySideNotes = !this.consoleMode || this.config.displaySideNotes.getValue();
		
		if(instruction.hasWarningMessages()) {
			builder.append(instruction.getWarningMessages());
			builder.append('\n');
			builder.append('\n');
		}
		
		if(instruction.hasErrorMessages()) {
			builder.append(instruction.getErrorMessages());
			builder.append('\n');
			builder.append('\n');
		}
		
		String output = instruction.getOutputText();
		
		if(!output.isEmpty()) {
			builder.append(output);
			builder.append('\n');
			builder.append('\n');
		}
		
		List<SideNote<C>> sideNotes = instruction.getSideNotes();
		
		if(displaySideNotes && !sideNotes.isEmpty()) {
			for(SideNote<C> sideNote : sideNotes) {
				if(sideNote.isDisplayed(instruction.getConsole())) {
					builder.append(sideNote);
					builder.append('\n');
				}
			}
			
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	/**
	 * Creates a String representing of the given instruction.
	 * @return a String representing of the given instruction.
	 */
	public String toString(Instruction<C> instruction) {
		return this.getInstructionFirstPart(instruction) + this.getInstructionSecondPart(instruction);
	}
	
	/**
	 * Creates a separator with the given title and options.
	 * @param title - the title to display in the separator.
	 * @param options - an optional list of lines to diplay under the title.
	 * @return a separator with the given title and options.
	 */
	public String getSeparator(final String title, final String... options) {
		String titleLine = "-- " + title + " --";
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < titleLine.length(); i++) {
			builder.append('-');
		}
		
		String separatorLine = builder.toString();
		builder.setLength(0);
		
		builder.append('\n');
		builder.append(separatorLine);
		builder.append('\n');
		builder.append(titleLine);
		builder.append('\n');
		builder.append(separatorLine);
		builder.append('\n');
		builder.append('\n');
		
		if(options.length > 0) {
			for(String option : options) {
				builder.append("-- ");
				builder.append(option);
				builder.append('\n');
			}
			
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	protected void print(String text) {
		System.out.print(text);
	}
	
	protected void println(String text) {
		System.out.println(text);
	}
	
	protected void println() {
		System.out.println();
	}
	
	/**
	 * Prints a command prompt in the console.
	 */
	protected void prompt() {
		this.print(PROMPT);
	}
	
}
