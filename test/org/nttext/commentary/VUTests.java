/**
 * 
 */
package org.nttext.commentary;

import junit.framework.TestCase;
import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.MySQLCommentaryRepo;
import org.nttext.commentary.persist.VURepository;

/**
 * @author Neal Audenaert
 */
public class VUTests extends TestCase {
    MySQLCommentaryRepo repo = null;
    VURepository vuRepo = null;
    
    public void setUp() {
        try {
            repo = MySQLCommentaryRepo.get();
            if (!repo.probe()) {
                repo.create();
            }
            
            vuRepo = repo.getVURepository();
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
    
    public void testCreateVU() {
        String ref = "1Pet.2.20";
        Passage passage = new VerseRange(ref);
        VariationUnit vu = vuRepo.create(passage);
        
        assertTrue(vu.getPassage().equals(passage));
        assertTrue(vu.getId() >= 0);
    }
    
    public void testUpdateVU() {
        String ref = "1Pet.2.20";
        String overview = "This is a short overview of this entry";
        Passage passage = new VerseRange(ref);
        VariationUnit vu = vuRepo.create(passage);
        
        long id = vu.getId();
        vu.setCommentary(overview);
        vuRepo.save(vu);
        
        vu = vuRepo.find(id);
        
        assertTrue(vu.getPassage().equals(passage));
        assertTrue(vu.getId() >= 0);
        assertEquals(overview, vu.getCommentary());
    }
    
    public void testRetrieveVU() {
        String ref = "1Pet.2.20";
        String overview = "This is a short overview of this entry";
        Passage passage = new VerseRange(ref);
        VariationUnit vu = vuRepo.create(passage);
        
        vu.setCommentary(overview);
        vuRepo.save(vu);
        
        vu = vuRepo.find(new VerseRange(ref));
        
        assertTrue(vu.getPassage().equals(passage));
        assertTrue(vu.getId() >= 0);
        assertEquals(overview, vu.getCommentary());
        
        vu = vuRepo.find(new VerseRange("1Pet.2.22"));
        assertNull(vu);
    }
    
    public void testRemoveVU() {
        String ref = "1Pet.2.20";
        String overview = "This is a short overview of this entry";
        Passage passage = new VerseRange(ref);
        VariationUnit vu = vuRepo.create(passage);
        
        vu.setCommentary(overview);
        vuRepo.save(vu);
        
        vu = vuRepo.find(new VerseRange(ref));
        
        assertTrue(vu.getPassage().equals(passage));
        assertTrue(vu.getId() >= 0);
        assertEquals(overview, vu.getCommentary());
        
        boolean success = vuRepo.remove(vu);
        
        assertTrue(success);
        vu = vuRepo.find(new VerseRange(ref));
        assertNull(vu);
    }
}
