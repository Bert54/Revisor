package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Component;

import javax.swing.JFormattedTextField;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.FloatConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.FormattedTextField;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;

/**
 * @author William Philbert
 */
public class FloatConfigPanel extends ConfigItemPanel<Float, FloatConfig> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final JFormattedTextField textField;
	
	// Constructors :
	
	public FloatConfigPanel(FloatConfig config) {
		super(config);
		
		this.textField = new FormattedTextField(new Float(0));
		// TODO - GUI - Lors de l'edition, il manque certains chiffres apres la virgule. Le probleme arrive pour les Float et les Double...
		this.textField.setColumns(GuiConstants.DEFAULT_TEXT_FIELD_SIZE);
		Layout.add(this.mainPanel, this.textField, 1, 0, 1, 0, Layout.CENTER, Layout.HORIZONTAL, 2);
	}
	
	// Methods :
	
	@Override
	protected Float getValue() {
		Object value = this.textField.getValue();
		return (value instanceof Float) ? ((Float) value) : null;
	}
	
	@Override
	protected void setValue(Float value) {
		this.textField.setValue(value);
	}
	
	@Override
	public Component getFieldComponent() {
		return this.textField;
	}
	
}
