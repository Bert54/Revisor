package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Component;

import javax.swing.JFormattedTextField;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.LongConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.FormattedTextField;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;

/**
 * @author William Philbert
 */
public class LongConfigPanel extends ConfigItemPanel<Long, LongConfig> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final JFormattedTextField textField;
	
	// Constructors :
	
	public LongConfigPanel(LongConfig config) {
		super(config);
		
		this.textField = new FormattedTextField(new Long(0));
		this.textField.setColumns(GuiConstants.DEFAULT_TEXT_FIELD_SIZE);
		Layout.add(this.mainPanel, this.textField, 1, 0, 1, 0, Layout.CENTER, Layout.HORIZONTAL, 2);
	}
	
	// Methods :
	
	@Override
	protected Long getValue() {
		Object value = this.textField.getValue();
		return (value instanceof Long) ? ((Long) value) : null;
	}
	
	@Override
	protected void setValue(Long value) {
		this.textField.setValue(value);
	}
	
	@Override
	public Component getFieldComponent() {
		return this.textField;
	}
	
}
