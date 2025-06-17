package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.entity.DbEntity;
import jakarta.persistence.*;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractRepository<T extends DbEntity> {
    Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public T findById(T item,Class<T> type) {
        return entityManager.find(type,item);
    }

    public T add(T item) {
        if (item != null) {
            entityManager.persist(item);
            entityManager.flush();
            logger.info("Item stored successfully");
            return item;
        } else {
            logger.info("Enable to save an object that is null");
            return null;
        }
    }

    public T update(T item) {
        if (item != null) {
            item = entityManager.merge(item);
            entityManager.flush();
            logger.info("Item updated successfully");
            return item;
        } else {
            logger.info("Enable to update an object that is null");
            return null;
        }
    }

    public void delete(T item) {
        if (item != null) {
            entityManager.remove(item);
            entityManager.flush();
            logger.info("Item deleted successfully");
        } else {
            logger.info("Enable to delete an object that is null");
        }
    }

    public Object jpqlQuerySingleResult(String query) {
        Query jpqlQuery = entityManager.createQuery(query);
        return jpqlQuery.getSingleResult();
    }

    public Object jpqlQueryWithParamsSingleResult(String query, Map<String, Object> parameters) {
        Query jpqlQuery;
        jpqlQuery = entityManager.createQuery(query);
        for(Map.Entry<String, Object> parameter : parameters.entrySet()) {
            jpqlQuery.setParameter(parameter.getKey(), parameter.getValue());
        }

        try {
            return jpqlQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public <X extends DbEntity> List<X> jpqlQuery(String query,Class<X> type) {
        TypedQuery<X> jpqlQuery = entityManager.createQuery(query,type);
        return jpqlQuery.getResultList();
    }
    @SuppressWarnings("unchecked")
    public <X extends DbEntity> List<X> jpqlQueryWithParams(String query, Map<String, Object> parameters, Class<X> classType) {
        Query jpqlQuery;
        jpqlQuery = entityManager.createQuery(query);
        for(Map.Entry<String, Object> parameter : parameters.entrySet()) {
            jpqlQuery.setParameter(parameter.getKey(), parameter.getValue());
        }

        return jpqlQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <X extends DbEntity> List<X> nativeQuery(String query,Class<X> type) {
        Query nativeQuery = entityManager.createNativeQuery(query,type);
        return nativeQuery.getResultList();
    }
    /**
     * Execute a native query.
     * @param query to be executed
     * @param resultSetMappingName of the mapped object that the returning list contains
     * @param parameters of query
     * @return list of objects defined by resultSetMappingName
     */
    @SuppressWarnings("unchecked")
    public <X> List<X> executeNativeQuery(String query, String resultSetMappingName, Map<String, Object> parameters) {
        TypedQuery<X> nativeQuery;

        List<Object> paramsList = new ArrayList<>(parameters.entrySet().size());
        query = replaceQueryParamsAndReturnNewQuery(query, parameters, paramsList);
        nativeQuery =  (TypedQuery<X>) entityManager.createNativeQuery(query, resultSetMappingName);

        fillQueryWithParams(nativeQuery, paramsList);

        return nativeQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Object> executeNativeQuery(String query, Map<String, Object> parameters) {
        TypedQuery<Object> nativeQuery;

        List<Object> paramsList = new ArrayList<>(parameters.entrySet().size());
        query = replaceQueryParamsAndReturnNewQuery(query, parameters, paramsList);
        nativeQuery =  (TypedQuery<Object>) entityManager.createNativeQuery(query);

        fillQueryWithParams(nativeQuery, paramsList);

        return nativeQuery.getResultList();
    }


    private String replaceQueryParamsAndReturnNewQuery(String query, Map<String, Object> namedOrNumberedParameters, List<Object> paramsList) {
        Map <String, Object> parameters;

        if (query.contains("?1")) {

            // In case of numbered parameters, i.e. ?1, ?2, etc. do not touch the query, just update the parameters list in an ordered way
            parameters = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
            parameters.putAll(namedOrNumberedParameters);
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                paramsList.add(parameter.getValue());
            }
        } else {

            // In case of named parameters, replace the :key with ?1, ?2, etc. and update the parameters list
            parameters = namedOrNumberedParameters;
            int parameterCounter = 1;
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query = query.replaceAll(StringUtils.join(":", parameter.getKey()), StringUtils.join("?", String.valueOf(parameterCounter++)));
                paramsList.add(parameter.getValue());
            }
        }

        return query;
    }
    private void fillQueryWithParams(Query q, List<Object> parameters) {
        int i = 1;
        for (Object obj : parameters) {
            q.setParameter(i++, obj);
        }
    }

    public <X extends DbEntity> X getReferenceById(Class<X> type,Long id) {
        return entityManager.getReference(type,id);
    }



}
