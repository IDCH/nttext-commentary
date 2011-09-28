/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;

public class BookTitleHandler extends StructureHandler {
        public static final String NAME = "BookTitle";
        
        public BookTitleHandler() {
            super(NAME);
        }
        
        public boolean matchesStart(PathElement p) {
            StructureHandler bookHandler = ctx.getHandler(BookHandler.NAME);
            
            return (bookHandler != null) && p.getName().equals("title");
        }
        
        public void start(PathElement p) {
            ctx.notInText();
        }
        
        public void end(PathElement p) {
            ctx.inText();
            
            // TODO need to handle Mark weirdness
//            Other Endings of Mark
//            Intermediate Ending
//            Long Ending
            
//            System.out.println(p.getText());
        }
    }