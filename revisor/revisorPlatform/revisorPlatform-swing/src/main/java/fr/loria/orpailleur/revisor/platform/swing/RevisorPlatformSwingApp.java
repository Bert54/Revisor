package fr.loria.orpailleur.revisor.platform.swing;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.platform.swing.components.RevisorPlatformMainPanel;
import fr.loria.orpailleur.revisor.platform.swing.components.named.NamedPanel;
import fr.loria.orpailleur.revisor.platform.swing.components.named.NamedPanelObserver;

/**
 * @author William Philbert
 */
public class RevisorPlatformSwingApp extends JFrame implements NamedPanelObserver {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	private static final String TITLE = GuiConstants.REVISOR_PLATFORM;
	
	protected static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	// Fields :
	
	protected final RevisorPlatformMainPanel mainPanel;
	
	// Constructors :
	
	public RevisorPlatformSwingApp() {
		super(TITLE);
		
		this.mainPanel = new RevisorPlatformMainPanel(this);
		this.mainPanel.setOpaque(true);
		this.setContentPane(this.mainPanel);
		
		JMenuBar menuBar = this.mainPanel.getMenuBar();
		this.setJMenuBar(menuBar);
		
		for(int i = 0; i < menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			
			if(menu != null && menu.getText().equals(GuiConstants.FILE)) {
				if(menu.getItemCount() > 0) {
					menu.addSeparator();
				}
				
				JMenuItem exitMenuItem = new JMenuItem(GuiConstants.EXIT, RevisorPlatformMainPanel.EXIT_ICON);
				menu.add(exitMenuItem);
				
				exitMenuItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						RevisorPlatformSwingApp.this.exit();
					}
					
				});
				
				break;
			}
		}
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent pwet) {
				RevisorPlatformSwingApp.this.exit();
			}
			
		});
		
		List<Image> images = new LinkedList<>();
		
		for(ImageIcon icon : RevisorPlatformMainPanel.ICONS) {
			if(icon != null) {
				images.add(icon.getImage());
			}
		}
		
		this.setIconImages(images);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setMinimumSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.5), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.5)));
		this.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.5), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.5)));
		this.pack();
		this.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));
		this.setVisible(true);
	}
	
	// Methods :
	
	protected void exit() {
		if(this.mainPanel.preExitActions()) {
			System.exit(0);
		}
	}
	
	@Override
	public void updateName(NamedPanel panel, String name) {
		if(name != null && !name.isEmpty()) {
			this.setTitle(String.format("%s - %s", TITLE, name));
		}
		else {
			this.setTitle(TITLE);
		}
	}
	
	// Main :
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Locale.setDefault(GuiConstants.DEFAULT_LOCAL);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception argh) {
			LOGGER.logError(argh, "Look and feel initialisation failed.");
		}
		
		try {
			new RevisorPlatformSwingApp();
		}
		catch(Exception argh) {
			LOGGER.logFatal(argh);
			System.exit(1);
		}
	}
	
}
