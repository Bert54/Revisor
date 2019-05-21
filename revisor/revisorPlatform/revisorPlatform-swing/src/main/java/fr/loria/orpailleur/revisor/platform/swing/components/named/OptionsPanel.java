package fr.loria.orpailleur.revisor.platform.swing.components.named;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import fr.loria.orpailleur.revisor.engine.core.utils.config.Configuration;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiUtils;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Label;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;

/**
 * @author William Philbert
 */
public class OptionsPanel extends NamedPanelTab {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	private final List<ConfigPanel> configPanels = new LinkedList<>();
	private final JTabbedPane tabs;
	
	protected final ActionListener closeActionListener;
	
	// Constructors :
	
	public OptionsPanel(ActionListener closeActionListener) {
		this.closeActionListener = closeActionListener;
		
		this.tabs = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		Layout.add(this, this.tabs, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		this.tabs.setBackground(null);
		
		Panel buttonsPanel = new Panel();
		Layout.add(this, buttonsPanel, 0, 1, 1, 0, Layout.WEST, Layout.NONE, 2);
		
		Label allTabsLabel = new Label(GuiConstants.ALL_TABS);
		Layout.add(buttonsPanel, allTabsLabel, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		JButton saveButton = new JButton(GuiConstants.SAVE);
		Layout.add(buttonsPanel, saveButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsPanel.this.save();
			}
			
		});
		
		JButton resetButton = new JButton(GuiConstants.RESET);
		Layout.add(buttonsPanel, resetButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsPanel.this.reset();
			}
			
		});
		
		JButton defaultButton = new JButton(GuiConstants.DEFAULT);
		Layout.add(buttonsPanel, defaultButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		defaultButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsPanel.this.setToDefault();
			}
			
		});
		
		JButton closeButton = new JButton(GuiConstants.CLOSE);
		Layout.add(buttonsPanel, closeButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		closeButton.addActionListener(this.closeActionListener);
	}
	
	// Methods :
	
	public void addConfig(String title, Configuration config) {
		for(ConfigPanel panel : this.configPanels) {
			if(panel.getConfig().equals(config)) {
				return;
			}
		}
		
		ConfigPanel configPanel = new ConfigPanel(config);
		this.configPanels.add(configPanel);
		this.tabs.add(title, configPanel);
	}
	
	protected void save() {
		for(ConfigPanel panel : this.configPanels) {
			panel.save();
		}
	}
	
	protected void reset() {
		for(ConfigPanel panel : this.configPanels) {
			panel.reset();
		}
	}
	
	protected void setToDefault() {
		for(ConfigPanel panel : this.configPanels) {
			panel.setToDefault();
		}
	}
	
	protected void commitChanges() {
		for(ConfigPanel panel : this.configPanels) {
			panel.commitChanges();
		}
	}
	
	public boolean hasUnsavedChanges() {
		for(ConfigPanel panel : this.configPanels) {
			if(panel.hasUnsavedChanges()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void gainFocus(NamedPanelTab oldTab) {
		this.reset();
	}
	
	@Override
	protected boolean looseFocus(NamedPanelTab newTab) {
		if(this.hasUnsavedChanges()) {
			int choice = GuiUtils.showOptions(this, GuiConstants.THERE_ARE_UNSAVED_CONFIG, GuiConstants.UNSAVED_CONFIG, 2, GuiConstants.SAVE, GuiConstants.DO_NOT_SAVE, GuiConstants.CANCEL);
			
			switch(choice) {
				case 0:
					this.save();
					break;
				case 1:
					// Nothing to do.
					break;
				default:
					return false;
			}
		}
		
		this.commitChanges();
		return true;
	}
	
	@Override
	public String getPanelName() {
		return GuiConstants.PREFERENCES;
	}
	
}
