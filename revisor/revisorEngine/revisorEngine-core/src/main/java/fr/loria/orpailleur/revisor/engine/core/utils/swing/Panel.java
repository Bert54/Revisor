package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author William Philbert.
 */
public class Panel extends JPanel implements SwingConstants {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEFAULT_IS_DOUBLE_BUFFERED = true;
	
	// Constructors :
	
	public Panel() {
		this(new GridBagLayout(), DEFAULT_IS_DOUBLE_BUFFERED);
	}
	
	public Panel(boolean isDoubleBuffered) {
		this(new GridBagLayout(), isDoubleBuffered);
	}
	
	public Panel(LayoutManager layout) {
		this(layout, DEFAULT_IS_DOUBLE_BUFFERED);
	}
	
	public Panel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.setBackground(null);
	}
	
}
