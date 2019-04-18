package fr.loria.orpailleur.revisor.engine.core.utils.files;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;

/**
 * @author William Philbert
 */
public class Resources {
	
	// Constants :
	
	private static final RevisorLogger LOGGER = RevisorLogger.instance();
	
	private static final String CANT_LOAD_IMAGE_FROM_FILE_X = "Couldn't load image from file '%s'.";
	
	// Methods :
	
	/**
	 * Creates a file if it does not exist yet. Also creates required directories.
	 * @param file - a File we want to ensure it exists.
	 */
	public static void createFileIfNotExists(File file) throws IOException, SecurityException {
		if(!file.exists()) {
			File parent = file.getParentFile();
			
			if(parent != null) {
				parent.mkdirs();
			}
			
			file.createNewFile();
		}
	}
	
	/**
	 * Reads a file with UTF8 charset and puts its content in a String. The file is found using the classpath.
	 * @param resource - The path of the resource (in the classpath).
	 * @return a String containing the content of the file.
	 */
	public static String readResource(String resource) throws IOException, SecurityException {
		return readResource(resource, StandardCharsets.UTF_8);
	}
	
	/**
	 * Reads a file with the given charset and puts its content in a String. The file is found using the classpath.
	 * @param resource - The path of the resource (in the classpath).
	 * @param encoding - The charset to be used to read the file.
	 * @return a String containing the content of the file.
	 */
	public static String readResource(String resource, Charset encoding) throws IOException, SecurityException {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			
			URI uri = classLoader.getResource(resource).toURI();
			String scheme = uri.getScheme();
			
			switch(scheme) {
				case "file":
					return new String(Files.readAllBytes(Paths.get(uri)), encoding);
				case "jar":
					String uriString = uri.toString();
					URI jarUri = new URI(uriString.substring(0, uriString.indexOf('!')));
					
					Map<String, String> env = new HashMap<>();
					env.put("create", "true");
					
					try(FileSystem jarFileSystem = FileSystems.newFileSystem(jarUri, env)) {
						return new String(Files.readAllBytes(Paths.get(uri)), encoding);
					}
				default:
					throw new IllegalArgumentException(String.format("Unsupported URI scheme: '%s'. Supported schemes: 'file', 'jar'.", scheme));
			}
		}
		catch(Exception argh) {
			throw new IOException(String.format("Couldn't read resource '%s'.", resource), argh);
		}
	}
	
	/**
	 * Creates an Image object from a resouce found using the classpath.
	 * @param resource - The path of the resource (in the classpath).
	 * @return an Image object.
	 * @throws IOException if the resource can't be find/open.
	 * @throws SecurityException if the current thread cannot get the context ClassLoader.
	 * @throws IllegalArgumentException if the resource can't be find/open.
	 */
	public static Image getImage(String resource) throws IOException, SecurityException, IllegalArgumentException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return ImageIO.read(classLoader.getResource(resource));
	}
	
	/**
	 * Creates an Image object from a resouce found using the classpath.
	 * @param resource - The path of the resource (in the classpath).
	 * @return an Image object or null if the resource can't be find/open.
	 */
	public static Image getImageOrNull(String resource) {
		try {
			return getImage(resource);
		}
		catch(Exception argh) {
			LOGGER.logError(argh, String.format(CANT_LOAD_IMAGE_FROM_FILE_X, resource));
			return null;
		}
	}
	
	/**
	 * Creates an ImageIcon object from a resouce found using the classpath.
	 * @param resource - The path of the resource (in the classpath).
	 * @return an ImageIcon object.
	 * @throws IOException if the resource can't be find/open.
	 * @throws SecurityException if the current thread cannot get the context ClassLoader.
	 * @throws IllegalArgumentException if the resource can't be find/open.
	 */
	public static ImageIcon getImageIcon(String resource) throws IOException, SecurityException, IllegalArgumentException {
		return new ImageIcon(getImage(resource));
	}
	
	/**
	 * Creates an ImageIcon object from a resouce found using the classpath.
	 * @param resource - The path of the resource (in the classpath).
	 * @return an ImageIcon object or null if the resource can't be find/open.
	 */
	public static ImageIcon getImageIconOrNull(String resource) {
		try {
			return getImageIcon(resource);
		}
		catch(Exception argh) {
			LOGGER.logError(argh, String.format(CANT_LOAD_IMAGE_FROM_FILE_X, resource));
			return null;
		}
	}
	
	/**
	 * Add jars to the classpath of the current ClassLoader.
	 * @param jars - jar files.
	 */
	public static void addJarsToClassPath(File... jars) {
		addJarsToClassPath(Thread.currentThread().getContextClassLoader(), jars);
	}
	
	/**
	 * Add jars to the classpath of the given ClassLoader.
	 * @param classLoader - a ClassLoader
	 * @param jars - jar files.
	 */
	public static void addJarsToClassPath(ClassLoader classLoader, File... jars) {
		if(classLoader instanceof URLClassLoader) {
			try {
				Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
				
				if(addUrlMethod != null) {
					addUrlMethod.setAccessible(true);
					
					for(File jar : jars) {
						try {
							addUrlMethod.invoke(classLoader, jar.toURI().toURL());
						}
						catch(Exception argh) {
							LOGGER.logError(argh);
						}
					}
				}
			}
			catch(Exception argh) {
				LOGGER.logError(argh);
			}
		}
		else {
			LOGGER.logError("Given ClassLoader isn't an URLClassLoader. Can't add jar to classpath.");
		}
	}
	
	public static final File getTmpDirectory() throws IOException {
		return getTmpDirectory(true);
	}
	
	public static final File getTmpDirectory(boolean deleteOnExit) throws IOException {
		File tmpDir = Files.createTempDirectory("revisor").toFile();
		
		if(deleteOnExit) {
			tmpDir.deleteOnExit();
		}
		
		LOGGER.logDebug("Created temp directory '%s'.", tmpDir);
		return tmpDir;
	}
	
	public static final File copyResourceToTmp(String resource, CopyOption... options) throws IOException, InvalidPathException {
		return copyResourceToTmp(resource, true, options);
	}
	
	public static final File copyResourceToTmp(String resource, File target, CopyOption... options) throws IOException, InvalidPathException {
		return copyResourceToTmp(resource, target, true, options);
	}
	
	public static final File copyResourceToTmp(String resource, FileSystem fileSystem, CopyOption... options) throws IOException, InvalidPathException {
		return copyResourceToTmp(resource, fileSystem, true, options);
	}
	
	public static final File copyResourceToTmp(String resource, FileSystem fileSystem, File target, CopyOption... options) throws IOException, InvalidPathException {
		return copyResourceToTmp(resource, fileSystem, target, true, options);
	}
	
	public static final File copyResourceToTmp(String resource, boolean deleteOnExit, CopyOption... options) throws IOException, InvalidPathException {
		return copyResourceToTmp(resource, getTmpDirectory(deleteOnExit), deleteOnExit, options);
	}
	
	public static final File copyResourceToTmp(String resource, FileSystem fileSystem, boolean deleteOnExit, CopyOption... options) throws IOException, InvalidPathException {
		return copyResourceToTmp(resource, fileSystem, getTmpDirectory(deleteOnExit), deleteOnExit, options);
	}
	
	public static final File copyResourceToTmp(String resource, File target, boolean deleteOnExit, CopyOption... options) throws IOException, InvalidPathException {
		Path sourcePath = Paths.get(resource).toAbsolutePath();
		Path targetPath = Paths.get(target.getPath()).toAbsolutePath();
		copyResourceToTmp(sourcePath, targetPath, deleteOnExit, options);
		return target;
	}
	
	public static final File copyResourceToTmp(String resource, FileSystem fileSystem, File target, boolean deleteOnExit, CopyOption... options) throws IOException, InvalidPathException {
		Path sourcePath = fileSystem.getPath(resource).toAbsolutePath();
		Path targetPath = Paths.get(target.getPath()).toAbsolutePath();
		copyResourceToTmp(sourcePath, targetPath, deleteOnExit, options);
		return target;
	}
	
	public static final void copyResourceToTmp(Path source, Path target, boolean deleteOnExit, CopyOption... options) throws IOException {
		Files.walkFileTree(source, new CopyDirVisitor(source.getParent(), target, deleteOnExit, options));
	}
	
	/**
	 * Returns a directory in which the given resources can be executed easily.<br />
	 * If the resources are all in the same directory, directly in the file system, the returned directory is the parent directory.<br />
	 * If the resources are in different directories or at least one resource is in a jar/zip, then the resources are copied in a temporary directory, which is returned.
	 * The temporary directory is deleted when the JVM exit normaly.
	 * @param resources - an list of strings representing files and/or directories.
	 * @return the directory in which the ressources can be executed easily. null if the list of resources is empty.
	 */
	public static final File getWorkingDirectoryWithResources(String... resources) {
		if(resources.length > 0) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			List<File> files = new LinkedList<>();
			Map<String, URI> jars = new HashMap<>();
			boolean needCopy = false;
			
			for(String resource : resources) {
				URL url = classLoader.getResource(resource);
				
				if(url != null) {
					try {
						URI uri = url.toURI();
						String scheme = uri.getScheme();
						
						switch(scheme) {
							case "file":
								files.add(new File(uri).getAbsoluteFile());
								break;
							case "jar":
								needCopy = true;
								String string = uri.toString();
								jars.put(resource, new URI(string.substring(0, string.indexOf('!'))));
								break;
							default:
								LOGGER.logError("Unsupported URI scheme: '%s'. Supported schemes: 'file', 'jar'.", scheme);
						}
					}
					catch(URISyntaxException | IllegalArgumentException | IndexOutOfBoundsException argh) {
						LOGGER.logError(argh);
					}
				}
				else {
					LOGGER.logError("Can't find resource '%s'.", resource);
				}
			}
			
			if(!needCopy && files.size() > 0) {
				File parent = files.get(0).getParentFile();
				
				for(int i = 0; i < files.size(); i++) {
					if(!files.get(i).getParentFile().equals(parent)) {
						needCopy = true;
						break;
					}
				}
				
				if(!needCopy) {
					return parent;
				}
			}
			
			if(needCopy) {
				try {
					File tmp = getTmpDirectory();
					
					for(File file : files) {
						try {
							copyResourceToTmp(file.toString(), tmp, StandardCopyOption.REPLACE_EXISTING);
						}
						catch(IOException | InvalidPathException argh) {
							LOGGER.logError(argh);
						}
					}
					
					Map<String, String> env = new HashMap<>();
					env.put("create", "true");
					
					for(Entry<String, URI> jar : jars.entrySet()) {
						try(FileSystem jarFileSystem = FileSystems.newFileSystem(jar.getValue(), env)) {
							copyResourceToTmp(jar.getKey(), jarFileSystem, tmp, StandardCopyOption.REPLACE_EXISTING);
						}
						catch(IOException | InvalidPathException argh) {
							LOGGER.logError(argh);
						}
					}
					
					return tmp;
				}
				catch(IOException aargh) {
					LOGGER.logError(aargh);
				}
			}
		}
		
		return null;
	}
	
}
