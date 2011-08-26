/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import openscriptures.text.MutableWork;
import openscriptures.text.WorkId;
import openscriptures.text.impl.mem.MemWork;
import openscriptures.utils.Language;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Neal Audenaert
 */
public class SBLGNTImporter extends DefaultHandler {
    private static final Logger LOGGER = Logger.getLogger(SBLGNTImporter.class.getName());


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

    // TODO better to stack an object with params for 
    private ProcessingPath path = new ProcessingPath();

    private MutableWork work = null;
    private Delegate delegate;
    
//    private StringBuilder elementContent = new StringBuilder();


//========================================================================================
// CONSTRUCTORS
//========================================================================================

    public SBLGNTImporter() {
    }

//========================================================================================
// DOM MANAGMENT METHODS
//========================================================================================
      
//    public boolean pathMatches(String test) {
//        String[] parts = test.split("/");
//        
//        if (!path.peek().equals(parts[parts.length - 1]))
//            return false;
//        
//        boolean matched = false;
//        int ix = this.path.size() - 1;
//        for (int i = parts.length - 2; i >= 0; i--) {
//            String part = parts[i];
//            if (!this.path.get(--ix).equals(part)) {
//                matched = false;
//                break;
//            }
//            
//            matched = true;
//        }
//        
//        return matched;
//    }
//    
//    public String getCurrentPath() {
//        StringBuilder sb = new StringBuilder();
//        for (String s : path) {
//            sb.append("/").append(s);
//        }
//        
//        return sb.toString();
//    }
//    
//    public String getCurrentElement() {
//        return this.path.peek();
//    }
    
//========================================================================================
// SAX Handlers
//========================================================================================
    
    private Set<String> paths = new HashSet<String>();
//    private boolean inBook = false;
    
    public void startDocument() throws SAXException {
        
    }
    
    private boolean inText = false;
    private boolean inBook = false;
    private boolean inChapter = false;
    private boolean inVerse = false;
    
    private boolean inParagraph = false;
    private boolean inWord = false;
    
    private boolean inHeader = false;
    private boolean foundHeader = false;
    
    private String currentBook = null;
    private int currentVerse = 0;
    private int currentChapter = 0;
    
    /**
     * 
     */
    public void startElement(
            String nsURI, String name, String qName, Attributes attrs)
    throws SAXException {
        
        PathElement el = this.path.push(nsURI, name, qName, attrs);
        if (inHeader) {
            // do nothing (we'll process these at the end tag
            
        } else if (name.equals("w")) {
            this.inWord = true;
            
        } else if (name.equals("verse")) {
            // TODO get the verse number
            if (el.hasAttribute("osisID")) {
                this.inVerse = true;
//                System.out.print("Starting: " + el.getAttribute("osisID"));
                
            } else if (el.hasAttribute("eID")) {
                this.inVerse = false;                
//                System.out.println(" ... Ending: " + el.getAttribute("eID"));
            }
            
        } else if (name.equals("p")) {
            
        } else if (name.equals("chapter")) {
            
        } else if (name.equals("div") && el.hasAttribute("type", "book")) {
            // parsing book div
            startBook(el);
            
        } else if (name.equals("osisText") && this.work == null) {
            // create an instance of the work.
            // TODO Need to migrate to JPA backed work.
            String osisIDWork = attrs.getValue(ATTR_OSIS_ID_WORK);
            this.work = new MemWork(new WorkId(osisIDWork));
            LOGGER.info("Ingesting new work: " + this.work.getWorkId());
            
        } else if (!this.foundHeader && path.matches("/osis/osisText/header")) {
            this.inHeader = true;
            this.foundHeader = true;
            
        } else {
            System.out.println(path);
        }
    }
    
    
    public void endElement(String namespaceURI, String name, String qName)
    throws SAXException {
        PathElement pElement = this.path.pop();
        if (!pElement.getName().equals(name)) {
            System.err.println("Badly nested elements.\n" +
                    "  Found: " + name + "\n" +
                    "  Expected: " + pElement);
            
        }
        
        if (this.inHeader) {
            if (name.equals("header")) {               // exit the header section
                this.inHeader = false;
            } else {
                processHeaderField(pElement);
            }
            
        } else if (name.equals("w")) {
            this.inWord = false;
            
        } else if (name.equals("verse")) {
            
        } else if (name.equals("p")) {
            this.inParagraph = false;
            
        } else if (name.equals("chapter")) {
            this.inChapter = false;
            
        } else if (name.equals("div") && pElement.hasAttribute("type", "book")) {
            // parsing book div
            endBook(pElement);
        }
    }

    public void characters(char[] ch, int start, int length) {
        path.characters(ch, start, length);
    }
    
    public void endDocument() throws SAXException {
         for (String path : paths) {
             System.out.println(path);
         }
    }
    
//========================================================================================
// UTILITY METHODS
//========================================================================================
  
    protected void createStructure(PathElement p) {
        
    }
    
    protected void closeStructure(PathElement p) {
        
    }
    
