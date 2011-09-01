/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class BookHandler extends StructureHandler {
    
    public static final String BOOK = "book";
        public BookHandler() {
        }
        
        public boolean matchesStart(PathElement p) {
            return p.getName().equals("div") && p.hasAttribute("type", "book");
        }
        
        public void start(PathElement p) {
            ctx.flag(BOOK, true);
            ctx.inText = true;
            
//            ctx.currentChapter = 0;
//            ctx.currentVerse = 0;
            
//            String osisId = p.getAttribute("osisID");
//            ctx.currentBook = osisId;
            
//            this.createStructure(p);
        }
        
        public void end(PathElement p) {
         // close any open paragraph, chapter, verse structures 
            ctx.flag(BOOK, false);
            ctx.inText = false;
            
//            ctx.currentChapter = 0;
//            ctx.currentVerse = 0;
            
//            ctx.currentBook = null;
            
//            this.closeStructure(p);
        }
    }