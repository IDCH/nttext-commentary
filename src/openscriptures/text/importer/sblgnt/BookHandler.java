/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import org.apache.log4j.Logger;

import openscriptures.text.Structure;
import openscriptures.text.Token;
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
    Book book = null;
    int start = 0;
    
    /** Default Constructor */
    public BookHandler() {
        super(NAME);
    }
    
    public Structure createStructure(PathElement p) {
        // if book is not null, close it.
        if (book != null) {
            this.close();       // TODO log warning. This shouldn't have happened.
        }
        
        String osisId = p.getAttribute("osisID");
        LOGGER.info("Creating Book: " + osisId);
        
        start = ctx.work.getEnd();
        this.book = new Book(ctx.work, null, null);
        
        // set OSIS ID
        this.book.setOsisId(osisId);
        
        // update the context so that others can know that we are active
        ctx.setHandler(this);

        return book;
    }
    
    private void closeContext() {
        ctx.flag(BOOK, false);
        ctx.inText = false;
        ctx.set(CHPT_NUM, 0);
        ctx.set(VS_NUM, 0);
        
        ctx.set(BOOK_ID, null);
        ctx.clearHandler(this.getName());
        
        this.book = null;
        this.start = -1;
    }
    
    private void updateStartToken() {
        if ((book == null) || (book.getStartToken() != null)) {
            return;                             // already initialized
        } 
        
        Token t = ctx.work.get(start);
        if (t.getType() == Token.Type.WHITESPACE)
            t = t.next(true);
        
        if (t != null) {
            book.setStartToken(t);
        } else {
            // TODO handle error, there should have been a next token.
        }
    }
    
    private void cleanUpContext() {
        // TODO close any open chapters and verses
        
    }
    
    private Token getEndToken() {
        // update the ending token. If we've read whitespace, we need to go ahead and back
        // up to the last word/punctuation mark in the chapter.
        Token end = ctx.work.get(ctx.work.getEnd() - 1);
        if (end.getType() == Token.Type.WHITESPACE)
            end = end.prev(true);
        
        assert end != null : "No end token for book of the Bible.";
        if (end == null) {
            // TODO throw exception
        }
        
        return end;
    }
    
    // TODO bump this up to the top-level so that we can close other handlers if they are open
    public Structure close() {
        Book book = this.book;
        if (book != null) {
            updateStartToken();
            cleanUpContext();
            
            book.setEndToken(getEndToken());
            LOGGER.info("End of Book: " + book.getOsisId());
        } else {
            // TODO should we do something here?
        }
        
        closeContext();
        return book;
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("div") && p.hasAttribute("type", "book");
    }
    
    public void start(PathElement p) {
        ctx.flag(BOOK, true);
        ctx.inText = true;
        
        ctx.set(CHPT_NUM, 0);
        ctx.set(VS_NUM, 0);
        
        String osisId = p.getAttribute("osisID");
        ctx.set(BOOK_ID, osisId);
        
        this.createStructure(p);
    }
    
    public void end(PathElement p) {
        this.close();
    }
}