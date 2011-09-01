/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class BookTitleHandler extends StructureHandler {
        public BookTitleHandler() {
        }
        
        public boolean matchesStart(PathElement p) {
            return ctx.check(BookHandler.BOOK) && p.getName().equals("title");
        }
        
        public void start(PathElement p) {
            ctx.inText = false;        // titles are paratextual information
        }
        
        public void end(PathElement p) {
            ctx.inText = true;
            
            // TODO need to handle Mark weirdness
//            Other Endings of Mark
//            Intermediate Ending
//            Long Ending
            
//            System.out.println(p.getText());
        }
    }