/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import openscriptures.text.MutableWork;
import openscriptures.text.Token;
import openscriptures.text.Work;
import openscriptures.text.WorkId;
import openscriptures.text.impl.BasicToken;
import openscriptures.text.impl.mem.MemWork;
import openscriptures.text.importer.Context;
import openscriptures.text.importer.PathElement;
import openscriptures.text.importer.ProcessingPath;
import openscriptures.text.importer.StructureHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Neal Audenaert
 */
public class SBLGNTImporter extends DefaultHandler {
    static final Logger LOGGER = Logger.getLogger(SBLGNTImporter.class.getName());

    public static final String ATTR_OSIS_REF_WORK = "osisRefWork";
    public static final String ATTR_LANG = "lang";
    public static final String ATTR_OSIS_ID_WORK = "osisIDWork";

    public static final String EL_OSIS_TEXT = "osisText";
    public static final String EL_HEADER = "header";

    public static final String EL_WORK = "work";
    
    /**
     * TODO move to org.idch.utils.Filenames
     * @param filename
     * @return
     */
    public static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
    
//========================================================================================
// MEMBER VARIABLES
//========================================================================================

    private ProcessingPath path = new ProcessingPath();

    private MutableWork work = null;
    

    String lastToken = null;
    boolean lastTokenWasWhitespace = false;
    
    Set<String> badTokens = new HashSet<String>();
    
    Context context = new Context();
    
    List<StructureHandler> structureHandlers = new ArrayList<StructureHandler>();
    

    String filename;
//========================================================================================
// CONSTRUCTORS
//========================================================================================

    public SBLGNTImporter(String filename) {
        this.filename = filename;
    }
    
    public void parse() throws ParserConfigurationException, SAXException, IOException {
        String url = convertToFileURL(filename);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);

        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);
        xmlReader.parse(url);
    }

    public Work getWork() {
        return this.work;
    }

    public void addHandler(StructureHandler h) { 
        h.setContext(this.context);
        this.structureHandlers.add(h);
    }
    
//========================================================================================
// SAX Handlers
//========================================================================================
    
//    private boolean inBook = false;
    
    public void startDocument() throws SAXException {
        
    }
    
   
    /**
     * 
     */
    public void startElement(
            String nsURI, String name, String qName, Attributes attrs)
    throws SAXException {
        
        boolean handled = false;
        
        PathElement el = this.path.push(nsURI, name, qName, attrs);
        if (context.inHeader || context.inFront) {
            // do nothing (we'll process these at the end tag)
            return;
        }

        // invoke appropriate handler
        for (StructureHandler h : structureHandlers) {
            if (h.matchesStart(el)) {
                h.start(el);
                handled = true;
                break;
            }
        }
        
        if (!handled) {
            //===============
            // PROCESS META INFO ABOUT THE WORK
            //===============    
            if (name.equals("osisText") && this.work == null) {
                // create an instance of the work.
                // TODO Need to migrate to JPA backed work.
                String osisIDWork = attrs.getValue(ATTR_OSIS_ID_WORK);
                this.work = new MemWork(new WorkId(osisIDWork));
                this.context.work = this.work;

                LOGGER.info("Ingesting new work: " + this.work.getWorkId());

            } else {
                //            System.out.println(path);
            }
        }
    }
    
    
    /**
     * 
     */
    public void endElement(String namespaceURI, String name, String qName)
    throws SAXException {
        PathElement el = this.path.pop();
        if (!el.getName().equals(name)) {
            System.err.println("Badly nested elements.\n" +
                    "  Found: " + name + "\n" +
                    "  Expected: " + el);
            
        }
        
        // invoke the appropriate end handler
        for (StructureHandler h : structureHandlers) {
            if (h.matchesEnd(el)) {
                h.end(el);
                break;
            }
        }
    }

    
    public void characters(char[] ch, int start, int length) {
        // This allows us to retrieve the text of any arbitrary element or between two 
        // different elements. It collects all text in the source file, including both
        // textual and paratextual elements.
        path.characters(ch, start, length);
        
        if (context.inText) {
            String text = new String(ch, start, length);
            
            Matcher mat = Pattern.compile(BasicToken.TOKENIZATION_PATTERN).matcher(text);
            while (mat.find()) {
                String token = mat.group();
                Token.Type type = BasicToken.classify(token); 
                if (type == null) {
                    badTokens.add(token);
                    continue;
                } else if (type == Token.Type.WHITESPACE) {
                    // normalize whitespace.
                    if (lastTokenWasWhitespace) {
                        // ignore this one
                    } else { 
                        lastTokenWasWhitespace = true;
                        if (work.size() > 0) {
                            // don't start with whitespace
                            work.addToken(" ");
                        }
                    }
                } else {
                    lastTokenWasWhitespace = false;
                    work.addToken(token);
                }
            }
        } 
    }
    
    public void endDocument() throws SAXException {
//         for (String token : badTokens) {
             // We need to extract these TC symbols from the text and add them 
             //    as editorial markup.
             // SEE http://unicode.org/charts/nameslist/n_2E00.html
             // U2E00 - Right_Angle_Substitution_Marker
             // U2E01 - Right_Angle_Dotted_Substitution_Marker
             // U2E02 - Left_Substitution_Bracket
             // U2E03 - Right_Substitution_Bracket
             // U2E04 - Left_Dotted_Substitution_Bracket
             // U2E05 - Right_Dotted_Substitution_Bracket
             
//             for (int i = 0; i < token.length(); i++) {
//                 char ch = token.charAt(i);
//                 String hex = Integer.toHexString(ch | 0x10000).substring(1);
//                 System.out.println(ch + " :: " + "\\u" +  hex);
//             }
//             if (ch == 0x2E00)
//             System.out.println(token + " :: " + value);
//         }
    }
    
//========================================================================================
// UTILITY METHODS
//========================================================================================
  
    
    
    public static void main(String[] args) {
        //		String filename = "SBLGNT.osis.xml";
        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\SBLGNT\\source\\SBLGNT.osis.xml";

        long start = System.currentTimeMillis();
        try {
            SBLGNTImporter importer = new SBLGNTImporter(filename);
            
            importer.addHandler(new HeaderHandler());
            importer.addHandler(new FrontMatterHandler());
            importer.addHandler(new WordHandler());
            importer.addHandler(new ParagraphHandler());
            importer.addHandler(new ChapterHandler());
            importer.addHandler(new BookHandler());
            importer.addHandler(new BookTitleHandler());
            
            importer.parse();
            
            Work work = importer.getWork();
            
            Iterator<Token> i = work.iterator();
            int ix = 0;
            while (i.hasNext() && ix++ < 1000) {
                System.out.print(i.next());
            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        System.out.println();
        System.out.println();
        System.out.println("Elapsed Time: " + (System.currentTimeMillis() - start));

    }

}