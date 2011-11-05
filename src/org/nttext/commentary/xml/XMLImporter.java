/**
 * 
 */
package org.nttext.commentary.xml;

import java.io.File;

import org.idch.persist.RepositoryAccessException;
import org.idch.util.xml.XMLUtil;
import org.nttext.commentary.persist.mysql.MySQLCommentaryModule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Neal Audenaert
 */
public class XMLImporter {
   
    
    
    public static void main(String[] args) {
        File input = new File("data/commentary/1Pet_1.xml");

        XMLEntryProxy proxy;
        try {
            proxy = new XMLEntryProxy(MySQLCommentaryModule.get());
            Document doc = XMLUtil.getXmlDocument(input);
            Element el = doc.getDocumentElement();
            NodeList entries = el.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                
                if (entry.getNodeType() != Node.ELEMENT_NODE) {
                    // skip text and comment nodes
                    continue;
                }
                
                proxy.importEntry((Element)entry);
            }
              
            System.out.println("done.");
        } catch (RepositoryAccessException e) {
            System.err.println("Error accessing repository: " + e);
        }
    }

}
