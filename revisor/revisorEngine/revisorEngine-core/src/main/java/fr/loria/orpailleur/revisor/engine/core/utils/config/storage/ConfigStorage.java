package fr.loria.orpailleur.revisor.engine.core.utils.config.storage;

import java.util.Properties;

/**
 * @author William Philbert
 */
public interface ConfigStorage {
	
	public void load(Properties properties) throws Exception;
	
	public void save(Properties properties, String description) throws Exception;
	
}
