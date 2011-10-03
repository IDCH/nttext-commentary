/**
 * 
 */
package org.nttext.mss;

/**
 * @author Neal Audenaert
 */
public class Catalog {

    
    /**
     * Finds the manuscript corresponding to a specific designation.
     * 
     * @param designation The designation of the manuscript to find.
     * @return The designated manuscript or null if no such manuscript exists.
     */
	public Manuscript find(Designation designation) {
	    return null;
	}
	
	public Designation createMSDesignation(String scheme, String id) {
	    Designation d = new Designation(scheme, id);
	    return d;
	}
}
