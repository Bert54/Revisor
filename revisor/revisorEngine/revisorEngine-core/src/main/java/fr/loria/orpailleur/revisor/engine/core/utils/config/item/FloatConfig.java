package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.FloatConfigPanel;

/**
 * @author William Philbert
 */
public class FloatConfig extends ConfigItem<Float> {
	
	// Constructors :
	
	public FloatConfig(String name, Float defaultValue) {
		super(name, defaultValue);
	}
	
	public FloatConfig(String name, Float defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Float defaultNullValue() {
		return 0F;
	}
	
	@Override
	public Float fromString(String str) throws IllegalArgumentException {
		try {
			return Float.parseFloat(str);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal float value: " + str, argh);
		}
	}
	
	@Override
	public String toString(Float value) {
		return String.valueOf(value);
	}
	
	@Override
	public FloatConfigPanel getSwingComponent() {
		return new FloatConfigPanel(this);
	}
	
}
