/**
 * 
 */
package openscriptures.text.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * @author Neal Audenaert
 */
public class JPARepository<T> {
    
    protected EntityManagerFactory m_emf = null;
    
    //=======================================================================================
    // CONSTRUCTOR
    //=======================================================================================
    
    public JPARepository(EntityManagerFactory emf) {
        m_emf = emf;
    }
    
    /**
     * 
     * @param object
     * @return
     */
    protected T create(T object) {
        EntityManager em = m_emf.createEntityManager();
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
            em.close();
        }
        
        return response;    
    }

}
