/**
 * 
 */
package org.nttext.commentary;

/**
 * @author Neal Audenaert
 */
public class PageDetails {
    
    String pageTile = "Commentary Entry";
    
    public String getTitle() {
        return this.pageTile;
    }
    
    public String getHome() {
        return "http://localhost:8080/nttext";
    }
    
    public String getSectionHome() {
        return "http://localhost:8080/nttext/commentary";
    }
}
