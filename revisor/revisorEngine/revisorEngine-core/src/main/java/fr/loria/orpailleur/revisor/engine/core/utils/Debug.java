package fr.loria.orpailleur.revisor.engine.core.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

/**
 * @author William Philbert
 */
public class Debug {
	
	// Constants :
	
	private static final RevisorLogger LOGGER = new RevisorLogger();
	
	// Methods :
	
	public static final void printWorkingDirectory() {
		LOGGER.logDebug("Working Directory:");
		LOGGER.logDebug("    %s", System.getProperty("user.dir"));
	}
	
	public static final void printCurrentDirectory() {
		LOGGER.logDebug("Current Directory:");
		LOGGER.logDebug("    %s", Paths.get("").toAbsolutePath().toString());
	}
	
	public static final void printClasspath() {
		ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
		LOGGER.logDebug("Classpath:");
		
		for(URL url : urls) {
			LOGGER.logDebug("    %s", url.getFile());
		}
	}
	
}
