package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * @author William Philbert
 */
public class NamedPanelTabMenuItem extends JMenuItem {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public NamedPanelTabMenuItem(final NamedPanelContainer container, final NamedPanelTab panel, final Icon icon) {
		this.setText(panel.getPanelName());
		
		if(icon != null) {
			this.setIcon(icon);
		}
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				container.setCurrentPanel(panel);
			}
			
		});
	}
	
}
