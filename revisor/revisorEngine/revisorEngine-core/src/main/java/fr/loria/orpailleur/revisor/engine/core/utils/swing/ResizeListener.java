package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Timer;

/**
 * @author William Philbert
 */
public abstract class ResizeListener implements ComponentListener, ActionListener {
	
	// Constants :
	
	public static final int DEFAULT_DELAY = 600;
	
	// Fields :
	
	private final Timer timer;
	
	private boolean resized = false;
	private boolean running = false;
	
	// Constructors :
	
	public ResizeListener() {
		this(DEFAULT_DELAY);
	}
	
	public ResizeListener(int delay) {
		this.timer = new Timer(delay, this);
		this.timer.setRepeats(false);
	}
	
	// Getters :
	
	public int getDelay() {
		return this.timer.getInitialDelay();
	}
	
	// Setters :
	
	public void setDelay(int delay) {
		this.timer.setInitialDelay(delay);
	}
	
	// Methods :
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.running) {
			this.running = true;
			
			while(this.resized) {
				this.resized = false;
				this.whenResized();
			}
			
			this.running = false;
		}
	}
	
	protected abstract void whenResized();
	
	@Override
	public void componentResized(ComponentEvent e) {
		this.resized = true;
		
		if(!this.running) {
			this.timer.restart();
		}
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
		// Nothing to do here.
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		// Nothing to do here.
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// Nothing to do here.
	}
	
}
