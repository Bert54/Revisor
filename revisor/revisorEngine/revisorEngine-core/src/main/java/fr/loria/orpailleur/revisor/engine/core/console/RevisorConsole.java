package fr.loria.orpailleur.revisor.engine.core.console;

import java.util.List;
import java.util.Observer;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.RevisorEngine;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.utils.IObservable;
import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.EngineConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Environment;

/**
 * All implementation of this interface should extends {@link java.util.Observable}.
 * 
 * @author William Philbert
 */
public interface RevisorConsole<C extends RevisorConsole<C, E, P, I>, E extends RevisorEngine, P extends EngineConfig, I extends Instruction<C>> extends IObservable, Observer {
	
	// Console :
	
	public String getShortName();
	
	public String getName();
	
	public E getEngine();
	
	public P getConfig();
	
	public Environment getEnvironment();
	
	/**
	 * Returns the RevisorLogger instance used by this console.
	 * @return a RevisorLogger instance.
	 */
	public RevisorLogger getLogger();
	
	// Instructions :
	
	public List<I> newInstructionList();
	
	public List<I> getInstructions();
	
	public List<Instruction<C>> getInstructionsToSave();
	
	public List<Instruction<C>> getInstructionsToSave(boolean recursiveSave);
	
	public int nbInstructions();
	
	public I getInstruction(final int i);
	
	public I getLastInstruction();
	
	public boolean isValidCommand(final String command);
	
	public I parseCommand(final String command);
	
	public I executeCommand(final String command);
	
	public void executeInstruction(final I instruction);
	
	public I invalidInstruction(final String command, final String errorMessage);
	
	// Variables :
	
	public Set<String> newVariableSet();
	
	public Set<String> getVariables();
	
	public boolean isVariable(final String name);
	
	public void registerVariables(final Set<String> vars);
	
	public void registerVariable(final String var);
	
	// Macros :
	
	public Set<String> getMacros();
	
	public boolean isMacro(final String name);
	
	// Names :
	
	public boolean isUsedName(final String name);
	
	// Actions :
	
	public void clear();
	
	// Tools :
	
	/**
	 * Returns a formated String of the given number. Used to display numbers in the console.
	 * @param number - the number to format.
	 * @return a formated String of the given number.
	 */
	public String format(double number);
	
}
