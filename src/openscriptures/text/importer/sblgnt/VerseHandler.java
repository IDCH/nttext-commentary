/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class VerseHandler extends StructureHandler {
    public static final String VERSE = "verse";
    
    private String vs;
    
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("verse");
    }
    
    @Override
    public String getName() {
        return "BibleVerse";
    }
    
    public void start(PathElement p) {
        if (p.hasAttribute("osisID")) {         // starting verse milestone marker
            this.vs = p.getAttribute("osisID");
            ctx.flag(VERSE, true);
            
        } else if (p.hasAttribute("eID")) {     // ending verse milestone marker
            String vs = p.getAttribute("eID");
            assert vs.equals(this.vs) : 
                "Verse Mismatch. Expected " + this.vs + " but found " + vs;
            
            ctx.flag(VERSE, false);
        }
    }
    
    public void end(PathElement p) {
    }
}