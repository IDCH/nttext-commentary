/*
 * Created on Oct 19, 2004
 */
package org.idch.util.xml;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;

/**
 * This class provides an abstract super class for implementing XML based 
 * serialization. This is (for now TODO) seperate from the Java serialization
 * mechanisms.
 * 
 * @author Neal Audenaert
 */


public class XmlSerializer  {

    /**
     * 
     * @uml.property name="serializable"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private XmlSerializable serializable = null;

    public XmlSerializer(XmlSerializable obj) {
        serializable = obj;
    }

    /**
     * 
     * @uml.property name="serializable"
     */
    public XmlSerializable getSerializable() {
        return serializable;
    }

    
    /**
     * This is a convenience method to print this object to a specified file.
     * 
     * @param file The file to which to write the XML encoded state of this
     *      object.
     */
    public void write(File file) {
        org.w3c.dom.Document d = this.toXml();
        XMLUtil.print(d, file);
    }
    
    /**
     * Restores this object from an XML formated file. This requires that the 
     * file parameter refers to an xml file created from a document obtained 
     * via the <code>toXML()</code> method.
     * 
     * @param file The xml file from which the state of this object is to be 
     *      restored. 
     * @return True if the object could be restored, false otherwise.
     */
    public boolean restore(File file) {
        boolean success = false;
        try {
            DocumentBuilderFactory factory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new FileInputStream(file));
            Node node = doc.getDocumentElement();
            success = serializable.restore(node);
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
            success = false;
        }
        return success;
    }
    
    /**
     * Constructs an XML document from this proper name. The root element of the 
     * document is the node returned by <code>toXML(Document)</code>.
     * 
     * @return An XML formatted instance of this proper name as a W3C DOM 
     *      Document object.
     * @see ProperName.toXML(org.w3c.dom.Document doc) 
     */    
    public org.w3c.dom.Document toXml() {
        org.w3c.dom.Document doc = null;
        try {
            DocumentBuilderFactory factory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            Node pn = serializable.toXml(doc);
            doc.appendChild(pn);
        } catch (ParserConfigurationException pce) {
            System.err.println("ERROR: Could not create document builder. " +
                    "Method: csdl.neal.thesis.ProperName.toXML()");
            return null;
        }
        
        return doc;
    }
    

}
