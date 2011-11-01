/**
 * 
 */
package org.idch.bible.importers.hcsb;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.idch.texts.TextModule;
import org.idch.texts.TextModuleInstance;
import org.idch.texts.Work;
import org.idch.texts.importer.Context;
import org.idch.texts.importer.Importer;
import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Neal Audenaert
 */
public class HCSBImporter extends Importer {
    static final Logger LOGGER = Logger.getLogger(HCSBImporter.class);
    
    private long elapsedTime = 0;
    private Work work = null;
    
    public HCSBImporter(String filename, TextModule repo) {
        super(filename, new Context(repo));
    }
    
    protected void initWork() {
        // initialize the work object associated with this importer
    }
    
    //===================================================================================
    // OVERWRITTEN SUPERCLASS METHODS
    //===================================================================================
    
    //========================================================================================
    // MEMBER VARIABLES
    //========================================================================================

        /** The supplied name of the file to import. */
        

        private Map<String, Integer> tagCounts = new HashMap<String, Integer>();

        private Map<String, Integer> noteTagCounts = new HashMap<String, Integer>();
        
    //========================================================================================
    // CONSTRUCTORS
    //========================================================================================

        
    public void parseDirectory(File dir) throws SAXException, ParserConfigurationException, IOException {
        for (File file : dir.listFiles()) {
            String fname = file.getName();
            if (!fname.matches("^[456]\\d\\S+_HCSB\\.textonly\\.xml$"))
                continue;
            
            parseFile(file);
        }
    }
    
    public void parseFile(File file) throws SAXException, ParserConfigurationException, IOException {
        String url = convertToFileURL(file.getAbsolutePath());
        LOGGER.info("Importing XML file: " + url);
            
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
   
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(url);
            
            LOGGER.info("Finished importing: " + url);
    }
    
    
    /**
     * Starts the import process.
     *  
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void parse() throws ParserConfigurationException, SAXException, IOException {
        File dir = new File(this.filename);
        if (dir.isDirectory()) parseDirectory(dir);
        else parseFile(dir);
        
        SortedSet<String> keys = new TreeSet<String>();
        keys.addAll(tagCounts.keySet());
        System.out.println("Tags in use in this document: " + keys.size() + " diffent tags.");
        for (String name : keys) {
            System.out.printf("    %25s   %d\n", name, this.tagCounts.get(name));
        }
        
        System.out.println("Handled Tags: ");
        for (StructureHandler h : handlerChain) {
            System.out.printf("    %25s   %d\n", h.getName(), h.getCount());
        }
    }

    //========================================================================================
    // SAX Handlers
    //========================================================================================
        
        
    public void startDocument() throws SAXException {
    }
    
    
    public void startElement(
            String nsURI, String name, String qName, Attributes attrs)
    throws SAXException {
        PathElement el = this.path.push(nsURI, name, qName, attrs);
        
     // invoke appropriate handler
        boolean handled = false;
        for (StructureHandler h : handlerChain) {
            if (h.matchesStart(el)) {
                h.start(el);
                handled = true;
                break;
            }
        }
        
        
        if (!handled) {
            int ct = 0;
            if (el.toPath().equals("/book/blockindent/p")) {
                System.out.println("p: " + el.toPath());
                
                PathElement p = el;
                while ((p = p.getParent()) != null) {
                    if (p.getName().equals("book")) {
                        System.out.println("Book: " + p.getAttribute("id"));
                        break;
                    }
                }
            }
            if (this.tagCounts.containsKey(name)) {
                ct = this.tagCounts.get(name);
            }
            
            this.tagCounts.put(name, ++ct);
        }
        
    }
    
    
    public void endElement(String namespaceURI, String name, String qName)
    throws SAXException {
        PathElement el = this.path.pop();
        if (!el.getName().equals(name)) {
            LOGGER.error("Badly nested elements: Found '" + name + "' Expected '" + el + "'");
        }
        
        // invoke the appropriate end handler
        boolean handled = false;
        for (StructureHandler h : handlerChain) {
            if (h.matchesEnd(el)) {
                h.end(el);
                handled = true;
                break;
            }
        }
        
        if (!handled) {
            LOGGER.info("Unhandled end tag: " + this.path);
        }
    }

    
    public void characters(char[] ch, int start, int length) {
        // This allows us to retrieve the text of any arbitrary element or between two 
        // different elements. It collects all text in the source file, including both
        // textual and paratextual elements.
        if (!this.context.check("note"))
            path.characters(ch, start, length);
    }
    
    /**
     * Handle any final clean up once the end of the document is encountered.
     */
    public void endDocument() throws SAXException {
        
    }
    
    //===================================================================================
    // MAIN METHOD
    //===================================================================================
    
    public void doImport() throws Exception {
        long start = System.currentTimeMillis();
        try {
            // TODO this is going to need to loop through all files in the directory.
            
            this.addHandler(new NoteHandler());
            this.addHandler(new ParagraphHandler());
            this.addHandler(new VerseHandler());
            this.addHandler(new SectionHandler());
            this.addHandler(new ChapterHandler());
            this.addHandler(new BookHandler());
            this.addHandler(new BookHandler.LastUpdate());
            this.addHandler(new BookHandler.BookName());
            
            this.addHandler(new ParagraphHandler.BlockIndent());
            this.addHandler(new ParagraphHandler.DynamicProse());
            this.addHandler(new ParagraphHandler.OTDynamicProse());
            this.addHandler(new ParagraphHandler.OTQuote());
            this.addHandler(new ParagraphHandler.List());
            this.addHandler(new ParagraphHandler.Item());
            
            this.addHandler(new DisputedHandler());
            this.addHandler(new FormatHandlers.RedLetters());
            this.addHandler(new FormatHandlers.Italics());
            this.addHandler(new FormatHandlers.Box());
            this.addHandler(new FormatHandlers.LineBreaks());
            this.addHandler(new Transliteration());
            
            this.addHandler(new PhraseSpaceHandler());
            this.addHandler(new SupHandler());
            
            this.addHandler(new TestHandler());
            
            this.parse();
            
        } catch (Exception ex) {
            LOGGER.error("Failed to import HCSB: " + this.filename, ex);
            throw ex;
        } finally {
            this.elapsedTime = System.currentTimeMillis() - start;
            LOGGER.info("Elapsed Time: " + this.elapsedTime);
        }
        
    }
    
    public long getElapsedTime() {
        return this.elapsedTime;
    }
    
    public Work getWork() {
        return this.work;
    }

    //===================================================================================
    // MAIN METHOD
    //===================================================================================
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        //      String filename = "SBLGNT.osis.xml";
//        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\HCSB\\source\\50-Phil_HCSB.textonly.xml";
        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\HCSB\\source";
        
        HCSBImporter importer;
        try {
            importer = new HCSBImporter(filename, TextModuleInstance.get());
            importer.doImport();

            System.out.println();
            System.out.println();
            System.out.println("Elapsed Time: " + importer.getElapsedTime());
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
      
    }
}