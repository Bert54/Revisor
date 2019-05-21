package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Component;
import java.util.Observable;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.UpdateType;
import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.ComponentList;

/**
 * @author William Philbert
 */
public class InstructionList<C extends RevisorConsole<C, ?, ?, Instruction<C>>> extends ComponentList<Instruction<C>> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final GuiConfig config;
	protected final C console;
	
	// Constructors :
	
	public InstructionList(final C console, final GuiConfig config) {
		super(console.getInstructions());
		this.console = console;
		this.config = config;
		
		console.addObserver(this);
	}
	
	// Methods :
	
	@Override
	protected Component createComponent(final Instruction<C> element, final int index) {
		return new InstructionPanel<>(element, this.config, this.console.getConfig());
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if((obs == this.console) && (obj == UpdateType.ADD)) {
			this.recreateComponents(this.nbComponents());
		}
	}
	
}
