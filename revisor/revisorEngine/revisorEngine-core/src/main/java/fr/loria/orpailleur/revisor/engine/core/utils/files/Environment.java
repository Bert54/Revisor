package fr.loria.orpailleur.revisor.engine.core.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.GuiUtils;

/**
 * @author William Philbert
 */
public class Environment {
	
	// Constants :
	
	private static final String FILE_X_IS_NOT_A_DIRECTORY = "The file '%s' is not a directory.";
	
	// Fields :
	
	private final Stack<File> stack;
	
	// Constructors :
	
	/**
	 * Creates a new environment with the working directory as base.
	 */
	public Environment() throws IllegalArgumentException, IOException, SecurityException {
		this(GuiUtils.WORKING_DIR);
	}
	
	/**
	 * Creates a new environment with the given base directory.
	 * @param baseDirectory - a File representing a directory.
	 */
	public Environment(File baseDirectory) throws IllegalArgumentException, IOException, SecurityException {
		this.stack = new Stack<>();
		this.goTo(baseDirectory);
	}
	
	// Methods :
	
	/**
	 * Returns the current directory of this environment.
	 * @return the current directory of this environment.
	 */
	public File getCurrentDirectory() {
		try {
			return this.stack.peek();
		}
		catch(EmptyStackException argh) {
			return null;
		}
	}
	
	/**
	 * Returns the canonical version of the given file.
	 * If the given file is not absolute, the result is based on the current file of this environment.
	 * @param file - the file to convert to a canonical version.
	 * @return the canonical version of the given file.
	 */
	public File getCanonicalFile(File file) throws IOException, SecurityException {
		if(!(this.stack.empty() || file.isAbsolute())) {
			File currentDir = this.getCurrentDirectory();
			file = new File(currentDir, file.getPath());
		}
		
		file = file.getCanonicalFile();
		
		return file;
	}
	
	/**
	 * Change the current directory to the parent directory of the given file.
	 * Previous directories are saved in a stack so back() can be used to get back.
	 * @param file - a directory.
	 * @return return true if the current directory has changed, else false.
	 */
	public boolean goToParent(File file) throws IllegalArgumentException, SecurityException, IOException {
		File parent = file.getParentFile();
		
		if(parent != null) {
			return this.goTo(parent);
		}
		else if(this.stack.empty()) {
			this.goTo(GuiUtils.WORKING_DIR);
		}
		
		return false;
	}
	
	/**
	 * Change the current directory to the given directory.
	 * Previous directories are saved in a stack so back() can be used to get back.
	 * @param file - a directory.
	 * @return return true if the current directory has changed, else false.
	 */
	public boolean goTo(File file) throws IllegalArgumentException, IOException, SecurityException {
		File dir = this.getCanonicalFile(file);
		
		if(!dir.isDirectory()) {
			throw new IllegalArgumentException(String.format(FILE_X_IS_NOT_A_DIRECTORY, dir));
		}
		
		if(this.stack.empty()) {
			this.stack.push(dir);
		}
		else if(!dir.equals(this.getCurrentDirectory())) {
			this.stack.push(dir);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Change the current directory to the previous one.
	 * @throws IllegalStateException if there is no previous directory.
	 */
	public void back() throws IllegalStateException {
		if(this.stack.size() > 1) {
			this.stack.pop();
		}
		else {
			throw new IllegalStateException();
		}
	}
	
}
