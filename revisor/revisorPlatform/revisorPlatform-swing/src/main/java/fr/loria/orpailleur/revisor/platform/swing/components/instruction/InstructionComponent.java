package fr.loria.orpailleur.revisor.platform.swing.components.instruction;

import java.awt.Color;
import java.util.regex.Matcher;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;
import fr.loria.orpailleur.revisor.engine.core.utils.config.GuiConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiConstants;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;
import fr.loria.orpailleur.revisor.platform.swing.components.RevisorPlatformMainPanel;

/**
 * @author William Philbert
 */
public abstract class InstructionComponent extends Panel {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	protected static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	// Fields :
	
	protected final GuiConfig config;
	protected final JComponent display;
	protected final String text;
	protected final String latex;
	protected final String latexWithLineBreaks;
	protected final boolean isLatex;
	
	// Constructors :
	
	public InstructionComponent(final GuiConfig config, final String text, final String latex) {
		this.config = config;
		this.text = text;
		
		Color borderColor = this.getBorderColor();
		boolean hasBorder = (borderColor != null) && (borderColor.getAlpha() != 0);
		this.setBorder(hasBorder ? BorderFactory.createLineBorder(borderColor, 1, true) : null);
		
		JPopupMenu popup = new JPopupMenu();
		this.setComponentPopupMenu(popup);
		
		Icon copyIcon = RevisorPlatformMainPanel.COPY_ICON;
		popup.add(new CopyToClipboardMenuItem(GuiConstants.COPY_TEXT, copyIcon, this.text));
		
		this.isLatex = latex != null;
		
		if(this.isLatex) {
			this.latex = "$" + latex.trim() + "$";
			// this.latexWithLineBreaks = "\\text{" + this.latex.replaceAll("\\s+", Matcher.quoteReplacement("$ $")) + "}";
			this.latexWithLineBreaks = "\\text{" + this.latex.replaceAll(System.lineSeparator(), Matcher.quoteReplacement("\\\\")) + "}";
			// the system implement by William breaks a lot of TeX commands and on top of that, doesn't work as expected. For
			// instance, environments cannot be used anymore.
			// Therefore I've taken the opportunity to slightly modify that. If you need a line break, tou can just insert a regular
			// Java line break. The new regxp replaces them with TeX line separators. Of course you can use TeX line separators
			// directly, the command will have the same behavior.
			popup.add(new CopyToClipboardMenuItem(GuiConstants.COPY_LATEX_LINEBREAKS, copyIcon, this.latexWithLineBreaks));
			popup.add(new CopyToClipboardMenuItem(GuiConstants.COPY_LATEX_FORMULA, copyIcon, this.latex));
		}
		else {
			this.latex = null;
			this.latexWithLineBreaks = null;
		}
		
		this.display = this.createDisplayComponent();
		this.display.setInheritsPopupMenu(true);
		
		Layout.add(this, this.display, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
	}
	
	// Methods :
	
	protected JComponent createDisplayComponent() {
		if(this.isLatex) {
			try {
				return new LatexDisplay(this.latexWithLineBreaks, this.getTextColor());
			}
			catch(Exception argh) {
				LOGGER.logError(argh);
			}
		}
		
		return new TextDisplay(this.text, this.getTextColor());
	}
	
	protected abstract Color getTextColor();
	
	protected abstract Color getBorderColor();
	
}
