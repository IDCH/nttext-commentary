/**
 * 
 */
package org.nttext.commentary;

import java.util.UUID;

import antlr.Token;

import openscriptures.text.Structure;
import openscriptures.text.Work;
import openscriptures.text.WorkId;

/**
 * @author Neal Audenaert
 */
public class Commentary {
    
    public static Commentary instance = null;
    
    public synchronized static Commentary getInstance() {
        // TODO allow for configuration options
        if (instance == null) {
            instance = new Commentary();
        }
        
        return instance;
    }

    private Commentary() {
        
    }
    
    public Work getWork(WorkId id) {
        return null;
    }
    
    public Structure getStructureById(UUID id) {
        return null;
    }
    
    public Token getTokenById(UUID id) {
        return null;
    }
}
