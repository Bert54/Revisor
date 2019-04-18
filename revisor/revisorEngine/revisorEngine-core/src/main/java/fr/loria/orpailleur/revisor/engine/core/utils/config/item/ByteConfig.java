package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.ByteConfigPanel;

/**
 * @author William Philbert
 */
public class ByteConfig extends ConfigItem<Byte> {
	
	// Constructors :
	
	public ByteConfig(String name, Byte defaultValue) {
		super(name, defaultValue);
	}
	
	public ByteConfig(String name, Byte defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Byte defaultNullValue() {
		return 0;
	}
	
	@Override
	public Byte fromString(String str) throws IllegalArgumentException {
		try {
			return Byte.parseByte(str);
		}
		catch(NumberFormatException argh) {
			throw new IllegalArgumentException("Illegal byte value: " + str, argh);
		}
	}
	
	@Override
	public String toString(Byte value) {
		return String.valueOf(value);
	}
	
	@Override
	public ByteConfigPanel getSwingComponent() {
		return new ByteConfigPanel(this);
	}
	
}
