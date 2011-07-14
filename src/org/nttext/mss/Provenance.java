/**
 * 
 */
package org.nttext.mss;

import java.util.Date;
import java.util.Set;

/**
 * @author Neal Audenaert
 */
public class Provenance {
	// Provenance & ownership history, & current location
	
	/**
	 * A narrative description of the provenance of this manuscript. 
	 */
	private String narrative = null;
	
	private Set<ProvenanceDetails> details;
	
	// TODO add references to bibliographic support for the narrative
	
	/**
	 * Represents the details of a single stage in the provenance of a MS.
	 * @author Neal Audenaert
	 */
	public static class ProvenanceDetails {
		/** The individual or institution that held the MS during this time period. */ 
		String holder = null;		// TODO change to a class so that we can enforce stronger 
									//      name authority constraints
		/** The start date for this period. */
		Date beginning = null;		// TODO use fuzzy dates
		
		/** The ending date for this period. */
		Date ending    = null;
		
		/** The location of the object during this period. */
		Location location;
		
		/** A description of the disposition of the object during this period. */ 
		String narrative;
		
		// TODO add references to bibliographic support for this history
	}
}
