/**
 * 
 */
package org.nttext.mss;

/**
 * @author Neal Audenaert
 */
public interface Catalog {
    public Manuscript createManuscript(String scheme, String identifier);
    
    public Manuscript lookup(Designation designation);
    
    public Designation createMSDesignation(String scheme, String id);
}
