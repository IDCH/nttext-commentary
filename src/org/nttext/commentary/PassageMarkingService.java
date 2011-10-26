/**
 * 
 */
package org.nttext.commentary;

import java.util.List;
import java.util.SortedSet;

import openscriptures.text.Structure;
import openscriptures.text.TextModule;
import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class PassageMarkingService {
    // I'm working on this here temporarily. It will need to move somewhere 
    // else as it matures. 
    
    String template = 
            "<div class=\"passage\" passage=\"${passageId}\">" +
            "  <#list tokens as token>" +
            "  </#list> " +
            "</div>";
    
    String testString = "κλέος εἰ ἁμαρτάνοντες";
    public static TextModule repo = null;
    
    
    
//    public static int getCharacterIndex(List<Token> tokens, Token token) {
//        
//    }
    
    
    public static String markText(Structure base, SortedSet<Structure> structures) {
        StringBuilder sb = new StringBuilder();
        
        Work w = repo.getWorkRepository().find(base.getWorkUUID());
        List<Token> tokens = repo.getTokenRepository().find(w, base.getStart(), base.getEnd());
        
        for (Token t : tokens) {
            sb.append("<span class=\"token\" id=\"")
                          .append(w.getId()).append(".").append(t.getPosition()).append(">")
                      .append(t.getText())
              .append("</span>");
        }
        return sb.toString();
    }
}
