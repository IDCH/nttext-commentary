/**
 * 
 */
package openscriptures.text.structures;

import openscriptures.text.Structure;
import openscriptures.text.StructureWrapper;

/**
 * @author Neal Audenaert
 */
public class Book extends StructureWrapper {
    
    public static final String STRUCTURE_NAME = "book"; 
    public static final String STRUCTURE_PERSPECTIVE = "bcv";       // book, chapter, verse 
    
    public static final String ATTR_OSIS_ID = "osisId";
    public static final String ATTR_TITLE = "title";
    
    /**
     * Initializes a newly created structure with the parameters for a book. Note that 
     * this should be called only once to initialize a new book structure, not as a means 
     * of creating wrapped version of a book. 
     * 
     * @param structure
     * @param osisId
     * @return
     */
    public static Book init(Structure structure, String osisId) {
        Book book = new Book(structure);
        
        structure.setPerspective(STRUCTURE_PERSPECTIVE);
        structure.setAttribute(ATTR_OSIS_ID, osisId);
        
        return book;
    }
    
    public static boolean isBook(Structure s) {
        return s.getName().equals(STRUCTURE_NAME);
    }
    
    //======================================================================================
    // CONSTRUCTORS
    //======================================================================================

    public Book(Structure s) {
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
}
