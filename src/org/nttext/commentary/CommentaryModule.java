/**
 * 
 */
package org.nttext.commentary;

import openscriptures.text.StructureRepository;
import openscriptures.text.TextRepository;
import openscriptures.text.TokenModule;
import openscriptures.text.WorkRepository;

/**
 * @author Neal Audenaert
 */
public interface CommentaryModule {

    public EntryRepository getEntryRepository();
    
    public VURepository getVURepository();
    
    public VariantReadingRepository getRdgRepository();
    
    public TextRepository getTextRepository();
    
    public WorkRepository getWorkRepository(); 
    
    public TokenModule getTokenRepository(); 
    
    public StructureRepository getStructureRepository(); 
    
}
