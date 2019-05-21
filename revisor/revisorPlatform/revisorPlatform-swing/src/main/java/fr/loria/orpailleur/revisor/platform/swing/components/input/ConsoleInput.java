package fr.loria.orpailleur.revisor.platform.swing.components.input;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;

/**
 * @author William Philbert
 */
public class ConsoleInput<C extends RevisorConsole<C, ?, ?, ?>> extends JTextArea implements DocumentListener, Observer {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	private static final String TYPED_ENTER = "ENTER";
	private static final String TYPED_UP = "UP";
	private static final String TYPED_DOWN = "DOWN";
	
	private static final String ENTER_ACTION = "ENTER action";
	private static final String UP_ACTION = "UP action";
	private static final String DOWN_ACTION = "DOWN action";
	
	// Fields :
	
	protected final C console;
	protected final GuiConfig config;
	protected final ObservableInputState observableState;
	protected final RevisorLogger logger;
	
	protected int currentCommand = 0;
	protected boolean canSendComand = true;
	
	// Constructors :
	
	public ConsoleInput(C console, final GuiConfig config) {
		this.console = console;
		this.config = config;
		this.observableState = new ObservableInputState();
		this.logger = RevisorLogger.instance();
		
		config.addObserver(this);
		
		this.setBackground(null);
		this.setRows(1);
		this.setEditable(true);
		this.setLineWrap(true);
		this.setWrapStyleWord(false);
		this.getDocument().addDocumentListener(this);
		
		InputMap inputMap = this.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = this.getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke(TYPED_ENTER), ENTER_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(TYPED_UP), UP_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(TYPED_DOWN), DOWN_ACTION);
		
		actionMap.put(ENTER_ACTION, new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConsoleInput.this.sendCommand();
			}
			
		});
		
		actionMap.put(UP_ACTION, new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConsoleInput.this.previousCommand();
			}
			
		});
		
		actionMap.put(DOWN_ACTION, new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConsoleInput.this.nextCommand();
			}
			
		});
		
		this.update(null, null);
	}
	
	// Getters :
	
	public ObservableInputState getObservableState() {
		return this.observableState;
	}
	
	public int getCurrentCommand() {
		return this.currentCommand;
	}
	
	// Setters :
	
	protected void setCurrentCommand(int currentCommand) {
		int min = 0;
		int max = this.console.nbInstructions();
		
		if(currentCommand < min) {
			this.currentCommand = min;
		}
		else if(currentCommand > max) {
			this.currentCommand = max;
		}
		else {
			this.currentCommand = currentCommand;
		}
		
		Instruction<C> instruction;
		
		if(this.currentCommand < max) {
			instruction = this.console.getInstruction(this.currentCommand);
		}
		else {
			instruction = null;
		}
		
		String text;
		
		if(instruction == null) {
			text = "";
		}
		else if(this.config.formatInput.getValue()) {
			text = instruction.getFormatedInputText();
		}
		else {
			text = instruction.getInputText();
		}
		
		this.setText(text);
	}
	
	// Methods :
	
	protected void checkCommandValidity() {
		String command = this.getText().trim();
		InputState state;
		
		if(command.isEmpty()) {
			state = InputState.NEUTRAL;
		}
		else {
			Instruction<C> instruction = this.console.parseCommand(command);
			instruction.validate(false);
			
			if(!instruction.isValid()) {
				state = InputState.INVALID;
			}
			else if(!instruction.getWarningMessages().isEmpty()) {
				state = InputState.WARNING;
			}
			else {
				state = InputState.VALID;
			}
		}
		
		this.observableState.setState(state);
	}
	
	protected void sendCommand() {
		String command = this.getText().trim();
		
		if(!command.isEmpty()) {
			this.canSendComand = false;
			this.console.executeCommand(this.getText().trim());
			this.setCurrentCommand(this.console.nbInstructions());
			this.canSendComand = true;
		}
	}
	
	protected void previousCommand() {
		this.setCurrentCommand(this.currentCommand - 1);
	}
	
	protected void nextCommand() {
		this.setCurrentCommand(this.currentCommand + 1);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.checkCommandValidity();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		this.checkCommandValidity();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		this.checkCommandValidity();
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		Color color = this.config.consoleNormalText.getValue();
		this.setForeground(color);
		this.setCaretColor(color);
	}
	
}
