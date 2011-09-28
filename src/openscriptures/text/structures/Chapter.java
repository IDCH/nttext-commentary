/**
 * 
 */
package openscriptures.text.structures;

import openscriptures.text.Structure;
import openscriptures.text.StructureWrapper;

/**
 * @author Neal Audenaert
 */
public class Chapter extends StructureWrapper {
    
    public static final String STRUCTURE_NAME = "chapter"; 
    public static final String STRUCTURE_PERSPECTIVE = "bcv";       // book, chapter, verse 
    
    public static final String ATTR_OSIS_ID = "osisId";
    public static final String ATTR_TITLE = "title";   // ?? is this relevant - probably
    public static final String ATTR_NUMBER = "n";
    
    /**
     * Initializes a newly created structure with the parameters for a chapter. Note that 
     * this should be called only once to initialize a new chapter structure, not as a means 
     * of creating wrapped version of a chapter. 
     * 
     * @param structure
     * @param osisId
     * @return
     */
    public static Chapter init(Structure structure, String osisId) {
        Chapter chapter = new Chapter(structure);
        
        structure.setPerspective(STRUCTURE_PERSPECTIVE);
        structure.setAttribute(ATTR_OSIS_ID, osisId);
        
        // TODO set chapter number
        
        return chapter;
    }
    
    public static boolean isChapter(Structure s) {
        return s.getName().equals(STRUCTURE_NAME);
    }
    
    //======================================================================================
    // CONSTRUCTORS
    //======================================================================================

    public Chapter(Structure s) {
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
    
    public String getTitle() {
        return this.getAttribute(ATTR_TITLE);
    }
    
    public String setTitle(String title) {
        return this.setAttribute(ATTR_TITLE, title);
    }
    
    public int getChapterNumber() {
        // NOTE we're assuming numeric chapter numbers. That might need to change to be more
        //      general, but this is intended to be sequential. 
        String n = this.getAttribute(ATTR_NUMBER);
        return Integer.parseInt(n);
    }
    
    public void setChapterNumber(int num) {
        this.setAttribute(ATTR_NUMBER, Integer.toString(num));
    }
}
