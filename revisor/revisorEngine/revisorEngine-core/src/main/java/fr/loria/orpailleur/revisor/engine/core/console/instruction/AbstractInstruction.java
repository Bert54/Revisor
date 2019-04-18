package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;
import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.core.utils.task.Task;
import fr.loria.orpailleur.revisor.engine.core.utils.task.VoidTask;

/**
 * @author William Philbert
 */
public abstract class AbstractInstruction<C extends RevisorConsole<C, ?, ?, ?>> extends Observable implements Instruction<C> {
	
	// Fields :
	
	protected final C console;
	protected final String inputText;
	
	protected Instruction<C> parent;
	protected final List<Instruction<C>> children = new LinkedList<>();
	
	protected String formatedInputText = "";
	protected String outputText = "";
	protected String outputLatex = "";
	protected String errorMessages = "";
	protected String warningMessages = "";
	protected String preExecutionMessage = "";
	protected Set<String> newVars;
	
	protected final List<SideNote<C>> sideNotes = new LinkedList<>();
	
	private boolean checked = false;
	private boolean valid = false;
	private boolean executed = false;
	private boolean successful = false;
	private boolean visible = true;
	
	// Constructors :
	
	protected AbstractInstruction(final C console, final String inputText) {
		this.console = console;
		this.inputText = inputText;
		this.resetNewVars();
	}
	
	// Getters :
	
	@Override
	public C getConsole() {
		return this.console;
	}
	
	@Override
	public Instruction<C> getParent() {
		return this.parent;
	}
	
	@Override
	public List<Instruction<C>> getChildren() {
		return Collections.unmodifiableList(this.children);
	}
	
	@Override
	public String getInputText() {
		return this.inputText;
	}
	
	@Override
	public Set<String> getNewVars() {
		return Collections.unmodifiableSet(this.newVars);
	}
	
	@Override
	public String getFormatedInputText() {
		return this.formatedInputText;
	}
	
	@Override
	public String getOutputText() {
		return this.outputText;
	}
	
	@Override
	public String getOutputLatex() {
		return this.outputLatex;
	}
	
	@Override
	public String getErrorMessages() {
		return this.errorMessages;
	}
	
	@Override
	public String getWarningMessages() {
		return this.warningMessages;
	}
	
	@Override
	public String getPreExecutionMessage() {
		return this.preExecutionMessage;
	}
	
	@Override
	public List<SideNote<C>> getSideNotes() {
		return Collections.unmodifiableList(this.sideNotes);
	}
	
	@Override
	public boolean isChecked() {
		return this.checked;
	}
	
	@Override
	public boolean isValid() {
		return this.valid;
	}
	
	@Override
	public boolean isExecuted() {
		return this.executed;
	}
	
	@Override
	public boolean isSuccessful() {
		return this.successful;
	}
	
	@Override
	public boolean isVisible() {
		return this.visible;
	}
	
	// Setters :
	
	@Override
	public void setParent(Instruction<C> parent) {
		this.parent = parent;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(this.visible != visible) {
			this.visible = visible;
			this.setChanged();
			this.notifyObservers(UpdateType.VISIBILITY);
		}
	}
	
	// Methods :
	
	protected final void resetNewVars() {
		this.newVars = this.console.newVariableSet();
	}
	
	@Override
	public void addChild(Instruction<C> newChild) {
		if(!this.children.contains(newChild)) {
			this.children.add(newChild);
			newChild.setParent(this);
			this.setChanged();
			this.notifyObservers(UpdateType.ADD);
		}
	}
	
	@Override
	public void setVisibleRecursively(boolean visible) {
		this.setVisible(visible);
		
		for(Instruction<C> child : this.children) {
			child.setVisibleRecursively(visible);
		}
	}
	
	@Override
	public List<Instruction<C>> getInstructionsToSave(boolean recursiveSave) {
		LinkedList<Instruction<C>> result = new LinkedList<>();
		
		if(this.isVisible()) {
			result.add(this);
		}
		
		return result;
	}
	
	@Override
	public Instruction<C> getLastChild() {
		int size = this.children.size();
		
		if(size > 0) {
			return this.children.get(size - 1);
		}
		
		return null;
	}
	
	@Override
	public boolean hasChild() {
		return !this.children.isEmpty();
	}
	
	@Override
	public boolean hasParent() {
		return this.parent != null;
	}
	
	@Override
	public boolean hasMultipleTasks() {
		return false;
	}
	
	@Override
	public String getOutput(boolean latex) {
		return latex ? this.getOutputLatex() : this.getOutputText();
	}
	
	@Override
	public void validate() {
		this.validate(true);
	}
	
