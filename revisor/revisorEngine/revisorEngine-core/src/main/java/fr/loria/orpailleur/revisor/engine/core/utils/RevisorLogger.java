package fr.loria.orpailleur.revisor.engine.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author William Philbert
 */
public class RevisorLogger {
	
	// Constants :
	
	private static final RevisorLogger INSTANCE = new RevisorLogger();
	
	// Fields :
	
	private Logger logger;
	
	// Constructors :
	
	public RevisorLogger() {
		this.logger = LogManager.getFormatterLogger(this.getLoggerName());
	}
	
	// Methods :
	
	public static RevisorLogger instance() {
		return INSTANCE;
	}
	
	public String getLoggerName() {
		return "RevisorLogger";
	}
	
	public void logFatal(String messgage) {
		this.logger.fatal(messgage);
	}
	
	public void logError(String messgage) {
		this.logger.error(messgage);
	}
	
	public void logWarning(String messgage) {
		this.logger.warn(messgage);
	}
	
	public void logInfo(String messgage) {
		this.logger.info(messgage);
	}
	
	public void logDebug(String messgage) {
		this.logger.debug(messgage);
	}
	
	public void logTrace(String messgage) {
		this.logger.trace(messgage);
	}
	
	public void logFatal(String messgage, Object... params) {
		this.logger.fatal(messgage, params);
	}
	
	public void logError(String messgage, Object... params) {
		this.logger.error(messgage, params);
	}
	
	public void logWarning(String messgage, Object... params) {
		this.logger.warn(messgage, params);
	}
	
	public void logInfo(String messgage, Object... params) {
		this.logger.info(messgage, params);
	}
	
	public void logDebug(String messgage, Object... params) {
		this.logger.debug(messgage, params);
	}
	
	public void logTrace(String messgage, Object... params) {
		this.logger.trace(messgage, params);
	}
	
	public void logFatal(Throwable t) {
		this.logger.catching(Level.FATAL, t);
	}
	
	public void logError(Throwable t) {
		this.logger.catching(Level.ERROR, t);
	}
	
	public void logWarning(Throwable t) {
		this.logger.catching(Level.WARN, t);
	}
	
	public void logInfo(Throwable t) {
		this.logger.catching(Level.INFO, t);
	}
	
	public void logDebug(Throwable t) {
		this.logger.catching(Level.DEBUG, t);
	}
	
	public void logTrace(Throwable t) {
		this.logger.catching(Level.TRACE, t);
	}
	
	public void logFatal(Throwable t, String messgage) {
		this.logger.fatal(messgage);
		this.logger.catching(Level.FATAL, t);
	}
	
	public void logError(Throwable t, String messgage) {
		this.logger.error(messgage);
		this.logger.catching(Level.ERROR, t);
	}
	
	public void logWarning(Throwable t, String messgage) {
		this.logger.warn(messgage);
		this.logger.catching(Level.WARN, t);
	}
	
	public void logInfo(Throwable t, String messgage) {
		this.logger.info(messgage);
		this.logger.catching(Level.INFO, t);
	}
	
	public void logDebug(Throwable t, String messgage) {
		this.logger.debug(messgage);
		this.logger.catching(Level.DEBUG, t);
	}
	
	public void logTrace(Throwable t, String messgage) {
		this.logger.trace(messgage);
		this.logger.catching(Level.TRACE, t);
	}
	
	public void logFatal(Throwable t, String messgage, Object... params) {
		this.logger.fatal(messgage, params);
		this.logger.catching(Level.FATAL, t);
	}
	
	public void logError(Throwable t, String messgage, Object... params) {
		this.logger.error(messgage, params);
		this.logger.catching(Level.ERROR, t);
	}
	
	public void logWarning(Throwable t, String messgage, Object... params) {
		this.logger.warn(messgage, params);
		this.logger.catching(Level.WARN, t);
	}
	
	public void logInfo(Throwable t, String messgage, Object... params) {
		this.logger.info(messgage, params);
		this.logger.catching(Level.INFO, t);
	}
	
	public void logDebug(Throwable t, String messgage, Object... params) {
		this.logger.debug(messgage, params);
		this.logger.catching(Level.DEBUG, t);
	}
	
	public void logTrace(Throwable t, String messgage, Object... params) {
		this.logger.trace(messgage, params);
		this.logger.catching(Level.TRACE, t);
	}
	
}
