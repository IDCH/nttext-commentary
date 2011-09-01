/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class ChapterHandler extends StructureHandler {
    public static final String CHAPTER = "chapter";
    public ChapterHandler() {
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals("chapter");
    }
    
    public void start(PathElement p) {
        ctx.flag(CHAPTER, true);
    }
    
    public void end(PathElement p) {
        assert ctx.check(CHAPTER) : "Cannot end chapter. Not in a chapter.";

        ctx.flag(CHAPTER, false);
    }
}