/**
 * 
 */
package org.idch.bible.importers.hcsb;

import org.idch.bible.ref.BookOrder;
import org.idch.texts.Structure;
import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

public class BookHandler extends StructureHandler {

    public static final String NAME = "Book";
    
    public static final String BOOK = "book";
    
    public BookHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals(BOOK);
    }
    
    @Override
    public void start(PathElement p) {
        count++;
        String id = p.getAttribute("id");
        id = id.replace("BK.", "");
        
        int ix = Integer.parseInt(id);
        String osisId = BookOrder.KJV.getId(ix - 1);        // book order indices are 0-based
        
        // TODO Book of the Bible. Create a structure to track them
        
        // mark in text
        // sub elements of note
        //     lastupdate
        //     bookname
        //     head1
        
    }
    
    @Override
    public void end(PathElement p) {
    }
    
    protected void close(Structure s) {
        // close any open chapters, verses or sectioins
        // by default, do nothing
    }
    
    //===================================================================================
    // INNER CLASSES (SUB-UNIT HANDLERS)
    //===================================================================================
    
    /**
     * 
     * @author Neal Audenaert
     */
    public static class LastUpdate extends StructureHandler {
        public static final String NAME = "LastUpdate";
        public static final String TAGNAME = "lastupdate";
        
        
        public LastUpdate() {
            super(NAME);
        }
        
        public boolean matchesStart(PathElement p) {
            return p.getName().equals(TAGNAME);
        }
        
        @Override
        public void start(PathElement p) {  count++; ctx.notInText(); }
        
        @Override
        public void end(PathElement p) {
            // definitely need to keep track of this.
        }
    }
    
    /**
     * 
     * @author Neal Audenaert
     */
    public static class BookName extends StructureHandler {
        public static final String NAME = "BookName";
        public static final String BOOK_NAME = "bookname";
        
        
        public BookName() { super(NAME); }
        
        public boolean matchesStart(PathElement p) {
            return p.getName().equals(BOOK_NAME);
        }
        
        @Override
        public void start(PathElement p) { count++; }
        
        @Override
        public void end(PathElement p) {
           // set the name of the open book structure
        }
    }
}