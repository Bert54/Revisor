package fr.loria.orpailleur.revisor.engine.core.utils.config;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.ConfigItem;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;

/**
 * @author William Philbert
 */
public abstract class Configuration extends Observable implements Observer {
	
	// Constants :
	
	public static final String CANT_INIT_CONFIGURATION = "Couldn't init configuration.";
	public static final String CANT_LOAD_CONFIGURATION = "Couldn't load configuration.";
	public static final String CANT_SAVE_CONFIGURATION = "Couldn't save configuration.";
	
	// Fields :
	
	private final Properties properties = new Properties();
	private final SortedSet<ConfigItem<?>> configs = new TreeSet<>();
	
	private boolean init = false;
	private ConfigStorage storage;
	
	// Constructors :
	
	protected Configuration(ConfigStorage storage) {
		this.storage = storage;
	}
	
	// Getters :
	
	public SortedSet<ConfigItem<?>> getConfigs() {
		return Collections.unmodifiableSortedSet(this.configs);
	}
	
	public ConfigStorage getStorage() {
		return this.storage;
	}
	
	// Setters :
	
	public void setStorage(ConfigStorage storage) {
		if(storage != null) {
			this.storage = storage;
		}
	}
	
	// Methods :
	
	public abstract String description();
	
	public void init() throws Exception {
		if(!this.init) {
			Field[] fields = this.getClass().getFields();
			
			for(Field field : fields) {
				Object value = field.get(this);
				
				if(value instanceof ConfigItem) {
					this.register((ConfigItem<?>) value);
				}
			}
			
			this.load();
			this.save(true);
			this.init = true;
		}
	}
	
	protected void register(ConfigItem<?> config) {
		this.configs.add(config);
		config.addObserver(this);
	}
	
	public void reset() {
		for(ConfigItem<?> config : this.configs) {
			config.reset();
		}
		
		this.notifyObservers();
	}
	
	public void load(ConfigStorage storage) throws Exception {
		storage.load(this.properties);
		
		for(ConfigItem<?> config : this.configs) {
			config.read(this.properties);
		}
		
		this.notifyObservers();
	}
	
	public void load() throws Exception {
		this.load(this.storage);
	}
	
	public void save(ConfigStorage storage, boolean forceSave) throws Exception {
		if(this.hasChanged() || forceSave) {
			for(ConfigItem<?> config : this.configs) {
				config.write(this.properties);
			}
			
			storage.save(this.properties, this.description());
		}
		
		this.notifyObservers();
	}
	
	public void save(ConfigStorage storage) throws Exception {
		this.save(storage, true);
	}
	
	public void save(boolean forceSave) throws Exception {
		this.save(this.storage, forceSave);
	}
	
	public void save() throws Exception {
		this.save(false);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
	}
	
}
