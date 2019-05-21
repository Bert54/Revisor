package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Color;

import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;

/**
 * @author William Philbert
 */
public class ErrorComponent extends InstructionComponent {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public ErrorComponent(final GuiConfig config, final String text) {
		super(config, text, null);
	}
	
	// Methods :
	
	@Override
	protected Color getTextColor() {
		return this.config.consoleErrorText.getValue();
	}
	
	@Override
	protected Color getBorderColor() {
		return this.config.consoleErrorBorder.getValue();
	}
	
}
