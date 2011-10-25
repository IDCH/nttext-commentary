/**
 * 
 */
package org.nttext.commentary;

import java.util.List;

import junit.framework.TestCase;
import openscriptures.ref.VerseRange;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.MySQLCommentaryRepo;
import org.nttext.commentary.persist.VURepository;
import org.nttext.commentary.persist.VariantReadingRepository;

/**
 * @author Neal Audenaert
 */
public class RdgTests extends TestCase {
    MySQLCommentaryRepo repo = null;
    VariantReadingRepository rdgRepo = null;
    
    VariationUnit vu = null;
    
    public void setUp() {
        try {
            repo = MySQLCommentaryRepo.get();
            if (!repo.probe()) {
                repo.create();
            }
            
            rdgRepo = repo.getRdgRepository();
            VURepository vuRepo = repo.getVURepository();
            
            vu = vuRepo.create(new VerseRange("1Pet.2.20"));
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
    
    public void testCreateReading() {
        VariantReading rdg = rdgRepo.create(vu);
        
        assertNotNull(rdg);
        assertTrue(vu.getReadings().contains(rdg));
        
        String eng = "English";
        String grc = "Greek";
        rdg = rdgRepo.create(vu, eng, grc);
        
        assertNotNull(rdg);
        assertEquals(eng, rdg.getEnglishReading());
        assertEquals(grc, rdg.getGreekReading());
        
        assertTrue(vu.getReadings().contains(rdg));
        assertTrue(vu.getReadings().indexOf(rdg) == 1);
    }
    
    public void testListReadings() {
        String eng = "English";
        String grc = "Greek";
        
        VariantReading rdg1 = rdgRepo.create(vu);
        VariantReading rdg2 = rdgRepo.create(vu, eng, grc);
        VariantReading rdg3 = rdgRepo.create(vu, eng, grc);
        
        List<VariantReading> readings = rdgRepo.find(vu);
        assertEquals(3, readings.size());
        assertEquals(rdg1.getId(), readings.get(0).getId());
        assertEquals(rdg2.getId(), readings.get(1).getId());
        assertEquals(rdg3.getId(), readings.get(2).getId());
        
        assertEquals(rdg3.getEnglishReading(), readings.get(2).getEnglishReading());
        assertEquals(rdg3.getGreekReading(), readings.get(2).getGreekReading());
        assertEquals(rdg3.getWitnesses(), readings.get(2).getWitnesses());
        
        assertTrue(!rdg1.getId().equals(rdg2.getId()));
        assertTrue(!rdg1.getId().equals(rdg3.getId()));
        assertTrue(!rdg2.getId().equals(rdg3.getId()));
    }
    
    public void testSave() {
        String eng = "English";
        String grc = "Greek";
        String wit = "A lot of manuscripts";
        
        VariantReading rdg = rdgRepo.create(vu);
        rdg.setEnglishReading(eng);
        rdg.setGreekReading(grc);
        rdg.setWitnessDescription(wit);
        
        rdgRepo.save(rdg);
        
        VariantReading rdg2 = rdgRepo.synchronize(
                new VariantReading(rdg.getId(), rdg.getVariationUnit()));
        assertFalse(rdg == rdg2);
        
        assertEquals(eng, rdg2.getEnglishReading());
        assertEquals(grc, rdg2.getGreekReading());
        assertEquals(wit, rdg2.getWitnessDescription());
    }
    
    
    public void testRemove() {
        VariantReading rdg1 = rdgRepo.create(vu, "Reading 1", "Logos 1");
        VariantReading rdg2 = rdgRepo.create(vu, "Reading 2", "Logos 2");
        VariantReading rdg3 = rdgRepo.create(vu, "Reading 3", "Logos 3");
        VariantReading rdg4 = rdgRepo.create(vu, "Reading 4", "Logos 4");
        VariantReading rdg5 = rdgRepo.create(vu, "Reading 5", "Logos 5");
        VariantReading rdg6 = rdgRepo.create(vu, "Reading 6", "Logos 6");
        VariantReading rdg7 = rdgRepo.create(vu, "Reading 7", "Logos 7");
        
        List<VariantReading> readings = rdgRepo.find(vu);
        assertEquals(7, readings.size());
        assertEquals(rdg1.getId(), readings.get(0).getId());
        assertEquals(rdg2.getId(), readings.get(1).getId());
        assertEquals(rdg3.getId(), readings.get(2).getId());
        assertEquals(rdg4.getId(), readings.get(3).getId());
        assertEquals(rdg5.getId(), readings.get(4).getId());
        assertEquals(rdg6.getId(), readings.get(5).getId());
        assertEquals(rdg7.getId(), readings.get(6).getId());
        
        boolean success = rdgRepo.remove(rdg4);
        assertTrue(success);
        
        readings = rdgRepo.find(vu);
        assertEquals(6, readings.size());
        assertEquals(rdg1.getId(), readings.get(0).getId());
        assertEquals(rdg2.getId(), readings.get(1).getId());
        assertEquals(rdg3.getId(), readings.get(2).getId());
        assertEquals(rdg5.getId(), readings.get(3).getId());
        assertEquals(rdg6.getId(), readings.get(4).getId());
        assertEquals(rdg7.getId(), readings.get(5).getId());
    }
    

//    public void testRemoveEntry() {
//        String ref = "1Pet.2.20";
//        String overview = "This is a short overview of this entry";
//        Passage passage = new VerseRange(ref);
//        VariationUnit vu = vuRepo.create(passage);
//        
//        vu.setCommentary(overview);
//        vuRepo.save(vu);
//        
//        vu = vuRepo.find(new VerseRange(ref));
//        
//        assertTrue(vu.getPassage().equals(passage));
//        assertTrue(vu.getId() >= 0);
//        assertEquals(overview, vu.getCommentary());
//        
//        boolean success = vuRepo.remove(vu);
//        
//        assertTrue(success);
//        vu = vuRepo.find(new VerseRange(ref));
//        assertNull(vu);
//    }
}
