/**
 * 
 */
package org.nttext.commentary.persist;

import org.nttext.commentary.Entry;

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
}
