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
    
    public Entry lookup(Passage passage);
    
    public Entry remove(Entry entry);
}
