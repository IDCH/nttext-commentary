/**
 * 
 */
package openscriptures.text.impl.jpa;

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
import openscriptures.text.TokenModule;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class JPATokenRepository extends JPARepository<Token> implements TokenModule {
//    private static final Logger LOGGER = Logger.getLogger(JPATokenRepository.class);
    
    //=======================================================================================
    // CONSTRUCTOR
    //=======================================================================================
    
    public JPATokenRepository(EntityManagerFactory emf) {
        super(emf);
    }
    

    private Token prep(Token t, Work w) {
//        t.setWork(w);
        return t;
    }
    
    private List<Token> prep(List<Token> tokens, Work w) {
//        for (Token t : tokens) {
//            t.setWork(w);
//        }
        
        return tokens;
    }
    
    //=======================================================================================
    // TOKEN RETRIEVAL METHODS
    //=======================================================================================

    public int getNumberOfTokens(Work work) {
        List<Integer> result = null;  
        EntityManager em = getEM();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT t.position FROM Token t WHERE t.work = :work ORDER BY t.position DESC", Integer.class);
            query.setMaxResults(1);
            query.setParameter("work", work);

            result = query.getResultList();
        } finally {
            close(em);
        }
        
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
        Token token = null;  
        EntityManager em = getEM();
        try {
            TypedQuery<Token> query = em.createQuery(
                    "SELECT t FROM Token t WHERE t.work = :work AND t.position = :pos", Token.class);
            query.setMaxResults(1);
            query.setParameter("work", w);
            query.setParameter("pos", pos);

            token = query.getSingleResult();
        } finally {
            close(em);
        }
        
        return (token != null) ? prep(token, w) : null;
//        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
//        CriteriaQuery<Token> criteria = builder.createQuery(Token.class);
//        
//        Root<Token> tokenRoot = criteria.from(Token.class);
//        criteria.where(builder.and(
//                builder.equal(tokenRoot.get("work"), w),
//                builder.equal(tokenRoot.get("position"), pos)
//            ) );
//        
//        return prep(this.queryOne(criteria), w);
    }
    
    public List<Token> find(Work w, int start, int end) {
        // TODO IMPLEMENT
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        CriteriaQuery<Token> criteria = builder.createQuery(Token.class);
        
//        Root<Token> tokenRoot = criteria.from(Token.class);
//        criteria.where(builder.and(
//                builder.equal(tokenRoot.get("work"), w),
//                builder.gt(tokenRoot.get("token_pos"), start), 
//                builder.lt(tokenRoot.get("token_pos"), end)
//            ) );
        
        return prep(this.query(criteria), w);
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public List<Token> find(Structure s) {
        // TODO implement
        return null;
//        UUID wId = s.getWorkUUID();
//        return this.find(s.getWorkUUID(), s.getStart(), s.getEnd());
    }
    
    //=======================================================================================
    // TOKEN UPDATE METHODS
    //=======================================================================================

   
}
