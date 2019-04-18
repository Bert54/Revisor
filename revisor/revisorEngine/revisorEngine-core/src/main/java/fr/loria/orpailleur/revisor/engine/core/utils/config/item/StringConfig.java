package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.StringConfigPanel;

/**
 * @author William Philbert
 */
public class StringConfig extends ConfigItem<String> {
	
	// Constructors :
	
	public StringConfig(String name, String defaultValue) {
		super(name, defaultValue);
	}
	
	public StringConfig(String name, String defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected String defaultNullValue() {
		return "";
	}
	
	@Override
	public String fromString(String str) throws IllegalArgumentException {
		return str;
	}
	
	@Override
	public String toString(String value) {
		return value;
	}
	
	@Override
	public StringConfigPanel getSwingComponent() {
		return new StringConfigPanel(this);
	}
	
}
