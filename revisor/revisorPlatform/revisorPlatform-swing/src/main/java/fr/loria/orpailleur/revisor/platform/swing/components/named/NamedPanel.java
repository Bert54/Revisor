package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.util.HashSet;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;

/**
 * @author William Philbert
 */
public abstract class NamedPanel extends Panel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fileds :
	
	private final Set<NamedPanelObserver> observers = new HashSet<>();
	
	// Methods :
	
	public abstract String getPanelName();
	
	public void addNameObserver(NamedPanelObserver observer) {
		this.observers.add(observer);
	}
	
	public void notifiyNameObserver() {
		for(NamedPanelObserver observer : this.observers) {
			observer.updateName(this, this.getPanelName());
		}
	}
	
}
