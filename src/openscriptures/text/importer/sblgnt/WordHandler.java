/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class WordHandler extends StructureHandler {
    public static final String WORD = "w";
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("w");
    }
    
    public void start(PathElement p) {
        ctx.flag(WORD, true);
    }
    
    public void end(PathElement p) {
        ctx.flag(WORD, false);
    }
}