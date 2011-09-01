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

    public Book(Work w, Token start, Token end) {
        super(new BasicStructure(w, "book", start, end));
    }
    
    public Book(Structure s) {
        super(s);
    }
    
    public boolean accepts(Structure s) {
        return s.getName().equals("book");
    }

}
