/**
 * 
 */
package openscriptures.text.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import openscriptures.text.TokenRepository;
import openscriptures.text.Work;
import openscriptures.text.WorkId;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public class JPAWorkRepository extends JPARepository<Work> implements WorkRepository {
    
    // TODO Need to implement more full-featured descriptions. A work should have 
    //      the title, copyright information, description, versification strategy, and 
    //      perhaps other information about how to resolve references to it.
    // TODO cross walk with FRBR.
    //      I think we have work = type, expression = <lg, slug>, manifestation = <publisher, edition> 
    
    private TokenRepository tokens;
    
    public JPAWorkRepository(EntityManagerFactory emf) {
        super(emf);
        
        // TODO may need to make this configurable.
        this.tokens = new JPATokenRepository(emf);
    }
    
    public TokenRepository getTokenRepository() {
        return this.tokens;
    }
    
    /**
     * 
     * @param workId
     * @return
     */
    public Work create(String workId) {
        Work w = new Work(new WorkId(workId));
        return create(w);
    }
    
    /**
     * 
     * @param id
     * @param title
     * @param abbr
     * @param desc
     * @return
     */
    public Work create(WorkId id, String title, String abbr, String desc) {
        Work w = new Work(id, title, abbr, desc);
        return create(w);
    }
    
    //===================================================================================
    // RETRIEVAL METHOS
    //===================================================================================
    
    /**
     * 
     * @param id
     * @return 
     */
    public List<Work> getByWorkId(WorkId id) {
        return null;
    }
    
    /**
     * 
     * @param type
     * @return
     */
    public List<Work> getByWorkId(WorkId.Type type) {
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        CriteriaQuery<Work> criteria = builder.createQuery(Work.class);
        Root<Work> workRoot = criteria.from(Work.class);
        
        criteria.where(builder.equal(workRoot.get("workId").get("type"), type));
        return this.query(criteria);
    }
    
    /**
     * 
     * @param abbreviation
     * @return
     */
    public List<Work> getByWorkId(String abbreviation) {
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        CriteriaQuery<Work> criteria = builder.createQuery(Work.class);
        Root<Work> workRoot = criteria.from(Work.class);
        
        criteria.where(builder.equal(workRoot.get("workId").get("name"), abbreviation));
        return this.query(criteria);
    }
    
    /**
     * 
     * @param type
     * @param lgCode
     * @return
     */
    public List<Work> getByWorkId(WorkId.Type type, String lgCode) {
        CriteriaBuilder builder = m_emf.getCriteriaBuilder();
        CriteriaQuery<Work> criteria = builder.createQuery(Work.class);
        Root<Work> workRoot = criteria.from(Work.class);
        
        criteria.where(
                builder.and(
                        builder.equal(workRoot.get("workId").get("type"), type),
                        builder.equal(workRoot.get("workId").get("lgCode"), lgCode)
                )
            );
        
        return this.query(criteria);
    }
    
    //===================================================================================
    // UPDATE METHOS
    //===================================================================================
    
}
