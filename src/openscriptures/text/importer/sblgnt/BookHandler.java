/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import org.apache.log4j.Logger;

import openscriptures.text.Structure;
import openscriptures.text.Token;
import openscriptures.text.importer.Context;
import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;
import openscriptures.text.structures.Book;

public class BookHandler extends StructureHandler {
    
    private static final Logger LOGGER = Logger.getLogger(BookHandler.class);
    
    public static final String NAME     = "BookOfTheBible";
    
    public static final String BOOK     = "book";
    public static final String CHPT_NUM = "chptNum";
    public static final String VS_NUM   = "vsNum";
    public static final String BOOK_ID  = "bookId";
    
    /** The book currently being processed. */
    private Book book = null;
    
    /** Default Constructor */
    public BookHandler() {
        super(NAME);
    }
    
    private void closeContext() {
        // This seems like a bad idea.
//        ctx.set(CHPT_NUM, 0);
//        ctx.set(VS_NUM, 0);
//        
//        ctx.set(BOOK_ID, null);
//        ctx.clearHandler(this.getName());
//        
//        this.book = null;
//        this.start = -1;
    }
    
    
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("div") && p.hasAttribute("type", "book");
    }
    
    public void start(PathElement p) {
        ctx.inText();
        
//        ctx.flag(BOOK, true);
//        
//        ctx.set(CHPT_NUM, 0);
//        ctx.set(VS_NUM, 0);
//        
//        String osisId = p.getAttribute("osisID");
//        ctx.set(BOOK_ID, osisId);
        
        String osisId = p.getAttribute("osisID");
        
        Structure struct = createStructure(Book.STRUCTURE_NAME);
        book = new Book(struct);
        this.book.setOsisId(osisId);
    }
    
    public void end(PathElement p) {
        this.closeActiveStructure();
    }
    
    public void close(Structure s) {
        // sanity checks
        assert book.getUUID().equals(s.getUUID());
        if (!book.getUUID().equals(s.getUUID())) {
            // TODO throw exception.
            LOGGER.error("Mismatched structures. Attempted to close " + s.getUUID() + 
                    " but subclass had reference to " + book.getUUID());
            this.book = null;
            return;
        }
        
        LOGGER.info("End of Book: " + book.getOsisId());
        
        // TODO clean up any open chapters and verses.
        
//        closeContext();
        ctx.notInText();
        this.book = null;
    }
}