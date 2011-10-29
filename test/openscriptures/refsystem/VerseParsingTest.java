/**
 * 
 */
package openscriptures.refsystem;

import openscriptures.ref.ReferenceParser;
import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class VerseParsingTest extends TestCase {

    public void setUp() {
        
    }
    
    public void tearDown() {
        
    }
    
    public void checkPassage(String osis, String ref) {
        assertEquals(osis, ReferenceParser.parseReference(ref));
    }
    
    public void testBasicVerses() {
        checkPassage("1John.3.1-1John.3.2", "1 John 3:1,2");
        checkPassage("1John.3.2-1John.3.3", "1 John 3:2-1 John 3:3");
        checkPassage("1John.3-1John.4", "1 John 3-1 John 4");
        checkPassage("1John.1.2-1John.4.2", "1 John 1:2-1 John 4:2");
        checkPassage("1John.1.2-1John.1.8", "1 John 1:2-8");
        checkPassage("1John.1.2-1John.2.8", "1 John 1:2-2:8");
    }
}
