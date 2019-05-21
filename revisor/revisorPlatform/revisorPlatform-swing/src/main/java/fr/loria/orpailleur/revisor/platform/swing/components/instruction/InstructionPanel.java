package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.UpdateType;
import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;
import fr.loria.orpailleur.revisor.engine.core.utils.config.EngineConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;

/**
 * @author William Philbert
 */
public class InstructionPanel<C extends RevisorConsole<C, ?, ?, Instruction<C>>> extends Panel implements Observer {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final Instruction<C> instruction;
	protected final GuiConfig guiConfig;
	protected final EngineConfig engineConfig;
	protected final List<InstructionPanel<C>> children = new LinkedList<>();
	
	// Constructors :
	
	public InstructionPanel(final Instruction<C> instruction, final GuiConfig config, final EngineConfig engineConfig) {
		this.instruction = instruction;
		this.guiConfig = config;
		this.engineConfig = engineConfig;
		
		this.instruction.addObserver(this);
		this.guiConfig.addObserver(this);
		this.engineConfig.addObserver(this);
		this.update(null, null);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if(obj instanceof UpdateType) {
			UpdateType type = (UpdateType) obj;
			
			if(type == UpdateType.VISIBILITY) {
				this.setVisible(this.instruction.isVisible());
			}
			else if(type == UpdateType.ADD) {
				this.children.add(new InstructionPanel<>(this.instruction.getLastChild(), this.guiConfig, this.engineConfig));
			}
		}
		
		if(this.isVisible()) {
			this.removeAll();
			final List<Component> components = new LinkedList<>();
			
			String inputText = this.guiConfig.formatInput.getValue() ? this.instruction.getFormatedInputText() : this.instruction.getInputText();
			components.add(new InputComponent(this.guiConfig, inputText));
			
			if(this.instruction.hasPreExecutionMessage()) {
				String preExecutionMessage = this.instruction.getPreExecutionMessage();
				components.add(new OutputComponent(this.guiConfig, preExecutionMessage, null));
			}
			
			for(InstructionPanel<C> child : this.children) {
				components.add(child);
			}
			
			if(this.instruction.hasWarningMessages()) {
				String warningMessages = this.instruction.getWarningMessages();
				components.add(new WarningComponent(this.guiConfig, warningMessages));
			}
			
			if(this.instruction.hasErrorMessages()) {
				String errorMessages = this.instruction.getErrorMessages();
				components.add(new ErrorComponent(this.guiConfig, errorMessages));
			}
			
			boolean latex = this.guiConfig.displayLatex.getValue() && !this.instruction.getOutputLatex().isEmpty();
			
			if(latex || !this.instruction.getOutputText().isEmpty()) {
				String outputText = this.instruction.getOutputText();
				String outputLatex = latex ? this.instruction.getOutputLatex() : null;
				components.add(new OutputComponent(this.guiConfig, outputText, outputLatex));
				
				if(this.guiConfig.displaySideNotes.getValue()) {
					for(SideNote<C> sideNote : this.instruction.getSideNotes()) {
						if(sideNote.isDisplayed(this.instruction.getConsole())) {
							if(sideNote.isFailed()) {
								outputText = sideNote.getWarning();
								components.add(new WarningComponent(this.guiConfig, outputText));
							}
							else {
								outputText = sideNote.toString();
								outputLatex = latex ? sideNote.toLatex() : null;
								components.add(new OutputComponent(this.guiConfig, outputText, outputLatex));
							}
						}
					}
				}
			}
			
			for(Component component : components) {
				Layout.add(this, component, 0, Layout.RELATIVE, 1, 0, Layout.CENTER, Layout.HORIZONTAL, (component instanceof InstructionPanel) ? 0 : 2);
			}
			
			this.revalidate();
			this.repaint();
		}
	}
	
}
