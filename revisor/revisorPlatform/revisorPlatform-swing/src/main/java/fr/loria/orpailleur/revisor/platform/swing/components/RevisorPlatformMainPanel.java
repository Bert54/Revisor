package fr.loria.orpailleur.revisor.platform.swing.components;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.loria.k.revisor.engine.revisorPCSFC.console.RevisorConsolePCSFC;
import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.Configuration;
import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigFileStorage;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Resources;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiUtils;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.RevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.RevisorConsolePLAK;
import fr.loria.orpailleur.revisor.platform.swing.components.instruction.LatexDisplay;
import fr.loria.orpailleur.revisor.platform.swing.components.named.ConsolePanel;
import fr.loria.orpailleur.revisor.platform.swing.components.named.NamedPanelConsoleActionMenuItem;
import fr.loria.orpailleur.revisor.platform.swing.components.named.NamedPanelContainer;
import fr.loria.orpailleur.revisor.platform.swing.components.named.NamedPanelObserver;
import fr.loria.orpailleur.revisor.platform.swing.components.named.NamedPanelTabMenuItem;
import fr.loria.orpailleur.revisor.platform.swing.components.named.OptionsPanel;

/**
 * @author William Philbert
 */
public class RevisorPlatformMainPanel extends Panel implements Observer {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	public static final String DIR = "resources/";
	public static final ImageIcon LOAD_ICON = Resources.getImageIconOrNull(DIR + "load.png");
	public static final ImageIcon SAVE_ICON = Resources.getImageIconOrNull(DIR + "save.png");
	public static final ImageIcon EXIT_ICON = Resources.getImageIconOrNull(DIR + "exit.png");
	public static final ImageIcon ENGINE_ICON = Resources.getImageIconOrNull(DIR + "engine.png");
	public static final ImageIcon SETTINGS_ICON = Resources.getImageIconOrNull(DIR + "settings.png");
	public static final ImageIcon COPY_ICON = Resources.getImageIconOrNull(DIR + "copy.png");
	public static final ImageIcon ICON_16 = Resources.getImageIconOrNull(DIR + "icon16.png");
	public static final ImageIcon ICON_32 = Resources.getImageIconOrNull(DIR + "icon32.png");
	public static final ImageIcon ICON_64 = Resources.getImageIconOrNull(DIR + "icon64.png");
	public static final ImageIcon ICON_128 = Resources.getImageIconOrNull(DIR + "icon128.png");
	public static final ImageIcon[] ICONS = {ICON_16, ICON_32, ICON_64, ICON_128};
	
	// Fields :
	
	protected final RevisorLogger logger;
	protected final GuiConfig config;
	protected final List<ConsolePanel<?>> consoles;
	protected final OptionsPanel optionsPanel;
	protected final NamedPanelContainer container;
	protected final JMenuBar menu;
	
	// Constructors :
	
	public RevisorPlatformMainPanel(NamedPanelObserver... nameObservers) {
		this.logger = RevisorLogger.instance();
		this.config = new GuiConfig(new ConfigFileStorage("./gui.properties"));
		
		try {
			this.config.init();
		}
		catch(Exception argh) {
			this.logger.logError(argh, Configuration.CANT_SAVE_CONFIGURATION);
			GuiUtils.showError(this, argh.getMessage(), Configuration.CANT_SAVE_CONFIGURATION);
		}
		
		this.config.addObserver(this);
		
		this.consoles = new LinkedList<>();
		
		try {
			this.consoles.add(new ConsolePanel<>(new RevisorConsolePL(), this.config));
			this.consoles.add(new ConsolePanel<>(new RevisorConsolePLAK(), this.config));
			this.consoles.add(new ConsolePanel<>(new RevisorConsolePCSFC(), this.config));
		}
		catch(ConsoleInitializationException argh) {
			this.logger.logError(argh);
			GuiUtils.showError(this, argh.getMessage(), GuiConstants.ERROR);
		}
		
		this.container = new NamedPanelContainer(this.consoles.get(0));
		Layout.add(this, this.container, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 0);
		
		for(NamedPanelObserver observer : nameObservers) {
			this.container.addNameObserver(observer);
		}
		
		this.optionsPanel = new OptionsPanel(this.container);
		this.optionsPanel.addConfig(GuiConstants.GLOBAL, this.config);
		
		this.menu = new JMenuBar();
		
		JMenu fileMenu = new JMenu(GuiConstants.FILE);
		this.menu.add(fileMenu);
		
		JMenuItem loadMenuItem = new NamedPanelConsoleActionMenuItem(this.container, GuiConstants.LOAD_COMMANDS_X, GuiConstants.LOAD_COMMANDS, LOAD_ICON) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void consoleAction(ConsolePanel<?> panel) {
				panel.loadCommands();
			}
			
		};
		
		fileMenu.add(loadMenuItem);
		
		JMenuItem saveMenuItem = new NamedPanelConsoleActionMenuItem(this.container, GuiConstants.SAVE_COMMANDS_X, GuiConstants.SAVE_COMMANDS, SAVE_ICON) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void consoleAction(ConsolePanel<?> panel) {
				panel.saveCommands();
			}
			
		};
		
		fileMenu.add(saveMenuItem);
		
		JMenu engineMenu = new JMenu(GuiConstants.ENGINES);
		this.menu.add(engineMenu);
		
		for(ConsolePanel<?> panel : this.consoles) {
			engineMenu.add(new NamedPanelTabMenuItem(this.container, panel, ENGINE_ICON));
			this.optionsPanel.addConfig(panel.getPanelName(), panel.getConsole().getConfig());
		}
		
		JMenu preferencesMenu = new JMenu(GuiConstants.OPTIONS);
		this.menu.add(preferencesMenu);
		
		preferencesMenu.add(new NamedPanelTabMenuItem(this.container, this.optionsPanel, SETTINGS_ICON));
		
		LatexDisplay.prepareIconBuilder();
		
		this.update(null, null);
		this.container.notifiyNameObserver();
	}
	
	// Methods :
	
	public JMenuBar getMenuBar() {
		return this.menu;
	}
	
	/**
	 * Do some checks and actions before the program exit.
	 * @return true if the the program can exit, false if the exit was canceled (some check failed).
	 */
	public boolean preExitActions() {
		return this.container.setCurrentPanel(null);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.setBackground(this.config.panelBackground.getValue());
	}
	
}
