package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.entity.DbEntity;
import com.uniwa.course_recommendation.entity.Question;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public <X extends DbEntity> List<X> jpqlQuery(String query,Class<X> type) {
        TypedQuery<X> jpqlQuery = entityManager.createQuery(query,type);
        return jpqlQuery.getResultList();
    }
    @SuppressWarnings("unchecked")
    public <X extends DbEntity> List<X> nativeQuery(String query,Class<X> type) {
        Query jpqlQuery = entityManager.createNativeQuery(query,type);
        return jpqlQuery.getResultList();
    }


}
