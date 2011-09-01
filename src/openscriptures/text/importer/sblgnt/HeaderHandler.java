/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import openscriptures.text.MutableWork;
import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.StructureHandler;
import openscriptures.utils.Language;

public class HeaderHandler extends StructureHandler {
        public HeaderHandler() {
        }
        
        public boolean matchesStart(PathElement p) {
            return p.toPath().matches("/osis/osisText/header");
        }
        
        public boolean matchesEnd(PathElement p) {
            return ctx.inHeader;
        }
        
        public void start(PathElement p) {
            ctx.inHeader = true;
        }
        
        public void end(PathElement p) {
            if (p.getName().equals("header")) {                // exit the header section
                ctx.inHeader = false;
            } else {
                processHeaderField(p);
            }
        }
        
        protected void processHeaderField(PathElement pElement) {
            String localName = pElement.getName();
            String value = pElement.getText();
            MutableWork work = ctx.work;
            
            if (localName.equals("title")) {
                work.setTitle(value);
                
            } else if (localName.equals("creator")) {
                work.setCreator(value);
                
            } else if (localName.equals("date")) {
                DateFormat fmt = new SimpleDateFormat("yyy-MM-dd");
                try {
                    work.setPublicationDate(fmt.parse(value));
                } catch (ParseException pe) {
                    SBLGNTImporter.LOGGER.warning("Could not parse publication date. " +
                            "Expected yyyy-MM-dd format: " + value + 
                            " (Work: " + work.getWorkId() + ")");
                }
                
            } else if (localName.equals("publisher")) {
                work.setPublisher(value);
                System.out.println("publisher: '" + value + "'");
                
            } else if (localName.equals("type")) {
                work.setType(value);
//                System.out.println("type: '" + value + "'");
                
            } else if (localName.equals("format")) {
                // not used.
                
            } else if (localName.equals("source")) {
                // not used.
                
            } else if (localName.equals("language")) {
                work.setLanguage(Language.lookup(value));
//                System.out.println("language: '" + value + "'");
                
            } else if (localName.equals("rights")) {
                work.setCopyright(value);
//                System.out.println("rights: '" + value + "'");
                
            } else if (localName.equals("scope")) {
                work.setScope(value);
//                System.out.println("scope: '" + value + "'");
                
            } else if (localName.equals("refSystem")) {
                work.setRefSystem(value);
//                System.out.println("refSystem: '" + value + "'");
            }
        }
    }