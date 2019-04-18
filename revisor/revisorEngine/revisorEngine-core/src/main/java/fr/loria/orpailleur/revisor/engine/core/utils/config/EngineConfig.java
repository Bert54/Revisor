package fr.loria.orpailleur.revisor.engine.core.utils.config;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.IntegerConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;

/**
 * @author William Philbert
 */
public abstract class EngineConfig extends Configuration {
	
	// Fields :
	
	public final IntegerConfig instructionTimeout = new IntegerConfig("Instruction timeout (in sec)", 60, "The time limit for the execution of each command (exept \"load\") of this engine. Set to 0 for no timeout.");
	
	// Constructors :
	
	protected EngineConfig(ConfigStorage storage) {
		super(storage);
	}
	
}
