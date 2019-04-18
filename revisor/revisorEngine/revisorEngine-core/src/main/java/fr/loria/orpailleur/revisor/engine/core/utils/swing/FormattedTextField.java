package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Format;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * @author William Philbert
 */
public class FormattedTextField extends JFormattedTextField {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public FormattedTextField() {
		super();
		this.init();
	}
	
	public FormattedTextField(Format format) {
		super(format);
		this.init();
	}
	
	public FormattedTextField(AbstractFormatter formatter) {
		super(formatter);
		this.init();
	}
	
	public FormattedTextField(AbstractFormatterFactory formatterFactory) {
		super(formatterFactory);
		this.init();
	}
	
	public FormattedTextField(Object object) {
		super(object);
		this.init();
	}
	
	public FormattedTextField(AbstractFormatterFactory formatterFactory, Object object) {
		super(formatterFactory, object);
		this.init();
	}
	
	// Methods :
	
	private void init() {
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(final MouseEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						if(e.getSource() instanceof JTextField) {
							JTextField textField = (JTextField) e.getSource();
							int offset = textField.viewToModel(e.getPoint());
							textField.setCaretPosition(offset);
						}
					}
					
				});
			}
			
		});
	}
	
}
