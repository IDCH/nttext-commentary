/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class ParagraphHandler extends StructureHandler {
    public static final String NAME = "Paragraph";
    
    public static String PARAGRAPH = "p";

    
    public ParagraphHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("p");
        
    }
    
    public void start(PathElement p) {
        ctx.flag(PARAGRAPH, true);
    }
    
    public void end(PathElement p) {
        ctx.flag(PARAGRAPH, false);
    }
}