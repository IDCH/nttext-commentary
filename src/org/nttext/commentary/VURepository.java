/**
 * 
 */
package org.nttext.commentary;


import org.idch.bible.ref.Passage;


/**
 * @author Neal Audenaert
 */
public interface VURepository {

    public VariationUnit create(Passage passage);
    
    public VariationUnit find(long id);
    
    public VariationUnit find(Passage passage);
    
    public boolean save(VariationUnit vu);
    
    public boolean remove(VariationUnit vu);
    
}
