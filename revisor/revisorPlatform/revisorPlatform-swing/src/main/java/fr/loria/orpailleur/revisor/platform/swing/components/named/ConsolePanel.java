package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.Observer;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Load;
import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiUtils;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.ScrollPane;
import fr.loria.orpailleur.revisor.platform.swing.components.input.ConsoleInput;
import fr.loria.orpailleur.revisor.platform.swing.components.input.ValidityPanel;
import fr.loria.orpailleur.revisor.platform.swing.components.instruction.InstructionList;

/**
 * @author William Philbert
 */
public class ConsolePanel<C extends RevisorConsole<C, ?, ?, Instruction<C>>> extends NamedPanelTab implements Observer {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	public static final String CANT_SAVE_INSTRUCTIONS = "Couldn't save instructions.";
	public static final String CANT_LOAD_INSTRUCTIONS = "Couldn't load instructions.";
	
	// Fields :
	
	protected final String name;
	protected final C console;
	protected final GuiConfig config;
	protected final ConsoleInput<C> input;
	protected final ScrollPane scrollPane;
	
	// Constructors :
	
	public ConsolePanel(final C console, final GuiConfig config) {
		this.name = console.getName();
		this.console = console;
		this.config = config;
		config.addObserver(this);
		
		this.scrollPane = ScrollPane.createVerticalScrollPane();
		Layout.add(this, this.scrollPane, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		
		InstructionList<C> instructionList = new InstructionList<>(console, config);
		Layout.add(this.scrollPane.getPanel(), instructionList, 0, 0, 1, 0, Layout.CENTER, Layout.HORIZONTAL, 2);
		
		Panel inputPanel = new Panel();
		Layout.add(this.scrollPane.getPanel(), inputPanel, 0, 1, 1, 1, Layout.NORTH, Layout.HORIZONTAL, 0);
		
		this.input = new ConsoleInput<>(console, config);
		Layout.add(inputPanel, this.input, 1, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		
		ValidityPanel indicator = new ValidityPanel(this.input.getObservableState(), config);
		Layout.add(inputPanel, indicator, 0, 0, 0, 1, Layout.CENTER, Layout.BOTH, 2);
		
		this.update(null, null);
	}
	
	// Getters :
	
	public C getConsole() {
		return this.console;
	}
	
	// Methods :
	
	public void loadCommands() {
		File file = null;
		
		try {
			file = GuiUtils.showFileChooser(this, false);
			
			if(file != null) {
				String path = file.getPath();
				this.console.executeInstruction(new Load<>(this.console, "load " + path, path));
				GuiUtils.showInfo(this, GuiConstants.COMMANDS_LOADED, GuiConstants.SUCCESS);
			}
		}
		catch(Exception argh) {
			this.console.getLogger().logError(argh, CANT_LOAD_INSTRUCTIONS);
			GuiUtils.showError(this, argh.getMessage(), CANT_LOAD_INSTRUCTIONS);
		}
	}
	
	public void saveCommands() {
		try {
			boolean done = false;
			
			while(!done) {
				File file = GuiUtils.showFileChooser(this, true);
				
				if(file != null) {
					if(GuiUtils.WORKING_DIR.equals(file.getParentFile())) {
						GuiUtils.showError(this, GuiConstants.CANT_WRITE_IN_PROGRAM_MAIN_DIR, GuiConstants.ERROR);
					}
					else {
						boolean export = true;
						
						if(file.exists()) {
							int choice = GuiUtils.showOptions(this, String.format(GuiConstants.FILE_X_ALREADY_EXISTS, file), GuiConstants.FILE_ALREADY_EXISTS, 1, GuiConstants.OVERWRITE, GuiConstants.CANCEL);
							export = (choice == 0);
						}
						
						if(export) {
							done = true;
							boolean formatInput = this.config.formatInput.getValue();
							boolean recusiveSave = this.config.recursiveSave.getValue();
							
							try(BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
								for(Instruction<C> instruction : this.console.getInstructionsToSave(recusiveSave)) {
									output.write(formatInput ? instruction.getFormatedInputText() : instruction.getInputText());
									output.newLine();
								}
							}
							
							GuiUtils.showInfo(this, GuiConstants.COMMANDS_SAVED, GuiConstants.SUCCESS);
						}
					}
				}
				else {
					done = true;
				}
			}
		}
		catch(Exception argh) {
			this.console.getLogger().logError(argh, CANT_SAVE_INSTRUCTIONS);
			GuiUtils.showError(this, argh.getMessage(), CANT_SAVE_INSTRUCTIONS);
		}
	}
	
	@Override
	protected void gainFocus(NamedPanelTab oldTab) {
		this.input.requestFocus();
	}
	
	@Override
	protected boolean looseFocus(NamedPanelTab newTab) {
		return true;
	}
	
	@Override
	public String getPanelName() {
		return this.name;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.scrollPane.getViewport().setBackground(this.config.consoleBackground.getValue());
	}
	
}
