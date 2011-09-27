/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class FrontMatterHandler extends StructureHandler {
    public FrontMatterHandler() {
    }
    
    @Override
    public String getName() {
        return "FrontMatter";
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("div") && p.hasAttribute("div", "front");
    }
    
    public void start(PathElement p) {
        ctx.inFront = true;
        ctx.inText = false;
    }
    
    public void end(PathElement p) {
        ctx.inFront = false;
    }
}