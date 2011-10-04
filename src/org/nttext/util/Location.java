/**
 * 
 */
package org.nttext.util;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * A geographical place name. 
 * 
 * @author Neal Audenaert
 */
@Entity
public class Location {
	
    @Id @GeneratedValue private Long id;
	@Basic private String name = "";
	
	// TODO add GIS(ish) related data, either X/Y or shape.
	// TODO add relational information such as contains/contained by (Paris is in France)
	
	Location() {
	    
	}
	
	public Location(String name) {
		this.name = name;
	}
	
	Long getId() {
	    return this.id;
	}
	
	public String getName() { 
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
