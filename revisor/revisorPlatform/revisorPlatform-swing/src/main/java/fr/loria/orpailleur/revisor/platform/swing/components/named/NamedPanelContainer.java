package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;

/**
 * @author William Philbert
 */
public class NamedPanelContainer extends NamedPanel implements ActionListener {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	private final Panel container;
	
	private NamedPanelTab currentPanel;
	private ConsolePanel<?> lastConsolePanel;
	
	// Constructors :
	
	public NamedPanelContainer(NamedPanelTab currentPanel) {
		this.container = new Panel();
		Layout.add(this, this.container, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 0);
		
		this.setCurrentPanel(currentPanel);
	}
	
	// Getters :
	
	public NamedPanelTab getCurrentPanel() {
		return this.currentPanel;
	}
	
	public ConsolePanel<?> getLastConsolePanel() {
		return this.lastConsolePanel;
	}
	
	// Setters :
	
	/**
	 * Sets the currently displayed panel. Calls oldPanel.looseFocus(), then newPanel.gainFocus().
	 * If oldPanel.looseFocus() returns false - the old panel cancels the change - the current panel isn't changed and newPanel.gainFocus() isn't called.
	 * @param currentPanel - the new panel.
	 * @return true if the current panel was successfully changed (or the new panel is equal to the old one), false if the change was canceled by the old panel.
	 */
	public boolean setCurrentPanel(NamedPanelTab currentPanel) {
		if(this.currentPanel != currentPanel) {
			if(this.currentPanel != null) {
				if(!this.currentPanel.looseFocus(currentPanel)) {
					return false;
				}
				
				this.container.removeAll();
			}
			
			if(currentPanel != null) {
				Layout.add(this.container, currentPanel, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 0);
				currentPanel.gainFocus(this.currentPanel);
				
				if(currentPanel instanceof ConsolePanel) {
					this.lastConsolePanel = (ConsolePanel<?>) currentPanel;
				}
			}
			
			this.currentPanel = currentPanel;
			this.notifiyNameObserver();
			
			this.revalidate();
			this.repaint();
		}
		
		return true;
	}
	
	// Methods :
	
	@Override
	public String getPanelName() {
		return (this.currentPanel != null) ? this.currentPanel.getPanelName() : "";
	}
	
	/**
	 * Sets the current panel to the last displayed console panel.
	 * @return true if the current panel was successfully changed (or the new panel is equal to the old one), false if the change was canceled by the old panel.
	 * @see NamedPanelContainer#setCurrentPanel(NamedPanelTab)
	 */
	public boolean goToLastConsolePanel() {
		return this.setCurrentPanel(this.lastConsolePanel);
	}
	
	/**
	 * Indicates if the current panel is a console panel.
	 * @return true if the current panel is a console panel, else false.
	 */
	public boolean isConsolePanelDisplayed() {
		return this.currentPanel instanceof ConsolePanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.goToLastConsolePanel();
	}
	
}
