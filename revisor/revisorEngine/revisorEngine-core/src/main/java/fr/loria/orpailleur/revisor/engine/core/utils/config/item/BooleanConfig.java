package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.BooleanConfigPanel;

/**
 * @author William Philbert
 */
public class BooleanConfig extends ConfigItem<Boolean> {
	
	// Constructors :
	
	public BooleanConfig(String name, Boolean defaultValue) {
		super(name, defaultValue);
	}
	
	public BooleanConfig(String name, Boolean defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Boolean defaultNullValue() {
		return false;
	}
	
	@Override
	public Boolean fromString(String str) throws IllegalArgumentException {
		if(str.equalsIgnoreCase(String.valueOf(true))) {
			return true;
		}
		
		if(str.equalsIgnoreCase(String.valueOf(false))) {
			return false;
		}
		
		throw new IllegalArgumentException("Illegal boolean value: " + str);
	}
	
	@Override
	public String toString(Boolean value) {
		return String.valueOf(value);
	}
	
	@Override
	public BooleanConfigPanel getSwingComponent() {
		return new BooleanConfigPanel(this);
	}
	
}
