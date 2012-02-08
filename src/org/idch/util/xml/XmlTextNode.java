/* Created on       Aug 27, 2007
 * Last Modified on $Date: 2008-07-17 19:08:27 $
 * $Revision: 1.1 $
 * $Log: XmlTextNode.java,v $
 * Revision 1.1  2008-07-17 19:08:27  neal
 * Reattached NADL Project to a CVS Repository. This time the HTML, JS, and other webcomponents are being backed up as well.
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

import org.apache.commons.lang.StringEscapeUtils;

public class XmlTextNode extends XmlElement {
    private String content = null;
    
    protected XmlTextNode(XmlElement parent) { super(parent); }
    
    // IMPLMENT override superclass methods as needed
    
    public void setContent(String content) { 
        this.content = StringEscapeUtils.escapeXml(content); 
    }
    
    public String getText() { 
        return StringEscapeUtils.unescapeXml(content); 
    }
    
    public String toString() {
        String result = "";
        String indent = this.getIndent();
        
        int maxChars = (indent.length() > 20) ? indent.length() + 60 : 80;
        String tmp = indent + this.content;
        while (tmp.length() > maxChars) {
            String line = tmp.substring(0, maxChars) + "\n";
            
            int index = line.lastIndexOf(" ");
            if (index < 20) {
                // find the first break on hte next line.
                String nextline = tmp.substring(maxChars) + "\n";
                index = line.length() + nextline.indexOf("\\w");
            } 
            
            result += tmp.substring(0, index) + "\n";
            tmp = indent + tmp.substring(index).trim();
        }
        
        result += tmp + "\n";
        
        
        return result;
    }

    public static void main(String[] args) {
//        XmlTextNode test = new XmlTextNode(2);
//        test.setContent("This little piggy went to market. " +
//                "this little piggy stayed home. " +
//                "This little piggy had roast beaf," +
//                "this little piggy had none. " +
//                "This little piggy cried wee wee wee all the way home. " +
//                "This is a very odd little example to test this node, " +
//                "but at least is works and is relatively easy for me to type " +
//                "quickly, except of course for the fact that I am sick and " +
//                "not typing very well at all.");
//        System.out.println(test);
    }
}
