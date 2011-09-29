/**
 * 
 */
package openscriptures.text;

import java.util.List;
import java.util.SortedSet;
import java.util.UUID;

/**
 * Provides a facade to a repository that is capable of retrieving and (optionally) creating
 * and updating <tt>{@link Structure}</tt>s for a given <tt>{@link Work}</tt>.   
 * 
 * TODO Document me
 * TODO Implement composite version that will search multiple StructureProvider instances
 * 
 *  efines a controller interface that allows for the retrieval and (optionally) creation
 * of <tt>Structure</tt>s for a given work. Since a major goal of <tt>openscriptures.text</tt> 
 * is to enable multiple independent content providers to create annotations on a single base
 * text, often available over the Web via a RESTful API, it is necessary to provide a 
 * mechanism to persist and retrieve those structures that is independent of the work 
 * itself.
 *  
 * @author Neal Audenaert
 */
public interface StructureFacade {
    
    public UUID getWorkId();
    
    public SortedSet<Structure> find(String name);
    
    /**
     * Returns the set of all structures with the specified name that are partially 
     * within the specfied range. That is, this will return all structures such that
     * <tt>{@link Structure#getEnd()} > start && {@link Structure#getStart()} < end</tt>
     * 
     * 
     * 
     * @param name
     * @param start
     * @param end The end range (exclusive) to be returned. If this is -1, this will return 
     *      all structures following the start makrer. 
     * @return
     */
    public SortedSet<Structure> find(String name, int start, int end);
    
    /**
     * 
     * @param name
     * @param start
     * @param end
     * @param strict
     * @return
     */
    public SortedSet<Structure> find(String name, int start, int end, boolean strict);
    
    /**
     * Returns all structures that overlap with the provided <tt>TokenSequence</tt>. 
     * 
     * @param name
     * @param structure
     * @return
     */
    public SortedSet<Structure> find(String name, TokenSequence structure);
    
    public Structure create(String name);
    
    public boolean save(Structure s);
    
    /**
     * 
     * @param position
     * @return
     */
    public SortedSet<Structure> find(int position);

}
