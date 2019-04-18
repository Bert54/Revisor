package fr.loria.orpailleur.revisor.engine.core.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark fields that must be cleared once they aren't useful anymore.
 * Warning : the fields marked with this annotation should be accessible (public or in the class where they are cleared).
 * @author William Philbert
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TempResource {
	
	// No parameters.
	
}
