package fr.loria.orpailleur.revisor.engine.core.utils.config.item;

import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.util.Observable;
import java.util.Properties;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.ConfigItemPanel;

/**
 * @author William Philbert
 */
public abstract class ConfigItem<T extends Object> extends Observable implements Comparable<ConfigItem<?>> {
	
	// Constants :
	
	private static final String ERROR_IN_CONFIG_ITEM_CLASS = "Something is wrong in a ConfigValue class.";
	private static final String COULDNT_READ_VALUE_X_FROM_PROPERTY_FILE = "Couldn't read value %s from properties file. Default value used.";
	
	private static final char SPACE = ' ';
	private static final char UNDERSCORE = '_';
	
	protected static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	// Fields :
	
	private final String name;
	private final String comment;
	private final T defaultValue;
	private T value;
	
	// Constructors :
	
	public ConfigItem(String name, T defaultValue) {
		this(name, defaultValue, null);
	}
	
	public ConfigItem(String name, T defaultValue, String comment) {
		this.name = name.trim().replace(SPACE, UNDERSCORE);
		this.comment = comment;
		this.defaultValue = (defaultValue == null) ? this.defaultNullValue() : defaultValue;
		this.reset();
	}
	
	// Getters :
	
	public String getName() {
		return this.name;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public T getDefaultValue() {
		return this.defaultValue;
	}
	
	public T getValue() {
		return this.value;
	}
	
	// Setters :
	
	public void setValue(T value) {
		if(value == null) {
			value = this.defaultNullValue();
		}
		
		if(this.value != value) {
			this.value = value;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	// Methods :
	
	protected abstract T defaultNullValue();
	
	public Class<?> getType() {
		try {
			return (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		catch(ClassCastException | GenericSignatureFormatError | TypeNotPresentException | MalformedParameterizedTypeException argh) {
			LOGGER.logError(argh, ERROR_IN_CONFIG_ITEM_CLASS);
			return Object.class;
		}
	}
	
	public String getPrettyName() {
		return this.getName().replace(UNDERSCORE, SPACE);
	}
	
	public String getTypeName() {
		return this.getType().getSimpleName();
	}
	
	public String getFullName() {
		return String.format("[%s]%s", this.getTypeName(), this.getName());
	}
	
	public void reset() {
		this.setValue(this.defaultValue);
	}
	
	public void read(Properties properties) {
		try {
			this.setValue(this.fromString(properties.getProperty(this.getFullName(), this.toString(this.defaultValue))));
		}
		catch(IllegalArgumentException | NullPointerException argh) {
			LOGGER.logError(argh, String.format(COULDNT_READ_VALUE_X_FROM_PROPERTY_FILE, this.getFullName()));
			this.reset();
		}
	}
	
	public void write(Properties properties) {
		properties.setProperty(this.getFullName(), this.toString(this.value));
	}
	
	public abstract T fromString(String str) throws IllegalArgumentException;
	
	public abstract String toString(T value);
	
	/**
	 * Returns a swing component which allows editing of this config item.
	 * Returns null by default, child classes should override this method.
	 * @return a swing component which allows editing of this config item. Can be null.
	 */
	public ConfigItemPanel<T, ?> getSwingComponent() {
		return null;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof ConfigItem) {
			return this.getFullName().equals(((ConfigItem<?>) object).getFullName());
		}
		
		return false;
	}
	
	@Override
	public int compareTo(ConfigItem<?> other) {
		return this.getFullName().compareTo(other.getFullName());
	}
	
}
