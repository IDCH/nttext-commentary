/**
 * 
 */
package openscriptures.text.structures;

import openscriptures.text.Structure;
import openscriptures.text.StructureWrapper;

/**
 * @author Neal Audenaert
 */
public class Verse extends StructureWrapper {
    
    public static final String STRUCTURE_NAME = "verse"; 
    public static final String STRUCTURE_PERSPECTIVE = "bcv";       // book, chapter, verse 
    
    public static final String ATTR_OSIS_ID = "osisId";
    public static final String ATTR_NUMBER = "n";
    
    /**
     * Initializes a newly created structure with the parameters for a verse. Note that 
     * this should be called only once to initialize a new verse structure, not as a means 
     * of creating wrapped version of a verse. 
     * 
     * @param structure
     * @param osisId
     * @return
     */
    public static Verse init(Structure structure, String osisId) {
        Verse verse = new Verse(structure);
        
        structure.setPerspective(STRUCTURE_PERSPECTIVE);
        structure.setAttribute(ATTR_OSIS_ID, osisId);
        
        // TODO set verse number
        
        return verse;
    }
    
    public static boolean isVerse(Structure s) {
        return s.getName().equals(STRUCTURE_NAME);
    }
    
    //======================================================================================
    // CONSTRUCTORS
    //======================================================================================

    public Verse(Structure s) {
        super(s);
    }

    public boolean accepts(Structure s) {
        return s.getName().equals(STRUCTURE_NAME);
    }
   
    //======================================================================================
    // CUSTOM ACCESSORS AND MUTATORS
    //======================================================================================

    /**
     * 
     * @return
     */
    public String getOsisId() {
        return this.getAttribute(ATTR_OSIS_ID);
    }
    
    public void setOsisId(String id) {
        // TODO check pattern?
        this.setAttribute(ATTR_OSIS_ID, id);
    }
    
    public int getVerseNumber() {
        // NOTE we're assuming numeric verse numbers. That might need to change to be more
        //      general, but this is intended to be sequential. 
        String n = this.getAttribute(ATTR_NUMBER);
        return Integer.parseInt(n);
    }
    
    public void setVerseNumber(int num) {
        this.setAttribute(ATTR_NUMBER, Integer.toString(num));
    }
}
