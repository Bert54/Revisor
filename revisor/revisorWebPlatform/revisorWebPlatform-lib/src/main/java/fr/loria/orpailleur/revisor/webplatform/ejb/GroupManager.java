package fr.loria.orpailleur.revisor.webplatform.ejb;

import java.io.Serializable;

import javax.ejb.Local;

import fr.loria.orpailleur.revisor.webplatform.entity.Group;
import fr.loria.orpailleur.revisor.webplatform.exception.PersistenceException;

/**
 * @author William Philbert
 */
@Local
public interface GroupManager extends Serializable {
	
	public Group getGroup(String name);
	
	public boolean isValidName(String name);
	
	public boolean isUsedName(String name);
	
	public void register(Group group) throws PersistenceException;
	
}
