package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

/**
 * @author William Philbert
 */
public abstract class NamedPanelConsoleActionMenuItem extends NamedPanelObserverMenuItem {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final String disabledText;
	
	// Constructors :
	
	public NamedPanelConsoleActionMenuItem(final NamedPanelContainer container, final String formatableText, final String disabledText, final Icon icon) {
		super(container, formatableText, icon);
		this.disabledText = disabledText;
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamedPanelTab panel = container.getCurrentPanel();
				
				if(panel instanceof ConsolePanel) {
					NamedPanelConsoleActionMenuItem.this.consoleAction(((ConsolePanel<?>) panel));
				}
			}
			
		});
	}
	
	// Methods :
	
	protected abstract void consoleAction(ConsolePanel<?> panel);
	
	@Override
	public void updateName(NamedPanel panel, String name) {
		boolean enabled = this.container.getCurrentPanel() instanceof ConsolePanel;
		this.setEnabled(enabled);
		
		if(enabled) {
			super.updateName(panel, name);
		}
		else {
			this.setText(this.disabledText);
		}
	}
	
}
