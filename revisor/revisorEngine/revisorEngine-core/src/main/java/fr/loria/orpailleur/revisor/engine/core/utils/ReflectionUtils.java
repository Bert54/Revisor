package fr.loria.orpailleur.revisor.engine.core.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author William Philbert
 */
public class ReflectionUtils {
	
	// Methods :
	
	public static List<Field> getAllFields(Class<?> type) {
		List<Field> fields = new LinkedList<>();
		
		for(Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		
		return fields;
	}
	
}
