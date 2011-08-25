/**
 * 
 */
package openscriptures.text;

import openscriptures.text.impl.mem.MemWork;
import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class WorkTests extends TestCase {
    
    
    
    public void testCreateWork() {
        Work work = new MemWork(new WorkId("Bible.grc.SBLGNT.2010"));
        
    }

}
