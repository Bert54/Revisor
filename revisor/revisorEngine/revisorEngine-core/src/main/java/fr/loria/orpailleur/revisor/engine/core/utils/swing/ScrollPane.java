package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * @author William Philbert
 */
public class ScrollPane extends JScrollPane {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	private final ScrollablePanel panel;
	
	// Constructors :
	
	protected ScrollPane(final ScrollablePanel panel, final int vsbPolicy, final int hsbPolicy) {
		super(panel, vsbPolicy, hsbPolicy);
		this.panel = panel;
		this.setBackground(null);
		this.getViewport().setBackground(null);
	}
	
	// Getters :
	
	public ScrollablePanel getPanel() {
		return this.panel;
	}
	
	// Methods :
	
	protected static ScrollPane createScrollPane(final boolean vertical, final boolean horizontal) {
		final ScrollablePanel panel = new ScrollablePanel(vertical, horizontal);
		final int vsbPolicy = vertical ? ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
		final int hsbPolicy = horizontal ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS : ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		return new ScrollPane(panel, vsbPolicy, hsbPolicy);
	}
	
	public static ScrollPane createVerticalScrollPane() {
		return createScrollPane(true, false);
	}
	
	public static ScrollPane createHorizontalScrollPane() {
		return createScrollPane(false, true);
	}
	
	public static ScrollPane createBothDirectionsScrollPane() {
		return createScrollPane(true, true);
	}
	
}
