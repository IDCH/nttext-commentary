/**
 * 
 */
package openscriptures.text;

/**
 * @author Neal_2
 */
public interface TextRepo {

    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository();
    
    public StructureRepository getStructureRepository();
}
