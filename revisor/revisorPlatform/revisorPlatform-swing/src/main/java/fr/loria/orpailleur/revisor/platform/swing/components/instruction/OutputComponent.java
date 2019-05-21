package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Color;

import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;

/**
 * @author William Philbert
 */
public class OutputComponent extends InstructionComponent {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public OutputComponent(final GuiConfig config, final String text, final String latex) {
		super(config, text, latex);
	}
	
	// Methods :
	
	@Override
	protected Color getTextColor() {
		return this.config.consoleResultText.getValue();
	}
	
	@Override
	protected Color getBorderColor() {
		return this.config.consoleResultBorder.getValue();
	}
	
}
