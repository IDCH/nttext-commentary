/**
 * 
 */
package openscriptures.text.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    // TOKEN CREATION METHODS
    //=======================================================================================
    
    private int getAppendIndex(Work w) {
        // TODO should use CriteriaQuery
        
        EntityManager em = m_emf.createEntityManager();
        TypedQuery<Integer> query = em.createQuery(
                "SELECT MAX(t.position) FROM Token t WHERE t.work = :work", Integer.class);
        query.setParameter("work", w);
        
        Integer result = query.getSingleResult();
        return (result != null) ? result + 1 : 0;
    }
    
    /**
     * 
     * @param w
     * @param value
     * @param pos
     * @return
     */
    public Token append(Work w, String value) {
        // XXX  this may have synchronization issues.
        return create(new Token(w, getAppendIndex(w), value));
    }
   
    /**
     * 
     * @param w
     * @param text
     * @return
     */
    public List<Token> appendAll(Work w, String text) {
        int pos = this.getAppendIndex(w);
        List<Token> tokens = new ArrayList<Token>();
        boolean lastTokenWasWhitespace = false;
        
        Matcher mat = Pattern.compile(Token.TOKENIZATION_PATTERN).matcher(text);
        while (mat.find()) {
            String token = mat.group();

            Token.Type type = Token.classify(token);
            if (type == null) {
                continue;       // TODO do something about this 

            } else if (type == Token.Type.WHITESPACE) {
                if (!lastTokenWasWhitespace && (pos > 0)) { // normalize whitespace.
                    tokens.add(new Token(w, pos++, " "));
                }

                lastTokenWasWhitespace = true;
            } else {
                lastTokenWasWhitespace = false;
                tokens.add(new Token(w, pos++, token));
            }
        }
        
        return this.create(tokens);
    }

    //=======================================================================================
    // TOKEN RETRIEVAL METHODS
    //=======================================================================================

    public int getMaxPosition(Work work) {
        EntityManager em = m_emf.createEntityManager();
        TypedQuery<Integer> query = em.createQuery(
                "SELECT MAX(t.position) FROM Token t WHERE t.work = :work", Integer.class);
        query.setParameter("work", work);
          
        return query.getSingleResult();
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
        return null;
        
    }
    
    public List<Token> find(Work w, int start, int end) {
        
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
