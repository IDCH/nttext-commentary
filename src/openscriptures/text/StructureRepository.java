/**
 * 
 */
package openscriptures.text;

import java.util.SortedSet;


/**
 * @author Neal Audenaert
 */
public interface StructureRepository {

    public Structure create(Work work, String name);
    
    public Structure create(Work work, String name, Token start, Token end);
    
    public boolean hasStructuresFor(Work work);
    
    public StructureFacade getStructureFacade(Work w);
    
    public SortedSet<Structure> find(Work w, String name);
    
    public SortedSet<Structure> find(Work w, int position);
    
    public SortedSet<Structure> find(Work w, String name, TokenSequence seq);
    
    public SortedSet<Structure> find(Work w, String name, int start, int end);
    
    public SortedSet<Structure> find(Work w, String name, int start, int end, boolean strict);
    
    public void save(Structure s);
}
