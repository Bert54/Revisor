package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.Configuration;
import fr.loria.orpailleur.revisor.engine.core.utils.config.item.ConfigItem;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigFileStorage;
import fr.loria.orpailleur.revisor.engine.core.utils.config.swing.ConfigItemPanel;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiUtils;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Label;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.ScrollPane;

/**
 * @author William Philbert
 */
public class ConfigPanel extends Panel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	protected static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	// Fields :
	
	private final Configuration config;
	private final List<ConfigItemPanel<?, ?>> configItemPanels;
	
	// Constructors :
	
	public ConfigPanel(final Configuration config) {
		this.config = config;
		this.configItemPanels = new LinkedList<>();
		
		ScrollPane scrollPane = ScrollPane.createVerticalScrollPane();
		Layout.add(this, scrollPane, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		
		Panel leftPanel = new Panel();
		Layout.add(scrollPane.getPanel(), leftPanel, 0, 0, 1, 1, Layout.WEST, Layout.VERTICAL, 2);
		
		Font titleFont = new Label().getFont();
		titleFont = titleFont.deriveFont(Font.BOLD + Font.ITALIC, titleFont.getSize() * 1.2F);
		
		int i = 0;
		String lastType = "";
		
		for(ConfigItem<?> configItem : this.config.getConfigs()) {
			ConfigItemPanel<?, ?> panel = configItem.getSwingComponent();
			
			if(panel != null) {
				this.configItemPanels.add(panel);
				
				String type = configItem.getTypeName();
				
				if(!type.equals(lastType)) {
					lastType = type;
					Label typeLabel = new Label(type + ":");
					typeLabel.setFont(titleFont);
					Layout.add(leftPanel, typeLabel, 0, i++, 3, 1, 0, 0, Layout.WEST, Layout.NONE, 10, 2, 10, 2);
				}
				
				Layout.add(leftPanel, panel.getNameLabel(), 0, i, 0, 0, Layout.CENTER, Layout.BOTH, 2);
				Layout.add(leftPanel, panel.getFieldComponent(), 1, i, 0, 0, Layout.CENTER, Layout.BOTH, 2);
				Layout.add(leftPanel, panel.getButtonsPanel(), 2, i, 0, 0, Layout.WEST, Layout.NONE, 2);
				
				i++;
			}
		}
		
		Panel buttonsPanel = new Panel();
		Layout.add(this, buttonsPanel, 0, 1, 1, 0, Layout.WEST, Layout.NONE, 2);
		
		Label thisTabLabel = new Label(GuiConstants.THIS_TAB);
		Layout.add(buttonsPanel, thisTabLabel, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		JButton saveButton = new JButton(GuiConstants.SAVE);
		Layout.add(buttonsPanel, saveButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigPanel.this.save();
			}
			
		});
		
		JButton resetButton = new JButton(GuiConstants.RESET);
		Layout.add(buttonsPanel, resetButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigPanel.this.reset();
			}
			
		});
		
		JButton defaultButton = new JButton(GuiConstants.DEFAULT);
		Layout.add(buttonsPanel, defaultButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		defaultButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigPanel.this.setToDefault();
			}
			
		});
		
		JButton importButton = new JButton(GuiConstants.IMPORT);
		Layout.add(buttonsPanel, importButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		importButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigPanel.this.importConfig();
			}
			
		});
		
		JButton exportButton = new JButton(GuiConstants.EXPORT);
		Layout.add(buttonsPanel, exportButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigPanel.this.exportConfig();
			}
			
		});
	}
	
	// Getters :
	
	public Configuration getConfig() {
		return this.config;
	}
	
	// Methods :
	
	public void save() {
		for(ConfigItemPanel<?, ?> panel : this.configItemPanels) {
			panel.save();
		}
		
		this.commitChanges();
	}
	
	public void reset() {
		for(ConfigItemPanel<?, ?> panel : this.configItemPanels) {
			panel.reset();
		}
	}
	
	public void setToDefault() {
		for(ConfigItemPanel<?, ?> panel : this.configItemPanels) {
			panel.setToDefault();
		}
	}
	
	public void commitChanges() {
		this.commitChanges(false);
	}
	
	public void commitChanges(boolean forceSave) {
		try {
			this.config.save(forceSave);
		}
		catch(Exception argh) {
			LOGGER.logError(argh, Configuration.CANT_SAVE_CONFIGURATION);
			GuiUtils.showError(this, argh.getMessage(), Configuration.CANT_SAVE_CONFIGURATION);
		}
	}
	
	public boolean hasUnsavedChanges() {
		for(ConfigItemPanel<?, ?> panel : this.configItemPanels) {
			if(panel.hasUnsavedChanges()) {
				return true;
			}
		}
		
		return false;
	}
	
	protected void importConfig() {
		try {
			File file = GuiUtils.showFileChooser(this, false);
			
			if(file != null) {
				this.config.load(new ConfigFileStorage(file));
				this.reset();
				this.commitChanges(true);
				GuiUtils.showInfo(this, GuiConstants.CONFIG_IMPORTED, GuiConstants.SUCCESS);
			}
		}
		catch(Exception argh) {
			LOGGER.logError(argh, Configuration.CANT_LOAD_CONFIGURATION);
			GuiUtils.showError(this, argh.getMessage(), Configuration.CANT_LOAD_CONFIGURATION);
		}
	}
	
	protected void exportConfig() {
		try {
			boolean done = false;
			
			while(!done) {
				File file = GuiUtils.showFileChooser(this, true);
				
				if(file != null) {
					if(GuiUtils.WORKING_DIR.equals(file.getParentFile())) {
						GuiUtils.showError(this, GuiConstants.CANT_WRITE_IN_PROGRAM_MAIN_DIR, GuiConstants.ERROR);
					}
					else {
						boolean export = true;
						
						if(file.exists()) {
							int choice = GuiUtils.showOptions(this, String.format(GuiConstants.FILE_X_ALREADY_EXISTS, file), GuiConstants.FILE_ALREADY_EXISTS, 1, GuiConstants.OVERWRITE, GuiConstants.CANCEL);
							export = (choice == 0);
						}
						
						if(export) {
							done = true;
							this.save();
							this.config.save(new ConfigFileStorage(file));
							GuiUtils.showInfo(this, GuiConstants.CONFIG_EXPORTED, GuiConstants.SUCCESS);
						}
					}
				}
				else {
					done = true;
				}
			}
		}
		catch(Exception argh) {
			LOGGER.logError(argh, Configuration.CANT_SAVE_CONFIGURATION);
			GuiUtils.showError(this, argh.getMessage(), Configuration.CANT_SAVE_CONFIGURATION);
		}
	}
	
}
