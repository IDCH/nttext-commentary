/* Created on Apr 19, 2006 */
package org.idch.util.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class XMLException extends RuntimeException {
    public final static String PARSER = ParserConfigurationException.class.getName();
    public final static String SAX    = SAXException.class.getName();
    public final static String IO     = IOException.class.getName();
    
    private Exception ex   = null;
    private String    type = "unknown";
    public XMLException(ParserConfigurationException ex) {
        this.ex = ex;
        this.type = PARSER;
    }
    
    public XMLException(SAXException ex) {
        this.ex = ex;
        this.type = SAX;
    }
    
    public XMLException(IOException ex) {
        this.ex = ex;
        this.type = IO;
    }
    
    public Exception getException() { return this.ex; }
    
    public String getType() { return this.type; }
}
