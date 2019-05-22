package fr.loria.orpailleur.revisor.webplatform.security;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Local;

/**
 * @author William Philbert
 */
@Local
public interface EncryptionManager {
	
	public String hash(String password) throws NoSuchAlgorithmException;
	
}
