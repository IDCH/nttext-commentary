/* Created on       Aug 29, 2007
 * Last Modified on $Date: 2008-07-17 19:08:27 $
 * $Revision: 1.1 $
 * $Log: XmlDocument.java,v $
 * Revision 1.1  2008-07-17 19:08:27  neal
 * Reattached NADL Project to a CVS Repository. This time the HTML, JS, and other webcomponents are being backed up as well.
 *
 * Revision 1.2  2007-11-24 23:31:32  neal
 * Added functionality to retrieve the textual contents of a node and to retrived elements from a document based on their element name.
 *
 * Revision 1.1  2007-11-08 15:39:20  neal
 * Creating a general project to provide a consistent codebase for NADL. This is being expanded to include most of the components from the old CSDLCommon and CSDLWeb packages, as I reorganize the package structure and improve those components.
 *
 */
package org.idch.util.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlDocument {
    private XmlElement root = null;
    
    public XmlDocument(String root) {
        this.root = new XmlElement(this, root); 
    }
    
    public XmlElement getRoot() { return this.root; }
    
    public boolean hasNamespace(String ns) {
        return this.root.hasNamespace(ns);
    }
    
    public void addNamespace(String ns, String uri) {
        this.root.setNamespace(ns, uri);
    }
    
    /** 
     * Returns the first <tt>XmlElement</tt> having the specified name (using 
     * a depth first search of the tree).
     * 
     * @param name The name of the element to be retrieved.
     * @return
     */
    public XmlElement getElement(String name) {
        // IMPLEMENT
        return null;
    }
    
    /**
     * Returns a list containing all elements from this document having the 
     * specified element name.
     * 
     * @param name The name of the element to be retrieved.
     * @return
     */
    public List<XmlElement> getAllElements(String name) {
        List<XmlElement> elements = new ArrayList<XmlElement>();
        getAllElements(this.root, name, elements);
        return elements;
    }
    
    /** Recursive helper method for get all elements. */
    private void getAllElements(XmlElement xml, String name, 
            List<XmlElement> results) {
        List<XmlElement> children = xml.getChildren();
        if (children == null) return;
        
        for (XmlElement child : children) {
            if (child.getName().equals(name)) results.add(child);
            
            getAllElements(child, name, results);
        }
    }
    
    /**
     * Returns a list containing all elements have the specified attribute
     * value pair. The provided value can be any regular expression.
     * 
     * @param attr
     * @param value
     * @return
     */
    public List<XmlElement> getElementsHaving(String attr, String value) {
//      IMPLEMENT
        return null;
    }
    
    /**
     * Returns a list containing all elements that match ALL of the attributes
     * specified by the provided <tt>Map</tt>. 
     * 
     * @param attributes A <tt>Map</tt> between attribute names and required 
     *      attribute values. The values may be any valid reqular expression.
     * @return
     */
    public List<XmlElement> getElementsHavingAll(Map<String, String> attributes) {
//      IMPLEMENT
        return null;
    }
    
    /**
     * Returns a list containing all elements that match ANY of the attributes
     * specified by the provided <tt>Map</tt>. 
     * 
     * @param attributes A <tt>Map</tt> between attribute names and attribute 
     *      values. The values may be any valid reqular expression.
     * @return
     */
    public List<XmlElement> getElementsHavingAny(Map<String, String> attributes) {
//      IMPLEMENT
        return null;
    }
    
    /**
     * Returns a <tt>String</tt> based representation of this XML document.
     */
    @Override
    public String toString() {
        String xml = "<?xml version=\"1.0\" ?>\n\n";
        xml += this.root.toString();
        
        return xml;
    }
    

}
