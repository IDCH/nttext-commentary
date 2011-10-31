/**
 * 
 */
package org.nttext.commentary;

import org.idch.texts.StructureRepository;
import org.idch.texts.TextModule;
import org.idch.texts.TokenRepository;
import org.idch.texts.WorkRepository;

/**
 * @author Neal Audenaert
 */
public interface CommentaryModule {

    public InstanceRepository getInstanceRepository();
    
    public VURepository getVURepository();
    
    public VariantReadingRepository getRdgRepository();
    
    public TextModule getTextRepository();
    
    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository(); 
    
    public StructureRepository getStructureRepository(); 
    
}
