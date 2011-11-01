/**
 * 
 */
package org.idch.bible.importers.hcsb;

import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

public class TestHandler extends StructureHandler {

    public static final String NAME = "Test";
    
    public static final String TAGNAME = "hiddenverse";
    
    
    public TestHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals(TAGNAME);
    }
    
    @Override
    public void start(PathElement p) {
        count++;
//        System.out.println(p.toPath());
        // TODO these are the words of Jesus, we should create a structure to track them.
    }
    
    @Override
    public void end(PathElement p) {
        System.out.println(p.getAttribute("n"));
    }
}