package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.Scrollable;

/**
 * @author William Philbert
 */
public class ScrollablePanel extends Panel implements Scrollable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEFAULT_SCROLL_VERTICALLY = true;
	private static final boolean DEFAULT_SCROLL_HORIZONTALLY = false;
	private static final boolean DEFAULT_DOUBLE_BUFFERED = true;
	
	// Fields :
	
	private final boolean trackWidth;
	private final boolean trackHeigh;
	
	// Constructors :
	
	public ScrollablePanel() {
		this(DEFAULT_SCROLL_VERTICALLY, DEFAULT_SCROLL_HORIZONTALLY, DEFAULT_DOUBLE_BUFFERED);
	}
	
	public ScrollablePanel(final boolean scrollVertically, final boolean scrollHorizontally) {
		this(scrollVertically, scrollHorizontally, DEFAULT_DOUBLE_BUFFERED);
	}
	
	public ScrollablePanel(final boolean isDoubleBuffered) {
		this(DEFAULT_SCROLL_VERTICALLY, DEFAULT_SCROLL_HORIZONTALLY, isDoubleBuffered);
	}
	
	public ScrollablePanel(final boolean scrollVertically, final boolean scrollHorizontally, final boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.trackHeigh = !scrollVertically;
		this.trackWidth = !scrollHorizontally;
	}
	
	// Methods :
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 25;
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 25;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return this.trackWidth;
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return this.trackHeigh;
	}
	
}
