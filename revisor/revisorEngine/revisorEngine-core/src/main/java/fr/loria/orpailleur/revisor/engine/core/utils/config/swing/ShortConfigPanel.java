package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Component;

import javax.swing.JFormattedTextField;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.ShortConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.FormattedTextField;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;

/**
 * @author William Philbert
 */
public class ShortConfigPanel extends ConfigItemPanel<Short, ShortConfig> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final JFormattedTextField textField;
	
	// Constructors :
	
	public ShortConfigPanel(ShortConfig config) {
		super(config);
		
		this.textField = new FormattedTextField(new Short((short) 0));
		this.textField.setColumns(GuiConstants.DEFAULT_TEXT_FIELD_SIZE);
		Layout.add(this.mainPanel, this.textField, 1, 0, 1, 0, Layout.CENTER, Layout.HORIZONTAL, 2);
	}
	
	// Methods :
	
	@Override
	protected Short getValue() {
		Object value = this.textField.getValue();
		return (value instanceof Short) ? ((Short) value) : null;
	}
	
	@Override
	protected void setValue(Short value) {
		this.textField.setValue(value);
	}
	
	@Override
	public Component getFieldComponent() {
		return this.textField;
	}
	
}
