package fr.loria.orpailleur.revisor.engine.core.utils.config;

import java.awt.Color;

import javax.swing.UIManager;

import fr.loria.orpailleur.revisor.engine.core.utils.config.item.BooleanConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.item.ColorConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.item.IntegerConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;

/**
 * @author William Philbert
 */
public class GuiConfig extends ConsoleConfig {
	
	// Fields :
	
	public final BooleanConfig displayLatex = new BooleanConfig("Display latex", true, "If true, results will be displayed in latex form; Else results will be displayed in text form.");
	public final BooleanConfig recursiveSave = new BooleanConfig("Recursive save", true, "If true, \"load\" commands will be saved; Else commands loaded by \"load\" commands will be saved.");
	
	public final ColorConfig panelBackground = new ColorConfig("Panel background", UIManager.getColor("Panel.background"), "The interface background color (not the console).");
	public final ColorConfig consoleBackground = new ColorConfig("Console background", Color.BLACK, "The console background color.");
	
	public final ColorConfig consoleNormalText = new ColorConfig("Console normal text", new Color(0xAACCEE), "The commands text color in the console.");
	public final ColorConfig consoleResultText = new ColorConfig("Console result text", Color.GREEN, "The results text color in the console.");
	public final ColorConfig consoleErrorText = new ColorConfig("Console error text", Color.RED, "The errors text color in the console.");
	public final ColorConfig consoleWarningText = new ColorConfig("Console warning text", Color.YELLOW, "The warnings text color in the console.");
	
	public final ColorConfig consoleNormalBorder = new ColorConfig("Console normal border", null, "The commands border color in the console.");
	public final ColorConfig consoleResultBorder = new ColorConfig("Console result border", null, "The results border color in the console.");
	public final ColorConfig consoleErrorBorder = new ColorConfig("Console error border", null, "The errors border color in the console.");
	public final ColorConfig consoleWarningBorder = new ColorConfig("Console warning border", null, "The warnings border color in the console.");
	
	public final ColorConfig consoleValidatorNeutral = new ColorConfig("Console parser waiting", new Color(0xAACCEE), "The command prompt color when there is no command.");
	public final ColorConfig consoleValidatorValid = new ColorConfig("Console parser valid", Color.GREEN, "The command prompt color when there is a valid command.");
	public final ColorConfig consoleValidatorInvalid = new ColorConfig("Console parser invalid", Color.RED, "The command prompt color when there is an invalid command.");
	public final ColorConfig consoleValidatorWarning = new ColorConfig("Console parser warning", Color.YELLOW, "The command prompt color when there is a command with warnings.");
	
	// TODO - GUI - Trouver comment controler globalement la taille de la police pour TOUTE l'interface (que ce soit dans la console ou dans le menu, du text ou du LaTeX).
	public final IntegerConfig fontSize = new IntegerConfig("Font size", 12, "This is not yet functional. It will allow to choose the size of all fonts in the interface.");
	
	// Constructors :
	
	public GuiConfig(ConfigStorage storage) {
		super(storage);
	}
	
	// Methods :
	
	@Override
	public String description() {
		return "This properties are used by the graphical user inteface (for all Revisor consoles).";
	}
	
	@Override
	protected boolean formatInput() {
		return true;
	}
	
}
