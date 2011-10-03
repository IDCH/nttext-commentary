/**
 * 
 */
package org.nttext.mss;

/**
 * Represents a designated identifier for this manuscript according to some specified 
 * reference scheme (GA number, ver Soden, shelf number). A single manuscript may have 
 * multiple designations.
 * 
 * @author Neal Audenaert
 */
public class Designation {
	
    /**
     * The persistent id of this designation. 
     * 
     */
    private Long pId;
    
	/** The identifier used for a manuscript in a given scheme. For example,
	 *  <tt>01</tt> for Codex Sinaiticus in Gregory Alland. */
	private String id;
	
	/** A short identifier for the naming scheme. For example, <tt>GA</tt> for 
	 *  Gregory Alland. */
	private String scheme;
	
	/**
	 * 
	 * @param scheme
	 * @param id
	 */
	public Designation(String scheme, String id) {
		this.scheme = scheme;
		this.id = id;
	}
	
	/** Returns a short identifier for the naming scheme of this identifier, for example, 
	 * <tt>GA</tt> for Gregory Alland numbers.
	 * 
	 * @return The naming scheme for this designation.
	 */
	public String getScheme() {
		return scheme;
	}
	
	/**
	 * The identifier for this designation.
	 * 
	 * @return The identifier for this designation.
	 */
	public String getId() {
		return id;
	}
	
	public String toString() {
	    return this.scheme + " " + this.id;
	}
	
	public boolean equals(Object o) {
	    Designation d = (Designation)o;
	    
	    return d.toString().equals(this.toString());
	}
	
	public int hashCode() {
	    return this.toString().hashCode();
	}

}
