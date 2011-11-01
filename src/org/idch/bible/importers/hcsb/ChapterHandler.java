/**
 * 
 */
package org.idch.bible.importers.hcsb;

import org.idch.texts.Structure;
import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

public class ChapterHandler extends StructureHandler {
    public static final String NAME = "Chapter";
    public static final String BOOK = "chap";
    
    
    public ChapterHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("chap") ||
               p.getName().equals("hiddenverse");       // marks a book with only one chapter
    }
    
    @Override
    public void start(PathElement p) {
        // <chap type="noind" n="bible.50.1.0">1</chap>
        count++;
        String id = p.getAttribute("n");        // bible.50.1.0
        String type = p.getAttribute("type");
        
        this.ctx.notInText();       // this marks a chapter number
        // TODO close any open verses and/or chapters
        
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