    /**
     * 
     * @param localName
     * @param value
     */
    protected void processHeaderField(PathElement pElement) {
        String localName = pElement.getName();
        String value = pElement.getText();
        
        if (localName.equals("title")) {
            work.setTitle(value);
            
        } else if (localName.equals("creator")) {
            work.setCreator(value);
            
        } else if (localName.equals("date")) {
            DateFormat fmt = new SimpleDateFormat("yyy-MM-dd");
            try {
                work.setPublicationDate(fmt.parse(value));
            } catch (ParseException pe) {
                LOGGER.warning("Could not parse publication date. " +
                        "Expected yyyy-MM-dd format: " + value + 
                        " (Work: " + work.getWorkId() + ")");
            }
            
        } else if (localName.equals("publisher")) {
            work.setPublisher(value);
//            System.out.println("publisher: '" + value + "'");
            
        } else if (localName.equals("type")) {
            work.setType(value);
//            System.out.println("type: '" + value + "'");
            
        } else if (localName.equals("format")) {
            // not used.
            
        } else if (localName.equals("source")) {
            // not used.
            
        } else if (localName.equals("language")) {
            work.setLanguage(Language.lookup(value));
//            System.out.println("language: '" + value + "'");
            
        } else if (localName.equals("rights")) {
            work.setCopyright(value);
//            System.out.println("rights: '" + value + "'");
            
        } else if (localName.equals("scope")) {
            work.setScope(value);
//            System.out.println("scope: '" + value + "'");
            
        } else if (localName.equals("refSystem")) {
            work.setRefSystem(value);
//            System.out.println("refSystem: '" + value + "'");
        }
    }

    protected void startBook(PathElement p) {
        this.inBook = true;
        this.inText = true;
        
        this.currentChapter = 0;
        this.currentVerse = 0;
        
        String osisId = p.getAttribute("osisID");
        this.currentBook = osisId;
        
        this.createStructure(p);
    }
    
    protected void endBook(PathElement p) {
        this.inBook = false;
        this.inText = false;
        
        this.currentChapter = 0;
        this.currentVerse = 0;
        
        this.currentBook = null;
        
        this.closeStructure(p);
    }
    
    //========================================================================================
    // XML PATH MANAGEMENT
    //========================================================================================
    
    public static class PathElement {
        private final String nsURI;
        private final String name;
        private final String qName;
        private final Map<String, String> attribs;
        
        private final PathElement parent;
        private final StringBuilder sb;
        
        private int start;
        private int end = -1;
        
        PathElement(PathElement parent, StringBuilder sb,
                String nsURI, String name, String qName, Attributes attrs) {
            this.parent = parent;
            
            this.nsURI = nsURI;
            this.name = name;
            this.qName = qName;
            
            // initialize string builder
            this.sb = sb;
            this.start = sb.length();

            // load attributes
            int len = attrs.getLength();
            if (len > 0) {
                attribs = new HashMap<String, String>();
                for (int ix = 0; ix < attrs.getLength(); ix++) {
                    attribs.put(attrs.getQName(ix), attrs.getValue(ix));
                }
                
            } else {
                attribs = null;
            }
        }
        
        public String getText() {
//            System.out.println(sb.length() + "/" + start + "/" + (end - start));
            return (end < 0)
                ? sb.substring(start)
                : sb.substring(start, end);
        }
        
        public String getNamespaceURI() {
            return nsURI;
        }
        
        public String getName() {
            return name;
        }
        
        public String getQName() {
            return qName;
        }
        
        public Map<String, String> getAttributes() {
            return Collections.unmodifiableMap(this.attribs);
        }
        
        public String getAttribute(String name) {
            return this.attribs.get(name);
        }
        
        public boolean hasAttribute(String name) {
            return this.attribs.containsKey(name);
        }
        
        public boolean hasAttribute(String name, String regex) {
            String v = this.attribs.get(name);
            return (v == null) ? false : v.matches(regex);
        }
                
        public String toPath() {
            StringBuilder sb = new StringBuilder();
            return (parent == null) 
                ? sb.append("/").append(name) .toString()
                : sb.append(parent.toPath()).append("/").append(name).toString(); 
        }
        
        public String toString() {
            return this.name;
        }
    }
    
    public static class ProcessingPath {
        Stack<PathElement> path = new Stack<PathElement>();
        private final StringBuilder sb = new StringBuilder();
        
        public boolean matches(String test) {
            if (path.size() == 0)
                return false;
            
            int ix = test.indexOf('/');
            if (ix < 0) {
                return this.path.peek().name.equals(test);
            } else if (ix == 0) {
                return this.toString().startsWith(test);
            } else if (ix > 0) {
                return this.toString().endsWith(test);
            } else {
                assert false : "unreachable code";
                return false;
            }
        }
        
        public PathElement push(
                String nsURI, String name, String qName, Attributes attrs) {
            PathElement parent = (this.path.size() > 0) ? this.path.peek() : null;
            PathElement e = new PathElement(parent, sb, 
                    nsURI, name, qName, attrs);
            
            this.path.push(e);
            
            return e;
        }
        
        public PathElement peek() {
            return this.path.peek();
        }
        
        public PathElement pop() {
            PathElement e = this.path.pop();
            e.end = sb.length();
            
            return e;
        }
        
