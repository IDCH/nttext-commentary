/**
 * 
 */
package org.nttext.mss;

import java.util.HashMap;
import java.util.Map;

/**
 * A geographical place name. 
 * 
 * @author Neal Audenaert
 */
public class Location {
	private static Map<String, Location> locations =
		new HashMap<String, Location>();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Location find(String name) {
		// TODO these should be updated to reflect DB-backed storage
		// TODO may have multiple places with the same name (or that should be returned
		//		based on a heuristic lookup). Update API to reflect this.
		
		Location loc = null;
		synchronized (locations) {
			loc = locations.get(name);
		}
		
		return loc;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Location findOrCreate(String name) {
		Location loc = null;
		synchronized(locations) {
			loc = locations.get(name);
			if (loc == null) {
				loc = new Location(name);
				locations.put(name, loc);
			}
		}
		
		return loc;
	}
	
	private String name = "";
	
	// TODO add GIS(ish) related data, either X/Y or shape.
	// TODO add relational information such as contains/contained by (Paris is in France)
	
	Location(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
