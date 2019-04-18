package fr.loria.orpailleur.revisor.engine.core.utils.config;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.BooleanConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;

/**
 * @author William Philbert
 */
public class ConsoleConfig extends Configuration {
	
	// Fields :
	
	public final BooleanConfig formatInput = new BooleanConfig("Format input", this.formatInput(), "If true, commands will be displayed in formated form; Else commands will be displayed as typed in the console.");
	public final BooleanConfig displaySideNotes = new BooleanConfig("Display side notes", true, "If true, side notes will be displayed after results; Else, side notes won't be displayed.");
	
	// Constructors :
	
	public ConsoleConfig(ConfigStorage storage) {
		super(storage);
	}
	
	// Methods :
	
	@Override
	public String description() {
		return "This properties are used by the console mode (for all Revisor consoles).";
	}
	
	protected boolean formatInput() {
		return false;
	}
	
}
