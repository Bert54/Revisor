package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.DoubleConfigPanel;

/**
 * @author William Philbert
 */
public class DoubleConfig extends ConfigItem<Double> {
	
	// Constructors :
	
	public DoubleConfig(String name, Double defaultValue) {
		super(name, defaultValue);
	}
	
	public DoubleConfig(String name, Double defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Double defaultNullValue() {
		return 0D;
	}
	
	@Override
	public Double fromString(String str) throws IllegalArgumentException {
		try {
			return Double.parseDouble(str);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal double value: " + str, argh);
		}
	}
	
	@Override
	public String toString(Double value) {
		return String.valueOf(value);
	}
	
	@Override
	public DoubleConfigPanel getSwingComponent() {
		return new DoubleConfigPanel(this);
	}
	
}
