package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.LongConfigPanel;

/**
 * @author William Philbert
 */
public class LongConfig extends ConfigItem<Long> {
	
	// Constructors :
	
	public LongConfig(String name, Long defaultValue) {
		super(name, defaultValue);
	}
	
	public LongConfig(String name, Long defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Long defaultNullValue() {
		return 0L;
	}
	
	@Override
	public Long fromString(String str) throws IllegalArgumentException {
		try {
			return Long.parseLong(str);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal long value: " + str, argh);
		}
	}
	
	@Override
	public String toString(Long value) {
		return String.valueOf(value);
	}
	
	@Override
	public LongConfigPanel getSwingComponent() {
		return new LongConfigPanel(this);
	}
	
}
