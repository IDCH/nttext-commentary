/**
 * 
 */
package openscriptures.text.impl;

import javax.persistence.EntityManagerFactory;

import openscriptures.text.Work;
import openscriptures.text.WorkId;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public class JPAWorkRepository extends JPARepository<Work> implements WorkRepository {
    
    public JPAWorkRepository(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Work create(String workId) {
        return create(new Work(new WorkId(workId)));
    }
    
    public Work create(WorkId id, String title, String abbr, String desc) {
        return create(new Work(id, title, abbr, desc));
    }
}
