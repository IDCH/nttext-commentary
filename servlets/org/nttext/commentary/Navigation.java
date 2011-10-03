/**
 * 
 */
package org.nttext.commentary;

/**
 * Model object for implementing navigational controls.
 * 
 * @author Neal Audenaert
 */
public class Navigation {

    public String getPrevChapterUrl() {
        return "entry/1Pet.2.1";
    }
    
    public String getPrevVerseUrl() {
        return "entry/1Pet.2.19";
    }
    
    public String getNextVerseUrl() {
        return "entry/1Pet.2.21";
    }
    
    public String getNextChapterUrl() {
        return "entry/1Pet.3.1";
    }
    
    public String getPrevChapterLabel() {
        return "1 Peter 2:1";
    }

    public String getPrevVerseLabel() {
        return "1 Peter 2:19";
    }

    public String getNextVerseLabel() {
        return "1 Peter 2:21";
    }

    public String getNextChapterLabel() {
        return "1 Peter 3:1";
    }
}
