package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Component;

import javax.swing.JTextField;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.StringConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;

/**
 * @author William Philbert
 */
public class StringConfigPanel extends ConfigItemPanel<String, StringConfig> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final JTextField textField;
	
	// Constructors :
	
	public StringConfigPanel(StringConfig config) {
		super(config);
		
		this.textField = new JTextField(GuiConstants.DEFAULT_TEXT_FIELD_SIZE);
		Layout.add(this.mainPanel, this.textField, 1, 0, 1, 0, Layout.CENTER, Layout.HORIZONTAL, 2);
	}
	
	// Methods :
	
	@Override
	protected String getValue() {
		return this.textField.getText();
	}
	
	@Override
	protected void setValue(String value) {
		this.textField.setText(value);
	}
	
	@Override
	public Component getFieldComponent() {
		return this.textField;
	}
	
}
