/**
 * 
 */
package org.nttext.mss;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Neal Audenaert
 */
public class Institution {
	private static Map<String, Institution> institutions =
		new HashMap<String, Institution>();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Institution find(String name) {
		// TODO these should be updated to reflect DB-backed storage
		// TODO may have multiple institutions with the same name (or that should be returned
		//		based on a heuristic lookup). Update API to reflect this.
		
		Institution inst = null;
		synchronized (institutions) {
			inst = institutions.get(name);
		}
		
		return inst;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Institution findOrCreate(String name) {
		Institution inst = null;
		synchronized(institutions) {
			inst = institutions.get(name);
			if (inst == null) {
				inst = new Institution(name);
				institutions.put(name, inst);
			}
		}
		
		return inst;
	}
	
	// for now, this is a place holder. We will eventually add more information, such
	// as alternative names, dates, physical locations, etc.
	
	
	private String name;
	
	public Institution(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
