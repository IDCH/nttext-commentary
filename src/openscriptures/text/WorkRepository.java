/**
 * 
 */
package openscriptures.text;

/**
 * @author Neal Audenaert
 */
public interface WorkRepository {
    
    public Work create(String workId);
    
    public Work create(WorkId id, String title, String abbr, String desc);

}
