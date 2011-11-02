/**
 * 
 */
package org.idch.texts;

import org.idch.persist.DBBackedRepository;
import org.idch.persist.RepositoryAccessException;
import org.idch.texts.persist.mem.MemTextModule;

/**
 * @author Neal Audenaert
 */
public class TextModuleInstance {
    public final static String MODULE_NAME = "texts";
    public final static MemTextModule module = new MemTextModule();
    
    public static final TextModule get()
            throws RepositoryAccessException {
        
        // return (TextModule)DBBackedRepository.get(MODULE_NAME);
        return module;
    }
}
