package fr.loria.orpailleur.revisor.platform.swing.components.input;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;

/**
 * @author William Philbert
 */
public class ValidityPanel extends JPanel implements Observer {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final ObservableInputState observableState;
	protected final GuiConfig config;
	
	// Constructors :
	
	public ValidityPanel(final ObservableInputState observableState, final GuiConfig config) {
		this.observableState = observableState;
		this.config = config;
		
		this.observableState.addObserver(this);
		this.config.addObserver(this);
		
		this.update(null, null);
	}
	
	// Methods :
	
	@Override
	public void update(Observable o, Object arg) {
		switch(this.observableState.getState()) {
			case NEUTRAL:
				this.setBackground(this.config.consoleValidatorNeutral.getValue());
				break;
			case VALID:
				this.setBackground(this.config.consoleValidatorValid.getValue());
				break;
			case INVALID:
				this.setBackground(this.config.consoleValidatorInvalid.getValue());
				break;
			case WARNING:
				this.setBackground(this.config.consoleValidatorWarning.getValue());
				break;
		}
	}
	
}
