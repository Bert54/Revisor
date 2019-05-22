package fr.loria.orpailleur.revisor.webplatform.validator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import fr.loria.orpailleur.revisor.webplatform.util.AppLogger;

/**
 * @author William Philbert
 */
@Named(value = "passwordConfirmationValidator")
@RequestScoped
public class PasswordConfirmationValidator implements Validator {
	
	// Constants :
	
	private static final String MESSAGE = "Password confimation is different from password";
	
	// Fields :
	
	@EJB
	private AppLogger logger;
	
	// Methods :
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		UIInput passwordField = (UIInput) component.getAttributes().get("passwordField");
		String password = (String) passwordField.getValue();
		String confirmPassword = (String) value;
		
		if(confirmPassword != null && !confirmPassword.equals(password)) {
			this.logger.logWarning("Validator : %s.", MESSAGE);
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE, null));
		}
	}
	
}
