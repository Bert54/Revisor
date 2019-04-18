package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.ConfigItem;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Label;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;

/**
 * @author William Philbert
 */
public abstract class ConfigItemPanel<T extends Object, C extends ConfigItem<T>> extends Panel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final C config;
	
	protected final Panel mainPanel;
	protected final Panel buttonsPanel;
	
	protected final Label nameLabel;
	protected final JButton saveButton;
	protected final JButton resetButton;
	protected final JButton defaultButton;
	
	// Constructors :
	
	protected ConfigItemPanel(C config) {
		this.config = config;
		
		this.mainPanel = new Panel();
		Layout.add(this, this.mainPanel, 0, 0, 0, 0, Layout.CENTER, Layout.NONE, 0);
		
		this.nameLabel = new Label(this.config.getPrettyName() + ":", LEFT);
		Layout.add(this.mainPanel, this.nameLabel, 0, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		String comment = this.config.getComment();
		
		if(comment != null) {
			this.nameLabel.setToolTipText(comment);
		}
		
		this.buttonsPanel = new Panel();
		Layout.add(this, this.buttonsPanel, 1, 0, 0, 0, Layout.CENTER, Layout.NONE, 0);
		
		this.saveButton = new JButton(GuiConstants.SAVE);
		Layout.add(this.buttonsPanel, this.saveButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		this.saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigItemPanel.this.save();
			}
			
		});
		
		this.resetButton = new JButton(GuiConstants.RESET);
		Layout.add(this.buttonsPanel, this.resetButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		this.resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigItemPanel.this.reset();
			}
			
		});
		
		this.defaultButton = new JButton(GuiConstants.DEFAULT);
		Layout.add(this.buttonsPanel, this.defaultButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		this.defaultButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigItemPanel.this.setToDefault();
			}
			
		});
	}
	
	// Methods :
	
	public void save() {
		this.config.setValue(this.getValue());
	}
	
	public void reset() {
		this.setValue(this.config.getValue());
	}
	
	public void setToDefault() {
		this.setValue(this.config.getDefaultValue());
	}
	
	public boolean hasUnsavedChanges() {
		T actualValue = this.getValue();
		T savedValue = this.config.getValue();
		return !((actualValue == null) ? (savedValue == null) : actualValue.equals(savedValue));
	}
	
	public Panel getMainPanel() {
		return this.mainPanel;
	}
	
	public Panel getButtonsPanel() {
		return this.buttonsPanel;
	}
	
	public Label getNameLabel() {
		return this.nameLabel;
	}
	
	public abstract Component getFieldComponent();
	
	protected abstract T getValue();
	
	protected abstract void setValue(T value);
	
}
