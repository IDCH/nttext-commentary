/**
 * 
 */
package openscriptures.text;

import java.util.SortedSet;
import java.util.UUID;


/**
 * @author Neal Audenaert
 */
public interface StructureRepository {

    public Structure create(Work work, String name);
    
    public Structure create(Work work, String name, Token start, Token end);
    
    public Structure create(Structure s);
    
    public boolean hasStructuresFor(UUID workId);
    
    public Structure find(UUID id);
    
    public Structure find(long id);
    
    public Structure synchronize(Structure s);
    
//    public SortedSet<Structure> find(Work w, String name);
//    
//    public SortedSet<Structure> find(Work w, int position);
//    
//    public SortedSet<Structure> find(Work w, String name, TokenSequence seq);
//    
//    public SortedSet<Structure> find(Work w, String name, int start, int end);
//    
//    public SortedSet<Structure> find(Work w, String name, int start, int end, boolean strict);
    
    public boolean save(Structure s);
}
