package fr.loria.orpailleur.revisor.platform.swing.components.input;

import java.util.Observable;

/**
 * @author William Philbert
 */
public class ObservableInputState extends Observable {
	
	// Fields :
	
	private InputState state = InputState.NEUTRAL;
	
	// Getters :
	
	public InputState getState() {
		return this.state;
	}
	
	// Setters :
	
	public void setState(InputState state) {
		if(this.state != state) {
			this.state = state;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
}
