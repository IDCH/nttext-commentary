/**
 * 
 */
package org.nttext.commentary;


import junit.framework.TestCase;
import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;
import openscriptures.text.Structure;
import openscriptures.text.StructureRepository;
import openscriptures.text.TextModule;
import openscriptures.text.Work;
import openscriptures.text.WorkRepository;
import openscriptures.text.structures.Verse;

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
    String commentary = "This is a short overview of this entry";
    
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
    
        vu.setCommentary(commentary);
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
            assertEquals(commentary, vu.getCommentary());
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
        createVU();
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
    
    public void testCreateReference() {
        // FIXME this depends on a very specific starting state for the DB that will, in 
        //       general not be the case. We need to implement a test framework for the 
        //       'Texts' module and tie this test into those components.
        
        TextModule textModule = repo.getTextRepository();
        WorkRepository workRepo = this.repo.getWorkRepository();
        StructureRepository structRepo = this.repo.getStructureRepository();
        
        Work sblgnt = workRepo.findByAbbr("SBLGNT").get(3);
        assertNotNull(sblgnt);
        
        Verse vs = Verse.getVerse(textModule, sblgnt, ref);
        assertNotNull(vs);

        VariationUnit vu = createVU();
        assertNotNull(vu);
        
        // Create the reference
        
        
        String refText = "κλέος εἰ ἁμαρτάνοντες";       // "ἀλλ’",  "καὶ"
        Structure s = textModule.createStructure(vs, refText, "VURef");
        assertNotNull(s);
        VUReference ref = VUReference.init(s, vu);
        assertNotNull(ref);
        
        structRepo.save(ref);
        
        // see if we can get it back
        
        vu = vuRepo.find(vu.getId());
        VUReference ref2 = VUReference.findReference(structRepo, sblgnt, vu);
        
        assertNotNull(ref2);
        assertEquals(vu.getId().longValue(), ref2.getVariationUnit().getId().longValue());
        
        
    }
}
