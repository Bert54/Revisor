package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Component;

import javax.swing.JCheckBox;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.BooleanConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;

/**
 * @author William Philbert
 */
public class BooleanConfigPanel extends ConfigItemPanel<Boolean, BooleanConfig> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final JCheckBox checkBox;
	
	// Constructors :
	
	public BooleanConfigPanel(BooleanConfig config) {
		super(config);
		
		this.checkBox = new JCheckBox();
		Layout.add(this.mainPanel, this.checkBox, 1, 0, 1, 0, Layout.WEST, Layout.NONE, 2);
		this.checkBox.setBackground(null);
	}
	
	// Methods :
	
	@Override
	protected Boolean getValue() {
		return this.checkBox.isSelected();
	}
	
	@Override
	protected void setValue(Boolean value) {
		this.checkBox.setSelected(value);
	}
	
	@Override
	public Component getFieldComponent() {
		return this.checkBox;
	}
	
}
