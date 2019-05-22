package fr.loria.orpailleur.revisor.webplatform.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author William Philbert
 */
public class PersistenceUtils {
	
	// Constructors :
	
	private PersistenceUtils() {
		// This class don't have to be instanciated.
	}
	
	// Methods :
	
	public static <T> T getQuerySingleResult(EntityManager em, String queryName, Class<T> targetClass, String parameterName, Object parameterValue) {
		TypedQuery<T> query = createSingleParameterQuery(em, queryName, targetClass, parameterName, parameterValue);
		return getResultOrNull(query);
	}
	
	public static <T> List<T> getQueryResults(EntityManager em, String queryName, Class<T> targetClass, String parameterName, Object parameterValue) {
		TypedQuery<T> query = createSingleParameterQuery(em, queryName, targetClass, parameterName, parameterValue);
		return query.getResultList();
	}
	
	public static <T> List<T> getQueryResults(EntityManager em, String queryName, Class<T> targetClass, Map<String, Object> queryParameters) {
		TypedQuery<T> query = createSingleParameterQuery(em, queryName, targetClass);
		for(Entry<String, Object> parameter : queryParameters.entrySet()) {
			query.setParameter(parameter.getKey(), parameter.getValue());
		}
		return query.getResultList();
	}
	
	public static <T> TypedQuery<T> createSingleParameterQuery(EntityManager em, String queryName, Class<T> targetClass) {
		return em.createNamedQuery(queryName, targetClass);
	}
	
	public static <T> TypedQuery<T> createSingleParameterQuery(EntityManager em, String queryName, Class<T> targetClass, String parameterName, Object parameterValue) {
		return createSingleParameterQuery(em, queryName, targetClass).setParameter(parameterName, parameterValue);
	}
	
	public static <T> T getResultOrNull(TypedQuery<T> query) {
		List<T> results = query.getResultList();
		if(results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}
	
}
