package fr.loria.orpailleur.revisor.engine.core.console;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.core.RevisorEngine;
import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.UpdateType;
import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.Configuration;
import fr.loria.orpailleur.revisor.engine.core.utils.config.EngineConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Environment;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;

/**
 * @author William Philbert
 */
public abstract class AbstractRevisorConsole<C extends AbstractRevisorConsole<C, E, P, I>, E extends RevisorEngine, P extends EngineConfig, I extends Instruction<C>> extends Observable implements RevisorConsole<C, E, P, I> {
	
	// Constants :
	
	private static final String CANT_INIT_CONSOLE = "Couldn't initilize console.";
	
	// Fields :
	
	private final E engine;
	private final P config;
	private final Environment environment;
	private final RevisorLogger logger;
	private final NumberFormat numberFormatter;
	private final List<I> instructions;
	private final Set<String> variables;
	
	// Constructors :
	
	protected AbstractRevisorConsole() throws ConsoleInitializationException {
		this(null, null);
	}
	
	protected AbstractRevisorConsole(final E engine) throws ConsoleInitializationException {
		this(engine, null);
	}
	
	protected AbstractRevisorConsole(final P config) throws ConsoleInitializationException {
		this(null, config);
	}
	
	protected AbstractRevisorConsole(final E engine, final P config) throws ConsoleInitializationException {
		this.logger = this.newLogger();
		this.engine = (engine != null) ? engine : this.newEngine();
		this.config = (config != null) ? config : this.newConfig();
		this.environment = this.newEnvironment();
		
		try {
			this.config.init();
		}
		catch(Exception argh) {
			this.logger.logError(argh, Configuration.CANT_INIT_CONFIGURATION);
		}
		
		this.config.addObserver(this);
		
		this.numberFormatter = this.newNumberFormatter();
		this.instructions = this.newInstructionList();
		this.variables = this.newVariableSet();
	}
	
	// Getters :
	
	@Override
	public E getEngine() {
		return this.engine;
	}
	
	@Override
	public P getConfig() {
		return this.config;
	}
	
	@Override
	public Environment getEnvironment() {
		return this.environment;
	}
	
	@Override
	public RevisorLogger getLogger() {
		return this.logger;
	}
	
	// Methods :
	
	@Override
	public String getName() {
		return "Revisor " + this.getShortName();
	}
	
	@SuppressWarnings("unchecked")
	protected C self() {
		return (C) this;
	}
	
	protected abstract E newEngine();
	
	protected abstract P newConfig();
	
	protected Environment newEnvironment() throws ConsoleInitializationException {
		try {
			return new Environment();
		}
		catch(IllegalArgumentException | SecurityException | IOException argh) {
			throw new ConsoleInitializationException(CANT_INIT_CONSOLE, argh);
		}
	}
	
	protected RevisorLogger newLogger() {
		return RevisorLogger.instance();
	}
	
	protected NumberFormat newNumberFormatter() {
		return GuiConstants.DEFAULT_NUMBER_FORMAT;
	}
	
	@Override
	public List<I> newInstructionList() {
		return new LinkedList<>();
	}
	
	@Override
	public Set<String> newVariableSet() {
		return new TreeSet<>();
	}
	
	@Override
	public List<I> getInstructions() {
		return Collections.unmodifiableList(this.instructions);
	}
	
	@Override
	public List<Instruction<C>> getInstructionsToSave() {
		return this.getInstructionsToSave(true);
	}
	
	@Override
	public List<Instruction<C>> getInstructionsToSave(boolean recursiveSave) {
		LinkedList<Instruction<C>> result = new LinkedList<>();
		
		for(I instruction : this.instructions) {
			result.addAll(instruction.getInstructionsToSave(recursiveSave));
		}
		
		return result;
	}
	
	@Override
	public int nbInstructions() {
		return this.instructions.size();
	}
	
	@Override
	public I getInstruction(final int i) {
		return this.instructions.get(i);
	}
	
	@Override
	public I getLastInstruction() {
		int size = this.instructions.size();
		
		if(size > 0) {
			return this.instructions.get(size - 1);
		}
		
		return null;
	}
	
	@Override
	public boolean isValidCommand(final String command) {
		try {
			I instruction = this.doParseCommand(command);
			instruction.validate(false);
			return instruction.isValid();
		}
		catch(Exception argh) {
			return false;
		}
	}
	
	@Override
	public I executeCommand(final String command) {
		I instruction = this.parseCommand(command);
		this.doExecuteInstruction(instruction);
		return instruction;
	}
	
	@Override
	public I parseCommand(final String command) {
		I instruction;
		
		try {
			instruction = this.doParseCommand(command);
		}
		catch(Exception argh) {
			instruction = this.invalidInstruction(command, argh.getMessage());
		}
		
		return instruction;
	}
	
	protected abstract I doParseCommand(final String command) throws Exception;
	
	@Override
	public void executeInstruction(final I instruction) {
		if((instruction.getConsole() == this) && !this.instructions.contains(instruction)) {
			this.doExecuteInstruction(instruction);
		}
	}
	
	protected void doExecuteInstruction(final I instruction) {
		this.instructions.add(instruction);
		
		this.setChanged();
		this.notifyObservers(UpdateType.ADD);
		
		instruction.validate();
		instruction.execute();
		
	}
	
	@Override
	public Set<String> getVariables() {
		return Collections.unmodifiableSet(this.variables);
	}
	
	@Override
	public boolean isVariable(final String name) {
		return this.variables.contains(name);
	}
	
	@Override
	public void registerVariables(final Set<String> vars) {
		for(String var : vars) {
			this.registerVariable(var);
		}
	}
	
	@Override
	public void registerVariable(final String var) {
		if(!this.isUsedName(var)) {
			this.variables.add(var);
		}
	}
	
	@Override
	public boolean isMacro(final String name) {
		return this.getMacros().contains(name);
	}
	
	@Override
	public boolean isUsedName(final String name) {
		return this.isVariable(name) || this.isMacro(name);
	}
	
	@Override
	public void clear() {
		for(I instruction : this.instructions) {
			instruction.setVisible(false);
		}
		
		this.variables.clear();
	}
	
	@Override
	public String format(final double number) {
		return this.numberFormatter.format(number);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		this.setChanged();
		this.notifyObservers(UpdateType.CONFIG);
	}
	
}
