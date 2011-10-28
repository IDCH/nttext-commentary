/**
 * 
 */
package org.nttext.commentary;

import java.util.Set;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.persist.mysql.MySQLCommentaryModule;

import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class EntryInstanceTests extends TestCase {
    
    MySQLCommentaryModule repo = null;
    InstanceRepository instanceRepo = null;
    
    
    public void setUp() {
        try {
            repo = MySQLCommentaryModule.get();
            if (!repo.probe()) {
                repo.create();
            }
            
            instanceRepo = repo.getInstanceRepository();
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
    String overview = "This is a short overview of this instance";
    
    private EntryInstance createInstance() {
        EntryInstance instance = instanceRepo.create(passage);
    
        instance.setOverview(overview);
        instanceRepo.save(instance);
        return instance;
    }
    
    public void checkInstance(EntryInstance instance) {
        this.checkInstance(instance, true);
    }
    
    public void checkInstance(EntryInstance instance, boolean checkOverview) {
        assertTrue(instance.getPassage().equals(passage));
        assertTrue(instance.getId() >= 0);
        
        if (checkOverview) 
            assertEquals(overview, instance.getOverview());
    }
    
    public void testCreateInstance() {
        EntryInstance instance = instanceRepo.create(passage);
        checkInstance(instance, false);
    }
    
    public void testUpdateInstance() {
        EntryInstance instance = createInstance();
        
        long id = instance.getId();
        
        instance = instanceRepo.find(id);
        checkInstance(instance);
    }
    
    public void testRetrieveInstance() {
        createInstance();
        EntryInstance instance = instanceRepo.find(passage);
        checkInstance(instance);
        
        instance = instanceRepo.find(new VerseRange("1Pet.2.22"));
        assertNull(instance);
    }
    
    public void testRemoveInstance() {
        createInstance();
        
        EntryInstance instance = instanceRepo.find(passage);
        checkInstance(instance);
        
        boolean success = instanceRepo.remove(instance);
        
        assertTrue(success);
        instance = instanceRepo.find(passage);
        assertNull(instance);
    }
    
    public void testAssociateVU() {
        EntryInstance instance = createInstance();
        VariationUnit vu1 = repo.getVURepository().create(passage);
        VariationUnit vu2 = repo.getVURepository().create(passage);
        
        assertTrue(instanceRepo.associate(instance, vu1));
        assertTrue(instanceRepo.associate(instance, vu2));
        
        Set<VariationUnit> VUs = instanceRepo.getVU(instance);
        assertEquals(2, VUs.size());
        for (VariationUnit vu : VUs) {
            long id = vu.getId();
            assertTrue(id == vu1.getId().longValue() || id == vu2.getId().longValue());
        }
        
        instance = instanceRepo.find(instance.getId());
        VUs = instance.getVariationUnits();
        assertEquals(2, VUs.size());
        for (VariationUnit vu : VUs) {
            long id = vu.getId();
            assertTrue(id == vu1.getId().longValue() || id == vu2.getId().longValue());
        }
        
        assertTrue(instanceRepo.disassociate(instance, vu2));
        VUs = instanceRepo.getVU(instance);
        assertEquals(1, VUs.size());
        for (VariationUnit vu : VUs) {
            long id = vu.getId();
            assertTrue(id == vu1.getId().longValue());
        }
    }
}
