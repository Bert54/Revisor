package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author William Philbert
 */
public abstract class ComponentList<T extends Object> extends ScrollablePanel implements Observer {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final List<T> elements;
	protected final List<Component> components;
	protected final Panel listPanel;
	
	// Constructors :
	
	protected ComponentList(final List<T> elements) {
		this.elements = elements;
		this.components = new LinkedList<>();
		
		this.listPanel = new Panel();
		Layout.add(this, this.listPanel, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 0);
		
		this.update(null, null);
	}
	
	// Methods :
	
	public int nbElements() {
		return this.elements.size();
	}
	
	public T getElementAt(final int index) {
		return this.elements.get(index);
	}
	
	public int nbComponents() {
		return this.components.size();
	}
	
	protected abstract Component createComponent(final T element, final int index);
	
	protected void recreateComponents(int begin) {
		int nbElements = this.nbElements();
		
		if(this.nbComponents() > nbElements) {
			for(int i = this.nbComponents() - 1; i >= nbElements; i--) {
				Component oldComponent = this.components.remove(i);
				this.listPanel.remove(oldComponent);
			}
		}
		
		int nbComponents = this.nbComponents();
		
		if(begin < 0) {
			begin = 0;
		}
		
		if(begin > nbComponents) {
			begin = nbComponents;
		}
		
		for(int i = begin; i < nbElements; i++) {
			Component newComponent = this.createComponent(this.getElementAt(i), i);
			
			if(i >= nbComponents) {
				this.components.add(newComponent);
			}
			else {
				Component oldComponent = this.components.set(i, newComponent);
				this.listPanel.remove(oldComponent);
			}
			
			Layout.add(this.listPanel, newComponent, 0, i, 1, 0, Layout.CENTER, Layout.HORIZONTAL, 0);
		}
		
		this.revalidate();
		this.repaint();
	}
	
	protected void updateComponents(Observable obs, Object obj) {
		for(Component component : this.components) {
			if(component instanceof Observer) {
				((Observer) component).update(obs, obj);
			}
		}
	}
	
	/**
	 * Indicates if an update with the given Observable and Object requires to create/remove some components, or only requires to update existing components.
	 * @param obs - the Observable which lauched the update.
	 * @param obj - the Object sent by the Observable.
	 * @return true if some components must be created/removed, false if they only need an update.
	 */
	protected boolean recreateUpate(Observable obs, Object obj) {
		return true;
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if(this.recreateUpate(obs, obj)) {
			int begin = 0;
			
			if(obj instanceof Integer) {
				begin = ((Integer) obj).intValue();
			}
			
			this.recreateComponents(begin);
		}
		else {
			this.updateComponents(obs, obj);
		}
	}
	
}