	@Override
	public void validate(boolean doLog) {
		if(!this.executed) {
			this.valid = false;
			this.resetNewVars();
			this.resetWarningMessages();
			
			try {
				this.formatedInputText = this.createFormatedInputText();
				this.preExecutionMessage = this.createPreExecutionMessage();
				this.doValidate();
				this.valid = true;
			}
			catch(InstructionValidationException argh) {
				this.addErrorMessage(argh.getMessage());
				
				if(doLog) {
					this.getLogger().logError(argh);
				}
			}
			
			this.checked = true;
			this.setChanged();
			this.notifyObservers(UpdateType.VALIDATE);
		}
	}
	
	@Override
	public void execute() {
		if(this.valid && !this.executed) {
			try {
				int timeout = this.hasMultipleTasks() ? 0 : this.getConsole().getConfig().instructionTimeout.getValue();
				
				if(timeout > 0) {
					final VoidTask task = new VoidTask() {
						
						@Override
						protected void execute() throws Exception {
							AbstractInstruction.this.doExecute();
						}
						
					};
					
					Task.executeTask(task, timeout, TimeUnit.SECONDS);
				}
				else {
					this.doExecute();
				}
				
				this.successful = true;
				this.outputText = this.createOutputText();
				this.outputLatex = this.createOutputLatex();
				this.clearTmpResources();
			}
			catch(Exception argh) {
				this.addErrorMessage(argh.getMessage());
				this.getLogger().logError(argh);
			}
			
			this.executed = true;
			this.setChanged();
			this.notifyObservers(UpdateType.EXECUTE);
		}
	}
	
	@Override
	public boolean hasErrorMessages() {
		return !((this.errorMessages == null) || this.errorMessages.isEmpty());
	}
	
	@Override
	public boolean hasWarningMessages() {
		return !((this.warningMessages == null) || this.warningMessages.isEmpty());
	}
	
	@Override
	public boolean hasPreExecutionMessage() {
		return !((this.preExecutionMessage == null) || this.preExecutionMessage.isEmpty());
	}
	
	@Override
	public void addErrorMessage(final String message) {
		if(this.hasErrorMessages()) {
			this.errorMessages += "\n" + message;
		}
		else {
			this.errorMessages = message;
		}
	}
	
	@Override
	public void addWarningMessage(final String message) {
		if(this.hasWarningMessages()) {
			this.warningMessages += "\n" + message;
		}
		else {
			this.warningMessages = message;
		}
	}
	
	@Override
	public void addWarningMessages(Expression<C, ?> expression) {
		if(expression != null && expression.hasWarningMessages()) {
			this.addWarningMessage(StringUtils.toString(expression.getWarningMessages(), "", "\n", ""));
		}
	}
	
	protected void resetWarningMessages() {
		this.warningMessages = "";
	}
	
	/**
	 * Used to clear ressources that are not useful anymore after the execution of the instruction.
	 */
	protected void clearTmpResources() {
		// Nothing to do in the general case.
	}
	
	/**
	 * Semantically validates this instruction. Checks that identifiers refer to the correct data types.
	 * @throws InstructionValidationException - if the instruction isn't semantically valid.
	 */
	protected abstract void doValidate() throws InstructionValidationException;
	
	/**
	 * <p>Executes this instruction. Computes and stores résults in the console.</p>
	 * <p><b>WARNING</b> : all code in this method should handle thread interruptions!</p>
	 * <code>if(Thread.currentThread().isInterrupted()) { closeResourcesAndThrowException(); }</code>
	 * <p>This is required for instruction execution timeout to work correctly.
	 * If the the thread is interrupted resources must be closed and an exception must be thrown.</p>
	 * 
	 * @throws InstructionExecutionException - if a problem occured while executing the instruction.
	 */
	protected abstract void doExecute() throws InstructionExecutionException;
	
	/**
	 * Creates a formated version of user input.
	 * @return a formated version of user input.
	 */
	protected abstract String createFormatedInputText();
	
	/**
	 * Creates a string/latex version of the output, depending on the argument.
	 * @param latex - true for the latex version, false for the text version.
	 * @return a string/latex version of the output.
	 */
	protected abstract String createOutput(boolean latex);
	
	/**
	 * Creates a text version of the output.
	 * @return a text version of the output.
	 */
	protected String createOutputText() {
		return this.createOutput(false);
	}
	
	/**
	 * Creates a latex version of the output.
	 * @return a latex version of the output.
	 */
	protected String createOutputLatex() {
		return this.createOutput(true);
	}
	
	/**
	 * Creates the pre-execution message (empty by default).
	 * @return the pre-execution message.
	 */
	protected String createPreExecutionMessage() {
		return "";
	}
	
	@Override
	public RevisorLogger getLogger() {
		return this.console.getLogger();
	}
	
	@Override
	public String format(double number) {
		return this.console.format(number);
	}
	
	/**
	 * Returns a simplified version of the given String.
	 * @param text - a String.
	 * @return a simplified version of the given String.
	 * @see StringUtils#simplifiedString(String)
	 */
	protected String simplifiedString(String text) {
		return StringUtils.simplifiedString(text);
	}
	
	@Override
	public String toString() {
		return this.getFormatedInputText();
	}
	
}
