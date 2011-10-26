/**
 * 
 */
package org.nttext.commentary;

import openscriptures.text.StructureRepository;
import openscriptures.text.TextRepository;
import openscriptures.text.TokenRepository;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public interface CommentaryRepo {

    public EntryRepository getEntryRepository();
    
    public VURepository getVURepository();
    
    public VariantReadingRepository getRdgRepository();
    
    public TextRepository getTextRepository();
    
    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository(); 
    
    public StructureRepository getStructureRepository(); 
    
}
