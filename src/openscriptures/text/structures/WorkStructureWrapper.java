/**
 * 
 */
package openscriptures.text.structures;

import openscriptures.text.Structure;
import openscriptures.text.StructureWrapper;
import openscriptures.text.TextRepository;
import openscriptures.text.Work;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public abstract class WorkStructureWrapper extends StructureWrapper {
    public static final String ATTR_OSIS_ID = "osisId";
    
    protected final TextRepository repo;
    
    public WorkStructureWrapper(TextRepository repo, Structure s) {
        super(s);
        
        this.repo = repo;
    }

    protected Work getWork() {
        WorkRepository worksRepo = repo.getWorkRepository();
        return worksRepo.find(this.getWorkUUID());
    }
    
    /**
     * 
     * @return
     */
    public String getOsisId() {
        return this.getAttribute(ATTR_OSIS_ID);
    }
    
    public void setOsisId(String id) {
        // TODO check pattern?
        this.setAttribute(ATTR_OSIS_ID, id);
    }
}
