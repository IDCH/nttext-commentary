/**
 * 
 */
package org.idch.texts.util.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.idch.texts.util.Language;
import org.idch.texts.util.LanguageRepository;


/**
 * @author Neal Audenaert
 */
public class JPALanguageRepository implements LanguageRepository {

    EntityManagerFactory m_emf = null;
    
    /**
     * 
     * @param emf
     */
    JPALanguageRepository(EntityManagerFactory emf) {
        m_emf = emf;
    }
    
    /**
     * 
     * @param lgCode
     * @param lg
     * @return
     */
    public Language create(String lgCode, String language) {
        Language lg = null;
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        try {
            lg = new Language(language, lgCode);
            em.persist(lg);
            
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            
            em.close();
        }
        
        return lg;
    }
    
    /**
     * 
     * @param lgCode
     * @param language
     * @return
     */
    @SuppressWarnings("unchecked")
    public Language findOrCreate(String lgCode, String language) {
        Language lg = null;
        List<Language> languages = null;
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        Session session = (Session) em.getDelegate();
        try {
            languages = (List<Language>)session.createCriteria(Language.class)
                                .add(Restrictions.eq("code", lgCode)).list();
            
            if ((languages != null) && languages.size() > 0)
                lg = languages.get(0);
            
            if (lg == null) {
                lg = new Language(language, lgCode);
                em.persist(lg);
            }
            
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            em.close();
        }
        
        return lg;
    }
    
    /**
     * 
     * @param lgCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public Language find(String lgCode) {
        Language lg = null;
        EntityManager em = m_emf.createEntityManager();
        
        Session session = (Session) em.getDelegate();
        try {
            List<Language> languages = 
                (List<Language>)session.createCriteria(Language.class)
                                .add(Restrictions.eq("code", lgCode)).list();
            
            if ((languages != null) && languages.size() > 0)
                lg = languages.get(0);
            
        } finally {
            em.close();
        }
        
        return lg;
    }
    
    /**
     * 
     * @param lg
     */
    public void save(Language lg) {
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        try {
            em.merge(lg);
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            
            em.close();
        }
    }

}
