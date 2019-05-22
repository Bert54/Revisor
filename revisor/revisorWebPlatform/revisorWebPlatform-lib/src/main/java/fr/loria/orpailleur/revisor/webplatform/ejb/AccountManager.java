package fr.loria.orpailleur.revisor.webplatform.ejb;

import java.io.Serializable;

import javax.ejb.Local;

import fr.loria.orpailleur.revisor.webplatform.entity.User;
import fr.loria.orpailleur.revisor.webplatform.exception.ValidationException;

/**
 * @author William Philbert
 */
@Local
public interface AccountManager extends Serializable {
	
	public User getUser(String email);
	
	public boolean isValidEmail(String email);
	
	public boolean isUsedEmail(String email);
	
	public boolean isValidPassword(String password);

	public void validateEmail(String email) throws ValidationException;

	public void validatePassword(String password) throws ValidationException;
	
	public boolean register(User user);
	
}
