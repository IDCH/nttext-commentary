/**
 * 
 */
package org.nttext.commentary.persist;

import openscriptures.ref.Passage;

import org.nttext.commentary.VariationUnit;

/**
 * @author Neal_2
 */
public interface VURepository {

    public VariationUnit create(Passage passage);
    
    public VariationUnit find(long id);
    
    public VariationUnit find(Passage passage);
    
    public boolean save(VariationUnit entry);
    
    public boolean remove(VariationUnit entry);
}
