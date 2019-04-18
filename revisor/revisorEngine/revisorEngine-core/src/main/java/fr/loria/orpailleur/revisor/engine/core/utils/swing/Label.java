package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author William Philbert
 */
public class Label extends JLabel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_TEXT = null;
	private static final Icon DEFAULT_IMAGE = null;
	private static final int DEFAULT_ALIGNMENT = CENTER;
	
	// Constructors :
	
	public Label() {
		this("");
	}
	
	public Label(String text) {
		this(text, DEFAULT_IMAGE, DEFAULT_ALIGNMENT);
	}
	
	public Label(Icon icon) {
		this(DEFAULT_TEXT, icon, DEFAULT_ALIGNMENT);
	}
	
	public Label(String text, Icon icon) {
		this(text, icon, DEFAULT_ALIGNMENT);
	}
	
	public Label(String text, int horizontalAlignment) {
		this(text, DEFAULT_IMAGE, horizontalAlignment);
	}
	
	public Label(Icon icon, int horizontalAlignment) {
		this(DEFAULT_TEXT, icon, horizontalAlignment);
	}
	
	public Label(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.setBackground(null);
	}
	
}
