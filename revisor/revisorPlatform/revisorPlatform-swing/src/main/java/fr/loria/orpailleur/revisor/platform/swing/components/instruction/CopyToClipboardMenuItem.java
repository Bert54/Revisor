package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiUtils;

/**
 * @author William Philbert
 */
public class CopyToClipboardMenuItem extends JMenuItem {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public CopyToClipboardMenuItem(final String name, Icon icon, final String textToCopy) {
		super(name, icon);
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiUtils.setClipboardContents(textToCopy);
			}
			
		});
	}
	
}
