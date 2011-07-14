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
	
	/** The identifier used for a manuscript in a given scheme. For example,
	 *  <tt>01</tt> for Codex Sinaiticus in Gregory Alland. */
	private String id;
	
	/** A short identifier for the naming scheme. For example, <tt>GA</tt> for 
	 *  Gregory Alland. */
	private String scheme;
	
	public Designation(String scheme, String id) {
		this.scheme = scheme;
		this.id = id;
	}
	
	public String getScheme() {
		return scheme;
	}
	
	public String getId() {
		return id;
	}

}
