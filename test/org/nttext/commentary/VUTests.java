/**
 * 
 */
package org.nttext.commentary;

import junit.framework.TestCase;
import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.mysql.MySQLCommentaryModule;

/**
 * @author Neal Audenaert
 */
public class VUTests extends TestCase {
    private MySQLCommentaryModule repo = null;
    private VURepository vuRepo = null;
    
    private String ref = "1Pet.2.20";
    private Passage passage = new VerseRange(ref);
    String overview = "This is a short overview of this entry";
    
    public void setUp() {
        try {
            repo = MySQLCommentaryModule.get();
            if (!repo.probe()) {
                repo.create();
            }
            
            vuRepo = repo.getVURepository();
        } catch (RepositoryAccessException e) {
            repo = null;
            e.printStackTrace();
        }
    }
    
    private VariationUnit createVU() {
        VariationUnit vu = vuRepo.create(passage);
    
        vu.setCommentary(overview);
        vuRepo.save(vu);
        return vu;
    }
    
    private void checkVU(VariationUnit vu) {
        checkVU(vu, true);
    }
    
    private void checkVU(VariationUnit vu, boolean checkCommentary) {
        assertTrue(vu.getPassage().equals(passage));
        assertTrue(vu.getId() >= 0);
        
        if (checkCommentary) 
            assertEquals(overview, vu.getCommentary());
    }
    
    public void tearDown() {
        try {
            repo.drop();
        } catch (RepositoryAccessException e) {
            e.printStackTrace();
        }
    }
    
    public void testCreateVU() {
        VariationUnit vu = vuRepo.create(passage);
        checkVU(vu, false);
    }
    
    public void testUpdateVU() {
        VariationUnit vu = createVU();
        checkVU(vuRepo.find(vu.getId()));
    }
    
    public void testRetrieveVU() {
        vuRepo.create(passage);
        checkVU(vuRepo.find(passage));
        
        VariationUnit vu = vuRepo.find(new VerseRange("1Pet.2.22"));
        assertNull(vu);
    }
    
    public void testRemoveVU() {
        createVU();
        
        VariationUnit vu = vuRepo.find(passage);
        checkVU(vu);
        
        assertTrue(vuRepo.remove(vu));
        vu = vuRepo.find(new VerseRange(ref));
        assertNull(vu);
    }
}
