package fr.loria.orpailleur.revisor.webplatform.validator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import fr.loria.orpailleur.revisor.webplatform.ejb.AccountManager;
import fr.loria.orpailleur.revisor.webplatform.exception.ValidationException;
import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;

/**
 * @author William Philbert
 */
@Named(value = "passwordValidator")
@RequestScoped
public class PasswordValidator implements Validator {
	
	// Fields :
	
	@EJB
	private AccountManager accountManager;
	
	@EJB
	private AppLogger logger;
	
	// Methods :
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		try {
			this.accountManager.validatePassword((String) value);
		}
		catch(ValidationException argh) {
			this.logger.logWarning("Validator : %s.", argh.getMessage());
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, argh.getMessage(), null));
		}
	}
	
}
