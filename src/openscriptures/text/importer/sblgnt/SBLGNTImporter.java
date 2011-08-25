/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import openscriptures.text.MutableWork;
import openscriptures.utils.Language;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Neal Audenaert
 */
public class SBLGNTImporter extends DefaultHandler {

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

    private Map<String, Integer> tags;

    public void startDocument() throws SAXException {
        tags = new HashMap<String, Integer>();
    }

    //========================================================================================
    // Main SAX Handlers
    //========================================================================================

    public static final String ATTR_OSIS_REF_WORK = "osisRefWork";
    public static final String ATTR_LANG = "lang";
    public static final String ATTR_OSIS_ID_WORK = "osisIDWork";

    public static final String EL_OSIS_TEXT = "osisText";
    public static final String EL_HEADER = "header";

    public static final String EL_WORK = "work";

    private MutableWork work;
    private Delegate delegate;

    /**
     * 
     */
    public void startElement(
            String namespaceURI, String localName, String qName, Attributes attrs)
    throws SAXException {
        if (this.delegate != null) { 
            this.delegate.startElement(localName, attrs);
        }
        
        if (localName.equals(EL_OSIS_TEXT)) {
            for (int i = 0; i < attrs.getLength(); i++) {
                // here we have attrs 'osisRefWork', 'lang', 'osisIDWork'

                System.out.println(attrs.getLocalName(i) + " = " + attrs.getValue(i));
            }
        } else if (localName.equals(EL_WORK)) {
            this.delegate = new WorkDelegate(this.work, localName, attrs);
            
            // just forward the request
        } 
    }

    private void handleDelegateResponse(Delegate delegate) {
        
        
    }
    
    public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException {
        if (this.delegate != null) { 
            if (localName.equals(delegate.getRootElementName())) {
                this.handleDelegateResponse(delegate);
                this.delegate = null;
            } else this.delegate.endElement(localName);
            
            return;
        }
        
       
    }

    public void characters(char[] ch, int start, int length) {
        if (this.delegate != null) { 
            this.delegate.characters(ch, start, length);
        }
    }
    
    public void endDocument() throws SAXException {
        for (String tag : tags.keySet()) {
            int count = tags.get(tag);
            System.out.println("Local Name \"" + tag + "\" occurs " + count + " times");
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
    // Main SAX Handlers
    //========================================================================================
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
            sb.append(ch, start, length);
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