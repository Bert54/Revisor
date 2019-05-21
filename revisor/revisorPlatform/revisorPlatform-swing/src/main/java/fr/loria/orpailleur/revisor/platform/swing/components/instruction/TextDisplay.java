package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Color;

import javax.swing.JTextArea;

/**
 * @author William Philbert
 */
public class TextDisplay extends JTextArea {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public TextDisplay(String text, Color textColor) {
		super(text);
		this.setEditable(false);
		this.setLineWrap(true);
		this.setWrapStyleWord(false);
		this.setForeground(textColor);
		this.setBackground(null);
	}
	
}
