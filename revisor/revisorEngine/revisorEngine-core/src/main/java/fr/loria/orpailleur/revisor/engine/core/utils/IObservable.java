package fr.loria.orpailleur.revisor.engine.core.utils;

import java.util.Observer;

/**
 * All implementation of this interface should extends {@link java.util.Observable}.
 * 
 * @author William Philbert
 */
public interface IObservable {
	
	/**
	 * @see java.util.Observable#addObserver(Observer)
	 */
	public void addObserver(Observer o);
	
	/**
	 * @see java.util.Observable#deleteObserver(Observer)
	 */
	public void deleteObserver(Observer o);
	
	/**
	 * @see java.util.Observable#notifyObservers()
	 */
	public void notifyObservers();
	
	/**
	 * @see java.util.Observable#notifyObservers(Object)
	 */
	public void notifyObservers(Object arg);
	
	/**
	 * @see java.util.Observable#deleteObservers()
	 */
	public void deleteObservers();
	
	/**
	 * @see java.util.Observable#hasChanged()
	 */
	public boolean hasChanged();
	
	/**
	 * @see java.util.Observable#countObservers()
	 */
	public int countObservers();
	
}
