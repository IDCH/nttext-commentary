/**
 * 
 */
package org.idch.bible.importers.hcsb;

import org.idch.texts.Structure;
import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

public class SectionHandler extends StructureHandler {
    public static final String NAME = "SectionHeader";
    
    public SectionHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("head1") || p.getName().equals("supertitle"); 
    }
    
    @Override
    public void start(PathElement p) {
        // <chap type="noind" n="bible.50.1.0">1</chap>
        count++;
        
        
        this.ctx.notInText();       // this marks a chapter number
        // TODO close any open sections
        
        // TODO open section
        

        
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