package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;
import fr.loria.orpailleur.revisor.engine.core.utils.IObservable;
import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;

/**
 * @author William Philbert
 */
public interface Instruction<C extends RevisorConsole<C, ?, ?, ?>> extends IObservable {
	
	/**
	 * Returns the console for this instruction. The console contains all the environment data.
	 * @return the console for this instruction.
	 */
	public C getConsole();
	
	/**
	 * Returns the parent instruction of this instruction. Can be null.
	 * @return the parent instruction of this instruction.
	 */
	public Instruction<C> getParent();
	
	/**
	 * Sets the parent instruction of this instruction.
	 * @param parent - the new parent of this instruction.
	 */
	public void setParent(Instruction<C> parent);
	
	/**
	 * Returns the children instructions of this instruction. Can be empty.
	 * @return the children instructions of this instruction.
	 */
	public Collection<Instruction<C>> getChildren();
	
	/**
	 * Add a child instruction to this instruction.
	 * @param newChild - the new child instruction.
	 */
	public void addChild(Instruction<C> newChild);
	
	/**
	 * Returns the text entered by the user.
	 * @return the text entered by the user.
	 */
	public String getInputText();
	
	/**
	 * Returns a set containing the names of the new propositional variables introduced by this instruction.
	 * @return a set containing the names of the new propositional variables introduced by this instruction.
	 */
	public Set<String> getNewVars();
	
	/**
	 * Returns the text entered by the user, formated to get a standard display.
	 * @return the text entered by the user, formated to get a standard display.
	 */
	public String getFormatedInputText();
	
	/**
	 * Returns the output of this instruction, in text form. Can be empty.
	 * @return the output of this instruction, in text form.
	 */
	public String getOutputText();
	
	/**
	 * Returns the output of this instruction, in latex form. Can be empty.
	 * @return the output of this instruction, in latex form.
	 */
	public String getOutputLatex();
	
	/**
	 * Returns the output of this instruction, in text/latex form, depending on the argument.
	 * @param latex - true for the latex version, false for the text version.
	 * @return the output of this instruction, in text/latex form.
	 */
	public String getOutput(boolean latex);
	
	/**
	 * Returns the error messages for the errors that occured while parsing, semantically validating or executing the instruction. Can be empty.
	 * @return the error messages for the errors that occured while parsing, semantically validating or executing the instruction.
	 */
	public String getErrorMessages();
	
	/**
	 * Returns the warning messages for this instruction. Can be empty.
	 * @return the warning messages for this instruction.
	 */
	public String getWarningMessages();
	
	/**
	 * Returns a message to display before the instruction is executed. Can be empty.
	 * @return a message to display before the instruction is executed.
	 */
	public String getPreExecutionMessage();
	
	/**
	 * Returns the list of the side notes of this instruction. Side notes can be diplayed in text or in latex.
	 * @return the list of the side notes of this instruction.
	 */
	public List<SideNote<C>> getSideNotes();
	
	/**
	 * Indicates whether this instruction was checked.
	 * @return true if this instruction was checked, else false.
	 */
	public boolean isChecked();
	
	/**
	 * Indicates whether this instruction was checked and declared semantically valid.
	 * @return true if this instruction was checkedand declared semantically valid, else false.
	 */
	public boolean isValid();
	
	/**
	 * Indicates whether this instruction was executed.
	 * @return true if this instruction was executed, else false.
	 */
	public boolean isExecuted();
	
	/**
	 * Indicates whether this instruction was successfully executed.
	 * @return true if this instruction was successfully executed, else false.
	 */
	public boolean isSuccessful();
	
	/**
	 * Indicates whether this instruction should be displayed.
	 * @return true if this instruction should be displayed, else false.
	 */
	public boolean isVisible();
	
	/**
	 * Sets whether this instruction should be displayed.
	 * @param visible - true if this instruction should be displayed, else false.
	 */
	public void setVisible(boolean visible);
	
	/**
	 * Sets whether this instruction and all its children should be displayed.
	 * @param visible - true if this instruction and all its children should be displayed, else false.
	 */
	public void setVisibleRecursively(boolean visible);
	
	/**
	 * Returns the list of instructions which must be saved when export is used.
	 * @param recursiveSave - true if load instructions must be saved, false if instructions loaded by load instructions must be saved.
	 * @return the list of instructions which must be saved when export is used.
	 */
	public List<Instruction<C>> getInstructionsToSave(boolean recursiveSave);
	
	/**
	 * Returns the last child of this instruction, if there is one, else null.
	 * @return the last child of this instruction.
	 */
	public Instruction<C> getLastChild();
	
	/**
	 * Indicates whether this instruction has at least one child.
	 * @return true if this instruction has at least one child, else false.
	 */
	public boolean hasChild();
	
	/**
	 * Indicates whether this instruction has a parent.
	 * @return true if this instruction has a parent, else false.
	 */
	public boolean hasParent();
	
	/**
	 * Indicates if this instruction has multiple tasks to execute (ex: "load" instruction creates and executes multiple other instructions).
	 * This is used to know if a timeout can be set on the execution of this instruction.
	 * @return true if this instruction has multiple tasks to execute, else false.
	 */
	public boolean hasMultipleTasks();
	
	/**
	 * Semantically validates this instruction.
	 * Checks that identifiers refer to the correct data types. Catches exeptions and creates error messages to display.
	 * Formats user input.
	 * Log validation exceptions.
	 */
	public void validate();
	
	/**
	 * Semantically validates this instruction.
	 * Checks that identifiers refer to the correct data types. Catches exeptions and creates error messages to display.
	 * Formats user input.
	 * @param doLog - specifies whether to log validation exceptions.
	 */
	public void validate(boolean doLog);
	
	/**
	 * Executes this instruction.
	 * Computes and stores résults in the console. Catches exeptions and creates error messages to display.
	 * Creates output text and latex to display.
	 */
	public void execute();
	
	/**
	 * Indicates whether this instruction has error messages.
	 * @return true if this instruction has error messages, else false.
	 */
	public boolean hasErrorMessages();
	
	/**
	 * Indicates whether this instruction has warning messages.
	 * @return true if this instruction has warning messages, else false.
	 */
	public boolean hasWarningMessages();
	
	/**
	 * Indicates whether this instruction has a pre-execution message.
	 * @return true if this instruction has a pre-execution message, else false.
	 */
	public boolean hasPreExecutionMessage();
	
	/**
	 * Appends an error message to the messages to display.
	 * @param message - the error message to add.
	 */
	public void addErrorMessage(String message);
	
	/**
	 * Appends a warning message to the messages to display.
	 * @param message - the warning message to add.
	 */
	public void addWarningMessage(String message);
	
	/**
	 * Appends the warning messages of the given expression to the messages to display.
	 * @param expression - an expression which may have warning messages to display.
	 */
	public void addWarningMessages(Expression<C, ?> expression);
	
	/**
	 * Return the RevisorLogger instance used by the console.
	 * @return a RevisorLogger instance.
	 */
	public RevisorLogger getLogger();
	
	/**
	 * Returns a formated String of the given number. Used to display numbers in the console.
	 * @param number - the number to format.
	 * @return a formated String of the given number.
	 */
	public String format(double number);
	
}
