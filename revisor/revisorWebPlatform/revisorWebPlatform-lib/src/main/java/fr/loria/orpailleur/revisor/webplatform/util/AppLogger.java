package fr.loria.orpailleur.revisor.webplatform.util;

import javax.ejb.Local;

/**
 * @author William Philbert
 */
@Local
public interface AppLogger {
	
	public void logFatal(String messgage);
	
	public void logError(String messgage);
	
	public void logWarning(String messgage);
	
	public void logInfo(String messgage);
	
	public void logDebug(String messgage);
	
	public void logTrace(String messgage);
	
	public void logFatal(String messgage, Object... params);
	
	public void logError(String messgage, Object... params);
	
	public void logWarning(String messgage, Object... params);
	
	public void logInfo(String messgage, Object... params);
	
	public void logDebug(String messgage, Object... params);
	
	public void logTrace(String messgage, Object... params);
	
	public void logFatal(Throwable t);
	
	public void logError(Throwable t);
	
	public void logWarning(Throwable t);
	
	public void logInfo(Throwable t);
	
	public void logDebug(Throwable t);
	
	public void logTrace(Throwable t);
	
	public void logFatal(Throwable t, String messgage);
	
	public void logError(Throwable t, String messgage);
	
	public void logWarning(Throwable t, String messgage);
	
	public void logInfo(Throwable t, String messgage);
	
	public void logDebug(Throwable t, String messgage);
	
	public void logTrace(Throwable t, String messgage);
	
	public void logFatal(Throwable t, String messgage, Object... params);
	
	public void logError(Throwable t, String messgage, Object... params);
	
	public void logWarning(Throwable t, String messgage, Object... params);
	
	public void logInfo(Throwable t, String messgage, Object... params);
	
	public void logDebug(Throwable t, String messgage, Object... params);
	
	public void logTrace(Throwable t, String messgage, Object... params);
	
}
