package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.config;

import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.config.PLConfig;

/**
 * @author William Philbert
 */
public class PLAKConfig extends PLConfig {
	
	// Fields :
	
	// Add public final ConfigItem fields here to create properties.
	
	// Constructors :
	
	public PLAKConfig(ConfigStorage storage) {
		super(storage);
	}
	
	// Methods :
	
	@Override
	public String description() {
		return "This properties are used by Revisor PLAK console.";
	}
	
}
