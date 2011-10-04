/**
 * 
 */
package org.nttext.util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Neal Audenaert
 */
@Entity
public class Institution {
	// for now, this is a place holder. We will eventually add more information, such
	// as alternative names, dates, physical locations, etc.
	
	@Id @GeneratedValue private Long id;
	private String name;
	
	/** Restricted access constructor for use by persistence framework. */
	Institution() {
	    
	}
	
	/**
	 * Construct a new location with the specified name.
	 * @param name
	 */
	public Institution(String name) {
		this.name = name;
	}
	
	/** Returns the unique identifier of this institution. */
	Long getId() {
	    return this.id;
	}
	
	/** Return the name of this location. */
	public String getName() {
		return name;
	}
}
