package fr.loria.orpailleur.revisor.webplatform.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;

/**
 * @author William Philbert
 */
@Stateless
public class EncryptionManagerBean implements EncryptionManager {
	
	// Constants :
	
	private static final String HASH_ALGORITHM = "SHA-256";
	
	// Methods :
	
	@Override
	public String hash(String message) throws NoSuchAlgorithmException {
		StringBuilder builder = new StringBuilder();
		
		MessageDigest sha256 = MessageDigest.getInstance(HASH_ALGORITHM);
		byte[] passBytes = message.getBytes();
		byte[] passHash = sha256.digest(passBytes);
		
		// Convertion de l'octet vers l'hexadécimal
		for(int i = 0; i < passHash.length; i++) {
			builder.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return builder.toString();
	}
	
}
