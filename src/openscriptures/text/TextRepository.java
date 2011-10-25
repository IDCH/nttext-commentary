/**
 * 
 */
package openscriptures.text;

/**
 * @author Neal Audenaert
 */
public interface TextRepository {

    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository();
    
    public StructureRepository getStructureRepository();
}
