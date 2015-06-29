package com.flaviolisboa.sistema.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.beanutils.BeanUtils;

public class JpaUtils {

	public static String getEntityName(Class<?> classeEntidade) {
		String nome = null;
		if (classeEntidade.isAnnotationPresent(Entity.class)) {
			nome = classeEntidade.getAnnotation(Entity.class).name();
			if (nome == null || nome.isEmpty()) {
				nome = classeEntidade.getSimpleName();
			}
		}
		return nome;
	}
    
    public static <T> T getSingleResult(TypedQuery<T> query) {
        List<T> listaResultados = query.getResultList();
        T resultado = null;
        if (listaResultados.size() > 0) {
            resultado = listaResultados.get(0);
        }
        return resultado;
    }
    
    public static <T> T getSingleResult(Query query, Class<T> classeReultado) {
        @SuppressWarnings("unchecked")
		List<T> listaResultados = (List<T>) query.getResultList();
        T resultado = null;
        if (listaResultados.size() > 0) {
            resultado = listaResultados.get(0);
        }
        return resultado;
    }
    
    public static <T> TypedQuery<T> createStandardizedNamedQuery(EntityManager entityManager, String partialName, T entity, Class<T> entityClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String queryName = getEntityName(entityClass) + "." + partialName;
        TypedQuery<T> query = entityManager.createNamedQuery(queryName, entityClass);
        
        if (query != null) {
            Set<Parameter<?>> parameters = query.getParameters();
            
            for (Parameter<?> parameter : parameters) {
                query.setParameter(parameter.getName(), BeanUtils.getProperty(entity, parameter.getName()));
            }
        }
        
        return query;
    }
    
    public static <T> Query createStandardizedNamedQuery(EntityManager entityManager, String partialName, T entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<T> entityClass = (Class<T>) entity.getClass();
        String queryName = getEntityName(entityClass) + "." + partialName;
        Query query = entityManager.createNamedQuery(queryName);
        
        if (query != null) {
            Set<Parameter<?>> parameters = query.getParameters();
            
            for (Parameter<?> parameter : parameters) {
                query.setParameter(parameter.getName(), BeanUtils.getProperty(entity, parameter.getName()));
            }
        }
        
        return query;
    }
}
