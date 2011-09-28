/**
 * 
 */
package openscriptures.text.structures;

import openscriptures.text.Structure;
import openscriptures.text.StructureWrapper;
import openscriptures.text.Token;
import openscriptures.text.Work;
import openscriptures.text.impl.BasicStructure;

/**
 * @author Neal Audenaert
 */
public class Book extends StructureWrapper {
    
    public static final String STRUCTURE_NAME = "book"; 
    public static final String STRUCTURE_PERSPECTIVE = "bcv";       // book, chapter, verse 
    
    public static final String ATTR_OSIS_ID = "osisId";
    public static final String ATTR_TITLE = "title";

    public Book(Work w, Token start, Token end) {
        super(new BasicStructure(w, STRUCTURE_NAME, start, end));
        
        this.setPerspective(STRUCTURE_PERSPECTIVE);
    }
    
    public Book(Structure s) {
        super(s);
    }
    
    public boolean accepts(Structure s) {
        return s.getName().equals("book");
    }
    
    public String getOsisId() {
        return this.getAttribute(ATTR_OSIS_ID);
    }
    
    public String getTitle() {
        return this.getAttribute(ATTR_TITLE);
    }
    
    
    public void setOsisId(String id) {
        // TODO check pattern?
        this.setAttribute(ATTR_OSIS_ID, id);
    }
    
    
    public String setTitle(String title) {
        return this.setAttribute(ATTR_TITLE, title);
    }
}
