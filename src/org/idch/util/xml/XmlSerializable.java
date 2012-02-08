/*
 * Created on Oct 29, 2004
 */
package org.idch.util.xml;

import org.w3c.dom.Node;

/**
 * @author Neal Audenaert
 */
public interface XmlSerializable {
    /**
     * Classes must implement this method to return an XML formatted  
     * instance of this object as a W3C DOM Node using the provided Document
     * object. 
     * 
     * @param doc The <code>Document</code> object to be used to create 
     *      the elements of the XML document. 
     * @return A <code>Node</code> containing the XML formatted contents 
     *      of this proper name.
     */
    public abstract Node toXml(org.w3c.dom.Document doc);
    
    /**
     * Classes must implement this method to read an XML node of the format
     * created by the <code>toXML(org.w3c.dom.Document doc)</code> and restore 
     * the state of this object to that of the object that create the XML node.
     * 
     * @param node The XML node from which this object will restore itself.
     * @return True if the object could be restored, false otherwise.
     */
    public abstract boolean restore(org.w3c.dom.Node node) 
        throws Exception;
}
