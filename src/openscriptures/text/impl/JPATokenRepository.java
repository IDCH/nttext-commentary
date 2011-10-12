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
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import openscriptures.text.Structure;
import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class JPATokenRepository implements TokenRepository {
    private static final Logger LOGGER = Logger.getLogger(JPATokenRepository.class);
    
    private EntityManagerFactory m_emf = null;
    
    //=======================================================================================
    // CONSTRUCTOR
    //=======================================================================================
    
    public JPATokenRepository(EntityManagerFactory emf) {
        m_emf = emf;
    }
    
    //=======================================================================================
    // TOKEN CREATION METHODS
    //=======================================================================================
    
    /**
     * 
     * @param w
     * @param value
     * @param pos
     * @return
     */
    public Token createToken(Work w, String value) {
        return null;
    }
    
    /**
     * 
     * @param w
     * @param value
     * @param pos
     * @return
     */
    public Token createToken(Work w, String value, int pos) {
        Token t = null;
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        try {
            
            t = new BasicToken(w, pos, value);
            em.persist(t);
            
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            em.close();
        }
        
        return t;
    }
    
    /**
     * 
     * @param em
     * @param w
     * @param text
     * @param pos
     * @return
     */
    private List<Token> doTokenization(EntityManager em, Work w, String text, int pos) {
        List<Token> tokens = new ArrayList<Token>();
        List<String> badTokens = new ArrayList<String>();
        boolean lastTokenWasWhitespace = false;
        
        Token t = null;
        String token = null;
        Matcher mat = Pattern.compile(BasicToken.TOKENIZATION_PATTERN).matcher(text);
        
        while (mat.find()) {
            t = null;
            token = mat.group();

            Token.Type type = BasicToken.classify(token);
            if (type == null) {
                badTokens.add(token);
                continue;

            } else if (type == Token.Type.WHITESPACE) {
                if (!lastTokenWasWhitespace && (w.size() > 0)) { // normalize whitespace.
                    t = new BasicToken(w, pos++, " ");
                }

                lastTokenWasWhitespace = true;
            } else {
                lastTokenWasWhitespace = false;
                t = new BasicToken(w, pos++, token);
            }

            if (t != null) {
                em.persist(t);
                tokens.add(t);
            }
        }
        
        return tokens;
    }
    
    public List<Token> tokenize(Work w, String text) {
        return null;
    }
    
    /**
     * 
     * @param w
     * @param text
     * @param pos
     * @return
     */
    public List<Token> tokenize(Work w, String text, int pos) {
        List<Token> tokens = null;

        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            tokens = doTokenization(em, w, text, pos);

        } finally {
            if (tx.isActive())
                tx.rollback();
            em.close();
        }
        return tokens;
    }

    //=======================================================================================
    // TOKEN RETRIEVAL METHODS
    //=======================================================================================

    public int getMaxPosition(Work work) {
        // TODO Implement
        return 0;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public Token find(UUID id) {
        List<Token> tokens = null;
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        Session session = (Session) em.getDelegate();
        try {
            tokens = (List<Token>)session.createCriteria(BasicToken.class)
                                .add(Restrictions.eq("id", id.toString())).list();
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            em.close();
        }
        
        Token t = null;
        if ((tokens != null) && (tokens.size() > 0)) {
            String errmsg = "Expected to find at most one token. " +
                "Found " + tokens.size();
            assert tokens.size() == 1 : errmsg;
            if (tokens.size() != 1)
                LOGGER.warn(errmsg);
            
            t = tokens.get(0);
        }
        
        return t;
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

    /**
     * 
     * @param s
     */
    public void save(Token t) {
        EntityManager em = m_emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        tx.begin();
        try {
            em.merge(t);
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
            em.close();
        }
    }
   
}
