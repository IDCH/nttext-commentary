/**
 * 
 */
package org.nttext.commentary;

import openscriptures.text.StructureRepository;
import openscriptures.text.TextModule;
import openscriptures.text.TokenRepository;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public interface CommentaryModule {

    public EntryRepository getEntryRepository();
    
    public VURepository getVURepository();
    
    public VariantReadingRepository getRdgRepository();
    
    public TextModule getTextRepository();
    
    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository(); 
    
    public StructureRepository getStructureRepository(); 
    
}
