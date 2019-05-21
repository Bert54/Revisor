package fr.loria.orpailleur.revisor.platform.swing.components.named;

/**
 * @author William Philbert
 */
public abstract class NamedPanelTab extends NamedPanel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Methods :
	
	protected abstract void gainFocus(NamedPanelTab oldTab);
	
	protected abstract boolean looseFocus(NamedPanelTab newTab);
	
}
