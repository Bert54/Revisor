package fr.loria.orpailleur.revisor.engine.core.utils.swing;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;

/**
 * @author William Philbert
 */
public class GuiUtils {
	
	// Constants :
	
	public static final int FILES_ONLY = JFileChooser.FILES_ONLY;
	public static final int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
	public static final int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;
	public static final File WORKING_DIR = Paths.get("").toAbsolutePath().toFile();
	public static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	private static final JFileChooser FILE_CHOOSER = new JFileChooser(WORKING_DIR);
	private static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	// Fields :
	
	private static boolean fileChooserOpen = false;
	
	// Methods :
	
	/**
	 * Displays an error message dialog.
	 * @param component - the component which sent the message.
	 * @param message - the message to display.
	 * @param title - a title for the dialog.
	 */
	public static void showError(final Component component, final String message, final String title) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays an waring message dialog.
	 * @param component - the component which sent the message.
	 * @param message - the message to display.
	 * @param title - a title for the dialog.
	 */
	public static void showWarning(final Component component, final String message, final String title) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Displays an information message dialog.
	 * @param component - the component which sent the message.
	 * @param message - the message to display.
	 * @param title - a title for the dialog.
	 */
	public static void showInfo(final Component component, final String message, final String title) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Dislpays an option dialog.
	 * @param component - the component which asked the question.
	 * @param question - the question to ask to the user.
	 * @param title - a title for the dialog.
	 * @param defaultChoice - the index of the default choice.
	 * @param options - the strings displayed on the choices buttons.
	 * @return the user choice.
	 */
	public static int showOptions(final Component component, final String question, final String title, final int defaultChoice, final String... options) {
		return JOptionPane.showOptionDialog(component, question, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[defaultChoice]);
	}
	
	/**
	 * Displays a file chooser, on the default directory, with the FILES_ONLY selection mode.
	 * @param component - the component which asked a file chooser.
	 * @param type - true for a "save" dialog, false for a "open" dialog.
	 * @return the selected File, if the user clicked "open", else null.
	 */
	public static File showFileChooser(final Component component, final boolean type) throws IOException, SecurityException {
		return showFileChooser(component, type, FILES_ONLY, (File) null);
	}
	
	/**
	 * Displays a file chooser, on the given directory, with the FILES_ONLY selection mode.
	 * @param component - the component which asked a file chooser.
	 * @param type - true for a "save" dialog, false for a "open" dialog.
	 * @param path - the path of the directory to display.
	 * @return the selected File, if the user clicked "open", else null.
	 */
	public static File showFileChooser(final Component component, final boolean type, final String path) throws IOException, SecurityException {
		return showFileChooser(component, type, FILES_ONLY, path);
	}
	
	/**
	 * Displays a file chooser, on the given directory, with the FILES_ONLY selection mode.
	 * @param component - the component which asked a file chooser.
	 * @param type - true for a "save" dialog, false for a "open" dialog.
	 * @param directory - the directory to display.
	 * @return the selected File, if the user clicked "open", else null.
	 */
	public static File showFileChooser(final Component component, final boolean type, final File directory) throws IOException, SecurityException {
		return showFileChooser(component, type, FILES_ONLY, directory);
	}
	
	/**
	 * Displays a file chooser, on the default directory, with the given selection mode.
	 * @param component - the component which asked a file chooser.
	 * @param type - true for a "save" dialog, false for a "open" dialog.
	 * @param selectionMode - the selection mode (FILES_ONLY, DIRECTORIES_ONLY, FILES_AND_DIRECTORIES).
	 * @return the selected File, if the user clicked "open", else null.
	 */
	public static File showFileChooser(final Component component, final boolean type, final int selectionMode) throws IOException, SecurityException {
		return showFileChooser(component, type, selectionMode, (File) null);
	}
	
	/**
	 * Displays a file chooser, on the given directory, with the given selection mode.
	 * @param component - the component which asked a file chooser.
	 * @param type - true for a "save" dialog, false for a "open" dialog.
	 * @param selectionMode - the selection mode (FILES_ONLY, DIRECTORIES_ONLY, FILES_AND_DIRECTORIES).
	 * @param path - the path of the directory to display.
	 * @return the selected File, if the user clicked "open", else null.
	 */
	public static File showFileChooser(final Component component, final boolean type, final int selectionMode, final String path) throws IOException, SecurityException {
		return showFileChooser(component, type, selectionMode, new File(path));
	}
	
	/**
	 * Displays a file chooser, on the given directory, with the given selection mode.
	 * @param component - the component which asked a file chooser.
	 * @param type - true for a "save" dialog, false for a "open" dialog.
	 * @param selectionMode - the selection mode (FILES_ONLY, DIRECTORIES_ONLY, FILES_AND_DIRECTORIES).
	 * @param directory - the directory to display.
	 * @return the selected File, if the user clicked "open", else null.
	 */
	public static File showFileChooser(final Component component, final boolean type, final int selectionMode, final File directory) throws SecurityException, IOException {
		File result = null;
		
		if(!fileChooserOpen) {
			FILE_CHOOSER.setFileSelectionMode(selectionMode);
			
			if(directory != null && directory.isDirectory()) {
				FILE_CHOOSER.setCurrentDirectory(directory);
			}
			
			try {
				fileChooserOpen = true;
				int choix = type ? FILE_CHOOSER.showSaveDialog(component) : FILE_CHOOSER.showOpenDialog(component);
				
				if(choix == JFileChooser.APPROVE_OPTION) {
					File selectedFile = FILE_CHOOSER.getSelectedFile();
					
					if(selectedFile != null) {
						result = selectedFile.getCanonicalFile();
					}
				}
			}
			finally {
				fileChooserOpen = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Places a String on the clipboard.
	 * @param text - the String to place on the clipboard.
	 */
	public static void setClipboardContents(String text) {
		CLIPBOARD.setContents(new StringSelection(text), null);
	}
	
	/**
	 * Returns the String residing on the clipboard.
	 * @return any text found on the Clipboard, or an empty string if there is none.
	 */
	public static String getClipboardContents() {
		Transferable contents = CLIPBOARD.getContents(null);
		
		if((contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				return (String) contents.getTransferData(DataFlavor.stringFlavor);
			}
			catch(UnsupportedFlavorException | IOException argh) {
				LOGGER.logError(argh);
			}
		}
		
		return "";
	}
	
}
