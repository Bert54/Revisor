package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import java.awt.Color;

import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.ColorConfigPanel;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

/**
 * @author William Philbert
 */
public class ColorConfig extends ConfigItem<Color> {
	
	// Constants :
	
	public static final Color NONE = new Color(0, 0, 0, 0);
	
	// Constructors :
	
	public ColorConfig(String name, Color defaultValue) {
		super(name, defaultValue);
	}
	
	public ColorConfig(String name, Color defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	// Methods :
	
	@Override
	protected Color defaultNullValue() {
		return NONE;
	}
	
	@Override
	public Color fromString(String str) throws IllegalArgumentException {
		return StringUtils.toColor(str);
	}
	
	@Override
	public String toString(Color value) {
		return StringUtils.toHexString(value);
	}
	
	@Override
	public ColorConfigPanel getSwingComponent() {
		return new ColorConfigPanel(this);
	}
	
}
