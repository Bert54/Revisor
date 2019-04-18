package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author William Philbert
 */
public class GuiConstants {
	
	// Constants :
	
	public static final Locale DEFAULT_LOCAL = Locale.US;
	public static final NumberFormat DEFAULT_NUMBER_FORMAT = NumberFormat.getInstance(DEFAULT_LOCAL);
	public static final int DEFAULT_TEXT_FIELD_SIZE = 15;
	
	public static final String REVISOR_PLATFORM = "Revisor Platform";
	
	public static final String FILE = "File";
	public static final String ENGINES = "Engines";
	public static final String OPTIONS = "Options";
	
	public static final String SAVE_COMMANDS = "Save Commands";
	public static final String LOAD_COMMANDS = "Load Commands";
	public static final String SAVE_COMMANDS_X = "Save Commands (%s)";
	public static final String LOAD_COMMANDS_X = "Load Commands (%s)";
	public static final String EXIT = "Exit";
	public static final String PREFERENCES = "Preferences";
	
	public static final String SAVE = "Save";
	public static final String DO_NOT_SAVE = "Don't save";
	public static final String RESET = "Reset";
	public static final String DEFAULT = "Default";
	public static final String NONE = "None";
	public static final String CLOSE = "Close";
	public static final String CANCEL = "Cancel";
	public static final String IMPORT = "Import";
	public static final String EXPORT = "Export";
	public static final String OVERWRITE = "Overwrite";
	
	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error";
	public static final String COMMANDS_LOADED = "Commands successfully loaded.";
	public static final String COMMANDS_SAVED = "Commands successfully saved.";
	public static final String CONFIG_IMPORTED = "Configuration successfully imported.";
	public static final String CONFIG_EXPORTED = "Configuration successfully exported.";
	public static final String CANT_WRITE_IN_PROGRAM_MAIN_DIR = "Can't write a file in the program main directory.";
	
	public static final String GLOBAL = "Global";
	public static final String THIS_TAB = "This tab :";
	public static final String ALL_TABS = "All tabs :";
	
	public static final String UNSAVED_CONFIG = "Unsaved configurations";
	public static final String THERE_ARE_UNSAVED_CONFIG = "There are unsaved configurations.";
	
	public static final String FILE_ALREADY_EXISTS = "File already exists";
	public static final String FILE_X_ALREADY_EXISTS = "The file '%s' already exists.";
	
	public static final String COPY_TEXT = "Copy (text version)";
	public static final String COPY_LATEX_LINEBREAKS = "Copy (latex version - auto line breaks)";
	public static final String COPY_LATEX_FORMULA = "Copy (latex version - pure formula)";
	
}
