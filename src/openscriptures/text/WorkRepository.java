/**
 * 
 */
package openscriptures.text;

import java.util.List;
import java.util.UUID;

/**
 * @author Neal Audenaert
 */
public interface WorkRepository {
    
    public TokenRepository getTokenRepository();
    
    public Work create(String workId);
    
//    public Work create(WorkId id, String title, String abbr, String desc);
    
    public Work find(long id);
    
    public Work find(UUID id);
    
//    public List<Work> getByWorkId(WorkId.Type type);
    
//    public List<Work> getByWorkId(String abbreviation);
    
//    public List<Work> getByWorkId(WorkId.Type type, String lgCode);

}
