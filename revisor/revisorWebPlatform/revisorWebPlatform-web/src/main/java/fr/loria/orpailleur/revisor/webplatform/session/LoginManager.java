package fr.loria.orpailleur.revisor.webplatform.session;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;

/**
 * @author William Philbert
 */
@Named("loginManager")
@RequestScoped
public class LoginManager implements Serializable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@Inject
	private SessionManager sessionManager;
	
	@EJB
	private AppLogger logger;
	
	private String email;
	private String password;
	
	// Constructors :
	
	public LoginManager() {
	}
	
	// Getters :
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	// Setters :
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	// Methods :
	
	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		try {
			request.login(this.email, this.password);
			this.sessionManager.connect(this.email);
			this.logger.logInfo("User '%s' logged in", this.email);
			return "pretty:home";
		}
		catch(ServletException argh) {
			this.logger.logWarning("Log in failed for user '%s'", this.email);
			context.addMessage("logIn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email/password.", null));
			return "";
		}
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String email = this.sessionManager.getConnectedUser().getEmail();
		
		try {
			request.logout();
			this.sessionManager.disconnect();
			this.logger.logInfo("User '%s' logged out", email);
		}
		catch(ServletException argh) {
			this.logger.logError(argh, "Error while disconnecting user '%s'", email);
			context.addMessage("logIn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured while disconnecting.", null));
		}
		
		return "pretty:home";
	}
	
}
