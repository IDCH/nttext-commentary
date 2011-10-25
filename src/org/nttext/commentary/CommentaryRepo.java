/**
 * 
 */
package org.nttext.commentary;

/**
 * @author Neal Audenaert
 */
public interface CommentaryRepo {

    public EntryRepository getEntryRepository();
    
    public VURepository getVURepository();
    
    public VariantReadingRepository getRdgRepository();
    
}
