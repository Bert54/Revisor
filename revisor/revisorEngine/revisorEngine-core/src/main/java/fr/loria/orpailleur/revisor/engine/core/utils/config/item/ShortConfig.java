package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.ShortConfigPanel;

/**
 * @author William Philbert
 */
public class ShortConfig extends ConfigItem<Short> {
	
	// Constructors :
	
	public ShortConfig(String name, Short defaultValue) {
		super(name, defaultValue);
	}
	
	public ShortConfig(String name, Short defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Short defaultNullValue() {
		return 0;
	}
	
	@Override
	public Short fromString(String str) throws IllegalArgumentException {
		try {
			return Short.parseShort(str);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal short value: " + str, argh);
		}
	}
	
	@Override
	public String toString(Short value) {
		return String.valueOf(value);
	}
	
	@Override
	public ShortConfigPanel getSwingComponent() {
		return new ShortConfigPanel(this);
	}
	
}
