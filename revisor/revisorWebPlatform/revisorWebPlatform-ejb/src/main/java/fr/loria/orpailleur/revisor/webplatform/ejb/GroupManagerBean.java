package fr.loria.orpailleur.revisor.webplatform.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.loria.orpailleur.revisor.webplatform.entity.Group;
import fr.loria.orpailleur.revisor.webplatform.exception.PersistenceException;
import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;
import fr.loria.orpailleur.revisor.webplatform.util.Constants;

/**
 * @author William Philbert
 */
@Stateless
public class GroupManagerBean implements GroupManager {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@PersistenceContext(unitName = Constants.PERSISTENCE_UNIT)
	private EntityManager em;
	
	@EJB
	private AppLogger logger;
	
	// Methods :
	
	@Override
	public Group getGroup(String name) {
		return this.em.find(Group.class, name);
	}
	
	@Override
	public boolean isValidName(String name) {
		return !(name == null || name.isEmpty());
	}
	
	@Override
	public boolean isUsedName(String name) {
		return this.getGroup(name) != null;
	}
	
	@Override
	public void register(Group group) throws PersistenceException {
		String message;
		
		if(!this.isValidName(group.getName())) {
			message = "Invalid name";
		}
		else if(this.isUsedName(group.getName())) {
			message = "Name already registered";
		}
		else {
			this.em.persist(group);
			this.logger.logInfo("Successfully registered group '%s'", group.getName());
			return;
		}
		
		this.logger.logError("Registration failed for group '%s' : %s.", group.getName(), message);
		throw new PersistenceException(message);
	}
	
}
