/**
 * 
 */
package openscriptures.text;


/**
 * @author Neal Audenaert
 */
public interface StructureFactory {
    
    /**
     * Creates a new named <tt>Structure</tt> for the specified <tt>Work</tt>.
     *  
     * @param work The work that the structure annotates
     * @param name The name of the structure to create
     * @param start The starting token for the structure (or null if the starting token
     *      will be assigned later)
     * @param end The ending token (exclusive) for the structure (or null if the ending 
     *      token will be assigned later)
     * @return The newly created structure.
     * @throws TODO this should throw something if the structure cannot be created 
     *      for whatever reason.
     */
    public Structure createStructure(Work work, String name, Token start, Token end);
    
    /**
     * 
     * @param s
     * @return
     */
    public boolean save(Structure s);
    
    public StructureFacade getStructureFacade(Work w);
}
