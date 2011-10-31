/**
 * 
 */
package org.nttext.commentary;


import java.util.Set;

import org.idch.bible.ref.Passage;


/**
 * Defines the methods for creating, retrieving and deleting entries from the commentary.
 * 
 * @author Neal Audenaert
 */
public interface InstanceRepository {

    public EntryInstance create(Passage passage);
    
    public EntryInstance find(long id);
    
    public EntryInstance find(Passage passage);
    
    public boolean save(EntryInstance instance);
    
    public boolean remove(EntryInstance instance);
    
    public boolean associate(EntryInstance instance, VariationUnit vu);
    
    public boolean disassociate(EntryInstance instance, VariationUnit vu);
    
    public Set<VariationUnit> getVU(EntryInstance instance);
}
