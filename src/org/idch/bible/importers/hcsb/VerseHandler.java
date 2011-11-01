/**
 * 
 */
package org.idch.bible.importers.hcsb;

import org.idch.texts.Structure;
import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

public class VerseHandler extends StructureHandler {

    public static final String NAME = "Verse";
    
    public static final String BOOK = "verse";
    
    
    public VerseHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals(BOOK);
    }
    
    @Override
    public void start(PathElement p) {
        // <chap type="noind" n="bible.50.1.0">1</chap>
        count++;
        String id = p.getAttribute("n");        // bible.50.1.0
        
        this.ctx.notInText();       // this marks a chapter number
        // TODO close any open verses 
        
        // TODO open verse - there are no markers for the first verse
        

        
    }
    
    @Override
    public void end(PathElement p) {
        this.ctx.inText();       // this marks a chapter number
    }
    
    protected void close(Structure s) {
        // close any open chapters, verses or sectioins
        // by default, do nothing
    }
}