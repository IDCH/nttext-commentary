/**
 * 
 */
package openscriptures.text.importer;

import java.util.HashMap;
import java.util.Map;

import openscriptures.text.MutableWork;

/**
 * Provides a shared context for use by both the main <tt>Importer</tt> and individual 
 * structure handlers. This is designed as a lightweight class that provides direct access to
 * its member variables. Use with care.
 * 
 * @author Neal Audenaert
 */
public class Context {
    
    private Map<String, Boolean> flags = new HashMap<String, Boolean>();
    
    /** The <tt>Work</tt> that is being imported. */
    public MutableWork work = null;
    
    /** Indicates that the parser is in the header section of the document. */
    public boolean inHeader = false;
    
    /** Indicates that the parser is in the front matter section of the document. */
    public boolean inFront = false;
    
    /** Indicates that the parser is in the text of the document. While this is 
     *  set to true, the importer will add tokens from the character data that 
     *  it encounters. */
    public boolean inText = false;
    
 
    public int currentVerse = 0;
    public int currentChapter = 0;
    
    public void flag(String state, Boolean value) {
        flags.put(state, value);
    }
    
    public boolean check(String state) {
        return flags.containsKey(state) 
               ? flags.get(state) 
               : false;
    }
}