package fr.loria.orpailleur.revisor.webplatform.util;

import javax.ejb.Stateless;

import fr.loria.orpailleur.revisor.engine.core.utils.RevisorLogger;

/**
 * @author William Philbert
 */
@Stateless
public class AppLoggerBean extends RevisorLogger implements AppLogger {
	
	// Methods :
	
	@Override
	public String getLoggerName() {
		return "RevisorWebPlatformLogger";
	}
	
}
