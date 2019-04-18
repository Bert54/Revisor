package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.border.Border;

import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;

/**
 * @author William Philbert
 */
public class ColorChooser extends Panel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	protected static final Border NORMAL_BORDER = BorderFactory.createLineBorder(Color.GRAY, 1);
	protected static final Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);
	
	// Fields :
	
	private final JColorChooser chooser = new JColorChooser();
	private final JDialog dialog;
	private Color color;
	
	// Constructors :
	
	public ColorChooser() {
		this.color = Color.WHITE;
		this.setBorder(NORMAL_BORDER);
		this.setMinimumSize(new Dimension(10, 10));
		
		ActionListener okAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorChooser.this.ok();
			}
			
		};
		
		ActionListener cancelAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorChooser.this.cancel();
			}
			
		};
		
		this.chooser.setPreviewPanel(new Panel());
		this.dialog = JColorChooser.createDialog(this, "Select a color", true, this.chooser, okAction, cancelAction);
		this.dialog.setLocale(GuiConstants.DEFAULT_LOCAL);
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// Nothing do to here.
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// Nothing do to here.
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				ColorChooser.this.setBorder(NORMAL_BORDER);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				ColorChooser.this.setBorder(SELECTED_BORDER);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorChooser.this.showDialog();
			}
			
		});
	}
	
	// Getters :
	
	public Color getColor() {
		return this.color;
	}
	
	// Setters :
	
	public void setColor(Color color) {
		if(this.color != color) {
			this.color = color;
			this.setBackground((color == null || color.getAlpha() == 0) ? null : color);
			this.setToolTipText(StringUtils.toHexString(color));
		}
	}
	
	// Methods :
	
	public void showDialog() {
		this.chooser.setColor(this.color);
		this.dialog.setVisible(true);
	}
	
	protected void ok() {
		this.setColor(this.chooser.getColor());
		this.dialog.setVisible(false);
	}
	
	protected void cancel() {
		this.chooser.setColor(this.color);
		this.dialog.setVisible(false);
	}
	
}
