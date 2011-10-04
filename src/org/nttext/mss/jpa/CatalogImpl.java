/**
 * 
 */
package org.nttext.mss.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.nttext.mss.Catalog;
import org.nttext.mss.Designation;
import org.nttext.mss.Manuscript;

/**
 * @author Neal Audenaert
 */
public class CatalogImpl implements Catalog {

    EntityManagerFactory m_emf = null;
    
    public CatalogImpl(EntityManagerFactory emf) {
        this.m_emf = emf;
    }
    
    public Manuscript createManuscript(String scheme, String identifier) {
        Manuscript m = null;
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            Designation d = new Designation(scheme, identifier);    // VERIFY THAT THIS DOESN'T EXIST
            em.persist(d);
            
            m = new Manuscript(d);
            em.persist(m);
            
            tx.commit();
        } catch (Throwable t) {
            if (tx.isActive())
                tx.rollback();
        } finally { 
            em.close();
        }
        return m;
    }
    
    /**
     * Finds the manuscript corresponding to a specific designation.
     * 
     * @param designation The designation of the manuscript to find.
     * @return The designated manuscript or null if no such manuscript exists.
     */
	public Manuscript lookup(Designation designation) {
	    return null;
	}
	
	/**
	 * 
	 * @param scheme
	 * @param id
	 * @return
	 */
	public Designation createMSDesignation(String scheme, String id) {
	    EntityManager em = m_emf.createEntityManager();
        
	    Designation d = new Designation(scheme, id);
        em.persist(d);
        em.flush();
        
        em.close();
	    return d;
	}
}
