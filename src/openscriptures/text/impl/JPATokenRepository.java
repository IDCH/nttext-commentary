/**
 * 
 */
package openscriptures.text.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import openscriptures.text.Structure;
import openscriptures.text.Token;
import openscriptures.text.TokenRepository;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class JPATokenRepository extends JPARepository<Token> implements TokenRepository {
//    private static final Logger LOGGER = Logger.getLogger(JPATokenRepository.class);
    
    //=======================================================================================
    // CONSTRUCTOR
    //=======================================================================================
    
    public JPATokenRepository(EntityManagerFactory emf) {
        super(emf);
    }
    

    //=======================================================================================
    // TOKEN RETRIEVAL METHODS
    //=======================================================================================

    public int getMaxPosition(Work work) {
        EntityManager em = m_emf.createEntityManager();
        TypedQuery<Integer> query = em.createQuery(
                "SELECT t.position FROM Token t WHERE t.work = :work ORDER BY t.position DESC", Integer.class);
        query.setMaxResults(1);
        query.setParameter("work", work);
          
        List<Integer> result = query.getResultList();
        return (result != null && !result.isEmpty()) ? result.get(0) : -1;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public Token find(UUID id) {
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        CriteriaQuery<Token> criteria = builder.createQuery(Token.class);
        
        Root<Token> tokenRoot = criteria.from(Token.class);
        criteria.where(builder.equal(tokenRoot.get("UUIDString"), id.toString()));
        
        return this.queryOne(criteria);
    }
    
    public Token find(Work w, int pos) {
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        CriteriaQuery<Token> criteria = builder.createQuery(Token.class);
        
        Root<Token> tokenRoot = criteria.from(Token.class);
        criteria.where(builder.and(
                builder.equal(tokenRoot.get("work"), w),
                builder.equal(tokenRoot.get("position"), pos)
            ) );
        
        return this.queryOne(criteria);
        
    }
    
    public List<Token> find(Work w, int start, int end) {
        // TODO IMPLEMENT
        
        return null;
        
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public List<Token> find(Structure s) {
        return this.find(s.getWork(), s.getStart(), s.getEnd());
    }
    
    //=======================================================================================
    // TOKEN UPDATE METHODS
    //=======================================================================================

   
}
