/**
 * 
 */
package org.nttext.commentary;

import java.util.UUID;

import org.idch.texts.Structure;
import org.idch.texts.Work;
import org.idch.texts.WorkId;

import antlr.Token;


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
