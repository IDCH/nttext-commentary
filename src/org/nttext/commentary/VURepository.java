/**
 * 
 */
package org.nttext.commentary;


import openscriptures.ref.Passage;


/**
 * @author Neal Audenaert
 */
public interface VURepository {

    public VariationUnit create(Passage passage);
    
    public VariationUnit find(long id);
    
    public VariationUnit find(Passage passage);
    
    public boolean save(VariationUnit entry);
    
    public boolean remove(VariationUnit entry);
    
}