package fr.loria.orpailleur.revisor.engine.core.utils.config.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Resources;

/**
 * @author William Philbert
 */
public class ConfigFileStorage implements ConfigStorage {
	
	// Constants :
	
	private static final String CANT_GET_CANONICAL_PATH_OF_FILE = "Couldn't get canonical path of file '%s'.";
	
	// Fields :
	
	private final File configFile;
	
	// Constructors :
	
	public ConfigFileStorage(String configFilePath) {
		File configFile = new File(configFilePath);
		
		try {
			configFile = configFile.getCanonicalFile();
		}
		catch(Exception argh) {
			RevisorLogger.instance().logError(argh, String.format(CANT_GET_CANONICAL_PATH_OF_FILE, configFilePath));
		}
		
		this.configFile = configFile;
	}
	
	public ConfigFileStorage(File configFile) {
		this.configFile = configFile;
	}
	
	// Getters :
	
	public File getConfigFile() {
		return this.configFile;
	}
	
	// Methods :
	
	@Override
	public void load(Properties properties) throws Exception {
		Resources.createFileIfNotExists(this.configFile);
		
		try(FileInputStream input = new FileInputStream(this.getConfigFile())) {
			properties.load(input);
		}
	}
	
	@Override
	public void save(Properties properties, String description) throws Exception {
		Resources.createFileIfNotExists(this.configFile);
		
		try(FileOutputStream output = new FileOutputStream(this.getConfigFile())) {
			properties.store(output, description);
		}
	}
	
}