        public void characters(char[] ch, int start, int length) {
            sb.append(ch, start, length);
        }
        
        
        public int size() {
            return this.path.size();
        }
        
        public String toString() {
            if (this.path.size() > 0)
                return this.path.peek().toPath();
            else 
                return "";
        }
    }
    
    //========================================================================================
    // DELEGATE INTERFACE
    //========================================================================================
    private static interface Delegate { 
        String getRootElementName();

        void startElement(String localName, Attributes attrs) throws SAXException;

        /**
         * Handles end elements.
         *  
         * @param localName
         * @return <code>false</code> once the delegate has processed all content that 
         *      it knows about. 
         * @throws SAXException
         */
        void endElement(String localName) throws SAXException;

        void characters(char[] ch, int start, int length);
    }

    //========================================================================================
    // DELEGATE CLASSES
    //========================================================================================
    public static abstract class ElementHandler {
        protected final MutableWork work;
        protected final String name; 
        protected final Stack<String> path;    // DON'T TOUCH THE PATH
        
        public ElementHandler(MutableWork work, Stack<String> path, 
                String localName, Attributes attrs) {
            this.work = work;
            this.path = path;
            this.name = localName;
        }
        
        public abstract void characters(char[] ch, int start, int length);
        
        public abstract void close();
    }
    
    public static class TitleHandler extends ElementHandler {
        StringBuilder sb = new StringBuilder();
        
        public TitleHandler(MutableWork work, Stack<String> path, 
                String localName, Attributes attrs) {
            super(work, path, localName, attrs);
        }

        /* (non-Javadoc)
         * @see openscriptures.text.importer.sblgnt.SBLGNTImporter.ElementHandler#characters(char[], int, int)
         */
        @Override
        public void characters(char[] ch, int start, int length) {
            sb.append(ch, start, length);
        }

        /* (non-Javadoc)
         * @see openscriptures.text.importer.sblgnt.SBLGNTImporter.ElementHandler#close()
         */
        @Override
        public void close() {
            // TODO Auto-generated method stub
            
        }
    }
    
    private static class WorkDelegate implements Delegate {
        
        public static final String EL_TITLE = "title";
        public static final String EL_CREATOR = "creator";
        public static final String EL_DATE = "date";
        public static final String EL_PUBLISHER = "publisher";
        public static final String EL_TYPE = "type";
        public static final String EL_FORMAT = "format";
        public static final String EL_SOURCE = "source";
        public static final String EL_LANGUAGE = "language";
        public static final String EL_RIGHTS = "rights";
        public static final String EL_SCOPE = "scope";
        public static final String EL_REF_SYSTEM = "refSystem";

        private String rootElement;
        private String currentElement;
        private StringBuilder sb = null;

        private MutableWork work;
        
        public WorkDelegate(MutableWork work, String localName, Attributes attrs) {
            this.work = work;
            rootElement = localName;
        }

        public String getRootElementName() {
            return rootElement;
        }

        private Map<String, String> data = new HashMap<String, String>();

        /**
         * 
         */
        public void startElement(String localName, Attributes attrs) throws SAXException {
            this.currentElement = localName;
            this.sb = new StringBuilder();
        }

        private boolean assignValue(String key, String value) {
            if (key.equals(EL_TITLE)) {
                work.setTitle(value);
            } else if (key.equals(EL_CREATOR)) {
                work.setCreator(value);
            } else if (key.equals(EL_DATE)) {
//                DateFormatter df = new DateFormatter();
//                work.setPublicationDate(value);
            } else if (key.equals(EL_PUBLISHER)) {
                work.setPublisher(value);
            } else if (key.equals(EL_TYPE)) {
                work.setType(value);
            } else if (key.equals(EL_FORMAT)) {
//                work.setFormat(value);
            } else if (key.equals(EL_SOURCE)) {
//                work.setSource(value);
            } else if (key.equals(EL_LANGUAGE)) {
                work.setLanguage(Language.lookup(value));
            } else if (key.equals(EL_RIGHTS)) {
                work.setCopyright(value);
            } else if (key.equals(EL_SCOPE)) {
                work.setScope(value);
            } else if (key.equals(EL_REF_SYSTEM)) {
                work.setRefSystem(value);
            } else {
                return false;
            }
                
            return true;
        }
        
        /**
         * @return 
         * 
         */
        public void endElement(String localName) throws SAXException {
            if (localName.equals(currentElement)) {
                data.put(currentElement, sb.toString());
                this.assignValue(currentElement, sb.toString());
                sb = null;
            } else {
                System.err.println("Unexpected end token: " + localName);
            }
        }

        /**
         * 
         */
        public void characters(char[] ch, int start, int length)  {
//            sb.append(ch, start, length);
        }
    }

    public static void main(String[] args) {
        //		String filename = "SBLGNT.osis.xml";
        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\SBLGNT\\source\\SBLGNT.osis.xml";

        long start = System.currentTimeMillis();
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();

            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new SBLGNTImporter());
            xmlReader.parse(convertToFileURL(filename));
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }

        System.out.println("Elapsed Time: " + (System.currentTimeMillis() - start));
    }

}