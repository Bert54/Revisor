package fr.loria.orpailleur.revisor.webplatform.ejb;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.loria.orpailleur.revisor.webplatform.entity.User;
import fr.loria.orpailleur.revisor.webplatform.exception.ValidationException;
import fr.loria.orpailleur.revisor.webplatform.security.EncryptionManager;
import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;
import fr.loria.orpailleur.revisor.webplatform.util.Constants;

/**
 * @author William Philbert
 */
@Stateless
public class AccountManagerBean implements AccountManager {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@PersistenceContext(unitName = Constants.PERSISTENCE_UNIT)
	private EntityManager em;
	
	@EJB
	private EncryptionManager encryptionManager;
	
	@EJB
	private AppLogger logger;
	
	// Methods :
	
	@Override
	public User getUser(String email) {
		return this.em.find(User.class, email);
	}
	
	@Override
	public boolean isUsedEmail(String email) {
		return this.getUser(email.toLowerCase()) != null;
	}
	
	@Override
	public boolean isValidEmail(String email) {
		if(email != null) {
			Pattern p = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
			Matcher m = p.matcher(email.toLowerCase());
			return m.matches();
		}
		
		return false;
	}
	
	@Override
	public boolean isValidPassword(String password) {
		return !(password == null || password.isEmpty());
	}
	
	@Override
	public void validateEmail(String email) throws ValidationException {
		if(!this.isValidEmail(email)) {
			throw new ValidationException("Invalid email");
		}
		else if(this.isUsedEmail(email)) {
			throw new ValidationException("Email already registered");
		}
	}
	
	@Override
	public void validatePassword(String password) throws ValidationException {
		if(!this.isValidPassword(password)) {
			throw new ValidationException("Invalid password");
		}
	}
	
	@Override
	public boolean register(User user) {
		try {
			this.validateEmail(user.getEmail());
			this.validatePassword(user.getPassword());
			String encryptedPassword = this.encryptionManager.hash(user.getPassword());
			user.setPassword(encryptedPassword);
			this.em.persist(user);
			this.logger.logInfo("Successfully registed user '%s'.", user.getEmail());
			return true;
		}
		catch(ValidationException argh) {
			this.logger.logWarning("Registration failed for user '%s' : '%s'.", user.getEmail(), argh.getMessage());
		}
		catch(NoSuchAlgorithmException argh) {
			this.logger.logError(argh, "Registration failed for user '%s', password couldn't be hashed.", user.getEmail());
		}
		
		return false;
	}
	
}
