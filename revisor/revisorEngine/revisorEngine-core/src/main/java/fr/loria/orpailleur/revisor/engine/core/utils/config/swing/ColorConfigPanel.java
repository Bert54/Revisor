package fr.loria.orpailleur.revisor.engine.core.utils.config.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.ColorConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.ColorChooser;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;

/**
 * @author William Philbert
 */
public class ColorConfigPanel extends ConfigItemPanel<Color, ColorConfig> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final ColorChooser colorChooser;
	protected final JButton noneButton;
	
	// Constructors :
	
	public ColorConfigPanel(ColorConfig config) {
		super(config);
		
		this.colorChooser = new ColorChooser();
		Layout.add(this.mainPanel, this.colorChooser, 1, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		
		this.noneButton = new JButton(GuiConstants.NONE);
		Layout.add(this.buttonsPanel, this.noneButton, Layout.RELATIVE, 0, 0, 0, Layout.CENTER, Layout.NONE, 2);
		
		this.noneButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorConfigPanel.this.setToNull();
			}
			
		});
	}
	
	// Methods :
	
	public void setToNull() {
		this.setValue(ColorConfig.NONE);
	}
	
	@Override
	protected Color getValue() {
		return this.colorChooser.getColor();
	}
	
	@Override
	protected void setValue(Color value) {
		this.colorChooser.setColor(value);
	}
	
	@Override
	public Component getFieldComponent() {
		return this.colorChooser;
	}
	
}
