/**
 * 
 */
package org.nttext.commentary;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.EntryRepository;
import org.nttext.commentary.persist.MySQLCommentaryRepo;

/**
 * @author Neal Audenaert
 */
public class VUTests {
    MySQLCommentaryRepo repo = null;
    EntryRepository entryRepo = null;
    
    
    public void setUp() {
        try {
            repo = MySQLCommentaryRepo.get();
            if (!repo.probe()) {
                repo.create();
            }
            
            entryRepo = repo.getEntryRepository();
        } catch (RepositoryAccessException e) {
            repo = null;
            e.printStackTrace();
        }
    }
    
    public void tearDown() {
        try {
            repo.drop();
        } catch (RepositoryAccessException e) {
            e.printStackTrace();
        }
    }
}
