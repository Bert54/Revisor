package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.IntegerConfigPanel;

/**
 * @author William Philbert
 */
public class IntegerConfig extends ConfigItem<Integer> {
	
	// Constructors :
	
	public IntegerConfig(String name, Integer defaultValue) {
		super(name, defaultValue);
	}
	
	public IntegerConfig(String name, Integer defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Integer defaultNullValue() {
		return 0;
	}
	
	@Override
	public Integer fromString(String str) throws IllegalArgumentException {
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal integer value: " + str, argh);
		}
	}
	
	@Override
	public String toString(Integer value) {
		return String.valueOf(value);
	}
	
	@Override
	public IntegerConfigPanel getSwingComponent() {
		return new IntegerConfigPanel(this);
	}
	
}
