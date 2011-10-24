/**
 * 
 */
package org.nttext.commentary;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.EntryRepository;
import org.nttext.commentary.persist.MySQLCommentaryRepo;

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
    
    public void testCreateEntry() {
        String ref = "1Pet.2.20";
        Passage passage = new VerseRange(ref);
        Entry entry = entryRepo.create(passage);
        
        assertTrue(entry.getPassage().equals(passage));
        assertTrue(entry.getId() >= 0);
    }
    
    public void testUpdateEntry() {
        String ref = "1Pet.2.20";
        String overview = "This is a short overview of this entry";
        Passage passage = new VerseRange(ref);
        Entry entry = entryRepo.create(passage);
        
        long id = entry.getId();
        entry.setOverview(overview);
        entryRepo.save(entry);
        
        entry = entryRepo.find(id);
        
        assertTrue(entry.getPassage().equals(passage));
        assertTrue(entry.getId() >= 0);
        assertEquals(overview, entry.getOverview());
    }
    
    public void testRetrieveEntry() {
        String ref = "1Pet.2.20";
        String overview = "This is a short overview of this entry";
        Passage passage = new VerseRange(ref);
        Entry entry = entryRepo.create(passage);
        
        entry.setOverview(overview);
        entryRepo.save(entry);
        
        entry = entryRepo.find(new VerseRange(ref));
        
        assertTrue(entry.getPassage().equals(passage));
        assertTrue(entry.getId() >= 0);
        assertEquals(overview, entry.getOverview());
        
        entry = entryRepo.find(new VerseRange("1Pet.2.22"));
        assertNull(entry);
    }
    
    public void testRemoveEntry() {
        String ref = "1Pet.2.20";
        String overview = "This is a short overview of this entry";
        Passage passage = new VerseRange(ref);
        Entry entry = entryRepo.create(passage);
        
        entry.setOverview(overview);
        entryRepo.save(entry);
        
        entry = entryRepo.find(new VerseRange(ref));
        
        assertTrue(entry.getPassage().equals(passage));
        assertTrue(entry.getId() >= 0);
        assertEquals(overview, entry.getOverview());
        
        boolean success = entryRepo.remove(entry);
        
        assertTrue(success);
        entry = entryRepo.find(new VerseRange(ref));
        assertNull(entry);
    }
}
