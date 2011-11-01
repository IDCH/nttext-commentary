/**
 * 
 */
package org.idch.bible.importers.hcsb;

import org.idch.texts.Structure;
import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

public class DisputedHandler extends StructureHandler {
    public static final String NAME = "DisputedText";
    public static final String TAGNAME = "disputed";
    
    public DisputedHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals(TAGNAME);
    }
    
    @Override
    public void start(PathElement p) {
        count++;
        // This tag wraps a section of disputed text, we should wrap this with a structure
        
    }
    
    @Override
    public void end(PathElement p) {
    }
    
    protected void close(Structure s) {
        // close any open chapters, verses or sectioins
        // by default, do nothing
    }
}