/**
 * 
 */
package openscriptures.text.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import openscriptures.text.Work;
import openscriptures.text.WorkId;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public class JPAWorkRepository extends JPARepository<Work> implements WorkRepository {
    
    // TODO Need to implement lookup services that that will search for a work ID based on
    //      its title, language or type. For example, return all works of type 'Bible'
    // TODO Need to implement more full-featured descriptions. A work should have 
    //      the title, copyright information, description, versification strategy, and 
    //      perhaps other information about how to resolve references to it.
    // TODO cross walk with FRBR.
    //      I think we have work = type, expression = <lg, slug>, manifestation = <publisher, edition> 
    
    public JPAWorkRepository(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * 
     * @param workId
     * @return
     */
    public Work create(String workId) {
        return create(new Work(new WorkId(workId)));
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
        return create(new Work(id, title, abbr, desc));
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
