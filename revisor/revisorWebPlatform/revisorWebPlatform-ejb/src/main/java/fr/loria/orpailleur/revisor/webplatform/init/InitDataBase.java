package fr.loria.orpailleur.revisor.webplatform.init;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import fr.loria.orpailleur.revisor.webplatform.ejb.AccountManager;
import fr.loria.orpailleur.revisor.webplatform.ejb.GroupManager;
import fr.loria.orpailleur.revisor.webplatform.entity.Group;
import fr.loria.orpailleur.revisor.webplatform.entity.User;
import fr.loria.orpailleur.revisor.webplatform.exception.PersistenceException;
import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;

/**
 * @author William Philbert
 */
@Singleton
@Startup
public class InitDataBase implements Serializable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@EJB
	private AccountManager accountManager;
	
	@EJB
	private GroupManager groupManager;
	
	@EJB
	private AppLogger logger;
	
	// Methods :
	
	@PostConstruct
	public void initData() {
		try {
			Group[] groups = {Group.ADMIN, Group.USER, Group.REGISTERED, Group.DENIED};
			
			for(Group group : groups) {
				this.groupManager.register(group);
			}
			
			List<User> users = new LinkedList<>();
			
			users.add(new User("chuck.norris@gmail.com", "password", "Chuck", "Norris", Group.ADMIN));
			users.add(new User("john.doe@gmail.com", "password", "John", "Doe", Group.USER));
			users.add(new User("a@a.aa", "a", "Chuck", "Norris", Group.ADMIN));
			
			for(User user : users) {
				this.accountManager.register(user);
			}
		}
		catch(PersistenceException argh) {
			this.logger.logError(argh, "Data base initialization failed : %s.", argh.getMessage());
		}
		
	}
	
}
