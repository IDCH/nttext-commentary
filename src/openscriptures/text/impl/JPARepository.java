/**
 * 
 */
package openscriptures.text.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.log4j.Logger;

/**
 * @author Neal Audenaert
 */
public class JPARepository<T> {
    private final static Logger LOGGER = Logger.getLogger(JPARepository.class);
    
    // TODO figure out how to implement canonical metamodel
    //  SEE http://docs.jboss.org/hibernate/entitymanager/3.6/reference/en/html/metamodel.html#metamodel-static
    
    
    protected EntityManagerFactory m_emf = null;
    
    protected boolean keepOpen = false;
    protected EntityManager em = null;
    
    
    //=======================================================================================
    // CONSTRUCTOR
    //=======================================================================================
    
    public JPARepository(EntityManagerFactory emf) {
        m_emf = emf;
    }
    
    
    protected EntityManager getEM() {
        EntityManager em = m_emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
        return em;
    }
    
    protected void close(EntityManager em) {
        em.close();
    }
    
    /**
     * 
     * @param object
     * @return
     */
    public T create(T object) {
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        
        T response = null;
        tx.begin();
        try {
            em.persist(object);
            tx.commit();
            response = object;     // set the response object if we get to this point
        } finally {
            if (tx.isActive())
                tx.rollback();
            close(em);
        }
        
        return response;    
    }
    
    /**
     * 
     * @param objects
     * @return
     */
    public List<T> create(List<T> objects) {
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        
        List<T> response = null;
        tx.begin();
        try {
            for (T t : objects) {
                em.persist(t);
            }
            tx.commit();
            response = objects;     // set the response object if we get to this point
        } finally {
            if (tx.isActive())
                tx.rollback();
            close(em);
        }
        
        return response;    
    }

    public <Q> List<Q> query(CriteriaQuery<Q> criteria) {
        List<Q> results = null;
        EntityManager em = getEM();
        try {
             results = em.createQuery( criteria ).getResultList();
        } finally {
            close(em);
        }
        
        return results;
    }
    
    /**
     * 
     * @param <Q>
     * @param criteria
     * @return
     */
    public <Q> Q queryOne(CriteriaQuery<Q> criteria) {
        EntityManager em = getEM();
        
        Q q = null;
        try {
             List<Q> results = em.createQuery( criteria ).getResultList();
             if ((results != null) && (results.size() > 0)) {
                 String errmsg = 
                     "Expected to find at most one object. Found " + results.size();
                 assert results.size() == 1 : errmsg;
                 if (results.size() != 1)
                     LOGGER.warn(errmsg);
                 
                 q = results.get(0);
             }
        } finally {
            close(em);
        }
        
        return q;
    }
    
    /**
     * 
     * @param <Q>
     * @param clazz
     * @return
     */
    public <Q> CriteriaQuery<Q> getCriteriaQuery(Class<Q> clazz) {
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        return builder.createQuery( clazz );
    }
    
    //===================================================================================
    // UPDATES
    //===================================================================================
    
    /**
     * 
     * @param t
     */
    public boolean save(T t) {
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        try {
            em.merge(t);
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            close(em);
        }
        
        return true;
    }
}
