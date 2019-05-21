package fr.loria.orpailleur.revisor.engine.revisorPL.console.config;

import fr.loria.orpailleur.revisor.engine.core.utils.config.EngineConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.item.BooleanConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.item.IntegerConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;

/**
 * @author William Philbert
 */
public class PLConfig extends EngineConfig {
	
	// Fields :
	
	public final BooleanConfig displaySubstitutions = new BooleanConfig("Display_substitution", false, "If true, substitution side notes will be displayed; Else, substitution side notes won't be displayed.");
	public final IntegerConfig substitutionTimeout = new IntegerConfig("Substitution timeout (in sec)", 3, "The time limit for the computation of a substitution. Set to 0 for no timeout.");
	
	// Constructors :
	
	public PLConfig(ConfigStorage storage) {
		super(storage);
	}
	
	// Methods :
	
	@Override
	public String description() {
		return "This properties are used by Revisor PL console.";
	}
	
}
