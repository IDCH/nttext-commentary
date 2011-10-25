/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
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
    
    private List<VariantReading> createReadings() {
        List<VariantReading> readings = new ArrayList<VariantReading>();
        readings.add(rdgRepo.create(vu, "Reading 1", "Logos 1"));
        readings.add(rdgRepo.create(vu, "Reading 2", "Logos 2"));
        readings.add(rdgRepo.create(vu, "Reading 3", "Logos 3"));
        readings.add(rdgRepo.create(vu, "Reading 4", "Logos 4"));
        readings.add(rdgRepo.create(vu, "Reading 5", "Logos 5"));
        readings.add(rdgRepo.create(vu, "Reading 6", "Logos 6"));
        readings.add(rdgRepo.create(vu, "Reading 7", "Logos 7"));
        
        return readings;
    }
    
    private void compareReadings(List<VariantReading> expected, List<VariantReading> observed) {
        assertEquals(expected.size(), observed.size());
        
        int sz = expected.size();
        for (int i = 0; i < sz; i++) {
            assertEquals(expected.get(i).getId(), observed.get(i).getId());
            assertEquals(expected.get(i).getEnglishReading(), observed.get(i).getEnglishReading());
            assertEquals(expected.get(i).getGreekReading(), observed.get(i).getGreekReading());
            assertEquals(expected.get(i).getWitnessDescription(), observed.get(i).getWitnessDescription());
        }
    }
    
    public void testRemove() {
        List<VariantReading> rdgs = createReadings();
        compareReadings(rdgs, rdgRepo.find(vu));
        
        VariantReading removed = rdgs.remove(3);
        boolean success = rdgRepo.remove(removed);
        assertTrue(success);
        compareReadings(rdgs, rdgRepo.find(vu));
    }
    
    /** Tests to see that variant readings are properly restored along with the 
     *  variation unit. */
    public void testRestoreVU() {
        List<VariantReading> rdgs = createReadings();
        
        VariationUnit vu2 = repo.getVURepository().find(vu.getId());
        assertFalse(vu2 == vu);
        
        compareReadings(rdgs, vu2.getReadings());
    }

}
