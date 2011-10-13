/**
 * 
 */
package openscriptures.text;

import java.util.List;

/**
 * @author Neal Audenaert
 */
public interface WorkRepository {
    
    public Work create(String workId);
    
    public Work create(WorkId id, String title, String abbr, String desc);
    
    public List<Work> getByWorkId(WorkId.Type type);
    
    public List<Work> getByWorkId(String abbreviation);
    
    public List<Work> getByWorkId(WorkId.Type type, String lgCode);

}
