/**
 * 
 */
package org.nttext.commentary;


import java.util.Set;

import openscriptures.ref.Passage;

/**
 * Defines the methods for creating, retrieving and deleting entries from the commentary.
 * 
 * @author Neal Audenaert
 */
public interface EntryRepository {

    public Entry create(Passage passage);
    
    public Entry find(long id);
    
    public Entry find(Passage passage);
    
    public boolean save(Entry entry);
    
    public boolean remove(Entry entry);
    
    public boolean associate(Entry entry, VariationUnit vu);
    
    public boolean disassociate(Entry entry, VariationUnit vu);
    
    public Set<VariationUnit> getVU(Entry entry);
}
