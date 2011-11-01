/**
 * 
 */
package org.idch.bible.importers.hcsb;

import java.util.HashSet;
import java.util.Set;

import org.idch.texts.importer.PathElement;
import org.idch.texts.importer.StructureHandler;

/**
 * The phrasespace tag is used to introduce non-breaking spaces in lines of poetry or other 
 * dynamic text. This tag can be safely ignored as the inserted space should be correctly 
 * interpreted as a whitespace token. Specific display formatting (non-breaking lines) should 
 * be applied at the higher level of abstraction (the line of the dynamic text block). 
 *  
 * @author Neal Audenaert
 */
public class SupHandler extends StructureHandler {

    public static final String NAME = "Sup";
    
    public static final String TAGNAME = "sup";
    
    public Set<String> values = new HashSet<String>();
    
    
    public SupHandler() {
        super(NAME);
    }
    
    public boolean matchesStart(PathElement p) {
        return p.getName().equals(TAGNAME);
    }
    
    @Override
    public void start(PathElement p) {
        count++;
        // just ignore these.
    }
    
    @Override
    public void end(PathElement p) {
        String txt = p.getText();
        if (txt.equals("•")) {
            // Bullet Notes - the HCSB has an appendix of 145 words or phrases that average 
            // readers might need some help in understanding. These words, e.g. Asherah, 
            // Ashtoreth, or atone, are marked with a bullet on their first occurrence in a 
            // chapter of the biblical text. When readers see a bullet in the text, they can 
            // refer to the appendix if they want to learn more about the term.
            
            // TODO create a structure, we'll close it when we close the chapter, or when we 
            //      hit the next bullet word.
            
            return;
        }
        
        
        // TODO this happens once in the NT, in revelation to mark a 1/2.
        System.out.println("Fixme: Superscript");
       
    }
}