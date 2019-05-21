package fr.loria.orpailleur.revisor.platform.swing.components.named;

import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * @author William Philbert
 */
public class NamedPanelObserverMenuItem extends JMenuItem implements NamedPanelObserver {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final NamedPanelContainer container;
	protected final String formatableText;
	
	// Constructors :
	
	public NamedPanelObserverMenuItem(final NamedPanelContainer container, final String formatableText, final Icon icon) {
		this.container = container;
		this.formatableText = formatableText;
		
		if(icon != null) {
			this.setIcon(icon);
		}
		
		container.addNameObserver(this);
	}
	
	@Override
	public void updateName(NamedPanel panel, String name) {
		this.setText(String.format(this.formatableText, name));
	}
	
}
