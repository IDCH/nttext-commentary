/* Created on       Aug 27, 2007
 * Last Modified on $Date: 2008-12-19 22:54:41 $
 * $Revision: 1.2 $
 * $Log: XmlElement.java,v $
 * Revision 1.2  2008-12-19 22:54:41  neal
 * Restructuring and cleaning up code base. Notably, removed the HashMap data interchange from the Execution class.
 *
 * Revision 1.1  2008-07-17 19:08:27  neal
 * Reattached NADL Project to a CVS Repository. This time the HTML, JS, and other webcomponents are being backed up as well.
 *
 * Revision 1.4  2008-04-11 20:40:58  neal
 * minor modifications to remove warnings.
 *
 * Revision 1.3  2007-12-19 23:42:07  neal
 * Added methods to retrieve child elements and to allow an element to adopt a child that was created in another document.
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


public class XmlElement {
    private static final int ATTR_NAME      = 0;
    private static final int ATTR_VALUE     = 1;
    private static final int NS_NAME        = 0;
    private static final int NS_URI         = 1;
    private static final int MAX_ATTRIBUTES = 15;
    private static final int MAX_NAMESPACES = 30;
    
    private static final String TEXT_NODE_NAME = "TEXT_NODE";
    
    
    static final String INDENT_STRING = "  ";
    
    
    private XmlDocument document = null;
    private XmlElement  parent   = null;
    private int         nested   = 0;
    private String      name     = "";
    
    // NOTE we can only handle 15 attributes
    private String[][]       attributes = new String[MAX_ATTRIBUTES][2];
    private String[][]       namespaces = new String[MAX_NAMESPACES][2];
    
    private int              ctAttr     = 0;
    private int              ctNS       = 0;
    
    private List<XmlElement> children   = new ArrayList<XmlElement>();
    
    private XmlElement(XmlElement element, String name) {
        assert !name.equals(TEXT_NODE_NAME) : 
            "Name may not be '" + TEXT_NODE_NAME + "'";
        // TODO test and throw exception
        
        this.parent   = element;
        this.document = element.getDocument();
        this.name     = name;
        this.nested   = element.nested + 1;
    }
    
    protected XmlElement(XmlElement element) {
        this.parent   = element;
        this.document = element.getDocument();
        this.name     = TEXT_NODE_NAME;
        this.nested   = element.nested + 1;
    }
    
    XmlElement(XmlDocument document, String name) {
        this.parent   = null;
        this.document = document;
        this.name     = name;
        this.nested   = 0;
    }
    
    public String getName() { return this.name; }
    
    /**
     * Returns a list containing all descendents of this element the specified 
     * element name.
     * 
     * @param name The name of the element to be retrieved.
     * @return
     */
    public List<XmlElement> getDescendents(String name) {
        List<XmlElement> elements = new ArrayList<XmlElement>();
        getAllElements(this, name, elements);
        return elements;
    }
    
    /** Recursive helper method for <tt>getDescendents</tt>. */
    private void getAllElements(XmlElement xml, String name, 
            List<XmlElement> results) {
        List<XmlElement> children = xml.getChildren();
        if (children == null) return;
        
        for (XmlElement child : children) {
            if (child.getName().equals(name)) results.add(child);
            
            getAllElements(child, name, results);
        }
    }
    
    /** Returns the textual content of this node an all of its children. */
    public String getText() {
        String result = "";
        List<XmlElement> textnodes = getDescendents(TEXT_NODE_NAME);
        for (XmlElement node : textnodes) {
            assert node instanceof XmlTextNode : "Element is not a text node";
            XmlTextNode text = (XmlTextNode)node;
            result += text.getText();
        }
        
        return result.trim();
    }
    
    public XmlDocument getDocument() { return this.document; }
    
    public XmlElement getParent() { return this.parent; }
    
    public List<XmlElement> getChildren() {
        return new ArrayList<XmlElement>(this.children);
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public String getAttributeValue(String name) {
        int index = this.findAttributeIndex(name);
        
        if (index >= 0 ) return this.attributes[index][ATTR_VALUE];
        else return null;
    }
    
    public List<String> getAttributes() {
        List<String> attr = new ArrayList<String>(this.ctAttr);
        for (int i = 0; i < this.ctAttr; i++) {
            attr.add(this.attributes[i][ATTR_NAME]);
        }
        
        return attr;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public String getNamespaceURI(String ns) {
        int index = this.findNamespaceIndex(ns);
        
        if (index >= 0 ) return this.namespaces[index][NS_URI];
        else return null;
    }
    
    /**
     * TODO cache seen namespaces
     * @param ns
     * @return
     */
    public boolean hasNamespace(String ns) {
        return this.findNamespaceIndex(ns) >= 0;
    }
    
    public List<String> getNamespaces() {
        List<String> nss = new ArrayList<String>(this.ctNS);
        for (int i = 0; i < this.ctNS; i++) {
            nss.add(this.attributes[i][NS_NAME]);
        }
        
        return nss;
    }
    
    public synchronized void addAttribute(String name, String value) {
        assert ctAttr < MAX_ATTRIBUTES : 
            "Violated the assumption that we will never need more than " + 
            MAX_ATTRIBUTES + " attributes.";
        
        int index = ctAttr++;
        this.attributes[index][ATTR_NAME]  = name;
        this.attributes[index][ATTR_VALUE] = value;
    }
    
    
    
    public void setNamespace(String namespace, String uri) {
        assert ctNS < MAX_NAMESPACES : 
            "Violated the assumption that we will never need more than " + 
            MAX_NAMESPACES + " namespace.";
        
        int index = ctNS++;
        this.namespaces[index][NS_NAME] = namespace;
        this.namespaces[index][NS_URI]  = uri;
    }
    
    /** 
     * Replaces the current value of the named attribute with the specified
     * value. It the value is not present in the list of values for this 
     * element, no action is taken. 
     * 
     * @param name The name of the attribute to be replaced.
     * @param value  The value to be assigned to the specified attribute.
     * 
     * @return The original value of this attribute, or null it the attribute
     *      was not found.
     */
    public String replaceAttribute(String name, String value) {
        String oldvalue = null;
        for (int i = 0; i < this.ctAttr; i++) {
            if (this.attributes[i][ATTR_NAME].equals(name)) {
                oldvalue = this.attributes[i][ATTR_VALUE];
                this.attributes[i][ATTR_VALUE] = value;
            }
        }
        
        return oldvalue;
    }
    
    /** 
     * Sets the value of the named attribute. Note that this method may be
     * slower than the <tt>addAttribute()</tt> method if it is known that
     * this attribute has not already been specified.
     * 
     * @param name The name of the attribute to set. 
     * @param value The value to set.
     * 
     * @return The previous value of this attribute or the empty string
     *      if the value was not previously set.
     */
    public String setAttribute(String name, String value) {
        String oldvalue = replaceAttribute(name, value);
        if (oldvalue == null) {
            addAttribute(name, value);
            oldvalue = "";
        }
        
        return oldvalue;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public int findAttributeIndex(String name) {
        for (int i = 0; i < this.ctAttr; i++) {
            if (this.attributes[i][ATTR_NAME].equals(name)) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * 
     * @param ns
     * @return
     */
    public int findNamespaceIndex(String ns) {
        for (int i = 0; i < this.ctNS; i++) {
            if (this.namespaces[i][NS_NAME].equals(name)) {
                return i;
            }
        }
        
        return -1;
    }
    
    /** Determines whether this element has the provided child element. Note 
     * this test only if the provided element is an immediate child of this 
     * element, not if it is a descendent. */
    public boolean hasChild(XmlElement child) {
        if (this.children.contains(child)) return true;
        else return false;
    }

    /** Removes the specified element if it is a child of this elmeent. */
    public boolean removeChild(XmlElement child) {
        boolean success = this.children.remove(child);
        if (success) child.parent = null;
        
        // TODO may need to replace document with null
//        child.document = null;
//        child.replaceDocument(child);
        
        return success;
    }
    
    /** 
     * Adopts the specified child, removing it from its current document
     * hierarchy and placing it under this element. Note that this will
     * alter the document tree of the provided child node.
     * 
     * @param child The XML element to be adopted.
     * @return True if the adoption succeeds, false otherwise. 
     */
    public boolean adopt(XmlElement child) {
        XmlElement parent = child.getParent();
        if (parent == null) {
            // this may be the root element of its document
            // XmlDocument doc = child.getDocument();
            // TODO remove root element from document
        } else {
            parent.removeChild(child);
        }
       
        child.parent = this;
        replaceDocument(child);
        
        return true;
    }
    
    private void replaceDocument(XmlElement node) {
        node.document = this.document;
        for (XmlElement child : node.children) {
            this.replaceDocument(child);
        }
    }
    
    /**
     * Creates a new <tt>XmlElement</tt> as a child of this element, adding it 
     * to the end of the list of children.
     * 
     * @param name
     * @return
     */
    public XmlElement createElement(String name) {
        XmlElement child = new XmlElement(this, name);
        this.children.add(child);
        
        return child;
    }
    
    
    /** Creates a new <tt>XmlElement</tt> as a child of this element, adding
     * it at the specified point in the list of children for this element.
     * 
     * @param index
     * @param name
     * @return
     */
    public XmlElement createElement(int index, String name) {
        XmlElement child = new XmlElement(this, name);
        this.children.add(index, child);
        
        return child;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public XmlTextNode createTextNode(String content) {
        XmlTextNode child = new XmlTextNode(this);
        child.setContent(content);
        
        this.children.add(child);
        
        return child;
    }
    
    
    
    
    public String toString() {
        String xml = "";
        
        // opening tag
        xml = this.getIndent() + "<" + name;
        for (int i = 0; i < this.ctAttr; i++) {
            String name  = this.attributes[i][ATTR_NAME];
            String value = this.attributes[i][ATTR_VALUE];
            xml += " " + name + "=\"" + value + "\""; 
        }
        
        String nsIndent = "\n" + this.getIndent() + INDENT_STRING;
        for (int i = 0; i < this.ctNS; i++) {
            String ns  = this.namespaces[i][NS_NAME];
            String uri = this.namespaces[i][NS_URI];
            xml += nsIndent + "xmlns:" + ns + "=\"" + uri + "\"";
        }
        
        if (this.children.size() == 0) { 
            xml += "/>\n";
            return xml;
        } else xml += ">\n";
        
        // contents
        for (XmlElement el : this.children) {
            xml += el.toString();
        }
        
        // closing tag
        xml += this.getIndent() + "</" + this.name + ">\n";
        
        return xml;
        
    }
    
    protected String getIndent() {
        String indent = "";
        for (int i = 0; i < this.nested; i++) indent += INDENT_STRING;
        
        return indent;
    }
    
    public static void main(String[] args) {
        XmlDocument doc = new XmlDocument("test");
        XmlElement el = doc.getRoot(); //new XmlElement("Test1", 0);
        
        el.setAttribute("attr1", "value 1");
        el.setAttribute("attr2", "value 2");
        el.setAttribute("attr3", "value 3");
        el.setAttribute("attr3", "value 4");
        
        XmlElement c1 = el.createElement("Child1");
        c1.createTextNode("This is the text of the first child.");
        el.createElement("Child1");
        el.createElement("Child1");
        el.createTextNode("This little piggy went to market");
        
        System.out.println(doc);
    }
}
