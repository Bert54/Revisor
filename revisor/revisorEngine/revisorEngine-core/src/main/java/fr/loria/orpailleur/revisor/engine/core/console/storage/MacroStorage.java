package fr.loria.orpailleur.revisor.engine.core.console.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Observable;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author William Philbert
 */
public class MacroStorage<T extends Object> extends Observable {
	
	// Fields :
	
	private final SortedMap<String, T> map;
	private String lastAdded;
	
	// Constructors :
	
	public MacroStorage() {
		this.map = this.newMap();
	}
	
	// Getters :
	
	public String getLastAdded() {
		return this.lastAdded;
	}
	
	// Setters :
	
	protected void setLastAdded(String lastAdded) {
		if(!this.map.containsKey(lastAdded)) {
			this.lastAdded = lastAdded;
			this.setChanged();
		}
	}
	
	// Methods :
	
	protected SortedMap<String, T> newMap() {
		return new TreeMap<>();
	}
	
	public SortedMap<String, T> getUnmodifiableMap() {
		return Collections.unmodifiableSortedMap(this.map);
	}
	
	public Set<String> getMacros() {
		return Collections.unmodifiableSet(this.map.keySet());
	}
	
	public Collection<T> getValues() {
		return Collections.unmodifiableCollection(this.map.values());
	}
	
	public boolean isMacro(String name) {
		return this.map.containsKey(name);
	}
	
	public T getValue(final String macro) {
		return this.map.get(macro);
	}
	
	public void addMacro(final String macro, final T value) {
		if(macro != null && value != null) {
			this.setLastAdded(macro);
			this.map.put(macro, value);
			this.notifyObservers();
		}
	}
	
}
