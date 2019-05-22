package fr.loria.orpailleur.revisor.webplatform.account;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.loria.orpailleur.revisor.webplatform.ejb.AccountManager;
import fr.loria.orpailleur.revisor.webplatform.entity.User;
import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;

/**
 * @author William Philbert
 */
@Named("signUpBean")
@ViewScoped
public class SignUpBean implements Serializable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@EJB
	private AccountManager accountManager;
	
	@EJB
	private AppLogger logger;
	
	private User user;
	private String confirmPassword;
	
	// Constructors :
	
	public SignUpBean() {
		this.user = new User();
		this.setConfirmPassword("");
	}
	
	// Getters :
	
	public User getUser() {
		return this.user;
	}
	
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	
	// Setters :
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	// Methods :
	
	public String register() {
		this.logger.logDebug("######################################## register()");
		if(this.confirmPassword.equals(this.user.getPassword()) && this.accountManager.register(this.user)) {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			return "pretty:home";
		}
		else {
			return "pretty:";
		}
	}
	
}
