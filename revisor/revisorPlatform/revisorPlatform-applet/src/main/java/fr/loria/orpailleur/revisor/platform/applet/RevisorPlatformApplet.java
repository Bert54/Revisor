package fr.loria.orpailleur.revisor.platform.applet;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.platform.swing.components.RevisorPlatformMainPanel;

/**
 * @author William Philbert
 */
public class RevisorPlatformApplet extends JApplet {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	protected static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	// Methods :
	
	@Override
	public void init() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					RevisorPlatformApplet.this.createGUI();
				}
				
			});
		}
		catch(Exception argh) {
			LOGGER.logFatal(argh, "Applet init() didn't complete successfully.");
		}
	}
	
	protected void createGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception argh) {
			LOGGER.logError(argh, "Look and feel initialisation failed.");
		}
		
		RevisorPlatformMainPanel contentPane = new RevisorPlatformMainPanel();
		contentPane.setOpaque(true);
		this.setContentPane(contentPane);
		this.setJMenuBar(contentPane.getMenuBar());
	}
	
}
