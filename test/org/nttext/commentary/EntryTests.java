/**
 * 
 */
package org.nttext.commentary;

import java.util.Set;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.mysql.MySQLCommentaryRepo;

import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class EntryTests extends TestCase {
    
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
    
    private String ref = "1Pet.2.20";
    private Passage passage = new VerseRange(ref);
    String overview = "This is a short overview of this entry";
    
    private Entry createEntry() {
        Entry entry = entryRepo.create(passage);
    
        entry.setOverview(overview);
        entryRepo.save(entry);
        return entry;
    }
    
    public void checkEntry(Entry entry) {
        this.checkEntry(entry, true);
    }
    
    public void checkEntry(Entry entry, boolean checkOverview) {
        assertTrue(entry.getPassage().equals(passage));
        assertTrue(entry.getId() >= 0);
        
        if (checkOverview) 
            assertEquals(overview, entry.getOverview());
    }
    
    public void testCreateEntry() {
        Entry entry = entryRepo.create(passage);
        checkEntry(entry, false);
    }
    
    public void testUpdateEntry() {
        Entry entry = createEntry();
        
        long id = entry.getId();
        
        entry = entryRepo.find(id);
        checkEntry(entry);
    }
    
    public void testRetrieveEntry() {
        createEntry();
        Entry entry = entryRepo.find(passage);
        checkEntry(entry);
        
        entry = entryRepo.find(new VerseRange("1Pet.2.22"));
        assertNull(entry);
    }
    
    public void testRemoveEntry() {
        createEntry();
        
        Entry entry = entryRepo.find(passage);
        checkEntry(entry);
        
        boolean success = entryRepo.remove(entry);
        
        assertTrue(success);
        entry = entryRepo.find(passage);
        assertNull(entry);
    }
    
    public void testAssociateVU() {
        Entry entry = createEntry();
        VariationUnit vu1 = repo.getVURepository().create(passage);
        VariationUnit vu2 = repo.getVURepository().create(passage);
        
        assertTrue(entryRepo.associate(entry, vu1));
        assertTrue(entryRepo.associate(entry, vu2));
        
        Set<VariationUnit> VUs = entryRepo.getVU(entry);
        assertEquals(2, VUs.size());
        for (VariationUnit vu : VUs) {
            long id = vu.getId();
            assertTrue(id == vu1.getId().longValue() || id == vu2.getId().longValue());
        }
        
        entry = entryRepo.find(entry.getId());
        VUs = entry.getVariationUnits();
        assertEquals(2, VUs.size());
        for (VariationUnit vu : VUs) {
            long id = vu.getId();
            assertTrue(id == vu1.getId().longValue() || id == vu2.getId().longValue());
        }
        
    }
}
