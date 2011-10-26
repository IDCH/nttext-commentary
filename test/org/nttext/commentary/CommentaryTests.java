/**
 * 
 */
package org.nttext.commentary;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.mysql.MySQLCommentaryModule;

import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class CommentaryTests extends TestCase {
    
    MySQLCommentaryModule repo = null;
    
    public void setUp() {
        try {
            repo = MySQLCommentaryModule.get();
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
    
    public void testDBCreate() throws RepositoryAccessException {
        if (repo.probe()) 
            repo.drop();
        
        repo.create();
    }

}
