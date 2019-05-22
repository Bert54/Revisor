package fr.loria.orpailleur.revisor.webplatform.session;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import fr.loria.orpailleur.revisor.webplatform.ejb.AccountManager;
import fr.loria.orpailleur.revisor.webplatform.entity.User;

/**
 * @author William Philbert
 */
@Named(value = "sessionManager")
@SessionScoped
public class SessionManager implements Serializable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@EJB
	private AccountManager userManger;
	
	private User connectedUser;
	
	// Constructors :
	
	public SessionManager() {
	}
	
	// Getters :
	
	public User getConnectedUser() {
		return this.connectedUser;
	}
	
	// Methods :
	
	public void connect(String email) {
		this.connectedUser = this.userManger.getUser(email);
	}
	
	public void disconnect() {
		this.connectedUser = null;
	}
	
	public boolean isConnected() {
		return this.connectedUser != null;
	}
	
	public boolean connectedUserIs(User user) {
		return this.isConnected() && this.connectedUser.equals(user);
	}
	
	public String getConnectedUserName() {
		return this.isConnected() ? this.connectedUser.getUserName() : "";
	}
	
	public void reloadConnectedUser() {
		this.connectedUser = this.userManger.getUser(this.connectedUser.getEmail());
	}
	
}
