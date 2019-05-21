package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Color;
import java.awt.Insets;

import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXFormula.TeXIconBuilder;
import org.scilab.forge.jlatexmath.TeXIcon;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.Label;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.ResizeListener;

/**
 * @author William Philbert
 */
public class LatexDisplay extends Label {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 1000;
	
	// Fields :
	
	protected final ResizeListener resizeListener;
	protected final TeXFormula formula;
	protected final TeXIconBuilder iconBuilder;
	
	// Constructors :
	
	public LatexDisplay(String latex, Color textColor) throws ParseException {
		this.formula = new TeXFormula(latex);
		this.iconBuilder = this.formula.new TeXIconBuilder();
		
		this.iconBuilder.setStyle(TeXConstants.STYLE_DISPLAY);
		this.iconBuilder.setType(TeXFormula.BOLD);
		this.iconBuilder.setSize(this.getFont().getSize() * 1.5F);
		this.iconBuilder.setFGColor(textColor);
		
		// setWidth() must be used before we can use setInterLineSpacing() and setIsMaxWidth().
		this.iconBuilder.setWidth(TeXConstants.UNIT_PIXEL, DEFAULT_WIDTH, TeXConstants.ALIGN_CENTER);
		this.iconBuilder.setInterLineSpacing(TeXConstants.UNIT_PIXEL, 2);
		this.iconBuilder.setIsMaxWidth(true);
		
		this.resizeListener = new ResizeListener(200) {
			
			@Override
			protected void whenResized() {
				LatexDisplay.this.updateImage();
			}
			
		};
		
		this.addComponentListener(this.resizeListener);
		this.updateImage();
	}
	
	// Methods :
	
	/**
	 * This method is used to know how much space is available to display LaTeX.
	 * @return the available width in this component.
	 */
	protected int getMaxWidth() {
		int width = this.getWidth();
		return (width > 0) ? width : DEFAULT_WIDTH;
	}
	
	/**
	 * Updates the LaTeX image.
	 */
	public void updateImage() {
		this.iconBuilder.setWidth(TeXConstants.UNIT_PIXEL, this.getMaxWidth(), TeXConstants.ALIGN_CENTER);
		TeXIcon icon = this.iconBuilder.build();
		icon.setInsets(new Insets(2, 2, 2, 2));
		this.setIcon(icon);
	}
	
	// Static methods :
	
	/**
	 * This method is used to avoid the load time when the first latex formula is displayed.
	 */
	public static void prepareIconBuilder() {
		TeXFormula formula = new TeXFormula("formula");
		formula.createTeXIcon(TeXFormula.BOLD, 15);
	}
	
}
